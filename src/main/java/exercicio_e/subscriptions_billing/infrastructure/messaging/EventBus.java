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
     * @param <E>
     */
    <E> void publish(EventEnvelope<E> event);

    /**
     * Publishes a collection of events to the event bus.
     *
     * @param events
     * @param <E>
     */
    default <E> void publishAll(Collection<EventEnvelope<E>> events) {
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
    <E> void subscribe(Class<E> eventType, EventHandler<E> handler);

}
