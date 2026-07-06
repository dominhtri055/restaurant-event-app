package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Table;
import nbcc.resto.repository.TableRepository;
import nbcc.resto.validation.TableValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class TableServiceImpl implements TableService {

    private final Logger logger = LoggerFactory.getLogger(TableServiceImpl.class);

    private final TableRepository tableRepository;
    private final TableValidationService validationService;

    public TableServiceImpl(TableRepository tableRepository, TableValidationService validationService) {
        this.tableRepository = tableRepository;
        this.validationService = validationService;
    }

    @Override
    public Result<Collection<Table>> getAll() {
        try {
            return ValidationResults.success(tableRepository.getAll());
        } catch (Exception e) {
            logger.error("Error retrieving all tables", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Table> get(Long id) {
        try {
            return ValidationResults.success(tableRepository.get(id));
        } catch (Exception e) {
            logger.error("Error retrieving table with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Table> create(Table table) {
        try {
            var errors = validationService.validate(table);

            if (!errors.isEmpty()) {
                return ValidationResults.invalid(table, errors);
            }

            if (table.getCreatedDate() == null) {
                table.setCreatedDate(LocalDateTime.now());
            }

            var createdTable = tableRepository.create(table);

            if (createdTable.getName() == null || createdTable.getName().isBlank()) {
                createdTable.setName("Table " + createdTable.getId());
                var updatedTable = tableRepository.update(createdTable);
                return ValidationResults.success(updatedTable);
            }

            return ValidationResults.success(createdTable);
        } catch (Exception e) {
            logger.error("Error creating table {}", table, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Table> update(Table table) {
        try {
            var errors = validationService.validate(table);

            if (!errors.isEmpty()) {
                logger.debug("Validation errors for table update {}: {}", table, errors);
                return ValidationResults.invalid(table, errors);
            }

            if (table.getName() == null || table.getName().isBlank()) {
                table.setName("Table " + table.getId());
            }

            var updatedTable = tableRepository.update(table);
            return ValidationResults.success(updatedTable);

        } catch (Exception e) {
            logger.error("Error updating table {}", table, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Void> delete(Long id) {
        try {
            tableRepository.delete(id);
            logger.debug("Table with id {} deleted", id);
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting table with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }
}
