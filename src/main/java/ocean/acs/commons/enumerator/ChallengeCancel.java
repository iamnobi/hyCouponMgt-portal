package ocean.acs.commons.enumerator;

public enum ChallengeCancel {
  CARDHOLDER_SELECTED_CANCEL("01"),
  RESERVED_FOR_FUTURE_EMVCO_USE("02"),
  TRANSACTION_TIMED_OUT_DECOUPLED_AUTHENTICATION("03"),
  TRANSACTION_TIMED_OUT_AT_ACS_OTHER_TIMEOUTS("04"),
  TRANSACTION_TIMED_OUT_AT_ACS_FIRST_CREQ_NOT_RECEIVED_BY_ACS("05"),
  TRANSACTION_ERROR("06"),
  UNKNOWN("07"),
  TRANSACTION_TIMED_OUT_AT_SDK("08");

  private String code;

  ChallengeCancel(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public static ChallengeCancel getByCode(String code) {
    ChallengeCancel[] challengeCancels = values();
    for (ChallengeCancel cancel : challengeCancels) {
      if (code.matches(cancel.code)) {
        return cancel;
      }
    }
    return null;
  }
}
