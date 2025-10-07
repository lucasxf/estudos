package exercicio_e.subscriptions_billing.domain.subscription.event;

import exercicio_e.subscriptions_billing.domain.subscription.plan.Plan;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public sealed interface SubscriptionEvent permits
        TrialStarted,
        SubscriptionConverted,
        SubscriptionCanceled,
        PlanUpgraded,
        PlanDowngraded {

    String TRIAL_STARTED = "TrialStarted";
    String SUBSCRIPTION_CONVERTED = "SubscriptionConverted";
    String SUBSCRIPTION_CANCELED = "SubscriptionCanceled";
    String PLAN_UPGRADED = "PlanUpgraded";
    String PLAN_DOWNGRADED = "PlanDowngraded";


    /**
     * Subscription Aggregate ID.
     *
     * @return this subscription's ID.
     */
    UUID subscriptionId();

    Instant timestamp();

    Plan plan();

}
