package com.cherri.acs_portal.validator.validation;

import com.cherri.acs_portal.validator.DateFormatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DateFormatValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface DateFormatValidation {

  String message() default "{format.error}";

  String dateFormat() default "yyyyMMdd";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    DateFormatValidation[] value();
  }
}
