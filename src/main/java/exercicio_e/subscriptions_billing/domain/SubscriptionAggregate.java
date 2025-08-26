package exercicio_e.subscriptions_billing.domain;

import exercicio_e.subscriptions_billing.domain.event.SubscriptionConvertedEvent;
import exercicio_e.subscriptions_billing.domain.event.SubscriptionEvent;
import exercicio_e.subscriptions_billing.domain.event.TrialStartedEvent;
import exercicio_e.subscriptions_billing.domain.subscription.SubscriptionState;
import exercicio_e.subscriptions_billing.repository.SubscriptionStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Subscription Command Handler.
 *
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public class SubscriptionAggregate {

    private static final Map<UUID, List<SubscriptionEvent>> cache = new HashMap<>();

    private final List<SubscriptionEvent> eventStream;
    private long version;
    private SubscriptionState state;
    private UUID id;
    private boolean hasTrialStarted;

    public SubscriptionAggregate(List<SubscriptionEvent> eventStream) {
        this.eventStream = eventStream;
        replay(eventStream);
    }

    private void replay(List<SubscriptionEvent> events) {
        for (SubscriptionEvent e : events) {
            switch(e.type()) {
                case TRIAL_STARTED -> apply((TrialStartedEvent) e);
                case SUBSCRIPTION_CONVERTED -> apply((SubscriptionConvertedEvent) e);
            }
        }
    }

    private void apply(TrialStartedEvent e) {
        this.version = 1L;
        this.state = SubscriptionState.TRIAL;
        this.id = e.id();
        this.hasTrialStarted = true;
    }

    private void apply(SubscriptionConvertedEvent e) {

    }

}
