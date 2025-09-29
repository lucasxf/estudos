package exercicio_e.subscriptions_billing.infrastructure.eventstore;

import java.time.Instant;
import java.util.UUID;

/**
 * An envelope for events.
 *
 * @author Lucas Xavier Ferreira
 * @date 25/08/2025
 */
public record StoredEvent(
        UUID id,
        /**
         * The type of the event, e.g., "AccountCreated", "UsernameReserved".
         */
        String type,
        /**
         * The aggregate type, e.g., "Account", "Username".
         */
        String aggregateType,
        String aggregateId,
        long version,
        Instant occurredAt,
        UUID correlationId,
        UUID causationId,
        String payloadJson) {
}
