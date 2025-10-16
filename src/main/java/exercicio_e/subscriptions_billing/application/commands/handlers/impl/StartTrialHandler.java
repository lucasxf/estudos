package exercicio_e.subscriptions_billing.application.commands.handlers.impl;

import exercicio_e.subscriptions_billing.application.commands.handlers.CommandHandler;
import exercicio_e.subscriptions_billing.domain.subscription.SubscriptionAggregate;
import exercicio_e.subscriptions_billing.domain.subscription.command.SubscriptionCommand.StartTrial;
import exercicio_e.subscriptions_billing.domain.subscription.event.TrialStarted;
import exercicio_e.subscriptions_billing.infrastructure.context.ContextScope;
import exercicio_e.subscriptions_billing.infrastructure.messaging.EventBus;
import exercicio_e.subscriptions_billing.infrastructure.repository.SubscriptionRepository;
import exercicio_e.subscriptions_billing.infrastructure.serialization.EventMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author lucas
 * @date 16/10/2025 08:30
 */
@Component
public class StartTrialHandler implements CommandHandler<StartTrial> {

    private final SubscriptionRepository repository;
    private final EventBus eventBus;
    private final EventMapper eventMapper;

    public StartTrialHandler(SubscriptionRepository repository, EventBus eventBus, EventMapper eventMapper) {
        this.repository = repository;
        this.eventBus = eventBus;
        this.eventMapper = eventMapper;
    }

    @Override
    public void handle(StartTrial command, UUID correlationId) {
        try (var scope = ContextScope.open(correlationId, command.commandId())) {
            final var subscriptionId = command.subscriptionId();
            final var stream = repository.load(subscriptionId);
            final SubscriptionAggregate subscriptionAggregate = SubscriptionAggregate.from(
                    subscriptionId,
                    stream.history(),
                    stream.lastVersion());

            final TrialStarted trialStarted = subscriptionAggregate.decide(command);
            final var trialStartedEvents = repository.append(
                            subscriptionId,
                            stream.lastVersion(),
                            trialStarted,
                            correlationId,
                            command.commandId())
                    .stream().map(eventMapper::toEnvelope).toList();
            eventBus.publishAll(trialStartedEvents, correlationId, command.commandId());
        }
    }

}
