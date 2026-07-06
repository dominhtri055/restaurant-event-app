package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Event;
import nbcc.resto.repository.EventRepository;
import nbcc.resto.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventValidationService {
    private final AnnotationValidationService annotationValidationService;
    private final EventRepository eventRepository;
    private final MenuRepository menuRepository;

    public EventValidationService(AnnotationValidationService annotationValidationService, EventRepository eventRepository, MenuRepository menuRepository) {
        this.annotationValidationService = annotationValidationService;
        this.eventRepository = eventRepository;
        this.menuRepository = menuRepository;
    }

    public List<ValidationError> validate(Event event) {
        var validationErrors = new ArrayList<ValidationError>();

        validationErrors.addAll(annotationValidationService.validate(event));
        validationErrors.addAll(validationNameExists(event));
        validationErrors.addAll(validateDateRange(event));
        validationErrors.addAll(validationMenuExists(event));
        validationErrors.addAll(validationLiveRequiresMenu(event));

        return validationErrors;
    }

    public List<ValidationError> validationNameExists(Event event) {
        if (event.getName() == null || event.getName().isBlank()) {
            return List.of();
        }

        var existingEvent = eventRepository.getByExactName(event.getName().trim());

        if (existingEvent.isPresent()) {
            var dbEvent = existingEvent.get();

            if (event.getId() == null || !dbEvent.getId().equals(event.getId())) {
                return List.of(new ValidationError("Event name already exists", "name"));
            }
        }

        return List.of();
    }

    public List<ValidationError> validateDateRange(Event event) {
        if (event.getStartDate() == null || event.getEndDate() == null) {
            return List.of();
        }

        if (event.getStartDate().isAfter(event.getEndDate())) {
            return List.of(new ValidationError("End date must be after or equal to start date", "endDate"));
        }
        return List.of();
    }

    public List<ValidationError> validationMenuExists(Event event) {
        if(event.getMenuId() == null){
            return List.of();
        }
        if(menuRepository.get(event.getMenuId()).isEmpty()){
            return List.of(new ValidationError("Selected menu does not exists", "menuId"));
        }
        return List.of();
    }

    public List<ValidationError> validationLiveRequiresMenu(Event event) {
        if(Boolean.TRUE.equals(event.getActive())&&event.getMenuId() == null){
            return List.of(new ValidationError("A menu must select before the event start", "menuId"));
        }
        return List.of();
    }
}