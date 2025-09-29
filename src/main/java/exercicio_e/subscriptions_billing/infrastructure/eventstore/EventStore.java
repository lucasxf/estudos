package exercicio_e.subscriptions_billing.infrastructure.eventstore;

import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
@Repository
public class EventStore {

    private static final Map<String, List<StoredEvent>> eventsById = new HashMap<>();

    public List<StoredEvent> append(
            String aggregateType, String aggregateId, StoredEvent event) {
        var key = storeKey(aggregateType, aggregateId);
        final var events = eventsById.getOrDefault(key, new ArrayList<>());
        events.add(event);
        eventsById.putIfAbsent(key, events);
        return events;
    }

    public List<StoredEvent> readStream(
            String aggregateType, String aggregateId) {
        var key = storeKey(aggregateType, aggregateId);
        return eventsById.getOrDefault(key, new ArrayList<>());
    }

    public long currentVersion(
            String aggregateType, UUID aggregateId) {
        return -1L;
    }

    private String storeKey(String aggregateType, String aggregateId) {
        return aggregateType + ":" + aggregateId;
    }

}
