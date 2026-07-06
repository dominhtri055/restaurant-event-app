package nbcc.resto.viewmodels;

import nbcc.resto.dto.Event;
import nbcc.resto.dto.ReservationRequest;

import java.util.Collection;

public class ApprovedReservationListViewModel {

    private final Event event;
    private final Collection<ReservationRequest> reservations;
    private final boolean canViewDetails;
    private final String message;

    public ApprovedReservationListViewModel(Event event, Collection<ReservationRequest> reservations, boolean canViewDetails, String message) {
        this.event = event;
        this.reservations = reservations;
        this.canViewDetails = canViewDetails;
        this.message = message;
    }

    public Event getEvent() {
        return event;
    }

    public Collection<ReservationRequest> getReservations() {
        return reservations;
    }

    public boolean isCanViewDetails() {
        return canViewDetails;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEmpty() {
        return reservations.isEmpty();
    }
}
