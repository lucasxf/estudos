package exercicio_e.subscriptions_billing.infrastructure.serialization;

import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.domain.subscription.event.*;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent;
import exercicio_e.subscriptions_billing.infrastructure.exception.EventMappingException;

import java.util.Map;

/**
 * @author lucas
 * @date 29/09/2025 20:16
 */
public final class EventTypeRegistry {

    private static final Map<String, Class<?>> REGISTRY = Map.of(
            // Username
            UsernameEvent.USERNAME_RESERVED, UsernameEvent.UsernameReserved.class,
            UsernameEvent.USERNAME_CLAIMED, UsernameEvent.UsernameClaimed.class,
            UsernameEvent.USERNAME_RELEASED, UsernameEvent.UsernameReleased.class,

            // Account
            AccountEvent.ACCOUNT_CREATED, AccountEvent.AccountCreated.class,
            AccountEvent.ACCOUNT_DELETED, AccountEvent.AccountDeleted.class,

            // Subscription
            SubscriptionEvent.TRIAL_STARTED, TrialStarted.class,
            SubscriptionEvent.SUBSCRIPTION_CONVERTED, SubscriptionConverted.class,
            SubscriptionEvent.SUBSCRIPTION_CANCELED, SubscriptionCanceled.class,
            SubscriptionEvent.PLAN_UPGRADED, PlanUpgraded.class,
            SubscriptionEvent.PLAN_DOWNGRADED, PlanDowngraded.class
    );


    public static Class<?> getEventClass(String eventType) {
        Class<?> clazz = REGISTRY.get(eventType);
        if (clazz == null) {
            throw new EventMappingException("Event type not found: " + eventType);
        }
        return clazz;
    }

}
