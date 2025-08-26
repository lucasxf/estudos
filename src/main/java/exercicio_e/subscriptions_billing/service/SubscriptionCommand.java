package exercicio_e.subscriptions_billing.service;

import exercicio_e.subscriptions_billing.domain.plan.Plan;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public sealed interface SubscriptionCommand {

    UUID id();

    Instant timestamp();

    record StartTrialCommand(UUID id, Instant timestamp, Plan preferredPlan) implements SubscriptionCommand { }

    record ConvertSubscriptionCommand(UUID id, Instant timestamp, Plan plan) implements SubscriptionCommand { }

    record ChangePlanCommand(UUID id, Instant timestamp, Plan newPlan) implements SubscriptionCommand {}

    record CancelSubscriptionCommand(UUID id, Instant timestamp) implements SubscriptionCommand { }

}
