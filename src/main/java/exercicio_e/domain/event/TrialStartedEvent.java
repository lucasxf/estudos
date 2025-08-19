package exercicio_e.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public record TrialStartedEvent(UUID id, LocalDateTime dateTime, EventType type) implements SubscriptionEvent {

    public TrialStartedEvent(UUID id, LocalDateTime dateTime) {
        this(id, dateTime, EventType.TRIAL_STARTED);
    }

}
