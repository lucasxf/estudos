package exercicio_e.subscriptions_billing.domain.username.command;

import exercicio_e.subscriptions_billing.domain.Command;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 06/10/2025
 */
public sealed interface UsernameCommand extends Command {

    UUID commandId();

    Instant timestamp();

    String usernameKey();

    record ReserveUsername(UUID commandId,
                           Instant timestamp,
                           String usernameKey) implements UsernameCommand { }

    record ClaimUsername(UUID commandId,
                         Instant timestamp,
                         String usernameKey,
                         UUID accountId) implements UsernameCommand { }

    record ReleaseUsername(UUID commandId,
                           Instant timestamp,
                           String usernameKey,
                           String reason) implements UsernameCommand { }

}
