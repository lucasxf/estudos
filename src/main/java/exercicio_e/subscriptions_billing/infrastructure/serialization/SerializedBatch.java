package exercicio_e.subscriptions_billing.infrastructure.serialization;

import java.util.List;

/**
 * @author Lucas Xavier Ferreira
 * @date 07/10/2025
 */
public record SerializedBatch(List<String> types, List<String> payloadJson) {
}
