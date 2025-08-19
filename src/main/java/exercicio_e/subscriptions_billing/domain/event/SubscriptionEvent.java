package exercicio_e.subscriptions_billing.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public interface SubscriptionEvent {

    UUID id();

    LocalDateTime dateTime();

    EventType type();

}
