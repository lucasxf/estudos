package exercicio_e.subscriptions_billing.application.commands.handlers.impl;

import exercicio_e.subscriptions_billing.application.commands.handlers.CommandHandler;
import exercicio_e.subscriptions_billing.domain.username.UsernameAggregate;
import exercicio_e.subscriptions_billing.domain.username.command.UsernameCommand.ClaimUsername;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent;
import exercicio_e.subscriptions_billing.infrastructure.messaging.EventBus;
import exercicio_e.subscriptions_billing.infrastructure.repository.UsernameRepository;
import exercicio_e.subscriptions_billing.infrastructure.serialization.EventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author lucas
 * @date 16/10/2025 08:25
 */
@Slf4j
@Component
public class ClaimUsernameHandler implements CommandHandler<ClaimUsername> {

    private final UsernameRepository repository;
    private final EventBus eventBus;
    private final EventMapper eventMapper;

    public ClaimUsernameHandler(UsernameRepository repository, EventBus eventBus, EventMapper eventMapper) {
        this.repository = repository;
        this.eventBus = eventBus;
        this.eventMapper = eventMapper;
    }

    @Override
    public void handle(ClaimUsername command, UUID correlationId) {
        final var usernameKey = command.usernameKey();
        try (var scope = exercicio_e.subscriptions_billing.infrastructure.context.ContextScope.open(correlationId,
                command.commandId())) {
            log.info("Handling ClaimUsername command for username: {}", usernameKey);
            final UsernameRepository.LoadedStream usernameReload = repository.load(usernameKey);
            final UsernameAggregate usernameAggRefresh =
                    UsernameAggregate.from(usernameKey, usernameReload.history(), usernameReload.lastVersion());
            final UsernameEvent.UsernameClaimed usernameClaimed =
                    usernameAggRefresh.decide(command);
            final var usernameClaimedEvents = repository.append(
                            usernameKey,
                            usernameReload.lastVersion(),
                            usernameClaimed,
                            correlationId,
                            command.commandId())
                    .stream().map(eventMapper::toEnvelope).toList();
            eventBus.publishAll(usernameClaimedEvents, correlationId, command.commandId());
            log.info("Username {} claimed successfully for account {}.", usernameKey, command.accountId());
        }
    }

}
