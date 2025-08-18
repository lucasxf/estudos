package exercicio_e.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlanDowngradedEvent(UUID id, LocalDateTime dateTime, EventType type) implements Event {

    public PlanDowngradedEvent(UUID id, LocalDateTime dateTime) {
        this(id, dateTime, EventType.PLAN_DOWNGRADED);
    }

}
