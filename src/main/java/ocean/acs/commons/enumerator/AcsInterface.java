package ocean.acs.commons.enumerator;

public enum AcsInterface {
  NATIVE_UI("01"),
  HTML_UI("02");

  private String code;

  AcsInterface(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
