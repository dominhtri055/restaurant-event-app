package nbcc.resto.repository;

import nbcc.resto.dto.MenuItem;
import nbcc.resto.mapper.MenuItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuItemRepositoryAdapter implements MenuItemRepository {
    private final MenuItemJPARepository menuItemJPARepository;
    private final MenuItemMapper menuItemMapper;

    public MenuItemRepositoryAdapter(MenuItemJPARepository menuItemJPARepository,
                                     MenuItemMapper menuItemMapper) {
        this.menuItemJPARepository = menuItemJPARepository;
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    public List<MenuItem> getByMenuId(Long menuId) {
        var entities = menuItemJPARepository.findByMenuId(menuId);
        return menuItemMapper.toDTO(entities);
    }

    @Override
    public Optional<MenuItem> get(Long id) {
        var entities = menuItemJPARepository.findById(id);
        return menuItemMapper.toDTO(entities);
    }

    @Override
    public MenuItem create(MenuItem menuItem) {
        var entity = menuItemMapper.toEntity(menuItem);
        entity = menuItemJPARepository.save(entity);
        return menuItemMapper.toDTO(entity);
    }

    @Override
    public MenuItem update(MenuItem menuItem) {
        var entityExist = menuItemJPARepository.findById(menuItem.getId());

        if (entityExist.isEmpty()) {
            throw new IllegalArgumentException("Menu item not found");
        }

        var entity = entityExist.get();
        entity.setName(menuItem.getName());
        entity.setDescription(menuItem.getDescription());

        entity = menuItemJPARepository.save(entity);
        return menuItemMapper.toDTO(entity);
    }

    @Override
    public void delete(Long id) {
        menuItemJPARepository.deleteById(id);
    }
}
