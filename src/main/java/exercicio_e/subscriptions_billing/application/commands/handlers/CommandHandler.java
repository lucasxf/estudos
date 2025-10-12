package exercicio_e.subscriptions_billing.application.commands.handlers;

import java.util.UUID;

/**
 * Interface for handling commands of type C.
 *
 * @author lucas
 * @date 12/10/2025 10:00
 */
@FunctionalInterface
public interface CommandHandler<C> {

    /**
     * Handles the given command with the provided correlation ID.
     *
     * @param command
     * @param correlationId
     */
    void handle(C command, UUID correlationId);

}
