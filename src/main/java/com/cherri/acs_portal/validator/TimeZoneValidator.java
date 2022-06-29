package com.cherri.acs_portal.validator;

import com.cherri.acs_portal.validator.validation.TimeZoneValidation;
import java.time.ZoneId;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class TimeZoneValidator implements ConstraintValidator<TimeZoneValidation, String> {

  @Override
  public void initialize(TimeZoneValidation constraintAnnotation) {}

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isEmpty(value)) {
      return true;
    }

    try {
      ZoneId.of(value);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
