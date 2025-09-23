package exercicio_e.subscriptions_billing.infrastructure.subscription.event_store;

import exercicio_e.subscriptions_billing.domain.subscription.event.SubscriptionEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * An envelope for Subscription events.
 *
 * @author Lucas Xavier Ferreira
 * @date 25/08/2025
 */
public record StoredEvent(
        UUID eventId,
        UUID aggregateId,
        long version,
        Instant timestamp,
        UUID correlationId,
        SubscriptionEvent payload) {
}
