package com.cherri.acs_portal.validator;

import com.cherri.acs_portal.validator.validation.HexKeyValidation;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class HexKeyValidator implements ConstraintValidator<HexKeyValidation, String> {

    @Override
    public void initialize(HexKeyValidation constraintAnnotation) {
    }

    private static final Pattern HEX_PATTERN = Pattern.compile("^[0-9a-fA-F]+$");
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            // 空值不檢查
            return true;
        }
        return HEX_PATTERN.matcher(value.toUpperCase()).matches();
    }

}
