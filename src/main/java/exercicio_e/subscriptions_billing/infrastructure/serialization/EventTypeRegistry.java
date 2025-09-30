package exercicio_e.subscriptions_billing.infrastructure.serialization;

import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent;

import java.util.Map;

/**
 * @author lucas
 * @date 29/09/2025 20:16
 */
public final class EventTypeRegistry {

    private static final Map<String, Class<?>> REGISTRY = Map.of(
            UsernameEvent.USERNAME_RESERVED, UsernameEvent.UsernameReserved.class,
            UsernameEvent.USERNAME_CLAIMED, UsernameEvent.UsernameClaimed.class,
            UsernameEvent.USERNAME_RELEASED, UsernameEvent.UsernameReleased.class);


    public static Class<?> getEventClass(String eventType) {
        return REGISTRY.get(eventType);
    }

}
