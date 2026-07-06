package nbcc.resto.repository;

import nbcc.resto.dto.Menu;
import nbcc.resto.mapper.MenuMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuRepositoryAdapter implements MenuRepository {

    private final MenuMapper menuMapper;
    private final MenuJPARepository menuJPARepository;

    public MenuRepositoryAdapter(MenuMapper menuMapper, MenuJPARepository menuJPARepository) {
        this.menuMapper = menuMapper;
        this.menuJPARepository = menuJPARepository;
    }

    @Override
    public List<Menu> getAll() {
        var entities = menuJPARepository.findAll();
        return menuMapper.toDTO(entities);
    }

    @Override
    public List<Menu> search(String name) {
        var entities = menuJPARepository.search(normalize(name));
        return menuMapper.toDTO(entities);
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    @Override
    public Optional<Menu> get(Long id) {
        var entity = menuJPARepository.findById(id);
        return menuMapper.toDTO(entity);
    }

    @Override
    public Menu create(Menu menu) {
        var entity = menuMapper.toEntity(menu);
        entity = menuJPARepository.save(entity);
        return menuMapper.toDTO(entity);
    }

    @Override
    public Menu update(Menu menu) {
        var existing = menuJPARepository.findById(menu.getId());

        if (existing.isEmpty()) {
            throw new IllegalStateException("Menu not found");
        }

        var entity = existing.get();
        entity.setName(menu.getName());
        entity.setDescription(menu.getDescription());
        entity = menuJPARepository.save(entity);

        return menuMapper.toDTO(entity);
    }


    @Override
    public void delete(Long id) {
        menuJPARepository.deleteById(id);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return menuJPARepository.existsByNameIgnoreCase(name);
    }
}