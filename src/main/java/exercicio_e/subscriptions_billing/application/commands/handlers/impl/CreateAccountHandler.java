package exercicio_e.subscriptions_billing.application.commands.handlers.impl;

import exercicio_e.subscriptions_billing.application.commands.handlers.CommandHandler;
import exercicio_e.subscriptions_billing.domain.account.AccountAggregate;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand.CreateAccount;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.infrastructure.context.ContextScope;
import exercicio_e.subscriptions_billing.infrastructure.messaging.EventBus;
import exercicio_e.subscriptions_billing.infrastructure.repository.AccountRepository;
import exercicio_e.subscriptions_billing.infrastructure.serialization.EventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author lucas
 * @date 12/10/2025 10:01
 */
@Slf4j
@Component
public class CreateAccountHandler implements CommandHandler<CreateAccount> {

    private final AccountRepository repository;
    private final EventBus eventBus;
    private final EventMapper eventMapper;

    public CreateAccountHandler(AccountRepository repository, EventBus eventBus, EventMapper eventMapper) {
        this.repository = repository;
        this.eventBus = eventBus;
        this.eventMapper = eventMapper;
    }

    @Override
    public void handle(CreateAccount command, UUID correlationId) {
        try (final var scope = ContextScope.open(correlationId, command.commandId())) {
            log.info("Handling CreateAccount command for username: {}", command.username());
            var accountId = command.accountId();
            final var accountCommandId = command.commandId();

            // 1. Load current state
            final var accountStream = repository.load(accountId);
            final var accountAggregate = AccountAggregate.from(
                    accountId, accountStream.history(), accountStream.lastVersion());

            // 2. Decide new events
            final AccountEvent.AccountCreated accountCreated = accountAggregate.decide(command);

            // 3. Persist new events
            final var accountEvents = repository.append(
                            accountId,
                            accountStream.lastVersion(),
                            accountCreated,
                            correlationId,
                            accountCommandId)
                    .stream().map(eventMapper::toEnvelope).toList();

            // 4. Publish new events
            eventBus.publishAll(accountEvents, correlationId, accountCommandId);
            log.info("Account {} created successfully for username: {}", accountId, command.username());
        }
    }

}
