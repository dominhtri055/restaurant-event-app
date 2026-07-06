package nbcc.resto.repository;

import nbcc.resto.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventJPARepository extends JpaRepository<EventEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
    Optional<EventEntity> findByNameIgnoreCase(String name);

    @Query("""
           SELECT e FROM EventEntity e
           WHERE (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
             AND (:after IS NULL OR e.startDate >= :after)
             AND (:before IS NULL OR e.startDate <= :before)
             AND e.isArchived = false
           ORDER BY e.startDate ASC
           """)
    List<EventEntity> search(
            @Param("name") String name,
            @Param("after") LocalDate after,
            @Param("before") LocalDate before
    );
}
