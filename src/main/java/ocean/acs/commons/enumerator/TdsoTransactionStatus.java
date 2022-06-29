package ocean.acs.commons.enumerator;

public enum TdsoTransactionStatus {
  Authentication("Y"),
  NotAuthenticated("N"),
  CouldNotVerify("U"),
  Attempt("A");

  private final String code;

  TdsoTransactionStatus(String code) {
    this.code = code;
  }

  public static TdsoTransactionStatus getByCode(String code) {
    TdsoTransactionStatus[] transStatuses = values();
    for (TdsoTransactionStatus status : transStatuses) {
      if (code.matches(status.code)) {
        return status;
      }
    }
    return null;
  }

  public String getCode() {
    return code;
  }
}
