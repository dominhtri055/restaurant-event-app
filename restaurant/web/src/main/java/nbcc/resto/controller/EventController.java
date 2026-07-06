package nbcc.resto.controller;

import jakarta.validation.Valid;
import nbcc.common.service.LoginService;
import nbcc.resto.dto.Event;
import nbcc.resto.dto.Menu;
import nbcc.resto.service.EventService;
import nbcc.resto.service.MenuService;
import nbcc.resto.viewmodels.EventListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
//@PreAuthorize("isAuthenticated()")
@RequestMapping("/event")
public class EventController {
    private final LoginService loginService;
    private final EventService eventService;
    private final MenuService menuService;

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);
    public EventController(LoginService loginService, EventService eventService, MenuService menuService) {
        this.loginService = loginService;
        this.eventService = eventService;
        this.menuService = menuService;
    }

    @ModelAttribute("menus")
    public Collection<Menu> menus() {
        var result = menuService.getAll();
        if (result.isError() || result.getValue() == null) {
            return List.of();
        }
        return result.getValue();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public String getAll(@RequestParam(required = false) String name,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate after,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate before,
                         Model model) {

        var result = eventService.search(name, after, before);

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving Events");
            return "error/errorPage";
        }

        EventListViewModel viewModel = new EventListViewModel(
                result.getValue(),
                loginService.isLoggedIn(),
                null,
                name,
                after,
                before
        );

        model.addAttribute("viewModel", viewModel);
        return "event/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{eventId}")
    public String getById(@PathVariable("eventId") Long eventId, Model model) {
        var result = eventService.get(eventId);

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving event");
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "Event with id " + eventId + " not found");
            return "error/errorPage";
        }

        model.addAttribute("event", result.getValue());
        return "event/details";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("event", new Event());
        return "event/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("event") Event event, BindingResult br) {
        if (br.hasErrors()) {
            return "event/create";
        }
        var result = eventService.create(event);
        if (result.isError()) {
            return "error/errorPage";
        }
        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return "event/create";
        }
        return "redirect:/event/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit/{eventId}")
    public String edit(@PathVariable("eventId") Long eventId, Model model) {
        var result = eventService.get(eventId);

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving event");
            return "error/errorPage";
        }
        if (result.isEmpty()) {
            model.addAttribute("event", "Event with id " + eventId + " not found");
            return "error/errorPage";
        }

        model.addAttribute("event", result.getValue());
        return "event/edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/{eventId}")
    public String edit(@PathVariable("eventId") Long eventId,
                       @Valid @ModelAttribute("event") Event event,
                       BindingResult br,
                       Model model) {
        event.setId(eventId);

        if (br.hasErrors()) {
            return "event/edit";
        }

        var result = eventService.update(event);

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving event");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return "event/edit";
        }
        return "redirect:/event/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{eventId}")
    public String confirmDelete(@PathVariable("eventId") Long eventId, Model model) {
        var result = eventService.get(eventId);

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving event");
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "Event with id " + eventId + " not found");
            return "error/errorPage";
        }

        model.addAttribute("event", result.getValue());
        return "event/delete";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{eventId}")
    public String delete(@PathVariable("eventId") Long eventId, Model model) {
        var result = eventService.delete(eventId);

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving event");
            return "error/errorPage";
        }

        return "redirect:/event/list";
    }
}