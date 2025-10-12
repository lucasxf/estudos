package exercicio_e.subscriptions_billing.infrastructure.messaging;

/**
 * @author lucas
 * @date 09/10/2025 08:31
 */
@FunctionalInterface
public interface EventHandler<E> {

    /**
     * Handles the given event.
     *
     * @param event
     */
    void handle(EventEnvelope<?> event);

}
