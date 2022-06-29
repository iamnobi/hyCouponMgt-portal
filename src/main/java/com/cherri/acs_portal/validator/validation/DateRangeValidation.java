package com.cherri.acs_portal.validator.validation;

import com.cherri.acs_portal.validator.DateRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = DateRangeValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
@Documented
public @interface DateRangeValidation {

    String message() default "End date must be equal or after start date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String startDateFieldName();
    String endDateFieldName();
}
