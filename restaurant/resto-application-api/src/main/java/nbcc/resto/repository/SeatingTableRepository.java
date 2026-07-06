package nbcc.resto.repository;

import java.util.List;

public interface SeatingTableRepository {

    void create(Long seatingId, Long tableId);

    List<Long> getTableIdsBySeatingId(Long seatingId);

    void deleteBySeatingId(Long seatingId);
}
