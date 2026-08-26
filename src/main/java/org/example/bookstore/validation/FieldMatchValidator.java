package org.example.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator
        implements ConstraintValidator<FieldMatch, Object> {

    private String first;
    private String second;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        first = constraintAnnotation.first();
        second = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {

        Object firstValue = new BeanWrapperImpl(value)
                .getPropertyValue(first);

        Object secondValue = new BeanWrapperImpl(value)
                .getPropertyValue(second);

        if (firstValue == null && secondValue == null) {
            return true;
        }

        return firstValue != null && firstValue.equals(secondValue);
    }
}
