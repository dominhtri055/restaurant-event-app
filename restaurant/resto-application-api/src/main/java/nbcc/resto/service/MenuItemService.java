package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.MenuItem;

import java.util.Collection;

public interface MenuItemService {
    Result<Collection<MenuItem>> getByMenuId(Long menuId);

    ValidatedResult<MenuItem> get(Long id);

    ValidatedResult<MenuItem> create(MenuItem menuItem);

    ValidatedResult<MenuItem> update(MenuItem menuItem);

    Result<MenuItem> delete(Long id);
}