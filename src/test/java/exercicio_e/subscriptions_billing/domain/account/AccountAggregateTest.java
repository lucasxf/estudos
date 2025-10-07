package exercicio_e.subscriptions_billing.domain.account;

import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountAggregateTest {

    @Test
    void givenInactiveAccount_WhenCreateAccount_ThenAccountCreated() {
        // given
        final UUID accountId = UUID.randomUUID();
        final var aggregate = AccountAggregate.from(accountId, List.of(), 0);
        final var command = createAccountCommand("Lucas", "lucas");

        // when
        final var result = aggregate.decide(command);

        // then
        assertEquals(AccountStatus.NEW, aggregate.getStatus());
        assertEquals(command.accountId(), result.accountId());
        assertEquals(command.timestamp(), result.timestamp());
        assertEquals(command.username(), result.username());
    }

    @Test
    void

    private static AccountCommand.CreateAccount createAccountCommand(
            String username, String usernameKey) {
        return new AccountCommand.CreateAccount(
                UUID.randomUUID(),
                UUID.randomUUID(),
                Instant.now(),
                username,
                usernameKey);
    }

    private static AccountEvent.AccountCreated accountCreated(
            UUID accountId, String username) {
        return new AccountEvent.AccountCreated(
                accountId,
                Instant.now(),
                username);
    }

}