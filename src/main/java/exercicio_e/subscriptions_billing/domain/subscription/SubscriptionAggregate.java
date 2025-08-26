package exercicio_e.subscriptions_billing.domain.subscription;

import exercicio_e.subscriptions_billing.domain.event.*;
import exercicio_e.subscriptions_billing.domain.plan.Plan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Subscription Aggregate.
 *
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public class SubscriptionAggregate {

    private static final Map<UUID, List<SubscriptionEvent>> cache = new HashMap<>();

    private final UUID id;
    private final List<SubscriptionEvent> eventStream;
    private long version = 0L;
    private SubscriptionState state;
    private Plan currentPlan;

    public SubscriptionAggregate(UUID subscriptionId, List<SubscriptionEvent> eventStream) {
        this.id = subscriptionId;
        this.eventStream = eventStream;
        replay(eventStream);
    }

    public UUID getId() {
        return id;
    }

    public List<SubscriptionEvent> getEventStream() {
        return eventStream;
    }

    public long getVersion() {
        return version;
    }

    public SubscriptionState getState() {
        return state;
    }

    public Plan getCurrentPlan() {
        return currentPlan;
    }

    private void replay(List<SubscriptionEvent> events) {
        for (SubscriptionEvent e : events) {
            if (e instanceof TrialStartedEvent) {
                apply((TrialStartedEvent) e);
            } else if (e instanceof SubscriptionConvertedEvent) {
                apply((SubscriptionConvertedEvent) e);
            } else if (e instanceof PlanUpgradedEvent) {
                apply((PlanUpgradedEvent) e);
            } else if (e instanceof PlanDowngradedEvent) {
                apply((PlanDowngradedEvent) e);
            } else if (e instanceof SubscriptionCanceledEvent) {
                apply();
            }
        }
    }

    private void apply(TrialStartedEvent e) {
        this.version = 1L;
        this.state = SubscriptionState.TRIAL;
        this.currentPlan = e.preferredPlan();
    }

    private void apply() {
        this.version++;
        this.state = SubscriptionState.CANCELED;
        this.currentPlan = null;
    }

    private void apply(SubscriptionConvertedEvent e) {
        this.version++;
        this.state = SubscriptionState.ACTIVE;
        this.currentPlan = e.newPlan();
    }

    private void apply(PlanUpgradedEvent e) {
        this.version++;
        this.state = SubscriptionState.ACTIVE;
        this.currentPlan = e.newPlan();
    }

    private void apply(PlanDowngradedEvent e) {
        this.version++;
        this.state = SubscriptionState.ACTIVE;
        this.currentPlan = e.newPlan();
    }

}
