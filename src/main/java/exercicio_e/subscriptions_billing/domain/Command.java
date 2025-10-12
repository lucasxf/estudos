package exercicio_e.subscriptions_billing.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * @author lucas
 * @date 12/10/2025 10:29
 */
public interface Command {

    UUID commandId();

    Instant timestamp();

}
