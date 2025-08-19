package exercicio_e.subscriptions_billing.domain.plan;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public sealed interface Plan permits Basic, Standard, Premium {
}
