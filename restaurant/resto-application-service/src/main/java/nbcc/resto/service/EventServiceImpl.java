package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.resto.dto.Event;
import nbcc.resto.repository.EventRepository;
import nbcc.resto.validation.EventValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    private final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventValidationService validationService;
    private final EventRepository eventRepository;

    public EventServiceImpl(EventValidationService validationService, EventRepository eventRepository) {
        this.validationService = validationService;
        this.eventRepository = eventRepository;
    }

    @Override
    public Result<Collection<Event>> getAll() {
        try{
            var events = eventRepository.getAll()
                    .stream()
                    .filter(event -> !event.getArchive())
                    .toList();

            return ValidationResults.success(events);
        }catch(Exception e){
            logger.error("Error retrieving all events", e);
            return ValidationResults.error();
        }
    }

    @Override
    public Result<Collection<Event>> search(String name, LocalDate after, LocalDate before) {
        try {
            var events = eventRepository.search(name, after, before);
            return ValidationResults.success(events);
        } catch (Exception e) {
            logger.error("Error searching events", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Event> get(Long id) {
        try{
            return ValidationResults.success(eventRepository.get(id));
        }catch(Exception e){
            logger.error("Error retrieving event by id:{}",id, e);
            return ValidationResults.error();
        }
    }


    @Override
    public ValidatedResult<Event> create(Event event) {
        try{
            var errors = validationService.validate(event);
            if(errors.isEmpty()){
                event = eventRepository.create(event);
                return ValidationResults.success(event);
            }
            return ValidationResults.invalid(event, errors);
        }catch(Exception e){
            logger.error("Error creating event {}",event,e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Void> delete(Long id) {
        try {
            var optionalEvent = eventRepository.get(id);
            if (optionalEvent.isEmpty()) {
                return ValidationResults.error();
            }

            var event = optionalEvent.get();

            if (event.getEndDate() != null && event.getEndDate().isBefore(LocalDate.now())) {
                event.setArchive(true);
                event.setActive(false);
                event.setUpdatedAt(java.time.LocalDateTime.now());
                eventRepository.update(event);
            } else {
                event.setArchive(true);
                event.setActive(false);
                event.setUpdatedAt(java.time.LocalDateTime.now());
                eventRepository.update(event);
            }

            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting event by id:{}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Event> update(Event event) {
        try {
            var optionalExisting = eventRepository.get(event.getId());

            if (optionalExisting.isEmpty()) {
                return ValidationResults.error();
            }

            var errors = validationService.validate(event);
            if (!errors.isEmpty()) {
                return ValidationResults.invalid(event, errors);
            }

            event.setUpdatedAt(java.time.LocalDateTime.now());

            var updatedEvent = eventRepository.update(event);
            return ValidationResults.success(updatedEvent);

        } catch (Exception e) {
            logger.error("Error updating event {}", event, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Boolean> getByName(String name) {
        try{
            return ValidationResults.success(eventRepository.getByName(name));
        } catch (Exception e) {
            logger.error("Error checking if event {} exists", name, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Optional<Event> getByExactName(String name) {
        return Optional.empty();
    }
}

