package exercicio_e.subscriptions_billing.infrastructure.repository.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.EventStore;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.StoredEvent;
import exercicio_e.subscriptions_billing.infrastructure.repository.UsernameRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static exercicio_e.subscriptions_billing.domain.username.event.UsernameEvent.*;

/**
 * @author lucas
 * @date 29/09/2025 19:21
 */
@Repository
public class UsernameRepositoryImpl implements UsernameRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EventStore eventStore;

    public UsernameRepositoryImpl(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public LoadedStream load(String aggregateType, String usernameKey) {
        var storedEvents = eventStore.readStream(aggregateType, usernameKey);
        return null;
    }

    private List<UsernameEvent> fromStoredEvents(List<StoredEvent> storedEvents) {
        try {
            List<UsernameEvent> events = new ArrayList<>();
            for (StoredEvent e : storedEvents) {
                if (USERNAME_RESERVED.equals(e.type())) {
                    var event = objectMapper.readValue(e.payloadJson(), UsernameEvent.UsernameReserved.class);
                    events.add(event);
                } else if (USERNAME_CLAIMED.equals(e.type())) {
                    var event = objectMapper.readValue(e.payloadJson(), UsernameEvent.UsernameClaimed.class);
                    events.add(event);
                } else if (USERNAME_RELEASED.equals(e.type())) {
                    var event = objectMapper.readValue(e.payloadJson(), UsernameEvent.UsernameReleased.class);
                    events.add(event);
                }
            }
            return events;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UsernameEvent> append(String usernameKey, long expectedVersion, UsernameEvent newEvent) {
        return List.of();
    }

    @Override
    public List<UsernameEvent> append(String usernameKey, long expectedVersion, List<UsernameEvent> newEvents) {
        return List.of();
    }

}
