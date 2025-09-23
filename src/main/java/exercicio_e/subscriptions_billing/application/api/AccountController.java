package exercicio_e.subscriptions_billing.application.api;

import exercicio_e.subscriptions_billing.application.api.dto.AccountDto;
import exercicio_e.subscriptions_billing.application.api.dto.CreateAccountRequest;
import exercicio_e.subscriptions_billing.application.commands.AccountCommandHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public ResponseEntity<AccountDto> create(
            @RequestBody CreateAccountRequest request,
            @RequestHeader(value = "X-Correlation-Id", required = false) String corr) {
        var correlationId = parseCorrelationId(corr);
        var commandId = UUID.randomUUID();
        return null;
    }

    private UUID parseCorrelationId(String corr) {
        return corr != null && !corr.isBlank() ?
                UUID.fromString(corr) :
                UUID.randomUUID();
    }

}
