package exercicio_e.subscriptions_billing.application.api;

import exercicio_e.subscriptions_billing.application.api.dto.AccountResponse;
import exercicio_e.subscriptions_billing.application.commands.AccountCommandHandler;
import exercicio_e.subscriptions_billing.domain.account.command.AccountCommand.CreateAccount;
import exercicio_e.subscriptions_billing.infrastructure.context.ContextScope;
import exercicio_e.subscriptions_billing.infrastructure.context.ExecutionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

/**
 * Controller para gerenciamento de contas de usuário.
 *
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private final AccountCommandHandler commandHandler;

    public AccountController(AccountCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public ResponseEntity<AccountResponse> create(
            @PathVariable String username,
            @RequestHeader(value = "X-Correlation-Id", required = false) String corr) {

        var correlationId = getCorrelationId(corr);
        var causationId = UUID.randomUUID();
        try (var ignored = ContextScope.open(correlationId, causationId)) {
            var context = ExecutionContext.of(correlationId, causationId);
            context.applyToMdc();

            log.info("Received request to create account for username: {}", username);

            var command = createAccountCommand(causationId, username, usernameKey(username));
            commandHandler.handle(correlationId, command);

            log.info("Account creation command processed for username: {}", username);

            context.clearMdc();
            return ResponseEntity.accepted().build();
        }
    }

    private UUID getCorrelationId(String corr) {
        return corr != null && !corr.isBlank() ?
                UUID.fromString(corr) :
                UUID.randomUUID();
    }

    private CreateAccount createAccountCommand(UUID commandId, String username, String usernameKey) {
        return new CreateAccount(commandId, UUID.randomUUID(), Instant.now(), username, usernameKey);
    }

    private String usernameKey(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (!username.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("Username must be a valid email address");
        }
        return username.toLowerCase();
    }

}
