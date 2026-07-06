package nbcc.resto.repository;

import nbcc.resto.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemJPARepository extends JpaRepository<MenuItemEntity, Long> {
    List<MenuItemEntity> findByMenuId(Long menuId);
}
