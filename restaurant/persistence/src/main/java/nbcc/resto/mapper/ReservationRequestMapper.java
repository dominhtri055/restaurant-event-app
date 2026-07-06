package nbcc.resto.mapper;

import nbcc.resto.dto.ReservationRequest;
import nbcc.resto.entity.ReservationRequestEntity;
import nbcc.resto.entity.SeatingEntity;
import nbcc.resto.entity.TableEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationRequestMapper implements EntityMapper<ReservationRequest, ReservationRequestEntity>{
    @Override
    public ReservationRequest toDTO(ReservationRequestEntity entity) {
        if (entity == null) {
            return null;
        }

        var dto = new ReservationRequest()
                .setId(entity.getId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setEmailAddress(entity.getEmailAddress())
                .setGroupSize(entity.getGroupSize())
                .setStatus(entity.getStatus())
                .setRequestUuid(entity.getRequestUuid())
                .setCreatedAt(entity.getCreatedAt());

        if (entity.getSeating() != null) {
            dto.setSeatingId(entity.getSeating().getId())
                    .setSeatingName(entity.getSeating().getName())
                    .setSeatingStartTime(entity.getSeating().getStartTime());

            if (entity.getSeating().getEvent() != null) {
                dto.setEventId(entity.getSeating().getEvent().getId())
                        .setEventName(entity.getSeating().getEvent().getName());
            }
        }

        if (entity.getAssignedTable() != null) {
            dto.setTableId(entity.getAssignedTable().getId())
                    .setTableName(entity.getAssignedTable().getName());
        }

        return dto;
    }

    @Override
    public ReservationRequestEntity toEntity(ReservationRequest dto) {
        if (dto == null) {
            return null;
        }

        var entity = new ReservationRequestEntity()
                .setId(dto.getId())
                .setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setEmailAddress(dto.getEmailAddress())
                .setGroupSize(dto.getGroupSize())
                .setStatus(dto.getStatus())
                .setRequestUuid(dto.getRequestUuid())
                .setCreatedAt(dto.getCreatedAt());

        if (dto.getSeatingId() != null) {
            entity.setSeating(new SeatingEntity().setId(dto.getSeatingId()));
        }

        if (dto.getTableId() != null) {
            entity.setAssignedTable(new TableEntity().setId(dto.getTableId()));
        }

        return entity;
    }
}
