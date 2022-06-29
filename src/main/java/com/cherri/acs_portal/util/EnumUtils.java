package com.cherri.acs_portal.util;

import com.cherri.acs_portal.constant.MessageConstants;
import java.util.List;
import java.util.stream.Collectors;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.util.StringUtils;

public class EnumUtils {

    public static Enum getByName(Class enumClz, List<Enum> values, String name)
      throws OceanException {
        String clzName = enumClz.getSimpleName();
        if (StringUtils.isEmpty(name)) {
            String errorMsg = clzName + " " + MessageConstants.get(MessageConstants.COLUMN_NOT_EMPTY);
            throw new OceanException(ResultStatus.COLUMN_NOT_EMPTY, errorMsg);
        }
        try {
            return Enum.valueOf(enumClz, name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            String body = String.format(MessageConstants.get(MessageConstants.ENUM_NAME_INVALID), clzName, name);
            String errorMsg =
              values.stream().map(c -> c.name()).collect(Collectors.joining(", ", body, "."));
            throw new OceanException(ResultStatus.INVALID_FORMAT, errorMsg);
        }
    }
}
