package nbcc.resto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class ReservationRequest {

    private Long id;

    @NotNull(message = "Seating is required")
    private Long seatingId;

    private String seatingName;

    private LocalDateTime seatingStartTime;

    @NotNull(message = "EventId is required")
    private Long eventId;

    private String eventName;

    private Long tableId;

    private String tableName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email address is required")
    @Email(message = "Email address is invalid")
    private String emailAddress;

    @NotNull(message = "Group size is required")
    @Positive(message = "Group size must be greater than zero")
    private Integer groupSize;

    private String status;

    private String requestUuid;

    private LocalDateTime createdAt;

    public ReservationRequest() {
    }

    public Long getId() {
        return id;
    }

    public ReservationRequest setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getSeatingId() {
        return seatingId;
    }

    public ReservationRequest setSeatingId(Long seatingId) {
        this.seatingId = seatingId;
        return this;
    }

    public String getSeatingName() {
        return seatingName;
    }

    public ReservationRequest setSeatingName(String seatingName) {
        this.seatingName = seatingName;
        return this;
    }

    public LocalDateTime getSeatingStartTime() {
        return seatingStartTime;
    }

    public ReservationRequest setSeatingStartTime(LocalDateTime seatingStartTime) {
        this.seatingStartTime = seatingStartTime;
        return this;
    }

    public Long getEventId() {
        return eventId;
    }

    public ReservationRequest setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getEventName() {
        return eventName;
    }

    public ReservationRequest setEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public Long getTableId() {
        return tableId;
    }

    public ReservationRequest setTableId(Long tableId) {
        this.tableId = tableId;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public ReservationRequest setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ReservationRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ReservationRequest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public ReservationRequest setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public ReservationRequest setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ReservationRequest setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getRequestUuid() {
        return requestUuid;
    }

    public ReservationRequest setRequestUuid(String requestUuid) {
        this.requestUuid = requestUuid;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReservationRequest setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
