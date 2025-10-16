package exercicio_e.subscriptions_billing.application.process;

import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent.AccountCreated;
import exercicio_e.subscriptions_billing.domain.subscription.event.TrialStarted;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent.UsernameClaimed;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent.UsernameReserved;
import exercicio_e.subscriptions_billing.infrastructure.messaging.EventBus;
import exercicio_e.subscriptions_billing.infrastructure.messaging.EventEnvelope;

/**
 * @author lucas
 * @date 11/10/2025 10:25
 */
public class AccountSignupSaga implements AutoCloseable {

    private final EventBus eventBus;

    public AccountSignupSaga(EventBus eventBus) {
        this.eventBus = eventBus;
        registerHandlers();
    }

    private void registerHandlers() {
        final AutoCloseable reservedCloseable = eventBus.subscribe(UsernameReserved.class,
                this::handleUsernameReserved);
        final AutoCloseable createdCloseable = eventBus.subscribe(AccountCreated.class,
                this::handleAccountCreated);
        final AutoCloseable claimedCloseable = eventBus.subscribe(UsernameClaimed.class,
                this::handleUsernameClaimed);
        final AutoCloseable trialStartedCloseable = eventBus.subscribe(TrialStarted.class,
                this::handleTrialStarted);

    }

    private void handleUsernameReserved(EventEnvelope<?> eventEnvelope) {
    }

    private void handleAccountCreated(EventEnvelope<?> eventEnvelope) {

    }

    private void handleUsernameClaimed(EventEnvelope<?> eventEnvelope) {

    }

    private void handleTrialStarted(EventEnvelope<?> eventEnvelope) { }

    @Override
    public void close() throws Exception {
        // Unsubscribe from all events if needed

    }

}
