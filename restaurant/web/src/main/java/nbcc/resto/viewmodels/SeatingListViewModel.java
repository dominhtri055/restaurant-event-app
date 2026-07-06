package nbcc.resto.viewmodels;

import nbcc.resto.dto.Seating;

import java.util.ArrayList;
import java.util.Collection;

public class SeatingListViewModel {

    private final boolean canAdd;
    private final boolean canEdit;
    private final boolean canDelete;
    private final String message;

    private final Collection<SeatingEventGroupViewModel> eventGroups;

    public SeatingListViewModel(Collection<SeatingEventGroupViewModel> eventGroups, boolean canManage) {
        this(eventGroups, canManage, null);
    }

    public SeatingListViewModel(Collection<SeatingEventGroupViewModel> eventGroups, boolean canManage, String message) {
        this.canAdd = canManage;
        this.canEdit = canManage;
        this.canDelete = canManage;
        this.message = message;
        this.eventGroups = eventGroups;
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

    public Collection<SeatingEventGroupViewModel> getEventGroups() {
        return eventGroups;
    }

    public boolean isEmpty() {
        return getEventGroups().isEmpty();
    }
}