package exercicio_e.subscriptions_billing.infrastructure.context;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author lucas
 * @date 12/10/2025 08:29
 */
public record ExecutionContext(String correlationId, String causationId) {

    public static ExecutionContext of(UUID correlationId, UUID causationId) {
        return new ExecutionContext(correlationId.toString(), causationId.toString());
    }

    public static ExecutionContext of(String correlationId, String causationId) {
        return new ExecutionContext(correlationId, causationId);
    }

    public void applyToMdc() {
        // Implement MDC context application logic here
        MDC.put("correlationId", correlationId);
        MDC.put("causationId", causationId);
    }

    public void clearMdc() {
        MDC.clear();
    }

}
