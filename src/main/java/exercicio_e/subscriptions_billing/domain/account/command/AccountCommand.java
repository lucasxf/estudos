package exercicio_e.subscriptions_billing.domain.account.command;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
public sealed interface AccountCommand {

    UUID id();

    Instant timestamp();

    String username();

    record CreateAccountCommand(UUID id, Instant timestamp, String username) implements AccountCommand { }

    record DeleteAccountCommand(UUID id, Instant timestamp, String username) implements AccountCommand { }

}
