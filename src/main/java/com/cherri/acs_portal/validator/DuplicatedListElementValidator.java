package com.cherri.acs_portal.validator;

import com.cherri.acs_portal.validator.validation.DuplicatedListElementValidation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
public class DuplicatedListElementValidator implements ConstraintValidator<DuplicatedListElementValidation, List> {

    @Override
    public void initialize(DuplicatedListElementValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(List values, ConstraintValidatorContext context) {

        if (CollectionUtils.isEmpty(values)) {
            return true;
        }

        Set<Object> set = new HashSet<>();
        for (Object value : values) {
            boolean isAlreadyExist = false;
            if (value instanceof String) {
                String valueStr = (String) value;
                isAlreadyExist = !set.add(valueStr.toLowerCase()); // ignore case
            } else {
                isAlreadyExist = !set.add(value);
            }

            if (isAlreadyExist) {
                log.debug("[DuplicatedListElementValidator.isValid] value={} is duplicated", value);
                return false;
            }
        }
        return true;
    }
}
