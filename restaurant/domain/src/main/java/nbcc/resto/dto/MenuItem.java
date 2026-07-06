package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;

public class MenuItem {

    private Long id;

    @NotBlank(message = "Item name is required")
    private String name;

    @NotBlank(message = "Item description is required")
    private String description;

    private Long menuId;

    public MenuItem() {}

    public MenuItem(MenuItem menuItem) {
        this(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getMenuId()
        );
    }

    public MenuItem(Long id, String name, String description, Long menuId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.menuId = menuId;
    }

    public Long getId() {
        return id;
    }

    public MenuItem setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getMenuId() {
        return menuId;
    }

    public MenuItem setMenuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }
}