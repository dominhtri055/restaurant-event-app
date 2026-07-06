package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event {

    private Long id;

    @NotBlank(message = "Event name is required")
    private String name;

    private String description;

    @NotNull(message = "Event start date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "Event end date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "Event duration is required")
    @PositiveOrZero(message = "Duration must be greater than or equal to 0")
    private Integer duration;

    @NotNull(message = "Event price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private Long menuId;

    private String menuName;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isArchive;

    public Event() {
    }

    public Event(Event event) {
        this(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStartDate(),
                event.getEndDate(),
                event.getDuration(),
                event.getPrice(),
                event.getActive(),
                event.getCreatedAt(),
                event.getUpdatedAt(),
                event.getArchive()

        );
        this.menuId = event.menuId;
        this.menuName = event.menuName;
    }

    public Event(Long id, String name, String description, LocalDate startDate, LocalDate endDate,
                 Integer duration, BigDecimal price, Boolean isActive, LocalDateTime createdAt,LocalDateTime updatedAt, boolean isArchive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.price = price;
        this.isActive = isActive;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt;
        this.isArchive = isArchive;
    }

    public boolean isPast() {
        return endDate != null && endDate.isBefore(LocalDate.now());
    }

    public Long getId() {
        return id;
    }

    public Event setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Event setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Event setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Event setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Event setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Integer getDuration() {
        return duration;
    }

    public Event setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Event setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Event setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Event setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Event setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public boolean getArchive() {
        return isArchive;
    }

    public Event setArchive(boolean archive) {
        isArchive = archive;
        return this;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Event setMenuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public String getMenuName() {
        return menuName;
    }

    public Event setMenuName(String menuName) {
        this.menuName = menuName;
        return this;
    }

}