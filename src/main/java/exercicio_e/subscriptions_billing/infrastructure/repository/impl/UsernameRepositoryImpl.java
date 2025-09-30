package exercicio_e.subscriptions_billing.infrastructure.repository.impl;

import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.EventStore;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.StoredEvent;
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
public class UsernameRepositoryImpl implements UsernameRepository {

    private final EventStore eventStore;
    private final EventMapper eventMapper;

    public UsernameRepositoryImpl(EventStore eventStore, EventMapper eventMapper) {
        this.eventStore = eventStore;
        this.eventMapper = eventMapper;
    }

    @Override
    public LoadedStream load(String usernameKey) {
        var storedEvents = eventStore.readStream(AGGREGATE_TYPE, usernameKey);
        var events = fromStoredEvents(storedEvents);
        var version = getVersion(storedEvents);
        return new LoadedStream(usernameKey, events, version);
    }

    @Override
    public List<StoredEvent> append(String usernameKey,
                                    long expectedVersion,
                                    UsernameEvent newEvent,
                                    UUID correlationId,
                                    UUID causationId) {
        return append(usernameKey, expectedVersion,
                List.of(newEvent),
                correlationId,
                causationId);
    }

    @Override
    public List<StoredEvent> append(String usernameKey,
                                    long expectedVersion,
                                    List<UsernameEvent> newEvents,
                                    UUID correlationId,
                                    UUID causationId) {
        for (var event : newEvents) {
            var storedEvent = new StoredEvent(
                    event.id,
                    AGGREGATE_TYPE,
                    usernameKey,
                    expectedVersion + 1,
                    correlationId,
                    causationId,
                    eventMapper
            );
            var appended = eventStore.append(AGGREGATE_TYPE, usernameKey, storedEvent);
            expectedVersion = getVersion(appended);
        }
        return null;
    }

    private List<UsernameEvent> fromStoredEvents(List<StoredEvent> storedEvents) {
        return storedEvents.stream()
                .map(e -> eventMapper.<UsernameEvent>toDomain(e.type(), e.payloadJson()))
                .toList();
    }

    private long getVersion(List<StoredEvent> events) {
        return events.isEmpty() ? -1L : events.getLast().version();
    }

}
