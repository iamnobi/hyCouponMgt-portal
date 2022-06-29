package ocean.acs.commons.enumerator;

public enum ResendChallenge {
  RESEND("Y"),
  DO_NOT_RESEND("N");

  private String code;

  ResendChallenge(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
