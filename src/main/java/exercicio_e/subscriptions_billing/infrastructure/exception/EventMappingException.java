package exercicio_e.subscriptions_billing.infrastructure.exception;

/**
 * @author lucas
 * @date 30/09/2025 05:55
 */
public class EventMappingException extends RuntimeException {

    public EventMappingException(String message) {
        super(message);
    }

    public EventMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public static EventMappingException forType(String type, String details, Throwable cause) {
        return new EventMappingException("Event mapping failed for type=" + type + " " + details, cause);
    }

}
