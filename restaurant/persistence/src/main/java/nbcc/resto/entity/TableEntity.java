package nbcc.resto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_table")
@EntityListeners(AuditingEntityListener.class)
public class TableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Positive
    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    public TableEntity() {
    }

    public Long getId() {
        return id;
    }

    public TableEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TableEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public TableEntity setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public TableEntity setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }
}
