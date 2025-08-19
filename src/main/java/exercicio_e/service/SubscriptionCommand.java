package exercicio_e.service;

import java.time.LocalDateTime;
import java.util.UUID;

public sealed interface SubscriptionCommand {

    UUID id();

    LocalDateTime dateTime();

    record StartTrialCommand(UUID id, LocalDateTime dateTime) implements SubscriptionCommand { }

    record ConvertSubscriptionCommand(UUID id, LocalDateTime dateTime) implements SubscriptionCommand { }

    record UpgradePlanCommand(UUID id, LocalDateTime dateTime) implements SubscriptionCommand {}

    record DowngradePlanCommand(UUID id, LocalDateTime dateTime) implements SubscriptionCommand {}

    record CancelSubscriptionCommand(UUID id, LocalDateTime dateTime) implements SubscriptionCommand { }

}
