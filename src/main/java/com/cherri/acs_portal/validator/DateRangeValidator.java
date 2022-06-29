package com.cherri.acs_portal.validator;

import com.cherri.acs_portal.validator.validation.DateRangeValidation;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

@Log4j2
public class DateRangeValidator implements ConstraintValidator<DateRangeValidation, Object> {

    private static final DateTimeFormatter DAY_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM");
    private String startDateFieldName;
    private String endDateFieldName;

    @Override
    public void initialize(DateRangeValidation constraintAnnotation) {
        startDateFieldName = constraintAnnotation.startDateFieldName();
        endDateFieldName = constraintAnnotation.endDateFieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        String startDate = null;
        String endDate = null;
        try {
            startDate = BeanUtils.getProperty(value, startDateFieldName);
            endDate = BeanUtils.getProperty(value, endDateFieldName);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            String message = "[DateRangeValidator.isValid] get date property values error";
            log.error(message, e);
            throw new OceanException(ResultStatus.SERVER_ERROR, message);
        }

        if ((StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate))) {
            return false;
        }

        if (isMonthReport(startDate, endDate)) {
            return true;
        }

        try {
            LocalDate startLocalDate = LocalDate.parse(startDate, DAY_PATTERN);
            LocalDate endLocalDate = LocalDate.parse(endDate, DAY_PATTERN);
            return !startLocalDate.isAfter(endLocalDate);
        } catch (DateTimeParseException e) {
            String message = "[DateRangeValidator.isValid] date string format doesn't match yyyy-MM-dd";
            log.error(message);
            throw new OceanException(ResultStatus.INVALID_FORMAT, message);
        }

    }

    public static boolean isMonthReport(String startDate, String endDate) {
        int startDateSplitLength = startDate.split("-").length;
        int endDateSplitLength = endDate.split("-").length;
        return startDateSplitLength == 2 && endDateSplitLength == 2;
    }

}
