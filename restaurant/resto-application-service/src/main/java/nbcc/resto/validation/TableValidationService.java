package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Table;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class TableValidationService {

    private final AnnotationValidationService annotationValidationService;

    public TableValidationService(AnnotationValidationService annotationValidationService) {
        this.annotationValidationService = annotationValidationService;
    }

    public Collection<ValidationError> validate(Table table) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(table));
        return errors;
    }
}
