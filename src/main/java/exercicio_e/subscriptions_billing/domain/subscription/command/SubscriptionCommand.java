package exercicio_e.subscriptions_billing.domain.subscription.command;

import exercicio_e.subscriptions_billing.domain.subscription.plan.Plan;

import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public sealed interface SubscriptionCommand {

    /**
     * Command ID.
     *
     * @return this command's ID.
     */
    UUID commandId();

    /**
     * Subscription Aggregate ID.
     *
     * @return this subscription's ID.
     */
    UUID subscriptionId();


    record StartTrial(
            UUID commandId,
            UUID subscriptionId,
            Plan preferredPlan) implements SubscriptionCommand {
    }

    record ConvertSubscription(
            UUID commandId,
            UUID subscriptionId,
            Plan plan) implements SubscriptionCommand {
    }

    record ChangePlan(
            UUID commandId,
            UUID subscriptionId,
            Plan newPlan) implements SubscriptionCommand {
    }

    record CancelSubscription(
            UUID commandId,
            UUID subscriptionId) implements SubscriptionCommand {
    }

}
