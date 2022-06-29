package ocean.acs.commons.enumerator;

import ocean.acs.commons.utils.StringCustomizedUtils;

/** ACS Portal Database MODULE_SETTING.FUNCTION_TYPE */
public enum AuditFunctionType {
  // 持卡人/卡片管理
  UNLOCK_OTP("UNLOCK_OTP", Permission.CARD_AUDIT),
  THREE_DS_VERIFY_ENABLED("THREE_DS_VERIFY_ENABLED", Permission.CARD_AUDIT),

  // 風險控制
  BLACK_LIST_PAN("BLACK_LIST_PAN", Permission.BLACK_LIST_AUDIT),
  BLACK_LIST_BATCH_IMPORT("BLACK_LIST_BATCH_IMPORT", Permission.BLACK_LIST_AUDIT),
  BLACK_LIST_DEVICE("BLACK_LIST_DEVICE", Permission.BLACK_LIST_AUDIT),
  BLACK_LIST_IP("BLACK_LIST_IP", Permission.BLACK_LIST_AUDIT),
  BLACK_LIST_MERCHANT_URL("BLACK_LIST_MERCHANT_URL", Permission.BLACK_LIST_AUDIT),
  BLACK_LIST_AUTH_STATUS("BLACK_LIST_AUTH_STATUS", Permission.BLACK_LIST_AUDIT),
  WHITE_LIST("WHITE_LIST", Permission.WHITE_LIST_AUDIT),
  ATTEMPT("ATTEMPT", Permission.CARD_AUDIT),
  CLASSIC_RBA("CLASSIC_RBA", Permission.CLASSIC_RBA_AUDIT),

  // 使用者管理
  USER_GROUP("USER_GROUP", Permission.USER_GROUP_AUDIT),
  USER_ACCOUNT("USER_ACCOUNT", Permission.USER_GROUP_AUDIT),
  PERMISSION("PERMISSION", Permission.USER_GROUP_AUDIT),
  ACCOUNT_GROUP("ACCOUNT_GROUP", Permission.USER_GROUP_AUDIT),
  UNLOCK_ACTION("UNLOCK_ACTION", Permission.UNLOCK_AUDIT),

  // 銀行管理
  BANK_LOGO("BANK_LOGO", Permission.BANK_LOGO_AUDIT),
  BANK_CHANNEL("BANK_CHANNEL", Permission.BANK_CHANNEL_AUDIT),
  BANK_FEE("BANK_FEE", Permission.BANK_FEE_AUDIT),
  BANK_MANAGE("BANK_MANAGE", Permission.BANK_MANAGE_AUDIT),
  BANK_OTP_SENDING("BANK_OTP_SENDING", Permission.BANK_OTP_SENDING_AUDIT),
  BANK_OTP_KEY_UPLOAD("BANK_OTP_KEY_UPLOAD", Permission.BANK_OTP_SENDING_AUDIT),

  // 銀行管理員管理
  BANK_ADMIN("BANK_ADMIN", Permission.BANK_MANAGE_AUDIT),

  // 系統設定
  SYS_BIN_RANGE("SYS_BIN_RANGE", Permission.SYS_BIN_RANGE_AUDIT),
  SYS_CHALLENGE_VIEW("SYS_CHALLENGE_VIEW", Permission.SYS_CHALLENGE_VIEW_AUDIT),
  SYS_CHALLENGE_SMS_MSG("SYS_CHALLENGE_SMS_MSG", Permission.SYS_CHALLENGE_SMS_MSG_AUDIT),
  SYS_ACS_OPERATOR_ID("SYS_ACS_OPERATOR_ID", Permission.SYS_ACS_OPERATOR_ID_AUDIT),
  SYS_TIMEOUT("SYS_TIMEOUT", Permission.SYS_TIMEOUT_AUDIT),
  SYS_ERROR_CODE("SYS_ERROR_CODE", Permission.SYS_ERROR_CODE_AUDIT),
  SYS_KEY("SYS_KEY", Permission.SYS_KEY_AUDIT),
  SYS_CARD_BRAND_LOGO("SYS_CARD_BRAND_LOGO", Permission.SYS_CARD_LOGO_AUDIT),
  SYS_ISSUER_LOGO("SYS_ISSUER_LOGO", Permission.SYS_CHALLENGE_VIEW_AUDIT),

  // 憑證管理
  SYS_CA_CERT("SYS_CA_CERT", Permission.CERT_AUDIT),
  SYS_SIGNING_CERT("SYS_SIGNING_CERT", Permission.CERT_AUDIT),
  SYS_SSL_P12_CERT("SYS_SSL_P12_CERT", Permission.CERT_AUDIT),
  SYS_SSL_CERT("SYS_SSL_CERT", Permission.CERT_AUDIT),

  // UNKNOWN
  UNKNOWN("UNKNOWN", Permission.UNKNOWN);

  private String typeSymbol;
  private Permission typePermission;

  AuditFunctionType(String symbol, Permission requiredPermission) {
    typeSymbol = symbol;
    this.typePermission = requiredPermission;
  }

  public String getTypeSymbol() {
    return typeSymbol;
  }

  public Permission getPermissionType() {
    return typePermission;
  }

  public static AuditFunctionType getBySymbol(String typeSymbol) {
    if(StringCustomizedUtils.isEmpty(typeSymbol)) return  AuditFunctionType.UNKNOWN;

    for (AuditFunctionType dataType : AuditFunctionType.values()) {
      if (typeSymbol.equalsIgnoreCase(dataType.getTypeSymbol())) {
        return dataType;
      }
    }

    return AuditFunctionType.UNKNOWN;
  }

  public static AuditFunctionType getByPermission(Permission requiredPermission) {
    for (AuditFunctionType dataType : AuditFunctionType.values()) {
      if (requiredPermission == dataType.getPermissionType()) {
        return dataType;
      }
    }

    return AuditFunctionType.UNKNOWN;
  }
}
