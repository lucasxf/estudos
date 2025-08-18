package exercicio_e.repository;

import exercicio_e.domain.event.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lucas Xavier Ferreira
 * @date 18/08/2025
 */
public class EventStore {

    private Map<UUID, List<Event>> store = new HashMap<>();

}
