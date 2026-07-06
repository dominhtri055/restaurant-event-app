package nbcc.resto.service;

import jakarta.transaction.Transactional;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.resto.dto.Seating;
import nbcc.resto.repository.ReservationRequestRepository;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.repository.SeatingTableRepository;
import nbcc.resto.validation.SeatingValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

import static nbcc.common.result.ValidationResults.invalid;

@Service
public class SeatingServiceImpl implements SeatingService {

    private final Logger logger = LoggerFactory.getLogger(SeatingServiceImpl.class);

    private final SeatingRepository seatingRepository;
    private final SeatingTableRepository seatingTableRepository;
    private final SeatingValidationService seatingValidationService;
    private final EventService eventService;
    private final ReservationRequestRepository reservationRequestRepository;

    public SeatingServiceImpl(SeatingRepository seatingRepository, SeatingTableRepository seatingTableRepository, SeatingValidationService seatingValidationService, EventService eventService, ReservationRequestRepository reservationRequestRepository) {
        this.seatingRepository = seatingRepository;
        this.seatingTableRepository = seatingTableRepository;
        this.seatingValidationService = seatingValidationService;
        this.eventService = eventService;
        this.reservationRequestRepository = reservationRequestRepository;
    }

    @Override
    public Result<Collection<Seating>> getAll() {
        try {
            var seatings = seatingRepository.getAll()
                    .stream()
                    .filter(seating -> !seating.isArchived())
                    .toList();

            return ValidationResults.success(seatings);
        } catch (Exception e) {
            logger.error("Error retrieving all seatings", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Seating> get(Long id) {
        try {
            var result = seatingRepository.get(id);

            if (result.isEmpty()) {
                return ValidationResults.success();
            }

            var seating = result.get();
            seating.setTableIds(seatingTableRepository.getTableIdsBySeatingId(id));

            return ValidationResults.success(seating);
        } catch (Exception e) {
            logger.error("Error retrieving seating {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Seating> create(Seating seating) {
        try {

            var eventResult = eventService.get(seating.getEventId());

            var event = eventResult.getValue();

            if (seating.getDuration() == null) {
                seating.setDuration(event.getDuration());
            }

            var errors = seatingValidationService.validate(seating);

            if (!errors.isEmpty()) {
                return invalid(seating, errors);
            }

            if (seating.getCreatedDate() == null) {
                seating.setCreatedDate(LocalDateTime.now());
            }
            var createdSeating = seatingRepository.create(seating);

            if (seating.getTableIds() != null) {
                for(Long tableId : seating.getTableIds()) {
                    seatingTableRepository.create(createdSeating.getId(), tableId);
                }
            }
            createdSeating.setTableIds(
                    seatingTableRepository.getTableIdsBySeatingId(createdSeating.getId())
            );
            return ValidationResults.success(createdSeating);
        } catch (Exception e) {
            logger.error("Error creating seating {}", seating, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Seating> update(Seating seating) {
        try {
            var existingResult = seatingRepository.get(seating.getId());

            if (existingResult.isEmpty()) {
                return ValidationResults.success();
            }

            var existing = existingResult.get();

            if (seating.getTableIds() == null || seating.getTableIds().isEmpty()) {
                seating.setTableIds(seatingTableRepository.getTableIdsBySeatingId(seating.getId()));
            }

            if (seating.getDuration() == null) {
                var eventResult = eventService.get(seating.getEventId());
                var event = eventResult.getValue();
                seating.setDuration(event.getDuration());
            }

            seating.setCreatedDate(existing.getCreatedDate());
            seating.setArchived(seating.isArchived());
            seating.setUpdatedAt(LocalDateTime.now());

            var errors = seatingValidationService.validate(seating);

            if (!errors.isEmpty()) {
                return ValidationResults.invalid(seating, errors);
            }

            var updated = seatingRepository.update(seating);
            updated.setTableIds(seatingTableRepository.getTableIdsBySeatingId(updated.getId()));

            return ValidationResults.success(updated);
        } catch (Exception e) {
            logger.error("Error updating seating {}", seating, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    @Transactional
    public ValidatedResult<Seating> delete(Long id) {
        try {
            var existingResult = seatingRepository.get(id);

            if (existingResult.isEmpty()) {
                return ValidationResults.success();
            }

            boolean hasReservations = reservationRequestRepository.existsBySeatingId(id);

            if (hasReservations) {
                var seating = existingResult.get();
                seating.setArchived(true);
                seating.setUpdatedAt(LocalDateTime.now());
                seatingRepository.update(seating);
            } else {
                seatingTableRepository.deleteBySeatingId(id);
                seatingRepository.delete(id);
            }

            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting seating {}", id, e);
            return ValidationResults.error(e);
        }
    }
}
