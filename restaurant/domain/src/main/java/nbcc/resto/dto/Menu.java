package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class Menu {
    private Long id;

    @NotBlank(message = "Menu Name is required")
    private String name;

    private String description;

    private LocalDateTime creationDate;

    public Menu() {}

    public Menu(Menu menu) {
        this(
                menu.getId(),
                menu.getName(),
                menu.getDescription(),
                menu.getCreationDate()
        );
    }

    public Menu(Long id, String name, String description, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = date;

    }

    public Long getId() {
        return id;
    }

    public Menu setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Menu setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Menu setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Menu setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }
}