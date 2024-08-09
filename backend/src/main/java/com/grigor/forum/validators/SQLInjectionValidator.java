package com.grigor.forum.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SQLInjectionValidator implements ConstraintValidator<SQLInjectionValid, String> {
    private final List<Pattern> XSS_PATTERNS = Arrays.asList(
            Pattern.compile(" AND ", Pattern.CASE_INSENSITIVE),
            Pattern.compile(" OR ", Pattern.CASE_INSENSITIVE),
            Pattern.compile(" NOT ", Pattern.CASE_INSENSITIVE),
            Pattern.compile(" ' ", Pattern.CASE_INSENSITIVE),
            Pattern.compile(" \" " , Pattern.CASE_INSENSITIVE),
            Pattern.compile(" UNION ", Pattern.CASE_INSENSITIVE),
            Pattern.compile(" -- ", Pattern.CASE_INSENSITIVE),
            Pattern.compile(" # ", Pattern.CASE_INSENSITIVE),
            Pattern.compile(" = ", Pattern.CASE_INSENSITIVE),
            Pattern.compile(" SELECT ", Pattern.CASE_INSENSITIVE)
    );

    @Override
    public void initialize(SQLInjectionValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        for (Pattern pattern : XSS_PATTERNS){
            if (pattern.matcher(value).find())
                return false;
        }

        return true;
    }
}
