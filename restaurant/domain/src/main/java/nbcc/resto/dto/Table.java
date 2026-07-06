package nbcc.resto.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class Table {

    private Long id;

    private String name;

    @NotNull(message = "Capacity is required.")
    @Positive(message = "Capacity must be greater than zero.")
    private Integer capacity;

    private LocalDateTime createdDate;

    public Table() {
    }

    public Table(Table table) {
        this(table.getId(), table.getName(), table.getCapacity(), table.getCreatedDate());
    }

    public Table(Long id, String name, Integer capacity, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public Table setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Table setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Table setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Table setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }
}
