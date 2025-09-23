package exercicio_e.subscriptions_billing.infrastructure.repository.impl;

import exercicio_e.subscriptions_billing.domain.account.Account;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.StoredEvent;
import exercicio_e.subscriptions_billing.domain.subscription.event.SubscriptionEvent;
import exercicio_e.subscriptions_billing.infrastructure.repository.SubscriptionWriteRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 28/08/2025
 */
public class SubscriptionWriteRepositoryImpl implements SubscriptionWriteRepository {


    @Override
    public List<SubscriptionEvent> appendEvent(UUID subscriptionId, SubscriptionEvent event) {
        final StoredEvent storedEvent = toStoredEvent(subscriptionId, event);
        return List.of();
    }

    @Override
    public List<SubscriptionEvent> getEventsByAccount(Account account) {
        return SubscriptionWriteRepository.super.getEventsByAccount(account);
    }

    @Override
    public List<SubscriptionEvent> getEventsByAccountId(UUID id) {
        return List.of();
    }

    private StoredEvent toStoredEvent(UUID subscriptionId, SubscriptionEvent event) {
        return new StoredEvent(
                event.id(),
                subscriptionId,
                123L,
                event.timestamp(),
                UUID.randomUUID(),
                event);
    }

}
