package exercicio_e.subscriptions_billing.application.api;

import exercicio_e.subscriptions_billing.application.api.dto.AccountResponse;
import exercicio_e.subscriptions_billing.application.api.dto.CreateAccountRequest;
import exercicio_e.subscriptions_billing.application.commands.AccountCommandHandler;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand.CreateAccountCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountCommandHandler commandHandler;

    public AccountController(AccountCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public ResponseEntity<AccountResponse> create(
            @RequestBody CreateAccountRequest request,
            @RequestHeader(value = "X-Correlation-Id", required = false) String corr) {
        var correlationId = parseCorrelationId(corr);
        var commandId = UUID.randomUUID();
        var command = createAccountCommand(commandId, request);
        commandHandler.handle(correlationId, command);
        return ResponseEntity.accepted().build();
    }

    private CreateAccountCommand createAccountCommand(UUID commandId, CreateAccountRequest request) {
        return new CreateAccountCommand(commandId, Instant.now(), request.getUsername());

    }

    private UUID parseCorrelationId(String corr) {
        return corr != null && !corr.isBlank() ?
                UUID.fromString(corr) :
                UUID.randomUUID();
    }

}
