package exercicio_e.subscriptions_billing.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public record PlanDowngradedEvent(UUID id, LocalDateTime dateTime, EventType type) implements SubscriptionEvent {

    public PlanDowngradedEvent(UUID id, LocalDateTime dateTime) {
        this(id, dateTime, EventType.PLAN_DOWNGRADED);
    }

}
