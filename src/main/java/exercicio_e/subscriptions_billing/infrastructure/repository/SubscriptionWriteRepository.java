package exercicio_e.subscriptions_billing.infrastructure.repository;

import exercicio_e.subscriptions_billing.domain.account.Account;
import exercicio_e.subscriptions_billing.domain.subscription.event.SubscriptionEvent;

import java.util.List;
import java.util.UUID;

/**
 * Subscription write-side repository.
 *
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public interface SubscriptionWriteRepository {


    List<SubscriptionEvent> appendEvent(UUID subscriptionId, SubscriptionEvent event);

    default List<SubscriptionEvent> getEventsByAccount(Account account) {
        return getEventsByAccountId(account.getId());
    }

    List<SubscriptionEvent> getEventsByAccountId(UUID id);


}
