package exercicio_e.subscriptions_billing.domain.subscription.command;

import exercicio_e.subscriptions_billing.domain.Command;
import exercicio_e.subscriptions_billing.domain.subscription.plan.Plan;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public sealed interface SubscriptionCommand extends Command {

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

    Instant timestamp();

    record StartTrial(
            UUID commandId,
            Instant timestamp,
            UUID subscriptionId,
            Plan preferredPlan) implements SubscriptionCommand {
    }

    record ConvertSubscription(
            UUID commandId,
            Instant timestamp,
            UUID subscriptionId,
            Plan plan) implements SubscriptionCommand {
    }

    record ChangePlan(
            UUID commandId,
            Instant timestamp,
            UUID subscriptionId,
            Plan newPlan) implements SubscriptionCommand {
    }

    record CancelSubscription(
            UUID commandId,
            Instant timestamp,
            UUID subscriptionId) implements SubscriptionCommand {
    }

}
