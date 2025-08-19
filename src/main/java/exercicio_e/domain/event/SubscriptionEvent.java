package exercicio_e.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SubscriptionEvent {

    UUID id();

    LocalDateTime dateTime();

    EventType type();

}
