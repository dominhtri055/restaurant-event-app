package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Menu;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class MenuValidationService {

    private final AnnotationValidationService annotationValidationService;

    public MenuValidationService(AnnotationValidationService annotationValidationService) {
        this.annotationValidationService = annotationValidationService;
    }

    public Collection<ValidationError> validate(Menu menu) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(menu));
        return errors;
    }
}