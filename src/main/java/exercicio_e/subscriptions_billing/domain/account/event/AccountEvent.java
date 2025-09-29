package exercicio_e.subscriptions_billing.domain.account.event;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
public sealed interface AccountEvent {

    UUID id();

    Instant timestamp();

    String username();

    record AccountCreated(UUID id, Instant timestamp, String username) implements AccountEvent { }

    record AccountDeleted(UUID id, Instant timestamp, String username) implements AccountEvent { }

}
