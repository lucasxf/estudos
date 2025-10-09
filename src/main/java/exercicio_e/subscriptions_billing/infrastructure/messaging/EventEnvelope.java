package exercicio_e.subscriptions_billing.infrastructure.messaging;

import java.time.Instant;
import java.util.UUID;

/**
 * @author lucas
 * @date 09/10/2025 07:45
 */
public record EventEnvelope<E>(
        String aggregateType,
        String aggregateId,
        long aggregateVersion,
        Instant occurredAt,
        E event,
        UUID correlationId,
        UUID causationId) { }
