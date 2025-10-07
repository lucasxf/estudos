package exercicio_e.subscriptions_billing.infrastructure.repository.impl;

import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.EventStore;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.StoredEvent;
import exercicio_e.subscriptions_billing.infrastructure.repository.AbstractEventSourcingRepository;
import exercicio_e.subscriptions_billing.infrastructure.repository.UsernameRepository;
import exercicio_e.subscriptions_billing.infrastructure.serialization.EventMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author lucas
 * @date 29/09/2025 19:21
 */
@Repository
public class UsernameRepositoryImpl extends AbstractEventSourcingRepository<UsernameEvent> implements UsernameRepository {

    public UsernameRepositoryImpl(EventStore eventStore, EventMapper eventMapper) {
        super(eventStore, eventMapper);
    }

    @Override
    public LoadedStream load(String usernameKey) {
        var storedEvents = eventStore.readStream(AGGREGATE_TYPE, usernameKey);
        var version = eventStore.getCurrentVersion(AGGREGATE_TYPE, usernameKey);
        var events = deserialize(storedEvents);
        return new LoadedStream(usernameKey, events, version);
    }

    @Override
    public List<StoredEvent> append(
            String usernameKey,
            long expectedVersion,
            UsernameEvent newEvent,
            UUID correlationId,
            UUID causationId) {
        return append(usernameKey,
                expectedVersion,
                List.of(newEvent),
                correlationId,
                causationId);
    }

    @Override
    public List<StoredEvent> append(
            String usernameKey,
            long expectedVersion,
            List<UsernameEvent> newEvents,
            UUID correlationId,
            UUID causationId) {
        return appendDomainEvents(
                AGGREGATE_TYPE,
                usernameKey,
                expectedVersion,
                newEvents,
                correlationId,
                causationId);
    }

}
