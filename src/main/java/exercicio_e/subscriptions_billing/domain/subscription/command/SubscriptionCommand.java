package exercicio_e.subscriptions_billing.domain.subscription.command;

import exercicio_e.subscriptions_billing.domain.subscription.plan.Plan;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public sealed interface SubscriptionCommand {

    /**
     * Subscription Aggregate ID.
     *
     * @return this subscription's ID.
     */
    UUID subscriptionId();

    Instant timestamp();

    record StartTrialCommand(
            UUID subscriptionId,
            Instant timestamp,
            Plan preferredPlan) implements SubscriptionCommand {}

    record ConvertSubscriptionCommand(UUID subscriptionId,
                                      Instant timestamp,
                                      Plan plan) implements SubscriptionCommand {}

    record ChangePlanCommand(UUID subscriptionId,
                             Instant timestamp,
                             Plan newPlan) implements SubscriptionCommand {}

    record CancelSubscriptionCommand(UUID subscriptionId, Instant timestamp) implements SubscriptionCommand {}

}
