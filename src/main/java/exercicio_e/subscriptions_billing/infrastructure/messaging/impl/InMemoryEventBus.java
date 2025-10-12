package exercicio_e.subscriptions_billing.infrastructure.messaging.impl;

import exercicio_e.subscriptions_billing.infrastructure.context.ContextScope;
import exercicio_e.subscriptions_billing.infrastructure.messaging.EventBus;
import exercicio_e.subscriptions_billing.infrastructure.messaging.EventEnvelope;
import exercicio_e.subscriptions_billing.infrastructure.messaging.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lucas
 * @date 11/10/2025 09:56
 */
@Slf4j
public class InMemoryEventBus implements EventBus {

    private final Map<Class<?>, List<EventHandler<?>>> handlers = new ConcurrentHashMap<>();

    @Override
    public void publish(EventEnvelope<?> eventEnvelope) {
        try (final var scope = ContextScope.open(eventEnvelope.correlationId(), eventEnvelope.causationId())) {
            log.debug("[MDC] CorrelationId: {}, CausationId: {}", MDC.get("correlationId"), MDC.get("causationId"));
            final Class<?> eventType = eventEnvelope.event().getClass();
            var eventHandlers = handlers.getOrDefault(eventType, List.of());
            final String name = eventType.getSimpleName();
            if (eventHandlers.isEmpty()) {
                log.warn("No handlers found for event type: {}", name);
                return;
            }
            log.info("Publishing event of type: {} to {} handlers",
                    name, eventHandlers.size());
            for (EventHandler<?> handler : eventHandlers) {
                try {
                    handler.handle(eventEnvelope);
                } catch (Exception e) {
                    log.error("Error handling event of type: {}", name, e);
                }
            }
        }
    }

    @Override
    public <E> AutoCloseable subscribe(Class<E> eventType, EventHandler<E> handler) {
        handlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(handler);
        return () -> handlers.getOrDefault(eventType, List.of()).remove(handler);
    }

}
