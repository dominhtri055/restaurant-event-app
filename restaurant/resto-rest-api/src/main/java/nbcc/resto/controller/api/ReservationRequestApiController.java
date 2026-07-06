package nbcc.resto.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.resto.controller.api.result.ResultHandler;
import nbcc.resto.dto.ReservationRequest;
import nbcc.resto.service.EventService;
import nbcc.resto.service.ReservationRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "Reservation Request API", description = "Reservation Request Create")
@RestController
@RequestMapping("/api/reservation-request")
public class ReservationRequestApiController {

    private final ReservationRequestService reservationRequestService;
    private final EventService eventService;

    public ReservationRequestApiController(ReservationRequestService reservationRequestService, EventService eventService) {
        this.reservationRequestService = reservationRequestService;
        this.eventService = eventService;
    }

    @Operation(summary = "Create Reservation Request")
    @PostMapping
    public ResponseEntity<Result<ReservationRequest>> create(@RequestBody ReservationRequest request) {
        var result = this.reservationRequestService.create(request);
        return ResultHandler.handleResult(result, HttpStatus.CREATED);
    }


    @Operation(summary = "Get approved reservations for an event")
    @GetMapping("/event/{eventId}/approved")
    public ResponseEntity<Result<Collection<ReservationRequest>>> getApprovedByEventId(@PathVariable Long eventId) {
        var eventResult = eventService.get(eventId);

        if (eventResult.isError()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (eventResult.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var result = reservationRequestService.getApprovedByEventId(eventId);
        return ResultHandler.handleResult(result, HttpStatus.OK);
    }
}
