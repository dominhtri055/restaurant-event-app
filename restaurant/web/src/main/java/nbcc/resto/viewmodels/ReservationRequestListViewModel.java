package nbcc.resto.viewmodels;

import nbcc.resto.dto.Event;
import nbcc.resto.dto.ReservationRequest;

import java.util.Collection;

public class ReservationRequestListViewModel {

    private final Collection<ReservationRequest> requests;
    private final Collection<Event> events;
    private final Long selectedEventId;
    private final String selectedStatus;
    private final boolean canViewDetails;
    private final String message;

    public ReservationRequestListViewModel(Collection<ReservationRequest> requests, Collection<Event> events, Long selectedEventId, String selectedStatus, boolean canViewDetails, String message) {
        this.requests = requests;
        this.events = events;
        this.selectedEventId = selectedEventId;
        this.selectedStatus = selectedStatus;
        this.canViewDetails = canViewDetails;
        this.message = message;
    }

    public Collection<ReservationRequest> getRequests() {
        return requests;
    }

    public Collection<Event> getEvents() {
        return events;
    }

    public Long getSelectedEventId() {
        return selectedEventId;
    }

    public String getSelectedStatus() {
        return selectedStatus;
    }

    public boolean isCanViewDetails() {
        return canViewDetails;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEmpty() {
        return requests.isEmpty();
    }
}
