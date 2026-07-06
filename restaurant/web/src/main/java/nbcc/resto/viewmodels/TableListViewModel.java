package nbcc.resto.viewmodels;

import nbcc.resto.dto.Table;

import java.util.ArrayList;
import java.util.Collection;

public class TableListViewModel {
    private final boolean canAdd;
    private final boolean canEdit;
    private final boolean canDelete;
    private final String message;

    private final Collection<Table> tables;

    public TableListViewModel(Collection<Table> tables, boolean canManage) {
        this(tables, canManage, null);
    }


    public TableListViewModel(Collection<Table> tables, boolean canManage, String message) {
        this.canAdd = canManage;
        this.canEdit = canManage;
        this.canDelete = canManage;
        this.message = message;
        this.tables = tables;
    }

    public Collection<Table> getTables() {
        return tables != null ? tables : new ArrayList<>();
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEmpty() {
        return getTables().isEmpty();
    }
}
