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
            case SubscriptionCommand.StartTrialCommand cmd -> handleStartTrialCommand(accountId, cmd);
            case SubscriptionCommand.ConvertSubscriptionCommand cmd -> handleConvertSubscriptionCommand(accountId, cmd);
            case SubscriptionCommand.ChangePlanCommand cmd -> handleUpgradePlanCommand(accountId, cmd);
            case SubscriptionCommand.CancelSubscriptionCommand cmd -> handleCancelSubscriptionCommand(accountId, cmd);
            default -> throw new IllegalStateException("Unexpected value: " + command);
        }
        return null;
    }


    List<SubscriptionEvent> handleStartTrialCommand(UUID accountId, SubscriptionCommand.StartTrialCommand command);

    List<SubscriptionEvent> handleConvertSubscriptionCommand(UUID accountId, SubscriptionCommand.ConvertSubscriptionCommand command);

    List<SubscriptionEvent> handleUpgradePlanCommand(UUID accountId, SubscriptionCommand.ChangePlanCommand command);

    List<SubscriptionEvent> handleCancelSubscriptionCommand(UUID accountId, SubscriptionCommand.CancelSubscriptionCommand command);

}
