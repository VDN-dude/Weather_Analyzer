package com.example.spring_boot_calc.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MyLocalDateValidator implements ConstraintValidator<ValidDate, LocalDate> {
    public void initialize(ValidDate constraint) {
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(2);
        return !value.isBefore(start) && !value.isAfter(end);
    }
}
