package exercicio_e.subscriptions_billing.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public sealed interface SubscriptionEvent permits
        TrialStartedEvent,
        SubscriptionConvertedEvent,
        SubscriptionCanceledEvent,
        PlanUpgradedEvent,
        PlanDowngradedEvent {

    UUID id();

    Instant timestamp();

}
