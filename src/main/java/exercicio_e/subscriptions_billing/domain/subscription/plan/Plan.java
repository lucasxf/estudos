package exercicio_e.subscriptions_billing.domain.subscription.plan;

import lombok.Getter;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
@Getter
public enum Plan {

    BASIC(100),
    STANDARD(200),
    PREMIUM(300);

    private final int code;

    Plan(int code) {
        this.code = code;
    }

}
