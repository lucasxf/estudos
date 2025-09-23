package exercicio_e.subscriptions_billing.infrastructure.repository.impl;

import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.EventStore;
import exercicio_e.subscriptions_billing.infrastructure.repository.AccountRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 23/09/2025
 */
@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final EventStore eventStore;

    public AccountRepositoryImpl(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public List<AccountEvent> getEventsById(UUID id) {
        return List.of();
    }

}
