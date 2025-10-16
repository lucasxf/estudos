package exercicio_e.subscriptions_billing.application.commands;

import exercicio_e.subscriptions_billing.application.commands.handlers.impl.ClaimUsernameHandler;
import exercicio_e.subscriptions_billing.application.commands.handlers.impl.CreateAccountHandler;
import exercicio_e.subscriptions_billing.application.commands.handlers.impl.ReserveUsernameHandler;
import exercicio_e.subscriptions_billing.application.commands.handlers.impl.StartTrialHandler;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand.CreateAccount;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.domain.subscription.command.SubscriptionCommand.StartTrial;
import exercicio_e.subscriptions_billing.domain.subscription.plan.Plan;
import exercicio_e.subscriptions_billing.domain.username.command.UsernameCommand.ReserveUsername;
import exercicio_e.subscriptions_billing.infrastructure.context.ContextScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static exercicio_e.subscriptions_billing.domain.username.command.UsernameCommand.ClaimUsername;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
@Slf4j
@Component
public class AccountCommandHandler {

    private final CreateAccountHandler createAccountHandler;
    private final ReserveUsernameHandler reserveUsernameHandler;
    private final ClaimUsernameHandler claimUsernameHandler;
    private final StartTrialHandler startTrialHandler;

    public AccountCommandHandler(
            CreateAccountHandler createAccountHandler,
            ReserveUsernameHandler reserveUsernameHandler,
            ClaimUsernameHandler claimUsernameHandler,
            StartTrialHandler startTrialHandler) {
        this.createAccountHandler = createAccountHandler;
        this.reserveUsernameHandler = reserveUsernameHandler;
        this.claimUsernameHandler = claimUsernameHandler;
        this.startTrialHandler = startTrialHandler;
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
            final var reserveUsername = new ReserveUsername(
                    UUID.randomUUID(), Instant.now(), usernameKey);
            reserveUsernameHandler.handle(reserveUsername, correlationId);

            // 2. criar conta
            createAccountHandler.handle(command, correlationId);

            // 3. reivindicar nome de usuário
            final var claimUsername = new ClaimUsername(
                    UUID.randomUUID(), Instant.now(), usernameKey, accountId);
            claimUsernameHandler.handle(claimUsername, correlationId);

            // 4. Iniciar o período de teste de assinatura
            log.info("Handling StartTrial command for account: {}", command.accountId());
            final var startTrial = new StartTrial(
                    UUID.randomUUID(), Instant.now(), UUID.randomUUID(), Plan.STANDARD);
            startTrialHandler.handle(startTrial, correlationId);
            log.info("Trial started for account '{}' with subscription '{}'", accountId, startTrial.subscriptionId());
            return null;
        }
    }

    private void handleDeleteAccountCommand(
            UUID correlationId,
            AccountCommand.DeleteAccount cmd) {

    }

}
