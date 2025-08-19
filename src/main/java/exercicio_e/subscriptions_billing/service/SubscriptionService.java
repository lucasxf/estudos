package exercicio_e.subscriptions_billing.service;

import exercicio_e.subscriptions_billing.domain.event.SubscriptionEvent;
import exercicio_e.subscriptions_billing.service.SubscriptionCommand.StartTrialCommand;
import exercicio_e.subscriptions_billing.service.SubscriptionCommand.UpgradePlanCommand;

import java.util.List;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public interface SubscriptionService {

    default List<SubscriptionEvent> handleCommand(SubscriptionCommand command) {
        return switch (command) {
            case StartTrialCommand cmd -> handleStartTrialCommand(cmd);
            case UpgradePlanCommand cmd -> handleUpgradePlanCommand(cmd);
            default -> throw new IllegalStateException("Unexpected value: " + command);
        };
    }

    List<SubscriptionEvent> handleStartTrialCommand(SubscriptionCommand.StartTrialCommand command);

    List<SubscriptionEvent> handleConvertSubscriptionCommand(SubscriptionCommand.ConvertSubscriptionCommand command);

    List<SubscriptionEvent> handleUpgradePlanCommand(SubscriptionCommand.UpgradePlanCommand command);

    List<SubscriptionEvent> handleDowngradePlanCommand(SubscriptionCommand.DowngradePlanCommand command);

    List<SubscriptionEvent> handleCancelSubscriptionCommand(SubscriptionCommand.CancelSubscriptionCommand command);

}
