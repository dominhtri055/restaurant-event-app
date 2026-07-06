package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.Table;

import java.util.List;
import java.util.Optional;

public interface TableRepository {

    Table create(Table table);

    Table update(Table table) throws ConcurrencyException;

    List<Table> getAll();

    Optional<Table> get(Long id);

    void delete(Long id);
}
