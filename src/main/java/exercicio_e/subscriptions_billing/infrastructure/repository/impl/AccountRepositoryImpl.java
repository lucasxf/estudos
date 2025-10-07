package exercicio_e.subscriptions_billing.infrastructure.repository.impl;

import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.EventStore;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.StoredEvent;
import exercicio_e.subscriptions_billing.infrastructure.repository.AccountRepository;
import exercicio_e.subscriptions_billing.infrastructure.serialization.EventMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 23/09/2025
 */
@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final EventStore eventStore;
    private final EventMapper mapper;

    public AccountRepositoryImpl(EventStore eventStore, EventMapper mapper) {
        this.eventStore = eventStore;
        this.mapper = mapper;
    }

    @Override
    public LoadedStream load(String aggregateType, UUID aggregateId) {
        final var storedEvents = eventStore.readStream(AGGREGATE_TYPE, aggregateId.toString());
        final var version = eventStore.getCurrentVersion(AGGREGATE_TYPE, aggregateId.toString());
        final var events = fromStoredEvents(storedEvents);
        return new LoadedStream(aggregateId, events, version);
    }

    @Override
    public List<StoredEvent> append(
            UUID aggregateId,
            long expectedVersion,
            AccountEvent newEvent,
            UUID correlationId,
            UUID causationId) {
        return append(
                aggregateId,
                expectedVersion,
                List.of(newEvent),
                correlationId,
                causationId);
    }

    @Override
    public List<StoredEvent> append(
            UUID aggregateId,
            long expectedVersion,
            List<AccountEvent> newEvents,
            UUID correlationId,
            UUID causationId) {
        final List<String> types = new ArrayList<>();
        final List<String> payloads = new ArrayList<>();
        newEvents.forEach(event -> {
            types.add(event.getClass().getSimpleName());
            payloads.add(mapper.toJson(event));
        });
        return eventStore.appendRaw(
                AGGREGATE_TYPE,
                aggregateId.toString(),
                expectedVersion,
                types,
                payloads,
                correlationId,
                causationId);
    }

    private List<AccountEvent> fromStoredEvents(List<StoredEvent> storedEvents) {
        return storedEvents.stream()
                .map(e -> mapper.<AccountEvent>toDomain(e.type(), e.payloadJson()))
                .toList();
    }

}
