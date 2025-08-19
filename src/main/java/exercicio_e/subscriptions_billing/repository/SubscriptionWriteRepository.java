package exercicio_e.subscriptions_billing.repository;

import exercicio_e.subscriptions_billing.domain.event.SubscriptionEvent;

import java.util.List;

/**
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public interface SubscriptionWriteRepository {

    List<SubscriptionEvent> appendEvent(SubscriptionEvent event);

}
