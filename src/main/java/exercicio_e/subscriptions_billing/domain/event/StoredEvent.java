package exercicio_e.subscriptions_billing.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 25/08/2025
 */
public record StoredEvent(
        UUID eventId,
        UUID aggregateId,
        long version,
        Instant timestamp,
        EventType type,
        UUID correlationId,
        SubscriptionEvent payload) {
}
