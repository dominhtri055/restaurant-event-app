package nbcc.resto.controller;

import jakarta.validation.Valid;
import nbcc.common.service.LoginService;
import nbcc.resto.dto.Menu;
import nbcc.resto.dto.MenuItem;
import nbcc.resto.service.MenuItemService;
import nbcc.resto.service.MenuService;
import nbcc.resto.viewmodels.MenuListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/menu")
public class MenuController {

    private final LoginService loginService;
    private final MenuService menuService;
    private final MenuItemService menuItemService;
    private final Logger logger = LoggerFactory.getLogger(MenuController.class);

    public MenuController(LoginService loginService,
                          MenuService menuService,
                          MenuItemService menuItemService) {
        this.loginService = loginService;
        this.menuService = menuService;
        this.menuItemService = menuItemService;
    }

    @GetMapping("/list")
    public String getAll(@RequestParam(required = false) String name, Model model) {
        var result = menuService.search(name);

        if(result.isError()) {
            model.addAttribute("message", "Error Retrieve Menu List");
            return "error/errorPage";
        }

        var viewModel = new MenuListViewModel(result.getValue(), loginService.isLoggedIn(), null, name);
        model.addAttribute("viewModel", viewModel);
        return "menu/list";
    }

    @GetMapping("/{menuId}")
    public String getById(@PathVariable("menuId") Long menuId, Model model) {
        var result = menuService.get(menuId);

        if(result.isError()) {
            model.addAttribute("message", "Error Retrieve Menu List");
            return "error/errorPage";
        }

        if(result.isEmpty()){
            model.addAttribute("message", "Menu with id " + menuId + " not found");
            return "error/errorPage";
        }

        var menuItemsResult = menuItemService.getByMenuId(menuId);

        if (menuItemsResult.isError()) {
            model.addAttribute("message", "Error retrieving menu items");
            return "error/errorPage";
        }

        model.addAttribute("menu", result.getValue());
        model.addAttribute("menuItems", menuItemsResult.getValue());
        return "menu/details";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("menu", new Menu());
        return "menu/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("menu") Menu menu, BindingResult br) {
        if (br.hasErrors()) {
            return "menu/create";
        }

        var result = menuService.create(menu);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return "menu/create";
        }

        return "redirect:/menu/list";
    }

    @GetMapping("/edit/{menuId}")
    public String edit(@PathVariable("menuId") Long menuId, Model model) {
        var result = menuService.get(menuId);

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving menu");
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "Menu with id " + menuId + " not found");
            return "error/errorPage";
        }

        var menuItemsResult = menuItemService.getByMenuId(menuId);

        if (menuItemsResult.isError()) {
            model.addAttribute("message", "Error retrieving menu items");
            return "error/errorPage";
        }

        model.addAttribute("menu", result.getValue());
        model.addAttribute("menuItems", menuItemsResult.getValue());
        model.addAttribute("menuItem", new MenuItem().setMenuId(menuId));
        model.addAttribute("showItemActions", true);

        return "menu/edit";
    }

    @PostMapping("/edit/{menuId}")
    public String edit(@PathVariable("menuId") Long menuId,
                       @Valid @ModelAttribute("menu") Menu menu,
                       BindingResult br,
                       Model model) {

        menu.setId(menuId);

        if (br.hasErrors()) {
            var menuItemsResult = menuItemService.getByMenuId(menuId);

            model.addAttribute("menuItems", menuItemsResult.isSuccessful()
                    ? menuItemsResult.getValue()
                    : java.util.List.of());

            model.addAttribute("menuItem", new MenuItem().setMenuId(menuId));
            model.addAttribute("showItemActions", true);

            return "menu/edit";
        }

        var result = menuService.update(menu);

        if (result.isError()) {
            model.addAttribute("message", "Error updating menu");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);

            var menuItemsResult = menuItemService.getByMenuId(menuId);

            model.addAttribute("menuItems", menuItemsResult.isSuccessful()
                    ? menuItemsResult.getValue()
                    : java.util.List.of());

            model.addAttribute("menuItem", new MenuItem().setMenuId(menuId));
            model.addAttribute("showItemActions", true);

            return "menu/edit";
        }

        return "redirect:/menu/list";
    }

    @PostMapping("/edit/{menuId}/items")
    public String addMenuItem(@PathVariable("menuId") Long menuId,
                              @Valid @ModelAttribute("menuItem") MenuItem menuItem,
                              BindingResult br,
                              Model model) {

        menuItem.setMenuId(menuId);

        var menuResult = menuService.get(menuId);

        if (menuResult.isError() || menuResult.isEmpty()) {
            model.addAttribute("message", "Menu with id " + menuId + " not found");
            return "error/errorPage";
        }

        if (br.hasErrors()) {
            var menuItemsResult = menuItemService.getByMenuId(menuId);

            model.addAttribute("menu", menuResult.getValue());
            model.addAttribute("menuItems", menuItemsResult.isSuccessful()
                    ? menuItemsResult.getValue()
                    : java.util.List.of());
            model.addAttribute("menuItem", menuItem);
            model.addAttribute("showItemActions", true);

            return "menu/edit";
        }

        var result = menuItemService.create(menuItem);

        if (result.isError()) {
            model.addAttribute("message", "Error creating menu item");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);

            var menuItemsResult = menuItemService.getByMenuId(menuId);

            model.addAttribute("menu", menuResult.getValue());
            model.addAttribute("menuItems", menuItemsResult.isSuccessful()
                    ? menuItemsResult.getValue()
                    : java.util.List.of());
            model.addAttribute("menuItem", menuItem);
            model.addAttribute("showItemActions", true);

            return "menu/edit";
        }

        return "redirect:/menu/edit/" + menuId;
    }

    @GetMapping("/edit/{menuId}/items/{menuItemId}/edit")
    public String editMenuItem(@PathVariable("menuId") Long menuId,
                               @PathVariable("menuItemId") Long menuItemId,
                               Model model) {
        var menuResult = menuService.get(menuId);

        if (menuResult.isError() || menuResult.isEmpty()) {
            model.addAttribute("message", "Menu with id " + menuId + " not found");
            return "error/errorPage";
        }
        var menuItemResult = menuItemService.get(menuItemId);

        if (menuItemResult.isError()) {
            model.addAttribute("message", "Error retrieving menu item");
            return "error/errorPage";
        }

        if (menuItemResult.isEmpty() || !belongsToMenu(menuItemResult.getValue(), menuId)) {
            model.addAttribute("message", "Menu item with id " + menuItemId + " not found for this menu");
            return "error/errorPage";
        }

        model.addAttribute("menu", menuResult.getValue());
        model.addAttribute("menuItem", menuItemResult.getValue());
        return "menu/item-edit";
    }

    @PostMapping("/edit/{menuId}/items/{menuItemId}/edit")
    public String editMenuItem(@PathVariable("menuId") Long menuId,
                               @PathVariable("menuItemId") Long menuItemId,
                               @Valid @ModelAttribute("menuItem") MenuItem menuItem,
                               BindingResult br,
                               Model model) {
        menuItem.setId(menuItemId);
        menuItem.setMenuId(menuId);

        var menuResult = menuService.get(menuId);

        if (menuResult.isError() || menuResult.isEmpty()) {
            model.addAttribute("message", "Menu with id " + menuId + " not found");
            return "error/errorPage";
        }

        var existingMenuItemResult = menuItemService.get(menuItemId);

        if (existingMenuItemResult.isError()) {
            model.addAttribute("message", "Error retrieving menu item");
            return "error/errorPage";
        }

        if (existingMenuItemResult.isEmpty() || !belongsToMenu(existingMenuItemResult.getValue(), menuId)) {
            model.addAttribute("message", "Menu item with id " + menuItemId + " not found for this menu");
            return "error/errorPage";
        }

        if (br.hasErrors()) {
            model.addAttribute("menu", menuResult.getValue());
            model.addAttribute("menuItem", menuItem);
            return "menu/item-edit";
        }

        var result = menuItemService.update(menuItem);

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            model.addAttribute("menu", menuResult.getValue());
            model.addAttribute("menuItem", menuItem);
            return "menu/item-edit";
        }

        if (result.isError()) {
            model.addAttribute("message", "Error updating menu item");
            return "error/errorPage";
        }

        return "redirect:/menu/edit/" + menuId;
    }

    @GetMapping("/edit/{menuId}/items/{menuItemId}/delete")
    public String confirmDeleteMenuItem(@PathVariable("menuId") Long menuId,
                                        @PathVariable("menuItemId") Long menuItemId,
                                        Model model) {
        var menuResult = menuService.get(menuId);

        if (menuResult.isError() || menuResult.isEmpty()) {
            model.addAttribute("message", "Menu with id " + menuId + " not found");
            return "error/errorPage";
        }

        var menuItemResult = menuItemService.get(menuItemId);

        if (menuItemResult.isError()) {
            model.addAttribute("message", "Error retrieving menu item");
            return "error/errorPage";
        }

        if (menuItemResult.isEmpty() || !belongsToMenu(menuItemResult.getValue(), menuId)) {
            model.addAttribute("message", "Menu item with id " + menuItemId + " not found for this menu");
            return "error/errorPage";
        }

        model.addAttribute("menu", menuResult.getValue());
        model.addAttribute("menuItem", menuItemResult.getValue());
        return "menu/item-delete";
    }

    @PostMapping("/edit/{menuId}/items/{menuItemId}/delete")
    public String deleteMenuItem(@PathVariable("menuId") Long menuId,
                                 @PathVariable("menuItemId") Long menuItemId,
                                 Model model) {
        var menuResult = menuService.get(menuId);

        if (menuResult.isError() || menuResult.isEmpty()) {
            model.addAttribute("message", "Menu with id " + menuId + " not found");
            return "error/errorPage";
        }

        var menuItemResult = menuItemService.get(menuItemId);

        if (menuItemResult.isError()) {
            model.addAttribute("message", "Error retrieving menu item");
            return "error/errorPage";
        }

        if (menuItemResult.isEmpty() || !belongsToMenu(menuItemResult.getValue(), menuId)) {
            model.addAttribute("message", "Menu item with id " + menuItemId + " not found for this menu");
            return "error/errorPage";
        }

        var result = menuItemService.delete(menuItemId);

        if (result.isError()) {
            model.addAttribute("message", "Error deleting menu item");
            return "error/errorPage";
        }

        return "redirect:/menu/edit/" + menuId;
    }

    @GetMapping("/delete/{menuId}")
    public String confirmDelete(@PathVariable("menuId") Long menuId, Model model) {
        var result = menuService.get(menuId);

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving menu");
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "Menu with id " + menuId + " not found");
            return "error/errorPage";
        }

        model.addAttribute("menu", result.getValue());
        return "menu/delete";
    }

    @PostMapping("/delete/{menuId}")
    public String delete(@PathVariable("menuId") Long menuId, Model model) {
        var result = menuService.delete(menuId);

        if (result.isError()) {
            model.addAttribute("message", "Error deleting menu. This menu may still be used by an event.");
            return "error/errorPage";
        }

        return "redirect:/menu/list";
    }

    private boolean belongsToMenu(MenuItem menuItem, Long menuId) {
        return menuItem != null
                && menuItem.getMenuId() != null
                && menuItem.getMenuId().equals(menuId);
    }
}