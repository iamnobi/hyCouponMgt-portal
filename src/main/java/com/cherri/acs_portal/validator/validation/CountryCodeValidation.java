package com.cherri.acs_portal.validator.validation;

import com.cherri.acs_portal.validator.CountryCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CountryCodeValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface CountryCodeValidation {

  String message() default "{country.code.error}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    CountryCodeValidation[] value();
  }
}
