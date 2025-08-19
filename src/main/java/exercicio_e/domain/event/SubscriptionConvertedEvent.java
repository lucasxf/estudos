package exercicio_e.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubscriptionConvertedEvent(UUID id, LocalDateTime dateTime, EventType type) implements SubscriptionEvent {

    public SubscriptionConvertedEvent(UUID id, LocalDateTime dateTime) {
        this(id, dateTime, EventType.SUBSCRIPTION_CONVERTED);
    }

}
