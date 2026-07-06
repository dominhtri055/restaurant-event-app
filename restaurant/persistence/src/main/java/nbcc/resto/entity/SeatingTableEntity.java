package nbcc.resto.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "seating_table",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_seating_table",
                columnNames = {"seating_id", "table_id"}
        )
)
public class SeatingTableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seating_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_seating_table_seating"))
    private SeatingEntity seating;

    @ManyToOne(optional = false)
    @JoinColumn(name = "table_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_seating_table_table"))
    private TableEntity table;

    public SeatingTableEntity(){
    }

    public Long getId() {
        return id;
    }

    public SeatingTableEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public SeatingEntity getSeating() {
        return seating;
    }

    public SeatingTableEntity setSeating(SeatingEntity seating) {
        this.seating = seating;
        return this;
    }

    public TableEntity getTable() {
        return table;
    }

    public SeatingTableEntity setTable(TableEntity table) {
        this.table = table;
        return this;
    }
}
