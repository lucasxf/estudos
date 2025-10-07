package exercicio_e.subscriptions_billing.application.commands;

import exercicio_e.subscriptions_billing.domain.account.Account;
import exercicio_e.subscriptions_billing.domain.subscription.command.SubscriptionCommand;
import exercicio_e.subscriptions_billing.domain.subscription.event.SubscriptionEvent;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 25/08/2025
 */
public interface SubscriptionCommandHandler {

    default List<SubscriptionEvent> handleCommand(Account account, SubscriptionCommand command) {
        return handleCommand(account.getId(), command);
    }

    default List<SubscriptionEvent> handleCommand(UUID accountId, SubscriptionCommand command) {
        switch (command) {
            case SubscriptionCommand.StartTrial cmd -> handleStartTrialCommand(accountId, cmd);
            case SubscriptionCommand.ConvertSubscription cmd -> handleConvertSubscriptionCommand(accountId, cmd);
            case SubscriptionCommand.ChangePlan cmd -> handleUpgradePlanCommand(accountId, cmd);
            case SubscriptionCommand.CancelSubscription cmd -> handleCancelSubscriptionCommand(accountId, cmd);
            default -> throw new IllegalStateException("Unexpected value: " + command);
        }
        return null;
    }


    List<SubscriptionEvent> handleStartTrialCommand(UUID accountId, SubscriptionCommand.StartTrial command);

    List<SubscriptionEvent> handleConvertSubscriptionCommand(UUID accountId, SubscriptionCommand.ConvertSubscription command);

    List<SubscriptionEvent> handleUpgradePlanCommand(UUID accountId, SubscriptionCommand.ChangePlan command);

    List<SubscriptionEvent> handleCancelSubscriptionCommand(UUID accountId, SubscriptionCommand.CancelSubscription command);

}
