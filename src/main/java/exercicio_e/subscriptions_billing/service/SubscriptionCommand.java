package exercicio_e.subscriptions_billing.service;

import exercicio_e.subscriptions_billing.domain.plan.Plan;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public sealed interface SubscriptionCommand {

    UUID id();

    LocalDateTime startDateTime();

    LocalDateTime endDateTime();

    record StartTrialCommand(UUID id, LocalDateTime startDateTime, LocalDateTime endDateTime) implements SubscriptionCommand { }

    record ConvertSubscriptionCommand(UUID id, LocalDateTime startDateTime, LocalDateTime endDateTime) implements SubscriptionCommand { }

    record ChangePlanCommand(UUID id, LocalDateTime startDateTime, LocalDateTime endDateTime, Plan newPlan) implements SubscriptionCommand {}

    record CancelSubscriptionCommand(UUID id, LocalDateTime startDateTime, LocalDateTime endDateTime) implements SubscriptionCommand { }

}
