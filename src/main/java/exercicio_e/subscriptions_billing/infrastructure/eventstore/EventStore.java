package exercicio_e.subscriptions_billing.infrastructure.eventstore;

import exercicio_e.subscriptions_billing.infrastructure.exception.ConcurrencyException;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
@Repository
public class EventStore {

    private static final Map<String, List<StoredEvent>> eventsById = new ConcurrentHashMap<>();

    public List<StoredEvent> appendRaw(
            String aggregateType,
            String aggregateId,
            long expectedVersion,
            List<String> eventTypes,
            List<String> payloadJson,
            UUID correlationId,
            UUID causationId) {
        preValidate(aggregateType, aggregateId, eventTypes, payloadJson);
        final var key = storeKey(aggregateType, aggregateId);
        final var eventStream = eventsById.computeIfAbsent(key, __ -> new ArrayList<>());
        synchronized (eventStream) {
            long currentVersion = getCurrentVersion(aggregateType, aggregateId);
            if (currentVersion != expectedVersion) {
                throw new ConcurrencyException("Expected version[ " + expectedVersion + "] but was [" + currentVersion + "]");
            }
            var newEvents = new ArrayList<StoredEvent>(eventTypes.size());
            for (int i = 0; i < eventTypes.size(); i++) {
                var event = new StoredEvent(
                        UUID.randomUUID(),
                        eventTypes.get(i),
                        aggregateType,
                        aggregateId,
                        ++currentVersion,
                        Instant.now(),
                        correlationId,
                        causationId,
                        payloadJson.get(i));
                newEvents.add(event);
            }
            eventStream.addAll(newEvents);
            return List.copyOf(newEvents);
        }
    }

    public List<StoredEvent> appendOne(
            String aggregateType,
            String aggregateId,
            long expectedVersion,
            String eventType,
            String payloadJson,
            UUID correlationId,
            UUID causationId) {
        return appendRaw(
                aggregateType,
                aggregateId,
                expectedVersion,
                List.of(eventType),
                List.of(payloadJson),
                correlationId,
                causationId);
    }

    public long getCurrentVersion(String aggregateType, String aggregateId) {
        var key = storeKey(aggregateType, aggregateId);
        var events = eventsById.getOrDefault(key, new ArrayList<>());
        return getLastVersion(events);
    }

    private static long getLastVersion(List<StoredEvent> events) {
        return events.isEmpty() ? -1 : events.getLast().version();
    }

    public List<StoredEvent> readStream(
            String aggregateType, String aggregateId) {
        var key = storeKey(aggregateType, aggregateId);
        return List.copyOf(eventsById.getOrDefault(key, new ArrayList<>()));
    }

    private String storeKey(String aggregateType, String aggregateId) {
        return aggregateType + ":" + aggregateId;
    }

    private void preValidate(String aggregateType, String aggregateId, List<String> eventTypes,
                             List<String> payloadJson) {
        if (aggregateType == null || aggregateType.isBlank()) {
            throw new IllegalArgumentException("aggregateType is required");
        }
        if (aggregateId == null || aggregateId.isBlank()) {
            throw new IllegalArgumentException("aggregateId is required");
        }
        if (eventTypes == null || payloadJson == null || eventTypes.size() != payloadJson.size()) {
            throw new IllegalArgumentException("types/payload size mismatch");
        }
    }

}
