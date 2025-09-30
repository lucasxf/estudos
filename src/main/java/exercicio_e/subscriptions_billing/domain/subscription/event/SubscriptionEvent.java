package exercicio_e.subscriptions_billing.domain.subscription.event;

import exercicio_e.subscriptions_billing.domain.subscription.plan.Plan;

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
     * Subscription Aggregate ID.
     *
     * @return this subscription's ID.
     */
    UUID subscriptionId();

    Instant timestamp();

    Plan plan();

}
