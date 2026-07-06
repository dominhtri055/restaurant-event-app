package nbcc.resto.repository;

import nbcc.resto.entity.SeatingTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatingTableJPARepository extends JpaRepository<SeatingTableEntity, Long> {
    List<SeatingTableEntity> findBySeatingId(Long seatingId);

    void deleteBySeatingId(Long seatingId);
}
