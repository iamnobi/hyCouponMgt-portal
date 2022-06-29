package ocean.acs.commons.enumerator;

public enum TdsoErrorCode {
  CODE_1_ROOT_ELEMENT_MISSING("1", "Root element invalid."),
  CODE_2_MESSAGE_ELEMENT_NOT_A_DEFINED_MESSAGE("2", "Message element not a defined message."),
  CODE_3_REQUIRED_ELEMENT_MISSING("3", "Required element missing."),
  CODE_4_CRITICAL_ELEMENT_NOT_RECOGNIZED("4", "Critical element not recognized."),
  CODE_5_FORMAT_OF_ONE_OR_MORE_ELEMENTS_IS_INVALID(
      "5", "Format of one or more elements is invalid according to the specification."),
  CODE_6_PROTOCOL_VERSION_TOO_OLD("6", "Protocol version too old."),
  CODE_50_ACQUIRER_NOT_PARTICIPATING("50", "Acquirer not participating."),
  CODE_51_MERCHANT_NOT_PARTICIPATING("51", "Merchant not participating."),
  CODE_52_PASSWORD_MISSING("52", "Password Missing."),
  CODE_53_INCORRECT_PASSWORD("53", "Incorrect password."),
  CODE_58_INCORRECT_COMMON_NAME_VALUE_IN_CLIENT_CERTIFICATE(
      "58", "Incorrect Common Name value in Client Certificate."),
  CODE_98_TRANSIENT_SYSTEM_FAILURE("98", "Transient system failure."),
  CODE_99_PERMANENT_SYSTEM_FAILURE("99", "Permanent system failure.");

  private final String errorCode;
  private final String errorMessage;

  TdsoErrorCode(String errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
