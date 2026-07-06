package nbcc.resto.viewmodels;

import nbcc.resto.dto.Menu;

import java.util.ArrayList;
import java.util.Collection;

public class MenuListViewModel {

    private final boolean canAdd;
    private final String message;
    private final Collection<Menu> menus;
    private final String name;

    public MenuListViewModel(Collection<Menu> menus, boolean canManage) {
        this(menus, canManage, null, null);
    }

    public MenuListViewModel(Collection<Menu> menus, boolean canManage, String message, String name) {
        this.canAdd = canManage;
        this.message = message;
        this.menus = menus;
        this.name = name;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public String getMessage() {
        return message;
    }

    public Collection<Menu> getMenus() {
        return menus != null ? menus : new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return getMenus().isEmpty();
    }
}