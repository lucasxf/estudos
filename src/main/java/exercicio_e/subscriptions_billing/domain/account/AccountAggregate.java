package exercicio_e.subscriptions_billing.domain.account;

import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand.CreateAccount;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand.DeleteAccount;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent.AccountCreated;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent.AccountDeleted;
import exercicio_e.subscriptions_billing.domain.exception.AccountCreationException;
import exercicio_e.subscriptions_billing.domain.exception.DomainException;
import exercicio_e.subscriptions_billing.domain.exception.InvalidAccountException;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */

public class AccountAggregate {

    private final UUID id;
    private final List<AccountEvent> history;
    private long version = -1L;
    @Getter
    private AccountStatus status;

    private AccountAggregate(UUID id, List<AccountEvent> history, long lastVersion) {
        if (id == null || history == null) {
            throw new InvalidAccountException("Event stream and ID cannot be null");
        }
        this.id = id;
        this.history = history;
        this.status = AccountStatus.NEW;
        replay();
    }

    public static AccountAggregate from(UUID id, List<AccountEvent> history, long lastVersion) {
        final var aggregate = new AccountAggregate(id, history, lastVersion);
        aggregate.version = lastVersion;
        return aggregate;
    }

    public AccountCreated decide(CreateAccount command) {
        validateCommand(command);
        if (status != AccountStatus.NEW) {
            throw new AccountCreationException(
                    "Uma conta já com este nome de usuário já foi criada " +
                            "ou está em um estado inválido: " + command.username());
        }
        return new AccountCreated(command.accountId(), command.timestamp(), command.username());
    }

    public AccountDeleted decide(DeleteAccount command) {
        validateCommand(command);
        if (status == AccountStatus.DELETED) {
            throw new IllegalStateException("Account already deleted");
        }
        if (command.username() == null || command.username().isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return new AccountDeleted(command.accountId(), command.timestamp(), command.username());
    }

    private void replay() {
        for (AccountEvent event : history) {
            switch (event) {
                case AccountCreated e -> apply(e);
                case AccountDeleted e -> apply(e);
                default -> throw new DomainException("Unexpected value: " + event);
            }
        }
    }

    private void validateCommand(AccountCommand command) {
        if (command == null) {
            throw new DomainException("Comando não pode ser nulo");
        }
        if (command.username() == null || command.username().isBlank()) {
            throw new InvalidAccountException("Nome de usuário inválido");
        }
    }

    private void apply(AccountCreated event) {
        this.status = AccountStatus.ACTIVE;
    }

    private void apply(AccountDeleted event) {
        this.status = AccountStatus.DELETED;
    }

}
