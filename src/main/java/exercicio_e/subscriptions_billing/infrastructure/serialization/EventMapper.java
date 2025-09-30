package exercicio_e.subscriptions_billing.infrastructure.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exercicio_e.subscriptions_billing.infrastructure.serialization.exception.EventMappingException;

/**
 * @author lucas
 * @date 29/09/2025 20:16
 */
public final class EventMapper {

    private final ObjectMapper mapper;

    public EventMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    public <T> T toDomain(String eventType, String json) {
        var eventClass = EventTypeRegistry.getEventClass(eventType);
        if (eventClass == null) {
            throw new EventMappingException("Tipo de evento não pode ser nulo: " + eventType);
        }
        try {
            return (T) mapper.readValue(json, eventClass);
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

}
