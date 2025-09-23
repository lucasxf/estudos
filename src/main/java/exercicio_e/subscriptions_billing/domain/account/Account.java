package exercicio_e.subscriptions_billing.domain.account;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 20/08/2025
 */
public class Account {

    private UUID id;

    private String username;

    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
