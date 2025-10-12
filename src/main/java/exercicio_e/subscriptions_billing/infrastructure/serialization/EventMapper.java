package exercicio_e.subscriptions_billing.infrastructure.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.StoredEvent;
import exercicio_e.subscriptions_billing.infrastructure.exception.EventMappingException;
import exercicio_e.subscriptions_billing.infrastructure.messaging.EventEnvelope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucas
 * @date 29/09/2025 20:16
 */
@Component
public final class EventMapper {

    private final ObjectMapper mapper;

    public EventMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    public <T> T toDomain(String eventType, String json) {
        var eventClass = EventTypeRegistry.getEventClass(eventType);
        return toDomain(eventType, json, (Class<T>) eventClass);
    }

    public <T> T toDomain(String eventType, String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            String preview = json == null ? "null" : json.substring(0, Math.min(160, json.length()));
            throw EventMappingException.forType(eventType, "(payload preview: " + preview + ")", e);
        }
    }

    public String toJson(Object event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new EventMappingException("Falha ao serializar evento: " + event.getClass().getName(), e);
        }
    }

    public <E> EventEnvelope<E> toEnvelope(StoredEvent event) {
        E domainEvent = toDomain(event.type(), event.payloadJson());
        return new EventEnvelope<>(
                event.aggregateType(),
                event.aggregateId(),
                event.version(),
                event.occurredAt(),
                domainEvent,
                event.correlationId(),
                event.causationId());
    }

    public SerializedBatch serializeBatch(List<?> events) {
        final List<String> types = new ArrayList<>(events.size());
        final List<String> payloads = new ArrayList<>(events.size());
        events.forEach(event -> {
            types.add(event.getClass().getSimpleName());
            payloads.add(toJson(event));
        });
        return new SerializedBatch(List.copyOf(types), List.copyOf(payloads));
    }

}
