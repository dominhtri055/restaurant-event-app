package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nbcc.common.service.LoginService;
import nbcc.resto.dto.ReservationRequest;
import nbcc.resto.dto.ReservationStatusUpdate;
import nbcc.resto.service.*;
import nbcc.resto.viewmodels.ApprovedReservationListViewModel;
import nbcc.resto.viewmodels.ReservationRequestListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@RequestMapping("/reservation")
public class ReservationRequestController {
    private final Logger logger = LoggerFactory.getLogger(ReservationRequestController.class);

    private final ReservationRequestService reservationRequestService;
    private final EventService eventService;
    private final SeatingService seatingService;
    private final MenuService menuService;
    private final LoginService loginService;
    private final MenuItemService menuItemService;
    private final TableService tableService;

    public ReservationRequestController(ReservationRequestService reservationRequestService,
                                        EventService eventService,
                                        SeatingService seatingService, MenuService menuService, LoginService loginService, MenuItemService menuItemService, TableService tableService) {
        this.reservationRequestService = reservationRequestService;
        this.eventService = eventService;
        this.seatingService = seatingService;
        this.menuService = menuService;
        this.loginService = loginService;
        this.menuItemService = menuItemService;
        this.tableService = tableService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/request")
    public String create(@RequestParam(required = false) Long seatingId, Model model) {
        var reservationRequest = new ReservationRequest();

        if (seatingId != null) {
            reservationRequest.setSeatingId(seatingId);
        }

        model.addAttribute("reservationRequest", reservationRequest);
        loadModel(model);
        loadSelectedDetails(seatingId, model);

        return "reservation/request";
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/request")
    public String create(@ModelAttribute("reservationRequest") ReservationRequest reservationRequest,
                         BindingResult br,
                         Model model) {

        if (reservationRequest.getSeatingId() != null) {
            var seatingResult = seatingService.get(reservationRequest.getSeatingId());

            if (!seatingResult.isError() && !seatingResult.isEmpty()) {
                reservationRequest.setEventId(seatingResult.getValue().getEventId());
            }
        }

        var result = reservationRequestService.create(reservationRequest);

        if (result.isError()) {
            model.addAttribute("message", "Error creating reservation request");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            loadModel(model);
            loadSelectedDetails(reservationRequest.getSeatingId(), model);
            return "reservation/request";
        }

        model.addAttribute("reservationRequest", result.getValue());
        return "reservation/success";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/details/{requestUuid}")
    public String details(@PathVariable String requestUuid, Model model) {
        var result = reservationRequestService.getByRequestUuid(requestUuid);

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving reservation request");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            model.addAttribute("message", "A valid reservation UUID is required");
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "Reservation request not found");
            return "error/errorPage";
        }

        model.addAttribute("reservationRequest", result.getValue());
        return "reservation/details";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(@RequestParam(required = false) Long eventId,
                       @RequestParam(required = false) String status,
                       Model model) {

        var requestsResult = reservationRequestService.search(eventId, status);
        var eventsResult = eventService.getAll();

        if (requestsResult.isError() || eventsResult.isError()) {
            model.addAttribute("message", "Error retrieving reservation requests");
            return "error/errorPage";
        }

        var viewModel = new ReservationRequestListViewModel(
                requestsResult.getValue(),
                eventsResult.getValue(),
                eventId,
                status,
                loginService.isLoggedIn(),
                null
        );

        model.addAttribute("viewModel", viewModel);
        return "reservation/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String detailsForStaff(@PathVariable Long id, Model model) {
        var result = reservationRequestService.get(id);

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving reservation request");
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "Reservation request not found");
            return "error/errorPage";
        }

        var reservationRequest = result.getValue();

        model.addAttribute("reservationRequest", reservationRequest);
        model.addAttribute("statusUpdate", new ReservationStatusUpdate());
        loadAvailableTablesForReservation(reservationRequest, model);

        return "reservation/staff-details";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @ModelAttribute("statusUpdate") ReservationStatusUpdate statusUpdate,
                               BindingResult br,
                               Model model) {

        var requestResult = reservationRequestService.get(id);

        if (requestResult.isError()) {
            model.addAttribute("message", "Error retrieving reservation request");
            return "error/errorPage";
        }

        if (requestResult.isEmpty()) {
            model.addAttribute("message", "Reservation request not found");
            return "error/errorPage";
        }

        var updateResult = reservationRequestService.updateStatus(id, statusUpdate);

        if (updateResult.isError()) {
            model.addAttribute("message", "Error updating reservation request");
            return "error/errorPage";
        }

        if (updateResult.isInvalid()) {
            addErrorsToBindingResults(br, updateResult);
            model.addAttribute("reservationRequest", requestResult.getValue());
            loadAvailableTablesForReservation(requestResult.getValue(), model);
            return "reservation/staff-details";
        }

        return "redirect:/reservation/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/event/{eventId}/approved")
    public String approvedByEvent(@PathVariable Long eventId, Model model) {
        var eventResult = eventService.get(eventId);
        var reservationResult = reservationRequestService.getApprovedByEventId(eventId);

        if (eventResult.isError() || reservationResult.isError()) {
            model.addAttribute("message", "Error retrieving approved reservation");
            return "error/errorPage";
        }

        if (eventResult.isEmpty()) {
            model.addAttribute("message", "Event not found");
            return "error/errorPage";
        }

        var viewModel = new ApprovedReservationListViewModel(
                eventResult.getValue(),
                reservationResult.getValue(),
                loginService.isLoggedIn(),
                null
        );

        model.addAttribute("viewModel", viewModel);
        return "reservation/approved-list";
    }

    private void loadModel(Model model) {
        var eventsResult = eventService.getAll();
        var seatingsResult = seatingService.getAll();

        if (!eventsResult.isError()) {
            model.addAttribute("events", eventsResult.getValue());
        }

        if (!seatingsResult.isError()) {
            model.addAttribute("seatings", seatingsResult.getValue());
        }
    }

    private void loadSelectedDetails(Long seatingId, Model model) {
        if (seatingId == null) {
            return;
        }

        var seatingResult = seatingService.get(seatingId);

        if (seatingResult.isError() || seatingResult.isEmpty()) {
            return;
        }

        var seating = seatingResult.getValue();
        model.addAttribute("selectedSeating", seating);

        if (seating.getEventId() == null) {
            return;
        }

        var eventResult = eventService.get(seating.getEventId());

        if (eventResult.isError() || eventResult.isEmpty()) {
            return;
        }

        var event = eventResult.getValue();
        model.addAttribute("selectedEvent", event);

        if (event.getMenuId() == null) {
            return;
        }

        var menuResult = menuService.get(event.getMenuId());

        if (menuResult.isError() || menuResult.isEmpty()) {
            return;
        }

        var menu = menuResult.getValue();
        model.addAttribute("selectedMenu", menu);

        var menuItemResult = menuItemService.getByMenuId(menu.getId());

        if (!menuItemResult.isError()) {
            model.addAttribute("selectedMenuItems", menuItemResult.getValue());
        }
    }

    private void loadAvailableTablesForReservation(ReservationRequest reservationRequest, Model model) {
        var seatingResult = seatingService.get(reservationRequest.getSeatingId());
        var tablesResult = tableService.getAll();

        if (seatingResult.isError() || seatingResult.isEmpty() || tablesResult.isError()) {
            model.addAttribute("availableTables", new ArrayList<>());
            return;
        }

        var seating = seatingResult.getValue();
        var availableTables = tablesResult.getValue()
                .stream()
                .filter(table -> seating.getTableIds() != null && seating.getTableIds().contains(table.getId()))
                .toList();

        model.addAttribute("availableTables", availableTables);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public String authorizationDeniedHandler() {
        return "redirect:/login";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request) {
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI(), request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}