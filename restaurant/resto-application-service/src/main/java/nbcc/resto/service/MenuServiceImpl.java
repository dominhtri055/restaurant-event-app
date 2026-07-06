package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Menu;
import nbcc.resto.repository.MenuRepository;
import nbcc.resto.validation.MenuValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class MenuServiceImpl implements MenuService {

    private final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    private final MenuRepository menuRepository;
    private final MenuValidationService menuValidationService;

    public MenuServiceImpl(MenuRepository menuRepository, MenuValidationService menuValidationService) {
        this.menuRepository = menuRepository;
        this.menuValidationService = menuValidationService;
    }

    @Override
    public Result<Collection<Menu>> getAll() {
        try{
            return ValidationResults.success(menuRepository.getAll());
        }catch(Exception e){
            logger.error("Error retrieving all menu",e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<Menu>> search(String name) {
        try {
            return ValidationResults.success(menuRepository.search(name));
        } catch (Exception e) {
            logger.error("Error searching menus", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Menu> get(Long id) {
        try{
            return ValidationResults.success(menuRepository.get(id));
        }catch(Exception e){
            logger.error("Error retrieving menu with id: {}",id,e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Menu> create(Menu menu) {
        try {
            var errors = new ArrayList<ValidationError>();
            errors.addAll(menuValidationService.validate(menu));

            var normalizedName = menu.getName() != null ? menu.getName().trim() : null;

            if (normalizedName != null && !normalizedName.isBlank()
                    && menuRepository.existsByNameIgnoreCase(normalizedName)) {
                errors.add(new ValidationError("Menu name already exists", "name"));
            }

            if (!errors.isEmpty()) {
                return ValidationResults.invalid(menu, errors);
            }

            menu.setName(normalizedName);

            if (menu.getDescription() != null) {
                menu.setDescription(menu.getDescription().trim());
            }

            if (menu.getCreationDate() == null) {
                menu.setCreationDate(LocalDateTime.now());
            }

            return ValidationResults.success(menuRepository.create(menu));
        } catch (Exception e) {
            logger.error("Error creating menu {}", menu, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Menu> update(Menu menu) {
        try {
            var existing = menuRepository.get(menu.getId());

            if (existing.isEmpty()) {
                return ValidationResults.error();
            }

            var errors = menuValidationService.validate(menu);

            if (!errors.isEmpty()) {
                return ValidationResults.invalid(menu, errors);
            }

            if (!existing.get().getName().equalsIgnoreCase(menu.getName())
                    && menuRepository.existsByNameIgnoreCase(menu.getName())) {
                errors.add(new ValidationError("Menu name already exists", "name"));
                return ValidationResults.invalid(menu, errors);
            }

            menu.setCreationDate(existing.get().getCreationDate());

            return ValidationResults.success(menuRepository.update(menu));
        } catch (Exception e) {
            logger.error("Error updating menu {}", menu, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Menu> delete(Long id) {
        try {
            var existing = menuRepository.get(id);
            if (existing.isEmpty()) {
                return ValidationResults.error();
            }

            menuRepository.delete(id);
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting menu with id {}", id, e);
            return ValidationResults.error(e);
        }
    }

}