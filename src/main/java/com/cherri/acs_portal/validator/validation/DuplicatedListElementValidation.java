package com.cherri.acs_portal.validator.validation;

import com.cherri.acs_portal.validator.DuplicatedListElementValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DuplicatedListElementValidator.class)
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface DuplicatedListElementValidation {

    String message() default "Duplicated input values";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
