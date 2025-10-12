package exercicio_e.subscriptions_billing.infrastructure.repository;

import exercicio_e.subscriptions_billing.domain.subscription.event.SubscriptionEvent;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.StoredEvent;

import java.util.List;
import java.util.UUID;

/**
 * Subscription write-side repository.
 *
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public interface SubscriptionRepository {

    String AGGREGATE_TYPE = "Subscription";

    LoadedStream load(UUID subscriptionId);

    default List<StoredEvent> append(UUID subscriptionId,
                                     long expectedVersion,
                                     SubscriptionEvent newEvent,
                                     UUID correlationId,
                                     UUID causationId) {
        return append(subscriptionId, expectedVersion, List.of(newEvent), correlationId, causationId);
    }

    List<StoredEvent> append(UUID subscriptionId,
                             long expectedVersion,
                             List<SubscriptionEvent> newEvents,
                             UUID correlationId,
                             UUID causationId);

    record LoadedStream(UUID aggregateId, List<SubscriptionEvent> history, long lastVersion) {
    }

}
