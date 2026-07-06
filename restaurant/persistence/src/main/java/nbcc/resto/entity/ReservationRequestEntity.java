package nbcc.resto.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_request",
        uniqueConstraints = {@UniqueConstraint(name = "uk_reservation_request_uuid", columnNames = "request_uuid")}
)
public class ReservationRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "seating_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_reservation_request_seating")
    )
    private SeatingEntity seating;

    @ManyToOne
    @JoinColumn(
            name = "table_id",
            foreignKey = @ForeignKey(name = "fk_reservation_request_table")
    )
    private TableEntity assignedTable;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String emailAddress;

    @Column(nullable = false)
    private Integer groupSize;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, name = "request_uuid")
    private String requestUuid;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public ReservationRequestEntity() {
    }

    public Long getId() {
        return id;
    }

    public ReservationRequestEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public SeatingEntity getSeating() {
        return seating;
    }

    public ReservationRequestEntity setSeating(SeatingEntity seating) {
        this.seating = seating;
        return this;
    }

    public TableEntity getAssignedTable() {
        return assignedTable;
    }

    public ReservationRequestEntity setAssignedTable(TableEntity assignedTable) {
        this.assignedTable = assignedTable;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ReservationRequestEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ReservationRequestEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public ReservationRequestEntity setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public ReservationRequestEntity setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ReservationRequestEntity setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getRequestUuid() {
        return requestUuid;
    }

    public ReservationRequestEntity setRequestUuid(String requestUuid) {
        this.requestUuid = requestUuid;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReservationRequestEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
