package exercicio_e.subscriptions_billing.domain.subscription.event;

import exercicio_e.subscriptions_billing.domain.subscription.plan.Plan;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public record SubscriptionCanceledEvent(
        UUID id,
        Instant timestamp,
        Plan canceledPlan) implements SubscriptionEvent {

    @Override
    public Plan plan() {
        return canceledPlan;
    }

}
