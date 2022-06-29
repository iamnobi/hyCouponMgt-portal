package ocean.acs.commons.utils;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

public class StringCustomizedUtils {

  public static final int CARD_NUMBER_MAX_LENGTH = 19;

  public static boolean isNotEmpty(String input) {
    if (input != null) {
      input = input.replaceAll(" ", "");
    }
    return !Strings.isNullOrEmpty(input);
  }

  public static boolean isEmpty(String input) {
    if (input != null) {
      input = input.replaceAll(" ", "");
    }
    return Strings.isNullOrEmpty(input);
  }

  public static String appendZeroSuffix(String value, int maxLength, int paddingNum) {
    if (StringUtils.isBlank(value)) {
      return value;
    }
    return StringUtils.rightPad(value, maxLength, paddingNum + "");
  }

  /**
   * Method名稱底線轉駝峰
   *
   * @param prefix
   * @param param
   * @return
   */
  public static String methodNameUnderlineToCamel(String prefix, String param) {
    if (prefix == null || prefix.trim().isEmpty() || param == null || param.trim().isEmpty()) {
      return "";
    }
    final int len = param.length();
    StringBuilder sb = new StringBuilder(len);
    sb.append(prefix).append(Character.toUpperCase(param.charAt(0)));

    for (int i = 1; i < len; i++) {
      char c = Character.toLowerCase(param.charAt(i));
      if (c == '_') {
        if (++i < len) {
          sb.append(Character.toUpperCase(param.charAt(i)));
        }
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }
}
