package nbcc.resto.validation;

import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Seating;
import nbcc.resto.repository.EventRepository;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.repository.SeatingTableRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class SeatingValidationService {

    private final EventRepository eventRepository;
    private final SeatingRepository seatingRepository;
    private final SeatingTableRepository seatingTableRepository;


    public SeatingValidationService(EventRepository eventRepository, SeatingRepository seatingRepository, SeatingTableRepository seatingTableRepository) {
        this.eventRepository = eventRepository;
        this.seatingRepository = seatingRepository;
        this.seatingTableRepository = seatingTableRepository;
    }

    public Collection<ValidationError> validate(Seating seating) {
        var errors = new ArrayList<ValidationError>();

        if (seating.getEventId() == null) {
            errors.add(new ValidationError("eventId", "Event id is required."));
        }

        if (seating.getName() == null || seating.getName().isBlank()) {
            errors.add(new ValidationError("name", "Name is required."));
        }

        if (seating.getStartTime() == null) {
            errors.add(new ValidationError("startTime", "Start time is required."));
        }

        if (seating.getDuration() != null && seating.getDuration() <= 0) {
            errors.add(new ValidationError("duration", "Duration mút be greater than zero."));
        }

        if (seating.getTableIds() == null || seating.getTableIds().isEmpty()) {
            errors.add(new ValidationError("tableIds", "Table Ids is required."));
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        var eventOptional = eventRepository.get(seating.getEventId());
        if (eventOptional.isEmpty()) {
            errors.add(new ValidationError("eventId", "Selected event does not exist"));
            return errors;
        }

        var newStart = seating.getStartTime();
        var newEnd = seating.getStartTime().plusMinutes(seating.getDuration());

        var existingSeatings = seatingRepository.getAll();

        for (var existingSeating : existingSeatings) {
            if (existingSeating.isArchived()) {
                continue;
            }

            if (seating.getId() != null && seating.getId().equals(existingSeating.getId())) {
                continue;
            }

            var existingTableIds = seatingTableRepository.getTableIdsBySeatingId(existingSeating.getId());

            boolean hasMatchingTable = false;
            for (Long selectedTableId : seating.getTableIds()) {
                if (existingTableIds.contains(selectedTableId)) {
                    hasMatchingTable = true;
                    break;
                }
            }

            if (!hasMatchingTable) {
                continue;
            }

            var existingStart = existingSeating.getStartTime();
            var existingEnd = existingStart.plusMinutes(existingSeating.getDuration());

            boolean overlaps = newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);

            if (overlaps) {
                errors.add(new ValidationError("tableIds", "One or more selected tables are already in use during that time"));
                break;
            }
        }
        return errors;
    }
}
