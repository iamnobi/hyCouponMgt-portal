package com.cherri.acs_portal.validator;

import com.cherri.acs_portal.service.CardBrandConfigurationHelper;
import com.cherri.acs_portal.service.StaticContextHelper;
import com.cherri.acs_portal.validator.validation.CardBrandValidation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CardBrandValidator implements ConstraintValidator<CardBrandValidation, String> {

  @Override
  public void initialize(CardBrandValidation constraintAnnotation) {}

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    CardBrandConfigurationHelper cardBrandConfigurationHelper = StaticContextHelper
      .getBean(CardBrandConfigurationHelper.class);
    return cardBrandConfigurationHelper.findCardBrandList().stream()
      .anyMatch(cardBrandDTO -> cardBrandDTO.getName().equals(value));
  }
}
