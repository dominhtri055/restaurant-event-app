package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository {
    List<Event> getAll();

    List<Event> search(String name, LocalDate after, LocalDate before);

    Optional<Event> get(long id);

    boolean getByName(String name);

    Event create(Event event);

    Event update(Event event) throws ConcurrencyException;

    void delete(long id);

    Optional<Event> getByExactName(String name);
}
