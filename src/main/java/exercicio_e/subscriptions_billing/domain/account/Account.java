package exercicio_e.subscriptions_billing.domain.account;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 20/08/2025
 */
@Getter
@Setter
public class Account {

    private UUID id;
    private String username;
    private Instant createdAt;

}
