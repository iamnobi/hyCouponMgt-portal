package com.cherri.acs_portal.validator;

import com.cherri.acs_portal.validator.validation.DateFormatValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatValidator implements ConstraintValidator<DateFormatValidation, String> {

  private String dateFormat;

  @Override
  public void initialize(DateFormatValidation constraintAnnotation) {
    dateFormat = constraintAnnotation.dateFormat();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.length() == 0) {
      return true;
    }
    // check format like YYYYMMDDHHMM
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    Date ret;
    try {
      ret = sdf.parse(value);
    } catch (ParseException e) {
      e.printStackTrace();
      return false;
    }
    return sdf.format(ret).equals(value);
  }
}
