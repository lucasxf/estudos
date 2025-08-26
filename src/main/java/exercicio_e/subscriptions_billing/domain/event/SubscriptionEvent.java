package exercicio_e.subscriptions_billing.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public interface SubscriptionEvent {

    UUID id();

    Instant timestamp();

    EventType type();

}
