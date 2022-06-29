package ocean.acs.commons.constant;

import java.util.regex.Pattern;

public final class KernelSystemConstant {

	public static final String ERROR_DES_REQUIRED_PARAMETER = "A message element required as defined in Table A.1 is missing from the message";
	public static final String ERROR_DES_INVALID_FORMAT = "Data element not in the required format or value is invalid as defined in Table A.1";

	public static final String ERROR_DES_INVALID_FORMAT_PARAMETER = "Data element not in the required format or value is invalid as defined in Table A.1";

	public static final String ERROR_DES_INVALID_MESSAGE_TYPE = "Valid Message Type is sent to or from an inappropriate component.";

	public static final Pattern PATTERN_UUID = Pattern.compile("^([A-Fa-f0-9]{8})-([A-Fa-f0-9]{4})-([A-Fa-f0-9]{4})-([A-Fa-f0-9]{4})-([A-Fa-f0-9]{12})$");
	public static final Pattern PATTERN_CHALLENGE_CANCEL = Pattern.compile("^(01|04|05|06|07|08)$");
	public static final Pattern PATTERN_CHALLENGE_WINDOW_SIZE = Pattern.compile("^(01|02|03|04|05)$");
	public static final Pattern PATTERN_Y_N = Pattern.compile("^([YN])$");

	public static final String CURRENCY_TWD = "901";

	public static final String LANGUAGE_CODE_ZH_TW = "zh-tw";
	public static final String LANGUAGE_CODE_EN_US = "en-us";

	private KernelSystemConstant() {}

}
