package nbcc.resto.repository;

import nbcc.resto.dto.ReservationRequest;

import java.util.List;
import java.util.Optional;

public interface ReservationRequestRepository {

    ReservationRequest create(ReservationRequest request);

    ReservationRequest update(ReservationRequest request);

    Optional<ReservationRequest> get(Long id);

    boolean existsBySeatingId(Long seatingId);

    Optional<ReservationRequest> getByRequestUuid(String requestUuid);

    List<ReservationRequest> search(Long eventId, String status);

    boolean existsApprovedReservationUsingTable(Long seatingId, Long tableId, Long excludeReservationId);

    List<ReservationRequest> getApprovedByEventId(Long eventId);
}
