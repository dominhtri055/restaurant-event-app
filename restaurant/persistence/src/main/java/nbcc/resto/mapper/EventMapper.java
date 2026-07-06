package nbcc.resto.mapper;

import nbcc.resto.dto.Event;
import nbcc.resto.entity.EventEntity;
import nbcc.resto.entity.MenuEntity;
import org.springframework.stereotype.Component;

@Component
public class EventMapper implements EntityMapper<Event, EventEntity> {

    public EventMapper() {
    }

    @Override
    public Event toDTO(EventEntity eventEntity) {
        if (eventEntity == null) return null;

        return new Event()
                .setId(eventEntity.getId())
                .setName(eventEntity.getName())
                .setDescription(eventEntity.getDescription())
                .setStartDate(eventEntity.getStartDate())
                .setEndDate(eventEntity.getEndDate())
                .setDuration(eventEntity.getDuration())
                .setMenuId(eventEntity.getMenu() != null ? eventEntity.getMenu().getId() : null)
                .setMenuName(eventEntity.getMenu() != null ? eventEntity.getMenu().getName() : null)
                .setPrice(eventEntity.getPrice())
                .setActive(eventEntity.getActive())
                .setCreatedAt(eventEntity.getCreatedAt())
                .setUpdatedAt(eventEntity.getUpdatedAt())
                .setArchive(eventEntity.getArchived());
    }

    @Override
    public EventEntity toEntity(Event event) {
        if (event == null) return null;

        EventEntity entity = new EventEntity()
                .setId(event.getId())
                .setName(event.getName())
                .setDescription(event.getDescription())
                .setStartDate(event.getStartDate())
                .setEndDate(event.getEndDate())
                .setDuration(event.getDuration())
                .setPrice(event.getPrice())
                .setActive(event.getActive())
                .setCreatedAt(event.getCreatedAt())
                .setUpdatedAt(event.getUpdatedAt())
                .setArchived(event.getArchive());

        if (event.getMenuId() != null) {
            entity.setMenu(new MenuEntity().setId(event.getMenuId()));
        }

        return entity;
    }
}