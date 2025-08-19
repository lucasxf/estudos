package exercicio_e.service;

import exercicio_e.domain.event.SubscriptionEvent;
import exercicio_e.service.SubscriptionCommand.*;

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

    List<SubscriptionEvent> handleStartTrialCommand(StartTrialCommand command);

    List<SubscriptionEvent> handleConvertSubscriptionCommand(ConvertSubscriptionCommand command);

    List<SubscriptionEvent> handleUpgradePlanCommand(UpgradePlanCommand command);

    List<SubscriptionEvent> handleDowngradePlanCommand(DowngradePlanCommand command);

    List<SubscriptionEvent> handleCancelSubscriptionCommand(CancelSubscriptionCommand command);

}
