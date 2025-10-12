package exercicio_e.subscriptions_billing.infrastructure.context;

import org.slf4j.MDC;

import java.io.Closeable;
import java.util.UUID;

/**
 * ContextScope encapsula o ciclo de vida do MDC (Mapped Diagnostic Context)
 * e fornece um logger contextualizado para uso dentro do escopo.
 *
 * <p>Uso típico:
 * <pre>{@code
 * try (var scope = ContextScope.open(correlationId, causationId)) {
 *     scope.log().info("Processing command...");
 * }
 * }</pre>
 * @author lucas
 * @date 12/10/2025 08:45
 */
public final class ContextScope implements Closeable {

    private ContextScope(ExecutionContext context) {
        context.applyToMdc();
    }

    public static ContextScope open(UUID correlationId, UUID causationId) {
        return open(correlationId.toString(), causationId.toString());
    }

    public static ContextScope open(String correlationId, String causationId) {
        var context = ExecutionContext.of(correlationId, causationId);
        return new ContextScope(context);
    }

    @Override
    public void close() {
        MDC.clear();
    }

}
