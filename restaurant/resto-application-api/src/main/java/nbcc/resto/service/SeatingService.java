package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Seating;

import java.util.Collection;

public interface SeatingService {

    Result<Collection<Seating>> getAll();

    ValidatedResult<Seating> get(Long id);

    ValidatedResult<Seating> create(Seating seating);

    ValidatedResult<Seating> update(Seating seating);

    ValidatedResult<Seating> delete(Long id);

}
