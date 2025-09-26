package exercicio_e.subscriptions_billing.infrastructure.repository;

import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 23/09/2025
 */
public interface AccountRepository {

    String AGGREGATE_TYPE = "Account";

    default LoadedStream load(UUID aggregateId) {
        return load(AGGREGATE_TYPE, aggregateId);
    }

    LoadedStream load(String aggregateType, UUID aggregateId);

    List<AccountEvent> append(UUID aggregateId, long expectedVersion, AccountEvent newEvent);

    List<AccountEvent> append(UUID aggregateId, long expectedVersion, List<AccountEvent> newEvents);

    record LoadedStream(UUID aggregateId, List<AccountEvent> history, long lastVersion) { }

}
