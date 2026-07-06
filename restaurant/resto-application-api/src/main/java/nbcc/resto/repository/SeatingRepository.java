package nbcc.resto.repository;

import nbcc.resto.dto.Seating;

import java.util.List;
import java.util.Optional;

public interface SeatingRepository {

    Seating create(Seating seating);

    Seating update(Seating seating);

    List<Seating> getAll();

    Optional<Seating> get(Long id);

    void delete(Long id);
}
