package nbcc.resto.viewmodels;

import nbcc.resto.dto.Event;
import nbcc.resto.dto.Seating;

import java.util.ArrayList;
import java.util.Collection;

public class SeatingEventGroupViewModel {
    private final Event event;
    private final Collection<Seating> seatings;

    public SeatingEventGroupViewModel(Event event, Collection<Seating> seatings) {
        this.event = event;
        this.seatings = seatings;
    }

    public Event getEvent() {
        return event;
    }

    public Collection<Seating> getSeatings() {
        return seatings != null ? seatings : new ArrayList<>();
    }

    public boolean isEmpty() {
        return getSeatings().isEmpty();
    }
}
