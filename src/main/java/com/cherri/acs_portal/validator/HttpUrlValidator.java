package com.cherri.acs_portal.validator;

import com.cherri.acs_portal.validator.validation.HttpUrlValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUrlValidator implements ConstraintValidator<HttpUrlValidation, String> {

  private int max;

  private static final Pattern PATTERN =
      Pattern.compile("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

  @Override
  public void initialize(HttpUrlValidation constraintAnnotation) {
    max = constraintAnnotation.max();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    if (max < value.length()) {
      return false;
    }
    Matcher m = PATTERN.matcher(value);
    return m.matches();
  }
}
