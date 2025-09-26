package exercicio_e.subscriptions_billing.infrastructure.repository;

import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent;

import java.util.List;
import java.util.UUID;

public interface UsernameRepository {

    String AGGREGATE_TYPE = "Username";

    default LoadedStream load(UUID aggregateId) {
        return load(AGGREGATE_TYPE, aggregateId);
    }

    LoadedStream load(String aggregateType, UUID aggregateId);

    List<UsernameEvent> append(UUID aggregateId, long expectedVersion, UsernameEvent newEvent);

    List<UsernameEvent> append(UUID aggregateId, long expectedVersion, List<UsernameEvent> newEvents);

    record LoadedStream(UUID aggregateId, List<UsernameEvent> history, long lastVersion) { }

}
