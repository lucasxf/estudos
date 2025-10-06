package exercicio_e.subscriptions_billing.application.commands;

import exercicio_e.subscriptions_billing.domain.account.AccountAggregate;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand.CreateAccountCommand;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.domain.username.UsernameAggregate;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent.UsernameReserved;
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
        final String usernameKey = command.usernameKey();
        final var usernameStream = usernameRepository.load(usernameKey);

        // 1. reservar nome de usuário
        final UsernameAggregate usernameAggregate =
                UsernameAggregate.from(usernameKey, usernameStream.history(), usernameStream.lastVersion());
        final UsernameReserved usernameReserved = usernameAggregate.decideReserve();
        final UUID accountId = command.accountId();
        usernameRepository.append(
                usernameKey,
                usernameStream.lastVersion(),
                usernameReserved,
                correlationId,
                accountId);

        // 2. criar conta
        final var accountStream = accountRepository.load(accountId);
        final AccountAggregate accountAggregate =
                AccountAggregate.from(accountId, accountStream.history(), accountStream.lastVersion());
        final AccountEvent.AccountCreated accountCreated = accountAggregate.decide(command);
        accountRepository.append(
                accountId,
                accountStream.lastVersion(),
                accountCreated,
                correlationId,
                accountId);

        return null;
    }

    private void handleDeleteAccountCommand(UUID correlationId, AccountCommand.DeleteAccountCommand cmd) {

    }

}
