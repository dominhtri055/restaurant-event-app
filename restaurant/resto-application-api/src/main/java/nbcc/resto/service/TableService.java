package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Table;

import java.util.Collection;

public interface TableService {

    Result<Collection<Table>> getAll();

    ValidatedResult<Table> get(Long id);

    ValidatedResult<Table> create(Table table);

    ValidatedResult<Table> update(Table table);

    ValidatedResult<Void> delete(Long id);

}
