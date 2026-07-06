package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Event;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface EventService {
    Result<Collection<Event>> getAll();

    Result<Collection<Event>> search(String name, LocalDate after, LocalDate before);

    ValidatedResult<Event> get(Long id);

    ValidatedResult<Event> create(Event event);

    Result<Void> delete(Long id);

    ValidatedResult<Event> update(Event event);

    ValidatedResult<Boolean> getByName(String name);

    Optional<Event> getByExactName(String name);
}
