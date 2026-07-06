package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.dto.Table;
import nbcc.resto.mapper.TableMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TableRepositoryAdapter implements TableRepository {

    public final TableMapper tableMapper;
    private final TableJPARepository tableJPARepository;

    public TableRepositoryAdapter(TableMapper tableMapper, TableJPARepository tableJPARepository) {
        this.tableMapper = tableMapper;
        this.tableJPARepository = tableJPARepository;
    }

    @Override
    public List<Table> getAll() {
        var entities = tableJPARepository.findAll();
        return tableMapper.toDTO(entities);
    }

    @Override
    public Optional<Table> get(Long id) {
        var entity = tableJPARepository.findById(id);
        return tableMapper.toDTO(entity);
    }

    @Override
    public void delete(Long id) {
        tableJPARepository.deleteById(id);
    }

    @Override
    public Table create(Table table) {
        var entity = tableMapper.toEntity(table);
        entity = tableJPARepository.save(entity);
        return tableMapper.toDTO(entity);
    }

    @Override
    public Table update(Table table) throws ConcurrencyException {
        var entityOptional = tableJPARepository.findById(table.getId());

        if (entityOptional.isEmpty()) {
            throw new ConcurrencyException("Table not found");
        }

        var entity = entityOptional.get();

        entity.setName(table.getName());
        entity.setCapacity(table.getCapacity());

        entity = tableJPARepository.save(entity);

        return tableMapper.toDTO(entity);
    }
}
