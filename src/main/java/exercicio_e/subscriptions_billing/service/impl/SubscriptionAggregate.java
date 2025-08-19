package exercicio_e.subscriptions_billing.service.impl;

import exercicio_e.subscriptions_billing.domain.event.SubscriptionEvent;
import exercicio_e.subscriptions_billing.domain.event.TrialStartedEvent;
import exercicio_e.subscriptions_billing.repository.SubscriptionWriteRepository;
import exercicio_e.subscriptions_billing.service.SubscriptionCommand.*;
import exercicio_e.subscriptions_billing.service.SubscriptionService;

import java.util.List;

/**
 * Subscription Command Handler.
 *
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public class SubscriptionAggregate implements SubscriptionService {

    private final SubscriptionWriteRepository writeRepository;

    public SubscriptionAggregate(SubscriptionWriteRepository writeRepository) {
        this.writeRepository = writeRepository;
    }

    @Override
    public List<SubscriptionEvent> handleStartTrialCommand(StartTrialCommand command) {
        TrialStartedEvent event = new TrialStartedEvent(command.id(), command.dateTime());
        return List.of(event);
    }

    @Override
    public List<SubscriptionEvent> handleConvertSubscriptionCommand(ConvertSubscriptionCommand command) {
        return List.of();
    }

    @Override
    public List<SubscriptionEvent> handleUpgradePlanCommand(UpgradePlanCommand command) {
        return List.of();
    }

    @Override
    public List<SubscriptionEvent> handleDowngradePlanCommand(DowngradePlanCommand command) {
        return List.of();
    }

    @Override
    public List<SubscriptionEvent> handleCancelSubscriptionCommand(CancelSubscriptionCommand command) {
        return List.of();
    }

}
