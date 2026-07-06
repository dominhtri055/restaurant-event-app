package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.MenuItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class MenuItemValidationService {

    private final AnnotationValidationService annotationValidationService;
    public MenuItemValidationService(AnnotationValidationService annotationValidationService) {
        this.annotationValidationService = annotationValidationService;
    }

    public Collection<ValidationError> validate(MenuItem menuItem) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(menuItem));
        return errors;
    }
}
