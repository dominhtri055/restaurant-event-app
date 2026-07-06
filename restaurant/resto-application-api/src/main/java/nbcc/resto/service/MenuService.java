package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Menu;

import java.util.Collection;

public interface MenuService {
    Result<Collection<Menu>> getAll();

    Result<Collection<Menu>> search(String name);

    ValidatedResult<Menu> get(Long id);

    ValidatedResult<Menu> create(Menu menu);

    ValidatedResult<Menu> update(Menu menu);

    Result<Menu> delete(Long id);
}