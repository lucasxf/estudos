package exercicio_e.subscriptions_billing.domain.subscription;

import exercicio_e.subscriptions_billing.domain.subscription.event.*;
import exercicio_e.subscriptions_billing.domain.subscription.plan.Plan;
import exercicio_e.subscriptions_billing.domain.subscription.command.SubscriptionCommand;
import exercicio_e.subscriptions_billing.domain.subscription.command.SubscriptionCommand.*;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.UUID;

/**
 * Subscription Aggregate.
 *
 * @author Lucas Xavier Ferreira
 * @date 19/08/2025
 */
public class SubscriptionAggregate {

    private final UUID id;
    private final List<SubscriptionEvent> eventStream;
    private long version = -1L;
    private SubscriptionState state;
    private Plan currentPlan;

    private SubscriptionAggregate(UUID subscriptionId, long lastVersion) {
        this(subscriptionId, null, lastVersion);
    }

    private SubscriptionAggregate(UUID subscriptionId, List<SubscriptionEvent> eventStream, long lastVersion) {
        if (subscriptionId == null) {
            throw new RuntimeException("Invalid aggregate ID.");
        }
        this.id = subscriptionId;
        this.eventStream = eventStream;
        replay();
    }

    public static SubscriptionAggregate from(
            UUID subscriptionId,
            List<SubscriptionEvent> eventStream,
            long lastVersion) {
        return new SubscriptionAggregate(subscriptionId, eventStream, lastVersion);
    }

    /**
     * @param cmd
     * @return
     */
    public TrialStarted decide(StartTrial cmd) {
        preValidateCommand(cmd);
        if (state != null) {
            throw new RuntimeException("Invalid subscription state: " + state);
        }
        if (!id.equals(cmd.subscriptionId())) {
            throw new RuntimeException("Invalid subscription ID: " + cmd.subscriptionId());
        }
        Instant start = Instant.now();
        Instant trialEnd = calculateTrialEnd(start);
        return new TrialStarted(
                cmd.subscriptionId(), start, trialEnd, cmd.preferredPlan());
    }

    /**
     * @param cmd
     * @return
     */
    public SubscriptionConverted decide(ConvertSubscription cmd) {
        preValidateCommand(cmd);
        if (SubscriptionState.TRIAL != state) {
            throw new RuntimeException("Cannot convert a subscription that is not on trial");
        }
        // auto conversion
        if (cmd.plan() == null && currentPlan == null) {
            throw new RuntimeException("Cannot convert to invalid plan");
        }
        return new SubscriptionConverted(
                cmd.subscriptionId(), Instant.now(), currentPlan, cmd.plan());
    }

    /**
     * @param cmd
     * @return
     */
    public SubscriptionEvent decide(ChangePlan cmd) {
        preValidateCommand(cmd);
        if (state == SubscriptionState.CANCELED) {
            throw new RuntimeException("Cannot change plans on a canceled subscription");
        }
        Plan newPlan = cmd.newPlan();
        if (newPlan == null) {
            throw new RuntimeException("Invalid plan");
        }
        if (newPlan.getCode() > currentPlan.getCode()) {
            return new PlanUpgraded(cmd.subscriptionId(), Instant.now(), currentPlan, cmd.newPlan());
        } else if (newPlan.getCode() < currentPlan.getCode()) {
            return new PlanDowngraded(cmd.subscriptionId(), Instant.now(), currentPlan, cmd.newPlan());
        } else {
            throw new RuntimeException("Can't change to the same plan");
        }
    }

    /**
     *
     * @param cmd
     * @return
     */
    public SubscriptionEvent decide(CancelSubscription cmd) {
        if (state == SubscriptionState.CANCELED) {
            throw new RuntimeException("Subscription is already canceled.");
        }
        return new SubscriptionCanceled(
                cmd.subscriptionId(), Instant.now(), currentPlan);
    }

    private void preValidateCommand(SubscriptionCommand cmd) {
        if (cmd.subscriptionId() == null) {
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

    private void replay() {
        if (eventStream == null) {
            return;
        }
        for (SubscriptionEvent e : eventStream) {
            if (e instanceof TrialStarted) {
                apply((TrialStarted) e);
            } else if (e instanceof SubscriptionConverted) {
                apply((SubscriptionConverted) e);
            } else if (e instanceof PlanUpgraded) {
                apply((PlanUpgraded) e);
            } else if (e instanceof PlanDowngraded) {
                apply((PlanDowngraded) e);
            } else if (e instanceof SubscriptionCanceled) {
                apply();
            }
        }
    }

    private void apply(TrialStarted e) {
        this.version = 1L;
        this.state = SubscriptionState.TRIAL;
        this.currentPlan = e.preferredPlan();
    }

    private void apply() {
        this.version++;
        this.state = SubscriptionState.CANCELED;
    }

    private void apply(SubscriptionConverted e) {
        this.version++;
        this.state = SubscriptionState.ACTIVE;
        this.currentPlan = e.newPlan();
    }

    private void apply(PlanUpgraded e) {
        this.version++;
        this.state = SubscriptionState.ACTIVE;
        this.currentPlan = e.newPlan();
    }

    private void apply(PlanDowngraded e) {
        this.version++;
        this.state = SubscriptionState.ACTIVE;
        this.currentPlan = e.newPlan();
    }

}
