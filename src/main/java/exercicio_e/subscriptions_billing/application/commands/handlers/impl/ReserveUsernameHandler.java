package exercicio_e.subscriptions_billing.application.commands.handlers.impl;

import exercicio_e.subscriptions_billing.application.commands.handlers.CommandHandler;
import exercicio_e.subscriptions_billing.domain.username.UsernameAggregate;
import exercicio_e.subscriptions_billing.domain.username.command.UsernameCommand.ReserveUsername;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent;
import exercicio_e.subscriptions_billing.infrastructure.context.ContextScope;
import exercicio_e.subscriptions_billing.infrastructure.messaging.EventBus;
import exercicio_e.subscriptions_billing.infrastructure.repository.UsernameRepository;
import exercicio_e.subscriptions_billing.infrastructure.serialization.EventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author lucas
 * @date 16/10/2025 08:13
 */
@Slf4j
@Component
public class ReserveUsernameHandler implements CommandHandler<ReserveUsername> {

    private final UsernameRepository repository;
    private final EventBus eventBus;
    private final EventMapper eventMapper;

    public ReserveUsernameHandler(UsernameRepository repository, EventBus eventBus, EventMapper eventMapper) {
        this.repository = repository;
        this.eventBus = eventBus;
        this.eventMapper = eventMapper;
    }

    @Override
    public void handle(ReserveUsername command, UUID correlationId) {
        var usernameKey = command.usernameKey();
        try (var scope = ContextScope.open(correlationId, command.commandId())) {
            log.info("Handling ReserveUsername command for username: {}", usernameKey);

            // 1. Load current state
            final UsernameRepository.LoadedStream usernameStream = repository.load(usernameKey);
            final UsernameAggregate usernameAggregate =
                    UsernameAggregate.from(usernameKey, usernameStream.history(), usernameStream.lastVersion());

            // 2. Decide new events
            final UsernameEvent.UsernameReserved usernameReserved = usernameAggregate.decide(command);

            // 3. Persist new events
            final var usernameEvents = repository.append(
                            usernameKey,
                            usernameStream.lastVersion(),
                            usernameReserved,
                            correlationId,
                            command.commandId())
                    .stream().map(eventMapper::toEnvelope).toList();

            // 4. Publish new events
            eventBus.publishAll(usernameEvents, correlationId, command.commandId());
            log.info("Username {} reserved successfully.", usernameKey);
        }
    }

}
