package exercicio_e.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlanUpgradedEvent(UUID id, LocalDateTime dateTime, EventType type) implements SubscriptionEvent {

    public PlanUpgradedEvent(UUID id, LocalDateTime dateTime) {
        this(id, dateTime, EventType.PLAN_UPGRADED);
    }

}
