package com.grigor.forum.validators;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SQLInjectionValidator.class)
public @interface SQLInjectionValid {

    String message() default "Field potentially has dangerous characters!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}

