package exercicio_e.subscriptions_billing.service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public sealed interface SubscriptionCommand {

    UUID id();

    LocalDateTime dateTime();

    record StartTrialCommand(UUID id, LocalDateTime dateTime) implements SubscriptionCommand { }

    record ConvertSubscriptionCommand(UUID id, LocalDateTime dateTime) implements SubscriptionCommand { }

    record UpgradePlanCommand(UUID id, LocalDateTime dateTime) implements SubscriptionCommand {}

    record DowngradePlanCommand(UUID id, LocalDateTime dateTime) implements SubscriptionCommand {}

    record CancelSubscriptionCommand(UUID id, LocalDateTime dateTime) implements SubscriptionCommand { }

}
