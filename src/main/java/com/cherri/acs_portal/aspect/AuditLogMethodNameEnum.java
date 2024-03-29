package com.cherri.acs_portal.aspect;

public enum AuditLogMethodNameEnum {
    /** Login */
    LOGIN,
    LOGOUT,

    /** Cardholder management */
    CARDHOLDER_GET,
    CARDHOLDER_OTP_UNLOCK_UPDATE,
    CARDHOLDER_3DS_VERIFY_ENABLED_UPDATE,
    ATTEMPT_AUTH_SETTING_GET,
    ATTEMPT_AUTH_SETTING_CREATE,
    ATTEMPT_AUTH_LOG_LIST,
    /** 人工彈性授權 匯出人工彈性授權列表 */
//  ATTEMPT_AUTH_LOG_EXPORT,
    OTP_LOG_LIST,
    /** 持卡人 匯出OTP */
//  OTP_LOG_EXPORT,
    THREE_DS_LOG_LIST,
    /** 持卡人 匯出3DS驗證狀態 */
//  THREE_DS_LOG_EXPORT,

    /** User management */
    /** 緊急聯絡人 - 取得列表 */
//  EMERGENCY_CONTACT_LIST,
    /** 緊急聯絡人 - 刪除聯絡人 */
//  EMERGENCY_CONTACT_DELETE,
    /** 緊急聯絡人 - 建立聯絡人 */
//  EMERGENCY_CONTACT_CREATE,
    AUDIT_LOG_LIST,
    /** 操作記錄 - 匯出操作記錄 */
//  AUDIT_LOG_LIST_DOWNLOAD,
    /** 操作記錄 - 匯出選取操作記錄 */
//  AUDIT_LOG_LIST_DOWNLOAD_BY_ID,
    PERMISSION_MODULE_SETTING_LIST,
    PERMISSION_GET,
    PERMISSION_ROLE_LIST,
    PERMISSION_ROLE_DELETE,
    PERMISSION_ROLE_CREATE,
    PERMISSION_ROLE_UPDATE,
    PERMISSION_ROLE_PERMISSION_UPDATE,
    PERMISSION_ROLE_PERMISSION_GET,
    ACCOUNT_LOCK_USER_LIST,
    ACCOUNT_UNLOCK_USER,
    USER_LIST,
    USER_CREATE,
    USER_UPDATE,
    USER_DELETE,
    USER_PROFILE_GET,
    USER_PROFILE_UPDATE,

    /** Transaction log */
    TRANSACTION_LOG_LIST,
    TRANSACTION_LOG_SUMMARY_GET,
    TRANSACTION_LOG_DETAIL_GET,
    TRANSACTION_LOG_SMS_GET,
    TRANSACTION_LOG_EXPORT,

    /** VE log */
    VELOG_LIST,
    VELOG_EXPORT,

    /** PReq Records */
    PREQ_RECORD_LIST,

    /** Risk management */
    BLACK_LIST_AUTH_STATUS_LIST,
    BLACK_LIST_AUTH_STATUS_UPDATE,
    /** 交易紀錄查詢 - 從紀錄新增卡號黑名單 */
//  BLACK_LIST_PAN_CREATE_BY_TRANSACTION_LOG,
    BLACK_LIST_DELETE,
    BLACK_LIST_PAN_LIST,
    BLACK_LIST_PAN_CREATE_BY_MANUAL,
    BLACK_LIST_PAN_EXPORT,
    BLACK_LIST_DEVICE_ID_LIST,
    /** 交易紀錄查詢 - 從紀錄新增裝置ＩＤ黑名單 */
    BLACK_LIST_DEVICE_ID_CREATE_BY_TRANSACTION_LOG,
    /** 黑名單管理 - 匯出裝置ID黑名單列表 */
    BLACK_LIST_DEVICE_ID_EXPORT,
    BLACK_LIST_DEVICE_ID_DELETE,
    BLACK_LIST_IP_LIST,
    BLACK_LIST_IP_CREATE,
    BLACK_LIST_IP_EXPORT,
    BLACK_LIST_MERCHANT_URL_LIST,
  BLACK_LIST_MERCHANT_URL_CREATE,
  BLACK_LIST_MERCHANT_URL_EXPORT,
  WHITE_LIST_PAN_LIST,
  WHITE_LIST_PAN_CREATE,
  WHITE_LIST_PAN_DELETE,
  WHITE_LIST_PAN_EXPORT,
  BLACK_LIST_PAN_IMPORT_CREATE,
  BLACK_LIST_PAN_IMPORT_DELETE,
  /** 人工彈性授權 - 更新授權最大金額 */
//  ATTEMPT_AUTH_SETTING_ROLE_MAX_AMOUNT_UPDATE,
  /** 人工彈性授權 - 取得授權最大金額 */
//  ATTEMPT_AUTH_SETTING_ROLE_MAX_AMOUNT_GET,

  /** System setting */
  BIN_RANGE_LIST,
  BIN_RANGE_CREATE,
  BIN_RANGE_UPDATE,
  BIN_RANGE_DELETE,
  GENERAL_SETTING_LIST,
  GENERAL_SETTING_UPDATE,
  MCC_LIST,
  MCC_IMPORT,
  ISSUER_LOGO_GET,
  ISSUER_LOGO_UPDATE,
  CA_CERTIFICATE_GET,
  CA_CERTIFICATE_CREATE,
  CA_CERTIFICATE_DELETE,
  SSL_GET,
  SSL_RENEW_P12,
  SSL_UPLOAD_P12,
  SSL_GENERATE_KEY,
  SSL_DELETE_TEMP_KEY,
  SSL_DOWNLOAD_P12,
  SSL_RENEW_GENERATE_KEY,
    SSL_UPLOAD_CERTIFICATE,
    SSL_RENEW_UPLOAD_CERTIFICATE,
    SSL_CHANGE_CERT,
    SIGNING_GET,
    SIGNING_CERTIFICATE_UPDATE,
    SIGNING_ROOT_CA_UPDATE,
    SIGNING_SUB_CA_UPDATE,
    SIGNING_CHANGE_CERT,
    ERROR_CODE_LIST,
    ERROR_CODE_UPDATE,
    KEY_LIST,
    KEY_MODIFY,
    KEY_IMPORT_VERIFY,
    KEY_IMPORT_CONFIRM,

    /** Report */
    DASHBOARD,
    ABNORMAL_TRANSACTION_LIST,
    ABNORMAL_TRANSACTION_EXPORT,
    STATISTIC_REPORT_LIST,
    BROWSER_ERROR_LOG,

    /** Auditing */
    GRANTED_AUDIT_FUNCTION_LIST,
    AUDITING_LIST,
    SIGNING_AUDITING,
    AUDITING_PERSONAL_LOG,

    /** Bank management */
    ISSUER_BANK_GET,
    ISSUER_BANK_LIST,
    ISSUER_BANK_MODIFY,
    ISSUER_BANK_CREATE,
    ISSUER_BANK_DELETE,
    ISSUER_BANK_ADMIN_LIST,
    ISSUER_BANK_ADMIN_CREATE,
    ISSUER_BANK_ADMIN_UPDATE,
    ISSUER_BANK_ADMIN_DELETE,
    TRADING_CHANNEL_LIST,
    TRADING_CHANNEL_UPDATE,
    /** 會員銀行 - 系統費用設定 - 手續費計算方式查詢 */
    ISSUER_BANK_FEE_LIST,
    /** 會員銀行 - 系統費用設定 - 手續費計算方式匯出 */
    ISSUER_BANK_FEE_EXPORT,
    ISSUER_BANK_FEE_MODIFY,
    OTP_SENDING_GET,
    OTP_SENDING_MODIFY,

    /** Acquirer Bank */
    ACQUIRER_BANK_GET,
    ACQUIRER_BANK_CREATE,
    ACQUIRER_BANK_MODIFY,
    ACQUIRER_BANK_DELETE,
    ACQUIRER_BANK_ADD_BIN,
    ACQUIRER_BANK_DELETE_BIN,
    ACQUIRER_BANK_ADD_THREEDS_REF_NUMBER,
    ACQUIRER_BANK_DELETE_THREEDS_REF_NUMBER,
    ACQUIRER_BANK_QUERY_THREEDS_REF_NUMBERS,


    /** 經典風控 Classic RBA **/
    CLASSIC_RBA_LIST,
    CLASSIC_RBA_STATUS,
    CLASSIC_RBA_UPDATE,

    /** Bank Data Key **/
    BANK_DATA_KEY_GET,
    BANK_DATA_KEY_CREATE,

    /** Plugin 設定 **/
    PLUGIN_ISSUER_RUNTIME_PROPERTY_LIST,
    PLUGIN_ISSUER_RUNTIME_PROPERTY_MODIFY,

    /** 密碼原則管理 */
    MIMA_POLICY_QUERY,
    MIMA_POLICY_MODIFY,
    MIMA_MODIFY,

    /** 無存取權限 */
    NO_PERMISSION;
}
