package exercicio_e.subscriptions_billing.infrastructure.messaging;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lucas
 * @date 11/10/2025 09:56
 */
public class InMemoryEventBus implements EventBus {

    private final Map<Class<?>, List<EventHandler<?>>> handlers = new ConcurrentHashMap<>();

    @Override
    public void publish(EventEnvelope<?> event) {
        List<EventHandler<?>> eventHandlers = handlers.getOrDefault(event.event().getClass(), List.of());
        for (EventHandler handler : eventHandlers) {
            handler.handle(event);
        }
    }

    @Override
    public <E> AutoCloseable subscribe(Class<E> eventType, EventHandler<E> handler) {
        handlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(handler);
        return () -> handlers.getOrDefault(eventType, List.of()).remove(handler);
    }

}
