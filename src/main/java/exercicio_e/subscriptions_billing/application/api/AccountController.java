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

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private final AccountCommandHandler commandHandler;

    public AccountController(AccountCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public ResponseEntity<AccountResponse> create(
            @RequestBody CreateAccountRequest request,
            @RequestHeader(value = "X-Correlation-Id", required = false) String corr) {
        var correlationId = parseCorrelationId(corr);
        var command = createAccountCommand(request);
        commandHandler.handle(correlationId, command);
        return ResponseEntity.accepted().build();
    }

    private UUID parseCorrelationId(String corr) {
        return corr != null && !corr.isBlank() ?
                UUID.fromString(corr) :
                UUID.randomUUID();
    }

    private CreateAccountCommand createAccountCommand(CreateAccountRequest request) {
        return new CreateAccountCommand(UUID.randomUUID(), Instant.now(), request.getUsername());
    }

    private String usernameKey(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (!username.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("Username deve ser um e-mail válido");
        }
        return username.toLowerCase();
    }

}
