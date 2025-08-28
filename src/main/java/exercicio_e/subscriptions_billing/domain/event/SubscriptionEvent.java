package exercicio_e.subscriptions_billing.domain.event;

import exercicio_e.subscriptions_billing.domain.plan.Plan;

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

    /**
     * Event ID.
     *
     * @return this event ID.
     */
    UUID id();

    Instant timestamp();

    Plan plan();

}
