package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nbcc.common.service.LoginService;
import nbcc.resto.dto.Seating;
import nbcc.resto.service.EventService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.service.TableService;
import nbcc.resto.viewmodels.SeatingEventGroupViewModel;
import nbcc.resto.viewmodels.SeatingListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/seating")
public class SeatingController {

    private final LoginService loginService;
    private final SeatingService seatingService;
    private final EventService eventService;
    private final Logger logger = LoggerFactory.getLogger(SeatingController.class);
    private final TableService tableService;

    public SeatingController(LoginService loginService, SeatingService seatingService, EventService eventService, TableService tableService) {
        this.loginService = loginService;
        this.seatingService = seatingService;
        this.eventService = eventService;
        this.tableService = tableService;
    }

    @GetMapping("/list")
    public String getAll(Model model) {
        var seatingResult = seatingService.getAll();
        var eventResult = eventService.getAll();


        if (seatingResult.isError() || eventResult.isError()) {
            model.addAttribute("message", "Error retrieving seatings");
            return "error/errorPage";
        }

        var eventGroups = new ArrayList<SeatingEventGroupViewModel>();

        for (var event : eventResult.getValue()) {
            var eventSeatings = new ArrayList<Seating>();

            for (var seating : seatingResult.getValue()) {
                if (seating.getEventId() != null && seating.getEventId().equals(event.getId())) {
                    eventSeatings.add(seating);
                }
            }

            eventGroups.add(new SeatingEventGroupViewModel(event, eventSeatings));
        }

        var viewModel = new SeatingListViewModel(eventGroups, loginService.isLoggedIn());
        model.addAttribute("viewModel", viewModel);

        return "seating/list";
    }

    @GetMapping("/create")
    public String create(Model model) {

        var seating = new Seating();
        model.addAttribute("seating", seating);

        loadEvents(model);

        return "seating/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("seating") Seating seating,
                         BindingResult br, Model model) {

        if (br.hasErrors()) {
            loadEvents(model);
            return "seating/create";
        }

        var result = seatingService.create(seating);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            loadEvents(model);
            return "seating/create";
        }

        return "redirect:/seating/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        var result = seatingService.get(id);

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving seating");
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "Seating not found");
            return "error/errorPage";
        }

        model.addAttribute("seating", result.getValue());
        loadEvents(model);
        return "seating/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("seating") Seating seating,
                       BindingResult br,
                       Model model) {

        seating.setId(id);

        var existingResult = seatingService.get(id);
        if (!existingResult.isEmpty()) {
            seating.setTableIds(existingResult.getValue().getTableIds());
        }

        if (br.hasErrors()) {
            loadEvents(model);
            return "seating/edit";
        }

        var result = seatingService.update(seating);

        if (result.isError()) {
            model.addAttribute("message", "Error updating seating");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            loadEvents(model);
            return "seating/edit";
        }

        return "redirect:/seating/list";
    }

    @GetMapping("/delete/{id}")
    public String confirmDelete(@PathVariable Long id, Model model) {
        var result = seatingService.get(id);

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving seating");
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "Seating not found");
            return "error/errorPage";
        }

        model.addAttribute("seating", result.getValue());
        return "seating/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        var result = seatingService.delete(id);

        if (result.isError()) {
            model.addAttribute("message", "Error deleting seating");
            return "error/errorPage";
        }

        return "redirect:/seating/list";
    }

    private void loadEvents(Model model) {
        var eventResult = eventService.getAll();
        var tableResult = tableService.getAll();

        if (!eventResult.isError()) {
            model.addAttribute("events", eventResult.getValue());
        }

        if (!tableResult.isError()) {
            model.addAttribute("tables", tableResult.getValue());
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request){
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}
