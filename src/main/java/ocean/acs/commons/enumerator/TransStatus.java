package ocean.acs.commons.enumerator;

import ocean.acs.commons.utils.StringCustomizedUtils;

public enum TransStatus {
  Authentication("Y", " Authentication/ Account Verification Successful"),
  NotAuthenticated("N", "Not Authenticated /Account Not Verified; Transaction denied"),
  CouldNotVerify(
      "U",
      "Authentication/ Account Verification Could Not Be Performed; Technical or other problem, as indicated in ARes or RReq"),
  Attempted(
      "A",
      " Attempts Processing Performed; Not Authenticated/Verified , but a proof of attempted auth/verificat ion is provided"),
  Challenge("C", "Challenge Required; Additional auth is required using the CReq/CRes"),
  Rejected(
      "R",
      "Authentication/ Account Verification Rejected; Issuer is rejecting auth/verificat ion and request that authorisation not be attempted"),
  UNKNOWN("UNKNOWN", "Unknown Status");

  private final String code;
  private final String desc;

  TransStatus(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public static TransStatus codeOf(String code) {
    if(StringCustomizedUtils.isEmpty(code)) return UNKNOWN;

    TransStatus[] reasonList = values();
    for (TransStatus transStatus : reasonList) {
      if (transStatus.code.equalsIgnoreCase(code)) {
        return transStatus;
      }
    }
    return UNKNOWN;
  }

  public static boolean isNotAuthenticated(String transStatus) {
    return TransStatus.NotAuthenticated.getCode().equals(transStatus);
  }

  public static boolean isChallenge(String transStatus) {
    return TransStatus.Challenge.getCode().equals(transStatus);
  }

  
  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  @Override
  public String toString() {
    return this.code;
  }
}
