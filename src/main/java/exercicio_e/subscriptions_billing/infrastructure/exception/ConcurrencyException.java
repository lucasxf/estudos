package exercicio_e.subscriptions_billing.infrastructure.exception;


/**
 * @author lucas
 * @date 02/10/2025 05:42
 */
public class ConcurrencyException extends RuntimeException {

    public ConcurrencyException(String message) {
        super(message);
    }

}
