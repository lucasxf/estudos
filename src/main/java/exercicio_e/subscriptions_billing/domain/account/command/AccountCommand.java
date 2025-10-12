package exercicio_e.subscriptions_billing.domain.account.command;

import exercicio_e.subscriptions_billing.domain.Command;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
public sealed interface AccountCommand extends Command {

    UUID commandId();

    Instant timestamp();

    UUID accountId();

    String username();

    record CreateAccount(
            UUID commandId,
            Instant timestamp,
            UUID accountId,
            String username,
            String usernameKey) implements AccountCommand {
    }

    record DeleteAccount(
            UUID commandId,
            Instant timestamp,
            UUID accountId,
            String username,
            String usernameKey) implements AccountCommand {
    }

}
