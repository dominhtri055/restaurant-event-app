package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.Event;
import nbcc.resto.entity.MenuEntity;
import nbcc.resto.mapper.EventMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class EventRepositoryAdapter implements EventRepository {
   private final EventJPARepository eventJPARepository;
   private final EventMapper eventMapper;
   public EventRepositoryAdapter(EventJPARepository eventJPARepository, EventMapper eventMapper) {
       this.eventJPARepository = eventJPARepository;
       this.eventMapper = eventMapper;
   }

    @Override
    public List<Event> getAll() {
        var entities = eventJPARepository.findAll();
        return eventMapper.toDTO(entities);
    }

    @Override
    public List<Event> search(String name, LocalDate after, LocalDate before) {
        var entities = eventJPARepository.search(
                normalize(name),
                after,
                before
        );
        return eventMapper.toDTO(entities);
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    @Override
    public Optional<Event> get(long id) {
        var entity = eventJPARepository.findById(id);
        return eventMapper.toDTO(entity);
    }

    @Override
    public boolean getByName(String name) {
        return eventJPARepository.existsByNameIgnoreCase(name);
    }

    @Override
    public Event create(Event event) {
       var entity = eventMapper.toEntity(event);
       entity = eventJPARepository.save(entity);
       return eventMapper.toDTO(entity);

    }

    @Override
    public Event update(Event event) throws ConcurrencyException {
        var entityExist = eventJPARepository.findById(event.getId());

        if (entityExist.isEmpty()) {
            throw new ConcurrencyException("Event not found");
        }

        var entity = entityExist.get();
        entity.setName(event.getName());
        entity.setDescription(event.getDescription());
        entity.setStartDate(event.getStartDate());
        entity.setEndDate(event.getEndDate());
        entity.setDuration(event.getDuration());
        entity.setPrice(event.getPrice());
        entity.setMenu(event.getMenuId() != null ? new MenuEntity().setId(event.getMenuId()) : null);
        entity.setActive(Boolean.TRUE.equals(event.getActive()));
        entity.setUpdatedAt(event.getUpdatedAt());
        entity.setArchived(event.getArchive());

        entity = eventJPARepository.save(entity);
        return eventMapper.toDTO(entity);
    }

    @Override
    public void delete(long id) {
        eventJPARepository.deleteById(id);
    }

    @Override
    public Optional<Event> getByExactName(String name) {
        var entity = eventJPARepository.findByNameIgnoreCase(name);
        return eventMapper.toDTO(entity);
    }
}