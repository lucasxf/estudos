package exercicio_e.subscriptions_billing.application.commands;

import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand;
import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
@Component
public class AccountCommandHandler {

    public List<AccountEvent> handle(AccountCommand command) {
        switch (command) {
            case AccountCommand.CreateAccountCommand cmd -> handleCreateAccountCommand(cmd);
        }
        return null;
    }

    public List<AccountEvent> handleCreateAccountCommand(AccountCommand command) {
        return null;
    }

}
