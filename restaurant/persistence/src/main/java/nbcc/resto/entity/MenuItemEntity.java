package nbcc.resto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "menu_item")
public class MenuItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuEntity menu;

    public MenuItemEntity() {}

    public MenuItemEntity(Long id, String name, String description, MenuEntity menuId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.menu = menuId;
    }

    public Long getId() {
        return id;
    }

    public MenuItemEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuItemEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuItemEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public MenuEntity getMenu() {
        return menu;
    }

    public MenuItemEntity setMenu(MenuEntity menu) {
        this.menu = menu;
        return this;
    }
}
