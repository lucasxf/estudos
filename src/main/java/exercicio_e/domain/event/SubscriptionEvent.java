package exercicio_e.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public record SubscriptionEvent(UUID id, LocalDateTime dateTime, EventType type) implements Event {

    public SubscriptionEvent(UUID id, LocalDateTime dateTime, EventType type) {
        this.id = id;
        this.dateTime = dateTime;
        this.type = type;
    }

    public SubscriptionEvent(LocalDateTime dateTime, EventType type) {
        this(UUID.randomUUID(), dateTime, type);
    }

    public SubscriptionEvent(EventType type) {
        this(LocalDateTime.now(), type);
    }

    public SubscriptionEvent() {
        this(EventType.TRIAL_STARTED);
    }

    public SubscriptionEvent(Event event) {
        this(event.id(), event.dateTime(), event.type());
    }

}
