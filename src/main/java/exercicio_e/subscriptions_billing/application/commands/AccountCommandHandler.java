package exercicio_e.subscriptions_billing.application.commands;

import exercicio_e.subscriptions_billing.domain.account.AccountAggregate;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand.CreateAccountCommand;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent.AccountCreated;
import exercicio_e.subscriptions_billing.domain.subscription.SubscriptionAggregate;
import exercicio_e.subscriptions_billing.domain.subscription.command.SubscriptionCommand.StartTrial;
import exercicio_e.subscriptions_billing.domain.subscription.event.TrialStarted;
import exercicio_e.subscriptions_billing.domain.subscription.plan.Plan;
import exercicio_e.subscriptions_billing.domain.username.UsernameAggregate;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent.UsernameClaimed;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent.UsernameReserved;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.StoredEvent;
import exercicio_e.subscriptions_billing.infrastructure.repository.AccountRepository;
import exercicio_e.subscriptions_billing.infrastructure.repository.SubscriptionRepository;
import exercicio_e.subscriptions_billing.infrastructure.repository.UsernameRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static exercicio_e.subscriptions_billing.domain.username.command.UsernameCommand.ClaimUsername;
import static exercicio_e.subscriptions_billing.domain.username.command.UsernameCommand.ReserveUsername;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
@Component
public class AccountCommandHandler {

    private final UsernameRepository usernameRepository;
    private final AccountRepository accountRepository;
    private final SubscriptionRepository subscriptionRepository;

    public AccountCommandHandler(UsernameRepository usernameRepository, AccountRepository accountRepository, SubscriptionRepository subscriptionRepository) {
        this.usernameRepository = usernameRepository;
        this.accountRepository = accountRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<AccountEvent> handle(
            UUID correlationId, AccountCommand command) {
        switch (command) {
            case CreateAccountCommand cmd -> handleCreateAccountCommand(correlationId, cmd);
            case AccountCommand.DeleteAccountCommand cmd -> handleDeleteAccountCommand(correlationId, cmd);
        }
        return null;
    }

    public List<AccountEvent> handleCreateAccountCommand(
            UUID correlationId, CreateAccountCommand command) {
        final String usernameKey = command.usernameKey();
        final UsernameRepository.LoadedStream usernameStream = usernameRepository.load(usernameKey);

        // 1. reservar nome de usuário
        final UsernameAggregate usernameAggregate =
                UsernameAggregate.from(usernameKey, usernameStream.history(), usernameStream.lastVersion());
        final ReserveUsername reserveUsername = new ReserveUsername(UUID.randomUUID(), usernameKey);
        final UsernameReserved usernameReserved = usernameAggregate.decide(reserveUsername);
        final List<StoredEvent> usernameEvents = usernameRepository.append(
                usernameKey,
                usernameStream.lastVersion(),
                usernameReserved,
                correlationId,
                reserveUsername.commandId());

        // 2. criar conta
        final var accountId = command.accountId();
        final var accountCommandId = UUID.randomUUID();
        final AccountRepository.LoadedStream accountStream = accountRepository.load(accountId);
        final AccountAggregate accountAggregate =
                AccountAggregate.from(accountId, accountStream.history(), accountStream.lastVersion());
        final AccountCreated accountCreated = accountAggregate.decide(command);
        final List<StoredEvent> accountEvents = accountRepository.append(
                accountId,
                accountStream.lastVersion(),
                accountCreated,
                correlationId,
                accountCommandId);

        // 3. reivindicar nome de usuário
        final ClaimUsername claimUsername = new ClaimUsername(accountCommandId, usernameKey, accountId);
        UsernameClaimed usernameClaimed = usernameAggregate.decide(claimUsername);
        final List<StoredEvent> usernameClaimedEvents = usernameRepository.append(
                usernameKey,
                usernameStream.lastVersion(),
                usernameClaimed,
                correlationId,
                claimUsername.commandId());

        // 4. Iniciar o período de teste de assinatura
        final var subscriptionId = UUID.randomUUID();
        final var subscriptionStream = subscriptionRepository.load(subscriptionId);
        final StartTrial startTrial = new StartTrial(UUID.randomUUID(), UUID.randomUUID(), Plan.STANDARD);
        SubscriptionAggregate subscriptionAggregate = SubscriptionAggregate.from(
                subscriptionId,
                subscriptionStream.history(),
                subscriptionStream.lastVersion());

        final TrialStarted trialStarted = subscriptionAggregate.decide(startTrial);
        final List<StoredEvent> trialStartedEvents = subscriptionRepository.append(
                subscriptionId,
                accountStream.lastVersion(),
                trialStarted,
                correlationId,
                startTrial.commandId());
        return null;
    }

    private void handleDeleteAccountCommand(UUID correlationId, AccountCommand.DeleteAccountCommand cmd) {

    }

}
