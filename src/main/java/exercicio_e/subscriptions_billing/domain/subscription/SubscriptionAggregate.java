package exercicio_e.subscriptions_billing.domain.subscription;

import exercicio_e.subscriptions_billing.domain.event.*;
import exercicio_e.subscriptions_billing.domain.plan.Plan;
import exercicio_e.subscriptions_billing.service.SubscriptionCommand;
import exercicio_e.subscriptions_billing.service.SubscriptionCommand.*;

import java.time.Instant;
import java.time.Period;
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

    public SubscriptionAggregate(UUID subscriptionId) {
        this(subscriptionId, null);
    }

    public SubscriptionAggregate(UUID subscriptionId, List<SubscriptionEvent> eventStream) {
        if (subscriptionId == null) {
            throw new RuntimeException("Invalid aggregate ID.");
        }
        this.id = subscriptionId;
        this.eventStream = eventStream;
        replay(eventStream);
    }

    public TrialStartedEvent decide(StartTrialCommand cmd) {
        preValidateCommand(cmd);
        if (state != null) {
            throw new RuntimeException("Invalid subscription state: " + state);
        }
        if (!id.equals(cmd.id())) {
            throw new RuntimeException("Invalid subscription ID: " + cmd.id());
        }
        Instant start = cmd.timestamp();
        Instant trialEnd = calculateTrialEnd(start);
        return new TrialStartedEvent(
                cmd.id(), start, trialEnd, cmd.preferredPlan());
    }

    public SubscriptionConvertedEvent decide(ConvertSubscriptionCommand cmd) {
        preValidateCommand(cmd);
        if (SubscriptionState.TRIAL != state) {
            throw new RuntimeException("Cannot convert a subscription that is not on trial");
        }
        // auto conversion
        if (cmd.plan() == null && currentPlan == null) {
            throw new RuntimeException("Cannot convert to invalid plan");
        }
        return new SubscriptionConvertedEvent(
                cmd.id(), cmd.timestamp(), cmd.plan());
    }

    public SubscriptionEvent decide(ChangePlanCommand cmd) {
        preValidateCommand(cmd);
        return null;
        /*if (currentPlan.equals(cmd.newPlan())) {
            throw new RuntimeException("Can't change to the same plan");
        }
        Plan newPlan = cmd.newPlan();
        if (Plans.BASIC == currentPlan) {
            return new PlanUpgradedEvent(cmd.id(), cmd.timestamp(), cmd.newPlan());
        } else if (Plans.PREMIUM == currentPlan) {
            return new PlanDowngradedEvent(cmd.id(), cmd.timestamp(), cmd.newPlan());
        } else {
            if (newPlan == Plans.BASIC) {
                return new PlanUpgradedEvent(cmd.id(), cmd.timestamp(), cmd.newPlan());
            }
            return new PlanDowngradedEvent(cmd.id(), cmd.timestamp(), cmd.newPlan());
        }*/
    }

    private void preValidateCommand(SubscriptionCommand cmd) {
        if (cmd.id() == null || cmd.timestamp() == null) {
            throw new RuntimeException("Invalid command: " + cmd);
        }
    }

    private Instant calculateTrialEnd(Instant start) {
        return start.plus(Period.ofMonths(1));
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
        if (events == null) {
            return;
        }
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
