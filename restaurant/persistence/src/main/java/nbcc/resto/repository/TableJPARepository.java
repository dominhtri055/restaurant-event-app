package nbcc.resto.repository;

import nbcc.resto.entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableJPARepository extends JpaRepository<TableEntity, Long> {
}
