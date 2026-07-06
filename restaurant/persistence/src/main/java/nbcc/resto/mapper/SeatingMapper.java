package nbcc.resto.mapper;

import nbcc.resto.dto.Seating;
import nbcc.resto.entity.EventEntity;
import nbcc.resto.entity.SeatingEntity;
import org.springframework.stereotype.Component;

@Component
public class SeatingMapper implements EntityMapper<Seating, SeatingEntity> {

    public SeatingMapper() {
    }

    @Override
    public Seating toDTO(SeatingEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Seating()
                .setId(entity.getId())
                .setName(entity.getName())
                .setStartTime(entity.getStartTime())
                .setDuration(entity.getDuration())
                .setCreatedDate(entity.getCreatedDate())
                .setUpdatedAt(entity.getUpdatedAt())
                .setArchived(entity.isArchived())
                .setEventId(entity.getEvent() != null ? entity.getEvent().getId() : null)
                .setEventName(entity.getEvent() != null ? entity.getEvent().getName() : null);
    }

    @Override
    public SeatingEntity toEntity(Seating dto) {
        if (dto == null) {
            return null;
        }

        SeatingEntity entity = new SeatingEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setStartTime(dto.getStartTime())
                .setDuration(dto.getDuration())
                .setCreatedDate(dto.getCreatedDate())
                .setUpdatedAt(dto.getUpdatedAt())
                .setArchived(dto.isArchived());

        if (dto.getEventId() != null) {
            entity.setEvent(new EventEntity().setId(dto.getEventId()));
        }

        return entity;
    }
}