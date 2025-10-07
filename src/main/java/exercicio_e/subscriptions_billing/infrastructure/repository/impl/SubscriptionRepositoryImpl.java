package exercicio_e.subscriptions_billing.infrastructure.repository.impl;

import exercicio_e.subscriptions_billing.domain.account.Account;
import exercicio_e.subscriptions_billing.domain.subscription.event.SubscriptionEvent;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.EventStore;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.StoredEvent;
import exercicio_e.subscriptions_billing.infrastructure.repository.AbstractEventSourcingRepository;
import exercicio_e.subscriptions_billing.infrastructure.repository.SubscriptionRepository;
import exercicio_e.subscriptions_billing.infrastructure.serialization.EventMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 28/08/2025
 */
@Repository
public class SubscriptionRepositoryImpl extends AbstractEventSourcingRepository<SubscriptionEvent> implements SubscriptionRepository {

    public SubscriptionRepositoryImpl(EventStore eventStore, EventMapper eventMapper) {
        super(eventStore, eventMapper);
    }

    @Override
    public LoadedStream load(UUID subscriptionId) {
        return null;
    }

    @Override
    public List<StoredEvent> append(UUID subscriptionId, long expectedVersion, List<SubscriptionEvent> newEvents, UUID correlationId, UUID causationId) {
        return List.of();
    }

    @Override
    public List<SubscriptionEvent> getEventsByAccount(Account account) {
        return SubscriptionRepository.super.getEventsByAccount(account);
    }

    @Override
    public List<SubscriptionEvent> getEventsByAccountId(UUID id) {
        return List.of();
    }

    private StoredEvent toStoredEvent(UUID subscriptionId, SubscriptionEvent event) {
        return null;
    }

}
