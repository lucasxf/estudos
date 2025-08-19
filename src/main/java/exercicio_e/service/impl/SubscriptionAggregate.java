package exercicio_e.service.impl;

import exercicio_e.domain.event.SubscriptionEvent;
import exercicio_e.service.SubscriptionCommand;
import exercicio_e.service.SubscriptionService;

import java.util.List;

/**
 * Subscription Command Handler.
 *
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public class SubscriptionAggregate implements SubscriptionService {

    @Override
    public List<SubscriptionEvent> handleStartTrialCommand(SubscriptionCommand.StartTrialCommand command) {
        return List.of();
    }

    @Override
    public List<SubscriptionEvent> handleConvertSubscriptionCommand(SubscriptionCommand.ConvertSubscriptionCommand command) {
        return List.of();
    }

    @Override
    public List<SubscriptionEvent> handleUpgradePlanCommand(SubscriptionCommand.UpgradePlanCommand command) {
        return List.of();
    }

    @Override
    public List<SubscriptionEvent> handleDowngradePlanCommand(SubscriptionCommand.DowngradePlanCommand command) {
        return List.of();
    }

    @Override
    public List<SubscriptionEvent> handleCancelSubscriptionCommand(SubscriptionCommand.CancelSubscriptionCommand command) {
        return List.of();
    }

}
