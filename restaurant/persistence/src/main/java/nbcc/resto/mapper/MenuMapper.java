package nbcc.resto.mapper;

import nbcc.resto.dto.Menu;
import nbcc.resto.entity.MenuEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper implements EntityMapper<Menu, MenuEntity>{

    public MenuMapper() {

    }

    @Override
    public Menu toDTO(MenuEntity menuEntity) {
        if(menuEntity == null) return null;

        return new Menu()
                .setId(menuEntity.getId())
                .setName(menuEntity.getName())
                .setDescription(menuEntity.getDescription())
                .setCreationDate(menuEntity.getCreationDate());
    }

    @Override
    public MenuEntity toEntity(Menu menu) {
        if(menu == null) return null;
        return new MenuEntity()
                .setId(menu.getId())
                .setName(menu.getName())
                .setDescription(menu.getDescription())
                .setCreationDate(menu.getCreationDate());
    }
}