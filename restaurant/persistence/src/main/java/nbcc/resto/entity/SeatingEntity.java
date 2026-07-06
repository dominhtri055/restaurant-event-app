package nbcc.resto.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "seating")
@EntityListeners(AuditingEntityListener.class)
public class SeatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_seating_event"))
    private EventEntity event;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean archived = false;

    @OneToMany(mappedBy = "seating", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SeatingTableEntity> seatingTables = new HashSet<>();

    public SeatingEntity() {
    }

    public Long getId() {
        return id;
    }

    public SeatingEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public EventEntity getEvent() {
        return event;
    }

    public SeatingEntity setEvent(EventEntity event) {
        this.event = event;
        return this;
    }

    public String getName() {
        return name;
    }

    public SeatingEntity setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public SeatingEntity setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public Integer getDuration() {
        return duration;
    }

    public SeatingEntity setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public SeatingEntity setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public SeatingEntity setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public boolean isArchived() {
        return archived;
    }

    public SeatingEntity setArchived(boolean archived) {
        this.archived = archived;
        return this;
    }

    public Set<SeatingTableEntity> getSeatingTables() {
        return seatingTables;
    }

    public SeatingEntity setSeatingTables(Set<SeatingTableEntity> seatingTables) {
        this.seatingTables = seatingTables;
        return this;
    }
}
