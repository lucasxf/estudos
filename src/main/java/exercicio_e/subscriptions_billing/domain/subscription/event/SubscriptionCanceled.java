package exercicio_e.subscriptions_billing.domain.subscription.event;

import exercicio_e.subscriptions_billing.domain.subscription.plan.Plan;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public record SubscriptionCanceled(
        UUID subscriptionId,
        UUID accountId,
        Instant timestamp,
        Plan canceledPlan) implements SubscriptionEvent {

    @Override
    public Plan plan() {
        return canceledPlan;
    }

}
