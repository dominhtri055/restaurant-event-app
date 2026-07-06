package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.email.service.EmailService;
import nbcc.email.domain.EmailRequest;
import nbcc.resto.dto.ReservationRequest;
import nbcc.resto.dto.ReservationStatusUpdate;
import nbcc.resto.repository.ReservationRequestRepository;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.repository.SeatingTableRepository;
import nbcc.resto.repository.TableRepository;
import nbcc.resto.validation.ReservationRequestValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
public class ReservationRequestServiceImpl implements ReservationRequestService {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_APPROVED = "APPROVED";
    private static final String STATUS_DENIED = "DENIED";

    private final Logger logger = LoggerFactory.getLogger(ReservationRequestServiceImpl.class);


    private final ReservationRequestRepository reservationRequestRepository;
    private final ReservationRequestValidationService reservationRequestValidationService;
    private final SeatingRepository seatingRepository;
    private final SeatingTableRepository seatingTableRepository;
    private final TableRepository tableRepository;
    private final EmailService emailService;

    public ReservationRequestServiceImpl(ReservationRequestRepository reservationRequestRepository, ReservationRequestValidationService reservationRequestValidationService, SeatingRepository seatingRepository, SeatingTableRepository seatingTableRepository, TableRepository tableRepository, EmailService emailService) {
        this.reservationRequestRepository = reservationRequestRepository;
        this.reservationRequestValidationService = reservationRequestValidationService;
        this.seatingRepository = seatingRepository;
        this.seatingTableRepository = seatingTableRepository;
        this.tableRepository = tableRepository;
        this.emailService = emailService;
    }

    @Override
    public ValidatedResult<ReservationRequest> create(ReservationRequest request) {
        try {
            var errors = reservationRequestValidationService.validate(request);

            if (!errors.isEmpty()) {
                return ValidationResults.invalid(request, errors);
            }

            var seatingOptional = seatingRepository.get(request.getSeatingId());

            if (seatingOptional.isEmpty()) {
                var seatingErrors = new ArrayList<ValidationError>();
                seatingErrors.add(new ValidationError("seatingId", "Seating does not exist"));

                return ValidationResults.invalid(request, seatingErrors);
            }

            var seating = seatingOptional.get();

            request.setStatus(STATUS_PENDING);
            request.setRequestUuid(UUID.randomUUID().toString());
            request.setCreatedAt(LocalDateTime.now());
            request.setSeatingName(seating.getName());
            request.setSeatingStartTime(seating.getStartTime());
            request.setEventId(seating.getEventId());
            request.setEventName(seating.getEventName());

            var created = reservationRequestRepository.create(request);

            created.setSeatingName(seating.getName());
            created.setSeatingStartTime(seating.getStartTime());
            created.setEventId(seating.getEventId());
            created.setEventName(seating.getEventName());

            sendReservationReceivedEmail(created);
            return ValidationResults.success(created);
        } catch (Exception e) {
            logger.error("Error creating reservation request {}", request, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<ReservationRequest> getByRequestUuid(String requestUuid) {
        try {
            if (requestUuid == null || requestUuid.isBlank()) {
                var errors = new ArrayList<ValidationError>();
                errors.add(new ValidationError("requestUuid", "Reservation UUID is required"));

                return ValidationResults.invalid(null, errors);
            }
            return ValidationResults.success(reservationRequestRepository.getByRequestUuid(requestUuid.trim()));
        } catch (Exception e) {
            logger.error("Error getting reservation request {}", requestUuid, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<ReservationRequest> get(Long id) {
        try {
            return ValidationResults.success(reservationRequestRepository.get(id));
        } catch (Exception e) {
            logger.error("Error getting reservation request {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<ReservationRequest>> search(Long eventId, String status) {
        try {
            var requests = reservationRequestRepository.search(eventId, status);
            return ValidationResults.success(requests);
        } catch (Exception e) {
            logger.error("Error searching reservation request eventId={} status={}", eventId, status, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<ReservationRequest> updateStatus(Long id, ReservationStatusUpdate update) {
        try {
            var existingOptional = reservationRequestRepository.get(id);

            if (existingOptional.isEmpty()) {
                return ValidationResults.success();
            }

            var existing = existingOptional.get();
            var errors = new ArrayList<ValidationError>();

            if (STATUS_APPROVED.equalsIgnoreCase(existing.getStatus())) {
                errors.add(new ValidationError("status", "An approved reservation cannot be changed"));
                return ValidationResults.invalid(existing, errors);
            }

            if (update.getStatus() == null || update.getStatus().isBlank()) {
                errors.add(new ValidationError("status", "Status is required"));
                return ValidationResults.invalid(existing, errors);
            }

            var newStatus = update.getStatus().trim().toUpperCase();

            if (!STATUS_APPROVED.equals(newStatus) && !STATUS_DENIED.equals(newStatus)) {
                errors.add(new ValidationError("status", "Status must be APPROVED or DENIED"));
                return ValidationResults.invalid(existing, errors);
            }

            if (STATUS_APPROVED.equals(newStatus)) {
                if (update.getTableId() == null) {
                    errors.add(new ValidationError("tableId", "A table must be selected to approve a reservation"));
                    return ValidationResults.invalid(existing, errors);
                }

                var tableOptional = tableRepository.get(update.getTableId());
                if (tableOptional.isEmpty()) {
                    errors.add(new ValidationError("tableId", "Selected table does not exist"));
                    return ValidationResults.invalid(existing, errors);
                }

                var seatingTableIds = seatingTableRepository.getTableIdsBySeatingId(existing.getSeatingId());
                if (!seatingTableIds.contains(update.getTableId())) {
                    errors.add(new ValidationError("tableId", "Selected table does not belong to this seating"));
                    return ValidationResults.invalid(existing, errors);
                }

                boolean alreadyUsed = reservationRequestRepository.existsApprovedReservationUsingTable(
                        existing.getSeatingId(),
                        update.getTableId(),
                        existing.getId()
                );

                if (alreadyUsed) {
                    errors.add(new ValidationError("tableId", "Selected table is already assigned to another approved reservation for this seating"));
                    return ValidationResults.invalid(existing, errors);
                }

                existing.setStatus(STATUS_APPROVED);
                existing.setTableId(update.getTableId());
                existing.setTableName(tableOptional.get().getName());
            } else {
                existing.setStatus(STATUS_DENIED);
                existing.setTableId(null);
                existing.setTableName(null);
            }

            var updated = reservationRequestRepository.update(existing);

            updated.setSeatingName(existing.getSeatingName());
            updated.setSeatingStartTime(existing.getSeatingStartTime());
            updated.setEventId(existing.getEventId());
            updated.setEventName(existing.getEventName());
            updated.setTableName(existing.getTableName());

            sendReservationStatusChangedEmail(updated);
            return ValidationResults.success(updated);
        } catch (Exception e) {
            logger.error("Error updating reservation request status id={} update={}", id, update, e);
            return ValidationResults.error(e);
        }
    }

    private void sendReservationReceivedEmail(ReservationRequest request) {
        try {
            var emailRequest = new EmailRequest()
                    .setTo(request.getEmailAddress())
                    .setSubject("Reservation Request Received")
                    .setBody(
                            "Your reservation request has been received."
                                    + "\n\nEvent Name: " + request.getEventName()
                                    + "\nSeating Date and Time: " + request.getSeatingStartTime()
                                    + "\nGuest First Name: " + request.getFirstName()
                                    + "\nGuest Last Name: " + request.getLastName()
                                    + "\nGroup Size: " + request.getGroupSize()
                                    + "\nRequest Status: " + request.getStatus()
                    );

            logger.info("Sending reservation received email to user: {}", request.getEmailAddress());

            if (emailService.sendEmail(emailRequest)) {
                logger.info("Reservation received email successfully sent to user: {}", request.getEmailAddress());
            } else {
                logger.warn("Something went wrong when trying to send reservation received email to user: {}", request.getEmailAddress());
            }
        } catch (Exception e) {
            logger.error("Error when trying to send reservation received email to user: {}", request.getEmailAddress(), e);
        }
    }

    private void sendReservationStatusChangedEmail(ReservationRequest request) {
        try {
            var emailRequest = new EmailRequest()
                    .setTo(request.getEmailAddress())
                    .setSubject("Reservation Request Status Updated")
                    .setBody(
                            "Your reservation request status has been updated."
                                    + "\n\nEvent Name: " + request.getEventName()
                                    + "\nSeating Date and Time: " + request.getSeatingStartTime()
                                    + "\nGuest First Name: " + request.getFirstName()
                                    + "\nGuest Last Name: " + request.getLastName()
                                    + "\nGroup Size: " + request.getGroupSize()
                                    + "\nRequest Status: " + request.getStatus()
                    );

            logger.info("Sending reservation status email to user: {}", request.getEmailAddress());

            if (emailService.sendEmail(emailRequest)) {
                logger.info("Reservation status email successfully sent to user: {}", request.getEmailAddress());
            } else {
                logger.warn("Something went wrong when trying to send reservation status email to user: {}", request.getEmailAddress());
            }
        } catch (Exception e) {
            logger.error("Error when trying to send reservation status email to user: {}", request.getEmailAddress(), e);
        }
    }

    @Override
    public Result<Collection<ReservationRequest>> getApprovedByEventId(Long eventId) {
        try {
            return ValidationResults.success(reservationRequestRepository.getApprovedByEventId(eventId));
        } catch (Exception e) {
            logger.error("Error retrieving approved reservations for event {}", eventId, e);
            return ValidationResults.error(e);
        }
    }
}
