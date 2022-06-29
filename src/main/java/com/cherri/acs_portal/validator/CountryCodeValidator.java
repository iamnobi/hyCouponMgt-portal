package com.cherri.acs_portal.validator;

import com.cherri.acs_portal.validator.validation.CountryCodeValidation;
import com.neovisionaries.i18n.CountryCode;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountryCodeValidator implements ConstraintValidator<CountryCodeValidation, String> {

  @Override
  public void initialize(CountryCodeValidation constraintAnnotation) {}

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isEmpty(value)) {
      return true;
    }
    return CountryCode.getByCode(value.trim().toUpperCase()) != null;
  }
}
