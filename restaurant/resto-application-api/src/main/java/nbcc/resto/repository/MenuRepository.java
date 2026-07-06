package nbcc.resto.repository;

import nbcc.resto.dto.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {

    List<Menu> getAll();

    List<Menu> search(String name);

    Optional<Menu> get(Long id);

    Menu create(Menu menu);

    Menu update(Menu menu);

    void delete(Long id);

    boolean existsByNameIgnoreCase(String name);
}