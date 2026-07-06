package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.ReservationRequest;
import nbcc.resto.repository.SeatingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationRequestValidationService {

    private final AnnotationValidationService annotationValidationService;
    private final SeatingRepository seatingRepository;

    public ReservationRequestValidationService(AnnotationValidationService annotationValidationService, SeatingRepository seatingRepository) {
        this.annotationValidationService = annotationValidationService;
        this.seatingRepository = seatingRepository;
    }

    public List<ValidationError> validate(ReservationRequest request) {
        var errors = new ArrayList<ValidationError>();

        errors.addAll(annotationValidationService.validate(request));

        if (request.getEventId() == null) {
            errors.add(new ValidationError("eventId", "EventId is required"));
            return errors;
        }

        if (request.getSeatingId() == null) {
            errors.add(new ValidationError("seatingId", "SeatingId is required"));
            return errors;
        }

        var seatingOptional = seatingRepository.get(request.getSeatingId());

        if (seatingOptional.isEmpty()) {
            errors.add(new ValidationError("seatingId", "Selected seating does not exist"));
            return errors;
        }

        var seating = seatingOptional.get();

        if (seating.isArchived()) {
            errors.add(new ValidationError("seatingId", "Selected seating is no longer available"));
        }

        if (seating.getEventId() == null || !seating.getEventId().equals(request.getEventId())) {
            errors.add(new ValidationError("seatingId", "SeatingId is not valid for the specified EventId"));
        }

        return errors;
    }
}
