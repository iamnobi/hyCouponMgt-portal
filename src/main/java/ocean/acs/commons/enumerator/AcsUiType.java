package ocean.acs.commons.enumerator;

public enum AcsUiType {
  TEXT("01"),
  SINGLE_SELECT("02"),
  MULTI_SELECT("03"),
  OOB("04"),
  HTML("05");

  private String code;

  AcsUiType(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
