package nbcc.resto.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.common.result.ValidationResults;
import nbcc.resto.controller.api.response.MenuDetailsResponse;
import nbcc.resto.controller.api.result.ResultHandler;
import nbcc.resto.dto.Menu;
import nbcc.resto.service.MenuItemService;
import nbcc.resto.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "Menu API", description = "Public menu retrieval operations")
@RestController
@RequestMapping("/api/menu")
public class MenuApiController {

    private final MenuService menuService;
    private final MenuItemService menuItemService;

    public MenuApiController(MenuService menuService, MenuItemService menuItemService) {
        this.menuService = menuService;
        this.menuItemService = menuItemService;
    }

    @Operation(summary = "Get all menus")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Result<Collection<Menu>>> getAll(
            @RequestParam(required = false) String name) {

        var result = (name != null && !name.isBlank())
                ? menuService.search(name)
                : menuService.getAll();

        return ResultHandler.handleResult(result, HttpStatus.OK);
    }

    @Operation(summary = "Get menu by id including menu items")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{menuId}")
    public ResponseEntity<Result<MenuDetailsResponse>> getById(@PathVariable Long menuId) {
        var menuResult = menuService.get(menuId);

        if (menuResult.isError()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (menuResult.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var menuItemsResult = menuItemService.getByMenuId(menuId);

        if (menuItemsResult.isError()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        var response = new MenuDetailsResponse(menuResult.getValue(), menuItemsResult.getValue());
        return ResultHandler.handleResult(ValidationResults.success(response), HttpStatus.OK);
    }
}