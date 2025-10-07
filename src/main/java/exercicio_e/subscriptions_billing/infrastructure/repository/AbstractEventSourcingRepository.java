package exercicio_e.subscriptions_billing.infrastructure.repository;

import exercicio_e.subscriptions_billing.domain.account.event.AccountEvent;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.EventStore;
import exercicio_e.subscriptions_billing.infrastructure.eventstore.StoredEvent;
import exercicio_e.subscriptions_billing.infrastructure.serialization.EventMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 07/10/2025
 */
public abstract class AbstractEventSourcingRepository<E> {

    protected final EventStore eventStore;
    protected final EventMapper eventMapper;

    public AbstractEventSourcingRepository(EventStore eventStore, EventMapper eventMapper) {
        this.eventStore = eventStore;
        this.eventMapper = eventMapper;
    }

    protected List<E> deserialize(List<StoredEvent> stored) {
        var out = new ArrayList<E>(stored.size());
        for (var se : stored) {
            out.add(eventMapper.toDomain(se.type(), se.payloadJson()));
        }
        return out;
    }

    protected List<StoredEvent> appendDomainEvents(
            String aggregateType,
            String aggregateId,
            long expectedVersion,
            List<?> domainEvents,
            UUID correlationId,
            UUID causationId) {
        var batch = eventMapper.serializeBatch(domainEvents);
        return eventStore.appendRaw(
                aggregateType,
                aggregateId,
                expectedVersion,
                batch.types(),
                batch.payloadJson(),
                correlationId,
                causationId);
    }

}
