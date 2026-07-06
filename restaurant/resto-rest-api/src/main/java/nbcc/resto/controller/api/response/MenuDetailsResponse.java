package nbcc.resto.controller.api.response;

import nbcc.resto.dto.Menu;
import nbcc.resto.dto.MenuItem;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class MenuDetailsResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private Collection<MenuItem> menuItems;

    public MenuDetailsResponse() {
        this.menuItems = List.of();
    }

    public MenuDetailsResponse(Menu menu, Collection<MenuItem> menuItems) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.description = menu.getDescription();
        this.creationDate = menu.getCreationDate();
        this.menuItems = menuItems != null ? menuItems : List.of();
    }

    public Long getId() {
        return id;
    }

    public MenuDetailsResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuDetailsResponse setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuDetailsResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public MenuDetailsResponse setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Collection<MenuItem> getMenuItems() {
        return menuItems;
    }

    public MenuDetailsResponse setMenuItems(Collection<MenuItem> menuItems) {
        this.menuItems = menuItems;
        return this;
    }
}