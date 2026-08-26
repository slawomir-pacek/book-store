package org.example.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

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
        try {
            Field firstField = value.getClass()
                    .getDeclaredField(first);

            Field secondField = value.getClass()
                    .getDeclaredField(second);

            firstField.setAccessible(true);
            secondField.setAccessible(true);

            Object firstValue = firstField.get(value);
            Object secondValue = secondField.get(value);

            if (firstValue == null) {
                return secondValue == null;
            }

            return firstValue.equals(secondValue);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
