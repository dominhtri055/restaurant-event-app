package nbcc.resto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import nbcc.resto.dto.Menu;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="menu")
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItemEntity> menuItems = new ArrayList<>();

    public MenuEntity(){}

    public MenuEntity(Menu menu) {
        this(
                menu.getId(),
                menu.getName(),
                menu.getDescription(),
                menu.getCreationDate()
        );
    }
    public MenuEntity(Long id, String name, String description, LocalDateTime creationDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public MenuEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public MenuEntity setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public List<MenuItemEntity> getMenuItems() {
        return menuItems;
    }

    public MenuEntity setMenuItems(List<MenuItemEntity> menuItems) {
        this.menuItems = menuItems;
        return this;
    }
}
