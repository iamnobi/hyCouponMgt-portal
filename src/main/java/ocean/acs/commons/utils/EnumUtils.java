package ocean.acs.commons.utils;


import java.util.List;
import java.util.stream.Collectors;
import ocean.acs.commons.constant.KernelMessageConstant;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.util.StringUtils;

public class EnumUtils {

  public static Enum getByName(Class enumClz, List<Enum> values, String name)
      throws OceanException {
    String clzName = enumClz.getSimpleName();
    if (StringUtils.isEmpty(name)) {
      throw OceanException.builder()
          .status(ResultStatus.COLUMN_NOT_EMPTY)
          .message(clzName + " " + KernelMessageConstant.COLUMN_NOT_EMPTY)
          .build();
    }
    try {
      return Enum.valueOf(enumClz, name.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      String body = String.format(KernelMessageConstant.ENUM_NAME_INVALID, clzName, name);
      String errorMsg =
          values.stream().map(c -> c.name()).collect(Collectors.joining(", ", body, "."));
      throw OceanException.builder()
          .status(ResultStatus.INVALID_FORMAT)
          .message(errorMsg)
          .build();
    }
  }
}
