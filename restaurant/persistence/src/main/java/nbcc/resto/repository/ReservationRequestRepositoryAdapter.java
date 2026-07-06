package nbcc.resto.repository;

import nbcc.resto.dto.ReservationRequest;
import nbcc.resto.entity.TableEntity;
import nbcc.resto.mapper.ReservationRequestMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRequestRepositoryAdapter implements ReservationRequestRepository {

   private final ReservationRequestJPARepository reservationRequestJPARepository;
   private final ReservationRequestMapper reservationRequestMapper;

    public ReservationRequestRepositoryAdapter(ReservationRequestJPARepository reservationRequestJPARepository, ReservationRequestMapper reservationRequestMapper) {
        this.reservationRequestJPARepository = reservationRequestJPARepository;
        this.reservationRequestMapper = reservationRequestMapper;
    }

    @Override
    public ReservationRequest create(ReservationRequest request) {
        var entity = reservationRequestMapper.toEntity(request);
        entity = reservationRequestJPARepository.save(entity);
        return reservationRequestMapper.toDTO(entity);
    }

    @Override
    public ReservationRequest update(ReservationRequest request) {
        var entityOptional = reservationRequestJPARepository.findById(request.getId());

        if (entityOptional.isEmpty()) {
            throw new IllegalArgumentException("Reservation request not found");
        }

        var entity = entityOptional.get();
        entity.setStatus(request.getStatus());

        if (request.getTableId() == null) {
            entity.setAssignedTable(null);
        } else {
            entity.setAssignedTable(new TableEntity().setId(request.getTableId()));
        }

        entity = reservationRequestJPARepository.save(entity);
        return reservationRequestMapper.toDTO(entity);
    }

    @Override
    public Optional<ReservationRequest> get(Long id) {
        return reservationRequestMapper.toDTO(reservationRequestJPARepository.findById(id));
    }

    @Override
    public boolean existsBySeatingId(Long seatingId) {
        return reservationRequestJPARepository.existsBySeatingId(seatingId);
    }

    @Override
    public Optional<ReservationRequest> getByRequestUuid(String requestUuid) {
        return reservationRequestMapper.toDTO(reservationRequestJPARepository.findByRequestUuid(requestUuid));
    }

    @Override
    public List<ReservationRequest> search(Long eventId, String status) {
        var entities = reservationRequestJPARepository.search(eventId, normalizeStatus(status));
        return reservationRequestMapper.toDTO(entities);
    }

    @Override
    public boolean existsApprovedReservationUsingTable(Long seatingId, Long tableId, Long excludeReservationId) {
        return reservationRequestJPARepository.existsBySeatingIdAndAssignedTableIdAndStatusIgnoreCaseAndIdNot(
                seatingId, tableId, "APPROVED", excludeReservationId
        );
    }

    @Override
    public List<ReservationRequest> getApprovedByEventId(Long eventId) {
        return reservationRequestMapper.toDTO(reservationRequestJPARepository.getApprovedByEventId(eventId));
    }

    private String normalizeStatus(String status) {
        return status == null || status.isBlank() ? null : status.trim();
    }
}
