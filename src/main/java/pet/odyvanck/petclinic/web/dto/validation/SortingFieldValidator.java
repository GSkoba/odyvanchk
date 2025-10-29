package pet.odyvanck.petclinic.web.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

/**
 * Validator for check if sorting fields are allowed.
 */
public class SortingFieldValidator implements ConstraintValidator<ValidSortingFields, String[]> {

    private Set<String> allowedFields;

    @Override
    public void initialize(ValidSortingFields constraintAnnotation) {
        allowedFields = Set.of(constraintAnnotation.allowed());
    }

    @Override
    public boolean isValid(String[] sortBy, ConstraintValidatorContext context) {
        if (sortBy == null) return true;
        for (String field : sortBy) {
            if (!allowedFields.contains(field)) {
                return false;
            }
        }
        return true;
    }
}

