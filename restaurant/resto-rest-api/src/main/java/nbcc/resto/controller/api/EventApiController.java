package nbcc.resto.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.resto.dto.Event;
import nbcc.resto.controller.api.result.ResultHandler;
import nbcc.resto.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;

@Tag(name = "Event API", description = "Event CRUD operations")
@RestController
@RequestMapping("/api/event")
public class EventApiController {

    private final EventService eventService;

    public EventApiController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Get all Event")
    @GetMapping({"/list"})

    public ResponseEntity<Result<Collection<Event>>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate after,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate before) {

        var result = hasSearchCriteria(name, after, before)
                ? eventService.search(name, after, before)
                : eventService.getAll();

        return ResultHandler.handleResult(result, HttpStatus.OK);
    }

    @Operation(summary = "Get event by id")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{eventId}")
    public ResponseEntity<Result<Event>> getById(@PathVariable Long eventId) {
        var result = eventService.get(eventId);
        return ResultHandler.handleResult(result, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Create new event")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Result<Event>> create(@RequestBody Event event) {
        var result = eventService.create(event);
        return ResultHandler.handleResult(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Update existing event")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{eventId}")
    public ResponseEntity<Result<Event>> update(@PathVariable Long eventId, @RequestBody Event event) {
        var existingResult = eventService.get(eventId);
        if (existingResult.isError()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (existingResult.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        event.setId(eventId);
        var result = eventService.update(event);
        return ResultHandler.handleResult(result, HttpStatus.OK);
    }

    @Operation(summary = "Delete event by id")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable Long eventId) {
        var existingResult = eventService.get(eventId);
        if (existingResult.isError()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (existingResult.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var result = eventService.delete(eventId);
        if (result.isError()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean hasSearchCriteria(String name, LocalDate after, LocalDate before) {
        return (name != null && !name.isBlank()) || after != null || before != null;
    }
}