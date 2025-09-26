package exercicio_e.subscriptions_billing.application.commands;

import exercicio_e.subscriptions_billing.domain.account.AccountAggregate;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand.CreateAccountCommand;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.infrastructure.repository.AccountRepository;
import exercicio_e.subscriptions_billing.infrastructure.repository.UsernameRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
@Component
public class AccountCommandHandler {

    private final UsernameRepository usernameRepository;
    private final AccountRepository accountRepository;

    public AccountCommandHandler(UsernameRepository usernameRepository, AccountRepository accountRepository) {
        this.usernameRepository = usernameRepository;
        this.accountRepository = accountRepository;
    }

    public List<AccountEvent> handle(
            UUID correlationId, AccountCommand command) {
        switch (command) {
            case CreateAccountCommand cmd -> handleCreateAccountCommand(correlationId, cmd);
            case AccountCommand.DeleteAccountCommand cmd -> handleDeleteAccountCommand(correlationId, cmd);
        }
        return null;
    }

    public List<AccountEvent> handleCreateAccountCommand(
            UUID correlationId, CreateAccountCommand command) {

        var stream = accountRepository.load(command.id());
        var aggregate = new AccountAggregate(command.id(), stream.history());
        var event = aggregate.decide(command);
        return null;
    }

    private void handleDeleteAccountCommand(UUID correlationId, AccountCommand.DeleteAccountCommand cmd) {

    }

}
