package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.resto.dto.MenuItem;
import nbcc.resto.repository.MenuItemRepository;
import nbcc.resto.repository.MenuRepository;
import nbcc.resto.validation.MenuItemValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final Logger logger = LoggerFactory.getLogger(MenuItemServiceImpl.class);

    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;
    private final MenuItemValidationService menuItemValidationService;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, MenuRepository menuRepository, MenuItemValidationService menuItemValidationService) {
        this.menuItemRepository = menuItemRepository;
        this.menuRepository = menuRepository;
        this.menuItemValidationService = menuItemValidationService;
    }

    @Override
    public Result<Collection<MenuItem>> getByMenuId(Long menuId) {
        try {
            return ValidationResults.success(menuItemRepository.getByMenuId(menuId));
        } catch (Exception e) {
            logger.error("Error retrieving menu items for menu id {}", menuId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<MenuItem> get(Long id) {
        try{
            return ValidationResults.success(menuItemRepository.get(id));
        }catch (Exception e){
            logger.error("Error retrieving menu item for menu id {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<MenuItem> create(MenuItem menuItem) {
        try {
            var menuResult = menuRepository.get(menuItem.getMenuId());

            if (menuResult.isEmpty()) {
                return ValidationResults.error();
            }

            var errors = menuItemValidationService.validate(menuItem);

            if (!errors.isEmpty()) {
                return ValidationResults.invalid(menuItem, errors);
            }

            if (menuItem.getName() != null) {
                menuItem.setName(menuItem.getName().trim());
            }

            if (menuItem.getDescription() != null) {
                menuItem.setDescription(menuItem.getDescription().trim());
            }

            return ValidationResults.success(menuItemRepository.create(menuItem));
        } catch (Exception e) {
            logger.error("Error creating menu item {}", menuItem, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<MenuItem> update(MenuItem menuItem) {
        try {
            var existingMenuItem = menuItemRepository.get(menuItem.getId());

            if (existingMenuItem.isEmpty()) {
                return ValidationResults.error();
            }

            var menuResult = menuRepository.get(menuItem.getMenuId());

            if (menuResult.isEmpty()) {
                return ValidationResults.error();
            }

            if (menuItem.getName() != null) {
                menuItem.setName(menuItem.getName().trim());
            }

            if (menuItem.getDescription() != null) {
                menuItem.setDescription(menuItem.getDescription().trim());
            }

            var errors = menuItemValidationService.validate(menuItem);

            if (!errors.isEmpty()) {
                return ValidationResults.invalid(menuItem, errors);
            }

            return ValidationResults.success(menuItemRepository.update(menuItem));

        } catch (Exception e) {
            logger.error("Error updating menu item {}", menuItem, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<MenuItem> delete(Long id) {
        try {
            var existingMenuItem = menuItemRepository.get(id);

            if (existingMenuItem.isEmpty()) {
                return ValidationResults.error();
            }

            menuItemRepository.delete(id);
            return ValidationResults.success(existingMenuItem.get());
        } catch (Exception e) {
            logger.error("Error deleting menu item with id {}", id, e);
            return ValidationResults.error(e);
        }
    }
}
