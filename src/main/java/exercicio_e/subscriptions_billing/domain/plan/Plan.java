package exercicio_e.subscriptions_billing.domain.plan;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public enum Plan {

    BASIC(100),
    STANDARD(200),
    PREMIUM(300);

    private final int code;

    Plan(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
