package exercicio_e.subscriptions_billing.infrastructure.context;

import org.slf4j.MDC;

/**
 * Representa o contexto de execução técnico para rastreabilidade,
 * contendo os identificadores de correlação e causa (correlationId e causationId).
 *
 * <p>Este record é imutável e não executa efeitos colaterais.
 * O ciclo de vida do contexto (aplicação e limpeza no MDC) é gerenciado
 * pela classe {@link ContextScope}.
 *
 * @author lucas
 * @date 12/10/2025 08:29
 */

public record ExecutionContext(String correlationId, String causationId) {

    public static ExecutionContext of(String correlationId, String causationId) {
        return new ExecutionContext(correlationId, causationId);
    }

    public void applyToMdc() {
        MDC.put("correlationId", correlationId);
        MDC.put("causationId", causationId);
    }

}
