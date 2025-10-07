package exercicio_e.subscriptions_billing.domain.username.command;

import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 06/10/2025
 */
public sealed interface UsernameCommand {

    UUID commandId();

    String usernameKey();

    record ReserveUsername(UUID commandId, String usernameKey) implements UsernameCommand {}

    record ClaimUsername(UUID commandId, String usernameKey, UUID accountId) implements UsernameCommand {}

    record ReleaseUsername(UUID commandId, String usernameKey, String reason) implements UsernameCommand {}

}
