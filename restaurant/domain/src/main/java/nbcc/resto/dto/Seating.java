package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Seating {

    private Long id;

    @NotNull(message = "Event is required")
    private Long eventId;

    private String eventName;

    @NotBlank(message = "Seating name is required")
    private String name;

    @NotNull(message = "Start date and time is required")
    private LocalDateTime startTime;

    @Positive(message = "Duration must be greater than zero")
    private Integer duration;

    private LocalDateTime createdDate;

    private LocalDateTime updatedAt;

    private boolean archived;

    @NotEmpty(message = "At least one table must be selected")
    private List<Long> tableIds;

    private  List<String> tableNames;

    public Seating() {
    }

    public Long getId() {
        return id;
    }

    public Seating setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getEventId() {
        return eventId;
    }

    public Seating setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getEventName() {
        return eventName;
    }

    public Seating setEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public String getName() {
        return name;
    }

    public Seating setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Seating setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public Integer getDuration() {
        return duration;
    }

    public Seating setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Seating setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Seating setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public boolean isArchived() {
        return archived;
    }

    public Seating setArchived(boolean archived) {
        this.archived = archived;
        return this;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }

    public Seating setTableIds(List<Long> tableIds) {
        this.tableIds = tableIds;
        return this;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public Seating setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
        return this;
    }
}
