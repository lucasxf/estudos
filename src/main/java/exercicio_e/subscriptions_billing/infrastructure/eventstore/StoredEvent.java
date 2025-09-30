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
        String type,
        String aggregateType,
        String aggregateId,
        long version,
        Instant occurredAt,
        UUID correlationId,
        UUID causationId,
        String payloadJson) {
}
