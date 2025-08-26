package exercicio_e.subscriptions_billing.domain.event;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public record TrialStartedEvent(UUID id, Instant timestamp, EventType type) implements SubscriptionEvent {

    public TrialStartedEvent(UUID id, Instant timestamp) {
        this(id, timestamp, EventType.TRIAL_STARTED);
    }

}
