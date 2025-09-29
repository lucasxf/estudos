package exercicio_e.subscriptions_billing.infrastructure.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.EventStore;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.StoredEvent;
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
    private final ObjectMapper mapper;

    public AccountRepositoryImpl(EventStore eventStore, ObjectMapper mapper) {
        this.eventStore = eventStore;
        this.mapper = mapper;
    }

    @Override
    public LoadedStream load(String aggregateType, UUID aggregateId) {
        try {
            final List<StoredEvent> storedEvents = eventStore.readStream(aggregateType, aggregateId.toString());
            final var history = storedEvents.stream()
                    .map(this::toAccountEvent)
                    .toList();
            final var version = getVersion(storedEvents);
            return new LoadedStream(aggregateId, history, version);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AccountEvent> append(UUID aggregateId, long expectedVersion, AccountEvent newEvent) {
        return List.of();
    }

    @Override
    public List<AccountEvent> append(UUID aggregateId, long expectedVersion, List<AccountEvent> newEvents) {
        return List.of();
    }

    private long getVersion(List<StoredEvent> storedEvents) {
        return storedEvents.isEmpty() ? -1L : storedEvents.getLast().version();
    }

    private AccountEvent toAccountEvent(StoredEvent e) {
        try {
            return mapper.readValue(e.payloadJson(), AccountEvent.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }


}
