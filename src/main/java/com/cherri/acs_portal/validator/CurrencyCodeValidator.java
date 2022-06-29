package com.cherri.acs_portal.validator;

import com.cherri.acs_portal.validator.validation.CurrencyCodeValidation;
import com.neovisionaries.i18n.CurrencyCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CurrencyCodeValidator implements ConstraintValidator<CurrencyCodeValidation, String> {

  @Override
  public void initialize(CurrencyCodeValidation constraintAnnotation) {}

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (StringUtils.isEmpty(value)) {
      return true;
    }
    if (!StringUtils.isNumeric(value)) {
      return false;
    }
    return CurrencyCode.getByCode(Integer.parseInt(value)) != null;
  }
}
