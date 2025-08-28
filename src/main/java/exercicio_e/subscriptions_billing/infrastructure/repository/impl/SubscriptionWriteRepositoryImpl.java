package exercicio_e.subscriptions_billing.infrastructure.repository.impl;

import exercicio_e.subscriptions_billing.domain.Account;
import exercicio_e.subscriptions_billing.domain.event.SubscriptionEvent;
import exercicio_e.subscriptions_billing.infrastructure.repository.SubscriptionWriteRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 28/08/2025
 */
public class SubscriptionWriteRepositoryImpl implements SubscriptionWriteRepository {
    @Override
    public List<SubscriptionEvent> appendEvent(SubscriptionEvent event) {
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
}
