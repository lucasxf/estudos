package exercicio_e.subscriptions_billing.domain.username.event;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 26/09/2025
 */
public sealed interface UsernameEvent {

    String USERNAME_RESERVED = "UsernameReserved";
    String USERNAME_CLAIMED = "UsernameClaimed";
    String USERNAME_RELEASED = "UsernameReleased";

    String usernameKey();

    record UsernameReserved(String usernameKey, Instant reservedAt) implements UsernameEvent {
    }

    record UsernameClaimed(String usernameKey, UUID accountId,
                           Instant claimedAt) implements UsernameEvent {
    }

    record UsernameReleased(String usernameKey, UUID releasedByCommandId,
                            Instant releasedAt, String reason) implements UsernameEvent {
    }

}
