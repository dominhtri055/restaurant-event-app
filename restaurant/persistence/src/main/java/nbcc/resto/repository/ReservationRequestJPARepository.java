package nbcc.resto.repository;

import nbcc.resto.entity.ReservationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRequestJPARepository extends JpaRepository<ReservationRequestEntity, Long> {

    boolean existsBySeatingId(Long seatingId);

    Optional<ReservationRequestEntity> findByRequestUuid(String requestUuid);

    @Query("""
           SELECT r
           FROM ReservationRequestEntity r
           JOIN r.seating s
           JOIN s.event e
           WHERE (:eventId IS NULL OR e.id = :eventId)
             AND (:status IS NULL OR UPPER(r.status) = UPPER(:status))
           ORDER BY s.startTime ASC
           """)
    List<ReservationRequestEntity> search(@Param("eventId") Long eventId, @Param("status") String status);

    boolean existsBySeatingIdAndAssignedTableIdAndStatusIgnoreCaseAndIdNot(
            Long seatingId,
            Long assignedTableId,
            String status,
            Long id
    );

    @Query("""
           SELECT r
           FROM ReservationRequestEntity r
           JOIN r.seating s
           JOIN s.event e
           WHERE e.id = :eventId
             AND UPPER(r.status) = 'APPROVED'
           ORDER BY s.startTime ASC
           """)
    List<ReservationRequestEntity> getApprovedByEventId(@Param("eventId") Long eventId);
}

