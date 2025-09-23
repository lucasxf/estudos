package exercicio_e.subscriptions_billing.application.commands;

import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.infrastructure.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
@Component
public class AccountCommandHandler {

    private final AccountRepository repository;

    public AccountCommandHandler(AccountRepository repository) {
        this.repository = repository;
    }

    public List<AccountEvent> handle(
            UUID correlationId, AccountCommand command) {
        switch (command) {
            case AccountCommand.CreateAccountCommand cmd -> handleCreateAccountCommand(cmd);
        }
        return null;
    }

    public List<AccountEvent> handleCreateAccountCommand(AccountCommand command) {
        repository.getEventsById(command.id());
        return null;
    }

}
