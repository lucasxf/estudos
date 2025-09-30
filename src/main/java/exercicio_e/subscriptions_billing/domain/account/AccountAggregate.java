package exercicio_e.subscriptions_billing.domain.account;

import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand.CreateAccountCommand;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand.DeleteAccountCommand;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent.AccountCreated;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent.AccountDeleted;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
public class AccountAggregate {

    private final UUID id;
    private final List<AccountEvent> eventStream;

    private long version = -1L;
    private AccountStatus state;

    public AccountAggregate(UUID id, List<AccountEvent> eventStream) {
        if (id == null || eventStream == null) {
            throw new IllegalArgumentException("Event stream and ID cannot be null");
        }
        this.id = id;
        this.eventStream = eventStream;
        this.state = AccountStatus.INACTIVE;
        replay();
    }

    public AccountCreated decide(CreateAccountCommand command) {
        validateCommand(command);
        if (state != AccountStatus.INACTIVE) {
            throw new IllegalStateException("Account already created or deleted");
        }
        return new AccountCreated(command.accountId(), command.timestamp(), command.username());
    }

    public AccountDeleted decide(DeleteAccountCommand command) {
        validateCommand(command);
        if (state == AccountStatus.DELETED) {
            throw new IllegalStateException("Account already deleted");
        }
        if (command.username() == null || command.username().isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return new AccountDeleted(command.accountId(), command.timestamp(), command.username());
    }

    private void replay() {
        for (AccountEvent event : eventStream) {
            if (event instanceof AccountCreated) {
                apply((AccountCreated) event);
            } else if (event instanceof AccountDeleted) {
                apply((AccountDeleted) event);
            }
            this.version++;
        }
    }

    private void validateCommand(AccountCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
        if (command.timestamp() == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        if (command.username() == null || command.username().isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
    }

    private void apply(AccountCreated event) {
        this.state = AccountStatus.ACTIVE;
    }

    private void apply(AccountDeleted event) {
        this.state = AccountStatus.DELETED;
    }

}
