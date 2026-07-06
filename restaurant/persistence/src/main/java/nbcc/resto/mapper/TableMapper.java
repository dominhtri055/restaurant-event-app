package nbcc.resto.mapper;

import nbcc.resto.dto.Table;
import nbcc.resto.entity.TableEntity;
import org.springframework.stereotype.Component;

@Component
public class TableMapper implements EntityMapper<Table, TableEntity>{

    public TableMapper() {

    }

    @Override
    public Table toDTO(TableEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Table()
                .setId(entity.getId())
                .setName(entity.getName())
                .setCapacity(entity.getCapacity())
                .setCreatedDate(entity.getCreatedDate());
    }

    @Override
    public TableEntity toEntity(Table dto) {
        if(dto == null){
            return null;
        }

        return new TableEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setCapacity(dto.getCapacity())
                .setCreatedDate(dto.getCreatedDate());
    }
}
