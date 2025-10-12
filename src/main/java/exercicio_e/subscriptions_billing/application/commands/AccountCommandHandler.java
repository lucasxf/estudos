package exercicio_e.subscriptions_billing.application.commands;

import exercicio_e.subscriptions_billing.domain.account.AccountAggregate;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand.CreateAccount;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent.AccountCreated;
import exercicio_e.subscriptions_billing.domain.subscription.SubscriptionAggregate;
import exercicio_e.subscriptions_billing.domain.subscription.command.SubscriptionCommand.StartTrial;
import exercicio_e.subscriptions_billing.domain.subscription.event.TrialStarted;
import exercicio_e.subscriptions_billing.domain.subscription.plan.Plan;
import exercicio_e.subscriptions_billing.domain.username.UsernameAggregate;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent.UsernameClaimed;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent.UsernameReserved;
import exercicio_e.subscriptions_billing.infrastructure.context.ContextScope;
import exercicio_e.subscriptions_billing.infrastructure.messaging.EventBus;
import exercicio_e.subscriptions_billing.infrastructure.repository.AccountRepository;
import exercicio_e.subscriptions_billing.infrastructure.repository.SubscriptionRepository;
import exercicio_e.subscriptions_billing.infrastructure.repository.UsernameRepository;
import exercicio_e.subscriptions_billing.infrastructure.serialization.EventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static exercicio_e.subscriptions_billing.domain.username.command.UsernameCommand.ClaimUsername;
import static exercicio_e.subscriptions_billing.domain.username.command.UsernameCommand.ReserveUsername;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
@Slf4j
@Component
public class AccountCommandHandler {

    private final EventBus eventBus;
    private final UsernameRepository usernameRepository;
    private final AccountRepository accountRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EventMapper eventMapper;

    public AccountCommandHandler(
            EventBus eventBus, UsernameRepository usernameRepository,
            AccountRepository accountRepository,
            SubscriptionRepository subscriptionRepository, EventMapper eventMapper) {
        this.eventBus = eventBus;
        this.usernameRepository = usernameRepository;
        this.accountRepository = accountRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.eventMapper = eventMapper;
    }

    public List<AccountEvent> handle(
            UUID correlationId, AccountCommand command) {
        switch (command) {
            case CreateAccount cmd -> handleCreateAccountCommand(correlationId, cmd);
            case AccountCommand.DeleteAccount cmd -> handleDeleteAccountCommand(correlationId, cmd);
        }
        return null;
    }

    public List<AccountEvent> handleCreateAccountCommand(
            UUID correlationId, CreateAccount command) {
        final UUID accountCommandId = command.commandId();
        final String usernameKey = command.usernameKey();
        final var accountId = command.accountId();
        try (var scope = ContextScope.open(correlationId, accountCommandId)) {
            log.info("Processing CreateAccount command for username: {}", command.username());

            // 1. reservar nome de usuário
            final UsernameRepository.LoadedStream usernameStream = usernameRepository.load(usernameKey);
            final UsernameAggregate usernameAggregate =
                    UsernameAggregate.from(usernameKey, usernameStream.history(), usernameStream.lastVersion());
            final ReserveUsername reserveUsername = new ReserveUsername(UUID.randomUUID(), usernameKey);
            final UsernameReserved usernameReserved = usernameAggregate.decide(reserveUsername);
            final var usernameEvents = usernameRepository.append(
                            usernameKey,
                            usernameStream.lastVersion(),
                            usernameReserved,
                            correlationId,
                            reserveUsername.commandId())
                    .stream().map(eventMapper::toEnvelope).toList();
            eventBus.publishAll(usernameEvents, correlationId, reserveUsername.commandId());
            log.info("Username '{}' reserved for account '{}'", command.username(), accountId);

            // 2. criar conta
            final AccountRepository.LoadedStream accountStream = accountRepository.load(accountId);
            final AccountAggregate accountAggregate =
                    AccountAggregate.from(accountId, accountStream.history(), accountStream.lastVersion());
            final AccountCreated accountCreated = accountAggregate.decide(command);
            final var accountEvents = accountRepository.append(
                            accountId,
                            accountStream.lastVersion(),
                            accountCreated,
                            correlationId,
                            accountCommandId)
                    .stream().map(eventMapper::toEnvelope).toList();
            eventBus.publishAll(accountEvents, correlationId, accountCommandId);
            log.info("Account '{}' created for username '{}'", accountId, command.username());

            // 3. reivindicar nome de usuário
            final UsernameRepository.LoadedStream usernameReload = usernameRepository.load(usernameKey);
            final UsernameAggregate usernameAggRefresh =
                    UsernameAggregate.from(usernameKey, usernameReload.history(), usernameReload.lastVersion());
            final ClaimUsername claimUsername = new ClaimUsername(
                    UUID.randomUUID(), usernameKey, accountId);
            final UsernameClaimed usernameClaimed =
                    usernameAggRefresh.decide(claimUsername);
            final var usernameClaimedEvents = usernameRepository.append(
                            usernameKey,
                            usernameReload.lastVersion(),
                            usernameClaimed,
                            correlationId,
                            claimUsername.commandId())
                    .stream().map(eventMapper::toEnvelope).toList();
            eventBus.publishAll(usernameClaimedEvents, correlationId, claimUsername.commandId());

            // 4. Iniciar o período de teste de assinatura
            final var subscriptionId = UUID.randomUUID();
            final var subscriptionStream = subscriptionRepository.load(subscriptionId);
            final StartTrial startTrial = new StartTrial(UUID.randomUUID(), subscriptionId, Instant.now(),
                    Plan.STANDARD);
            final SubscriptionAggregate subscriptionAggregate = SubscriptionAggregate.from(
                    subscriptionId,
                    subscriptionStream.history(),
                    subscriptionStream.lastVersion());

            final TrialStarted trialStarted = subscriptionAggregate.decide(startTrial);
            final var trialStartedEvents = subscriptionRepository.append(
                            subscriptionId,
                            subscriptionStream.lastVersion(),
                            trialStarted,
                            correlationId,
                            startTrial.commandId())
                    .stream().map(eventMapper::toEnvelope).toList();
            eventBus.publishAll(trialStartedEvents, correlationId, startTrial.commandId());
            log.info("Trial started for account '{}' with subscription '{}'", accountId, subscriptionId);
            return null;
        }
    }

    private void handleDeleteAccountCommand(
            UUID correlationId,
            AccountCommand.DeleteAccount cmd) {

    }

}
