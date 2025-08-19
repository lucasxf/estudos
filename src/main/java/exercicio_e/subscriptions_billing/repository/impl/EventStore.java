package exercicio_e.subscriptions_billing.repository.impl;

import exercicio_e.subscriptions_billing.domain.event.SubscriptionEvent;
import exercicio_e.subscriptions_billing.repository.SubscriptionWriteRepository;

import java.util.*;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public class EventStore implements SubscriptionWriteRepository {

    private final Map<UUID, List<SubscriptionEvent>> store = new HashMap<>();

    @Override
    public List<SubscriptionEvent> appendEvent(SubscriptionEvent event) {
        List<SubscriptionEvent> events = store.computeIfAbsent(event.id(), k -> new ArrayList<>());
        events.add(event);
        return events;
    }

}
