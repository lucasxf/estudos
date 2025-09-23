package exercicio_e.subscriptions_billing.application.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 22/09/2025
 */
@Getter
@Setter
public class AccountResponse {

    private UUID id;
    private String username;

}
