package exercicio_e.subscriptions_billing.domain.account.command;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
public sealed interface AccountCommand {

    UUID commandId();

    UUID accountId();

    Instant timestamp();

    String username();

    record CreateAccount(
            UUID commandId,
            UUID accountId,
            Instant timestamp,
            String username,
            String usernameKey) implements AccountCommand {
    }

    record DeleteAccount(
            UUID commandId,
            UUID accountId,
            Instant timestamp,
            String username,
            String usernameKey) implements AccountCommand {
    }

}
