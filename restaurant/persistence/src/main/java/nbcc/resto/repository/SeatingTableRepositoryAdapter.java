package nbcc.resto.repository;

import nbcc.resto.entity.SeatingEntity;
import nbcc.resto.entity.SeatingTableEntity;
import nbcc.resto.entity.TableEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SeatingTableRepositoryAdapter implements SeatingTableRepository {

    private final SeatingTableJPARepository seatingTableJPARepository;

    public SeatingTableRepositoryAdapter(SeatingTableJPARepository seatingTableJPARepository) {
        this.seatingTableJPARepository = seatingTableJPARepository;
    }

    @Override
    public void create(Long seatingId, Long tableId) {
        var seatingTable = new SeatingTableEntity()
                .setSeating(new SeatingEntity().setId(seatingId))
                .setTable(new TableEntity().setId(tableId));
        seatingTableJPARepository.save(seatingTable);
    }

    @Override
    public List<Long> getTableIdsBySeatingId(Long seatingId) {
        return seatingTableJPARepository.findBySeatingId(seatingId)
                .stream()
                .map(x ->x.getTable().getId())
                .toList();
    }

    @Override
    public void deleteBySeatingId(Long seatingId) {
        seatingTableJPARepository.deleteBySeatingId(seatingId);
    }
}
