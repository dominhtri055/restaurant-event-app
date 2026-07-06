package nbcc.resto.viewmodels;

import nbcc.resto.dto.Event;

import java.time.LocalDate;
import java.util.Collection;

public class EventListViewModel {
    private final boolean canAdd;
    private final boolean canEdit;
    private final boolean canDelete;
    private final String message;
    private final Collection<Event> events;

    private final String name;
    private final LocalDate after;
    private final LocalDate before;

    public EventListViewModel(Collection<Event> events, boolean canManage) {
        this(events, canManage, null, null, null, null);
    }

    public EventListViewModel(Collection<Event> events,
                              boolean canManage,
                              String message,
                              String name,
                              LocalDate after,
                              LocalDate before) {
        this.events = events;
        this.canAdd = canManage;
        this.canEdit = canManage;
        this.canDelete = canManage;
        this.message = message;
        this.name = name;
        this.after = after;
        this.before = before;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public String getMessage() {
        return message;
    }

    public Collection<Event> getEvents() {
        return events;
    }

    public String getName() {
        return name;
    }

    public LocalDate getAfter() {
        return after;
    }

    public LocalDate getBefore() {
        return before;
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }
}