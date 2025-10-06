package exercicio_e.subscriptions_billing.infrastructure.repository;

import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.StoredEvent;

import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 23/09/2025
 */
public interface UsernameRepository {

    String AGGREGATE_TYPE = "Username";

    LoadedStream load(String usernameKey);

    /**
     * Append a new event to the event store.
     *
     * @param usernameKey
     * @param expectedVersion
     * @param newEvent
     * @param correlationId
     * @param causationId
     * @return
     */
    List<StoredEvent> append(String usernameKey,
                             long expectedVersion,
                             UsernameEvent newEvent,
                             UUID correlationId,
                             UUID causationId);

    /**
     *
     * @param usernameKey
     * @param expectedVersion
     * @param newEvents
     * @param correlationId
     * @param causationId
     * @return
     */
    List<StoredEvent> append(String usernameKey,
                             long expectedVersion,
                             List<UsernameEvent> newEvents,
                             UUID correlationId,
                             UUID causationId);

    record LoadedStream(String usernameKey, List<UsernameEvent> history, long lastVersion) { }

}
