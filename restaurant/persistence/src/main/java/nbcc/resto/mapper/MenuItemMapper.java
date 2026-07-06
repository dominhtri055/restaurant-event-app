package nbcc.resto.mapper;

import nbcc.resto.dto.MenuItem;
import nbcc.resto.entity.MenuEntity;
import nbcc.resto.entity.MenuItemEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper implements EntityMapper<MenuItem, MenuItemEntity> {

    public MenuItemMapper() {
    }

    @Override
    public MenuItem toDTO(MenuItemEntity menuItemEntity) {
        if (menuItemEntity == null) return null;

        return new MenuItem()
                .setId(menuItemEntity.getId())
                .setName(menuItemEntity.getName())
                .setDescription(menuItemEntity.getDescription())
                .setMenuId(menuItemEntity.getMenu() != null ? menuItemEntity.getMenu().getId() : null);
    }

    @Override
    public MenuItemEntity toEntity(MenuItem menuItem) {
        if (menuItem == null) return null;

        MenuEntity menuEntity = null;
        if (menuItem.getMenuId() != null) {
            menuEntity = new MenuEntity().setId(menuItem.getMenuId());
        }

        return new MenuItemEntity()
                .setId(menuItem.getId())
                .setName(menuItem.getName())
                .setDescription(menuItem.getDescription())
                .setMenu(menuEntity);
    }
}