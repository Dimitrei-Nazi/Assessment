package com.assignment.progresssoft.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = UniqueUsernamesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsernames {
    String message() default "Duplicate usernames in accessibleUsers";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
