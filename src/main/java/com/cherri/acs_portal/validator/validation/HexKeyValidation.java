package com.cherri.acs_portal.validator.validation;

import com.cherri.acs_portal.validator.HexKeyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = HexKeyValidator.class)
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface HexKeyValidation {

    String message() default "Only accept hex format(0-9,a-f)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}