package nbcc.resto.repository;

import nbcc.resto.dto.Seating;
import nbcc.resto.entity.EventEntity;
import nbcc.resto.mapper.SeatingMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SeatingRepositoryAdapter implements SeatingRepository {

    private final SeatingMapper seatingMapper;
    private final SeatingJPARepository seatingJPARepository;

    public SeatingRepositoryAdapter(SeatingMapper seatingMapper, SeatingJPARepository seatingJPARepository) {
        this.seatingMapper = seatingMapper;
        this.seatingJPARepository = seatingJPARepository;
    }

    @Override
    public Seating create(Seating seating) {
        var entity = seatingMapper.toEntity(seating);
        entity = seatingJPARepository.save(entity);
        return seatingMapper.toDTO(entity);
    }

    @Override
    public Seating update(Seating seating) {
        var entityOptional = seatingJPARepository.findById(seating.getId());

        if (entityOptional.isEmpty()) {
            throw new IllegalArgumentException("Seating not found");
        }

        var entity = entityOptional.get();
        entity.setName(seating.getName());
        entity.setStartTime(seating.getStartTime());
        entity.setDuration(seating.getDuration());
        entity.setUpdatedAt(seating.getUpdatedAt());
        entity.setArchived(seating.isArchived());

        if (seating.getEventId() != null) {
            entity.setEvent((new EventEntity().setId(seating.getEventId())));
        }

        entity = seatingJPARepository.save(entity);
        return seatingMapper.toDTO(entity);
    }

    @Override
    public List<Seating> getAll() {
        var entities = seatingJPARepository.findAll();
        return entities.stream().map(seatingMapper::toDTO).toList();
    }

    @Override
    public Optional<Seating> get(Long id) {
        var entity = seatingJPARepository.findById(id);
        return entity.map(seatingMapper::toDTO);
    }

    @Override
    public void delete(Long id) {
        seatingJPARepository.deleteById(id);
    }
}
