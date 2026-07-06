package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.ReservationRequest;
import nbcc.resto.dto.ReservationStatusUpdate;

import java.util.Collection;

public interface ReservationRequestService {

    ValidatedResult<ReservationRequest> create(ReservationRequest request);

    ValidatedResult<ReservationRequest> getByRequestUuid(String requestUuid);

    ValidatedResult<ReservationRequest> get(Long id);

    Result<Collection<ReservationRequest>> search(Long eventId, String status);

    ValidatedResult<ReservationRequest> updateStatus(Long id, ReservationStatusUpdate update);

    Result<Collection<ReservationRequest>> getApprovedByEventId(Long eventId);
}
