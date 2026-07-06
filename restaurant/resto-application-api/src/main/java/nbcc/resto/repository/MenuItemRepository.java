package nbcc.resto.repository;

import nbcc.resto.dto.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {
    List<MenuItem> getByMenuId(Long menuId);

    Optional<MenuItem> get(Long id);

    MenuItem create(MenuItem menuItem);

    MenuItem update(MenuItem menuItem);

    void delete(Long id);
}