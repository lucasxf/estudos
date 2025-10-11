package exercicio_e.subscriptions_billing.infrastructure.messaging;

import java.util.Collection;

/**
 * @author lucas
 * @date 09/10/2025 07:51
 */
public interface EventBus {

    /**
     * Publishes an event to the event bus.
     *
     * @param event
     */
    void publish(EventEnvelope<?> event);

    /**
     *
     * @param events
     */
    default void publishAll(Collection<? extends EventEnvelope<?>> events) {
        if (events == null || events.isEmpty()) {
            return;
        }
        events.forEach(this::publish);
    }

    /**
     * Subscribes to events of the specified type with the given handler.
     *
     * @param eventType
     * @param handler
     * @param <E>
     */
    <E> AutoCloseable subscribe(Class<E> eventType, EventHandler<E> handler);

}
