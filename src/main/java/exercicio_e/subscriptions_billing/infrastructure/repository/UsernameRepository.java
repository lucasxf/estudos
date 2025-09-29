package exercicio_e.subscriptions_billing.infrastructure.repository;

import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent;

import java.util.List;

/**
 * @author Lucas Xavier Ferreira
 * @date 23/09/2025
 */
public interface UsernameRepository {

    String AGGREGATE_TYPE = "Username";

    default LoadedStream load(String usernameKey) {
        return load(AGGREGATE_TYPE, usernameKey);
    }

    LoadedStream load(String aggregateType, String usernameKey);

    List<UsernameEvent> append(String usernameKey, long expectedVersion, UsernameEvent newEvent);

    List<UsernameEvent> append(String usernameKey, long expectedVersion, List<UsernameEvent> newEvents);

    record LoadedStream(String usernameKey, List<UsernameEvent> history, long lastVersion) { }

}
