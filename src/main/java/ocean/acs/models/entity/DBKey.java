package ocean.acs.models.entity;

public final class DBKey {

    private DBKey() {
    }

    // OperatorInfo for Portal's permission feature
    public static final String COL_AUDIT_STATUS = "audit_status";
    public static final String COL_CREATOR = "creator";
    public static final String COL_CREATE_MILLIS = "create_millis";
    public static final String COL_UPDATER = "updater";
    public static final String COL_UPDATE_MILLIS = "update_millis";
    public static final String COL_DELETE_FLAG = "delete_flag";
    public static final String COL_DELETER = "deleter";
    public static final String COL_DELETE_MILLIS = "delete_millis";
    public static final String COL_SYS_CREATOR = "sys_creator";
    public static final String COL_SYS_UPDATER = "sys_updater";

    // Table abnormal_transaction
    public static final String TABLE_ABNORMAL_TRANSACTION = "abnormal_transaction";
    public static final String COL_ABNORMAL_TRANSACTION_ID = "id";
    public static final String COL_ABNORMAL_TRANSACTION_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_ABNORMAL_TRANSACTION_YEAR = "year";
    public static final String COL_ABNORMAL_TRANSACTION_MONTH = "month";
    public static final String COL_ABNORMAL_TRANSACTION_DAY_OF_MONTH = "day_of_month";
    public static final String COL_ABNORMAL_TRANSACTION_MERCHANT_ID = "merchant_id";
    public static final String COL_ABNORMAL_TRANSACTION_MERCHANT_NAME = "merchant_name";
    public static final String COL_ABNORMAL_TRANSACTION_U_RATE = "u_rate";
    public static final String COL_ABNORMAL_TRANSACTION_N_RATE = "n_rate";
    public static final String COL_ABNORMAL_TRANSACTION_U_COUNT = "u_count";
    public static final String COL_ABNORMAL_TRANSACTION_N_COUNT = "n_count";
    public static final String COL_ABNORMAL_TRANSACTION_TOTAL_COUNT = "total_count";
    public static final String COL_ABNORMAL_TRANSACTION_SYS_CREATOR = "sys_creator";
    public static final String COL_ABNORMAL_TRANSACTION_CREATE_MILLIS = "create_millis";
    public static final String COL_ABNORMAL_TRANSACTION_SYS_UPDATER = "sys_updater";
    public static final String COL_ABNORMAL_TRANSACTION_UPDATE_MILLIS = "update_millis";

    // Table account_group
    public static final String TABLE_ACCOUNT_GROUP = "account_group";
    public static final String COL_ACCOUNT_GROUP_ID = "id";
    public static final String COL_ACCOUNT_GROUP_ACCOUNT_ID = "account_id";
    public static final String COL_ACCOUNT_GROUP_GROUP_ID = "group_id";
    public static final String COL_ACCOUNT_GROUP_AUDIT_STATUS = "audit_status";
    public static final String COL_ACCOUNT_GROUP_CREATOR = "creator";
    public static final String COL_ACCOUNT_GROUP_CREATE_MILLIS = "create_millis";

    // Table auditing
    public static final String TABLE_AUDITING = "auditing";
    public static final String COL_AUDITING_ID = "id";
    public static final String COL_AUDITING_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_AUDITING_FUNCTION_TYPE = "function_type";
    public static final String COL_AUDITING_ACTION_TYPE = "action_type";
    public static final String COL_AUDITING_AUDIT_STATUS = "audit_status";
    public static final String COL_AUDITING_DRAFT_CONTENT = "draft_content";
    public static final String COL_AUDITING_FILE_CONTENT = "file_content";
    public static final String COL_AUDITING_FILE_NAME = "file_name";
    public static final String COL_AUDITING_AUDIT_COMMENT = "audit_comment";

    // Table audit_log
    public static final String TABLE_AUDIT_LOG = "audit_log";
    public static final String COL_AUDIT_LOG_ID = "id";
    public static final String COL_AUDIT_LOG_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_AUDIT_LOG_IP = "ip";
    public static final String COL_AUDIT_LOG_METHOD_NAME = "method_name";
    public static final String COL_AUDIT_LOG_VAL = "val";
    public static final String COL_AUDIT_LOG_ACTION = "action";
    public static final String COL_AUDIT_LOG_ERROR_CODE = "error_code";
    public static final String COL_AUDIT_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_AUDIT_LOG_CREATE_MILLIS = "create_millis";

    // Table black_list_pan_batch
    public static final String TABLE_BLACK_LIST_PAN_BATCH = "black_list_pan_batch";
    public static final String COL_BLACK_LIST_PAN_BATCH_ID = "id";
    public static final String COL_BLACK_LIST_PAN_BATCH_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_BLACK_LIST_PAN_BATCH_BATCH_NAME = "batch_name";
    public static final String COL_BLACK_LIST_PAN_BATCH_FILE_NAME = "file_name";
    public static final String COL_BLACK_LIST_PAN_BATCH_FILE_CONTENT = "file_content";
    public static final String COL_BLACK_LIST_PAN_BATCH_TRANS_STATUS = "trans_status";
    public static final String COL_BLACK_LIST_PAN_BATCH_PAN_NUMBER = "pan_number";

    // Table bin_range
    public static final String TABLE_BIN_RANGE = "bin_range";
    public static final String COL_BIN_RANGE_ID = "id";
    public static final String COL_BIN_RANGE_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_BIN_RANGE_CARD_BRAND = "card_brand";
    public static final String COL_BIN_RANGE_CARD_TYPE = "card_type";
    public static final String COL_BIN_RANGE_START_BIN = "start_bin";
    public static final String COL_BIN_RANGE_END_BIN = "end_bin";

    // Table black_list_auth_status
    public static final String TABLE_BLACK_LIST_AUTH_STATUS = "black_list_auth_status";
    public static final String COL_BLACK_LIST_AUTH_STATUS_ID = "id";
    public static final String COL_BLACK_LIST_AUTH_STATUS_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_BLACK_LIST_AUTH_STATUS_CATEGORY = "category";
    public static final String COL_BLACK_LIST_AUTH_STATUS_TRANS_STATUS = "trans_status";

    // Table black_list_device_info
    public static final String TABLE_BLACK_LIST_DEVICE_INFO = "black_list_device_info";
    public static final String COL_BLACK_LIST_DEVICE_INFO_ID = "id";
    public static final String COL_BLACK_LIST_DEVICE_INFO_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_BLACK_LIST_DEVICE_INFO_DEVICE_ID = "device_id";
    public static final String COL_BLACK_LIST_DEVICE_INFO_DEVICE_CHANNEL = "device_channel";
    public static final String COL_BLACK_LIST_DEVICE_INFO_BROWSER_USER_AGENT = "browser_user_agent";
    public static final String COL_BLACK_LIST_DEVICE_INFO_IP = "ip";
    public static final String COL_BLACK_LIST_DEVICE_INFO_TRANS_STATUS = "trans_status";

    // Table black_list_ip_group
    public static final String TABLE_BLACK_LIST_IP_GROUP = "black_list_ip_group";
    public static final String COL_BLACK_LIST_IP_GROUP_ID = "id";
    public static final String COL_BLACK_LIST_IP_GROUP_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_BLACK_LIST_IP_GROUP_ORIGIN_VERSION = "origin_version";
    public static final String COL_BLACK_LIST_IP_GROUP_IP = "ip";
    public static final String COL_BLACK_LIST_IP_GROUP_CIDR = "cidr";
    public static final String COL_BLACK_LIST_IP_GROUP_TRANS_STATUS = "trans_status";

    // Table black_list_merchant_url
    public static final String TABLE_BLACK_LIST_MERCHANT_URL = "black_list_merchant_url";
    public static final String COL_BLACK_LIST_MERCHANT_URL_ID = "id";
    public static final String COL_BLACK_LIST_MERCHANT_URL_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_BLACK_LIST_MERCHANT_URL_URL = "url";
    public static final String COL_BLACK_LIST_MERCHANT_URL_TRANS_STATUS = "trans_status";

    // Table black_list_pan
    public static final String TABLE_BLACK_LIST_PAN = "black_list_pan";
    public static final String COL_BLACK_LIST_PAN_ID = "id";
    public static final String COL_BLACK_LIST_PAN_PAN_ID = "pan_id";
    public static final String COL_BLACK_LIST_PAN_TRANS_STATUS = "trans_status";
    public static final String COL_BLACK_LIST_PAN_BLACK_LIST_PAN_BATCH_ID = "black_list_pan_batch_id";

    // Table browser_error_log
    public static final String TABLE_BROWSER_ERROR_LOG = "browser_error_log";
    public static final String COL_BROWSER_ERROR_LOG_ID = "id";
    public static final String COL_BROWSER_ERROR_LOG_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_BROWSER_ERROR_LOG_YEAR = "year";
    public static final String COL_BROWSER_ERROR_LOG_MONTH = "month";
    public static final String COL_BROWSER_ERROR_LOG_DAY_OF_MONTH = "day_of_month";
    public static final String COL_BROWSER_ERROR_LOG_BROWSER_TYPE = "browser_type";
    public static final String COL_BROWSER_ERROR_LOG_ERROR_CODE = "error_code";
    public static final String COL_BROWSER_ERROR_LOG_ERROR_CODE_DAY_COUNT = "error_code_day_count";
    public static final String COL_BROWSER_ERROR_LOG_DATA_MILLIS = "data_millis";
    public static final String COL_BROWSER_ERROR_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_BROWSER_ERROR_LOG_CREATE_MILLIS = "create_millis";

    // Table ca_certificate
    public static final String TABLE_CA_CERTIFICATE = "ca_certificate";
    public static final String COL_CA_CERTIFICATE_ID = "id";
    public static final String COL_CA_CERTIFICATE_CARD_BRAND = "card_brand";
    public static final String COL_CA_CERTIFICATE_APPLY_MILLIS = "apply_millis";
    public static final String COL_CA_CERTIFICATE_CERTIFICATE = "certificate";
    public static final String COL_CA_CERTIFICATE_CERTIFICATE_INFORMATION = "certificate_information";
    public static final String COL_CA_CERTIFICATE_EXPIRE_MILLIS = "expire_millis";
    public static final String COL_CA_CERTIFICATE_HASH_ALGORITHM = "hash_algorithm";
    public static final String COL_CA_CERTIFICATE_ISSUER = "issuer";

    // Table card_brand_logo
    public static final String TABLE_CARD_BRAND_LOGO = "card_brand_logo";
    public static final String COL_CARD_BRAND_LOGO_ID = "id";
    public static final String COL_CARD_BRAND_LOGO_THREE_D_S_VERSION = "three_d_s_version";
    public static final String COL_CARD_BRAND_LOGO_CARD_BRAND = "card_brand";
    public static final String COL_CARD_BRAND_LOGO_IMAGE_NAME = "image_name";
    public static final String COL_CARD_BRAND_LOGO_IMAGE_CONTENT = "image_content";
    public static final String COL_CARD_BRAND_LOGO_IMAGE_SIZE = "image_size";

    // Table certificate
    public static final String TABLE_CERTIFICATE = "certificate";
    public static final String COL_CERTIFICATE_ID = "id";
    public static final String COL_CERTIFICATE_CERTIFICATE = "certificate";
    public static final String COL_CERTIFICATE_INFORMATION = "information";
    public static final String COL_CERTIFICATE_ISSUER = "issuer";
    public static final String COL_CERTIFICATE_APPLY_DATE = "apply_date";
    public static final String COL_CERTIFICATE_EXPIRY_DATE = "expiry_date";
    public static final String COL_CERTIFICATE_CREATE_MILLIS = "create_millis";

	// Table language
	public static final String TABLE_LANG = "lang";
	public static final String COL_LANG_ID = "id";
	public static final String COL_LANG_LANGUAGE_CODE = "language_code";
	public static final String COL_LANG_DISPLAY_TEXT = "display_text";
	public static final String COL_LANG_IS_DEFAULT = "is_default";

	// Table challenge_view_message
	public static final String TABLE_CHALLENGE_VIEW_MESSAGE = "challenge_view_message";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_ID = "id";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_ISSUER_BANK_ID = "issuer_bank_id";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_CATEGORY = "category";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_LANGUAGE_CODE = "language_code";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_MAIN_BODY_TITLE = "main_body_title";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_MAIN_BODY_MESSAGE = "main_body_message";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_NPA_MAIN_BODY_MESSAGE = "npa_main_body_message";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_WEB_REMARK_BODY_MESSAGE = "web_remark_body_message";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_WEB_REMARK_BODY_COLOR = "web_remark_body_color";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_VERIFY_INPUT_PLACEHOLDER = "verify_input_placeholder";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_APP_REMARK_BODY_MESSAGE = "app_remark_body_message";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_BTN_BODY_SUBMIT_BUTTON = "btn_body_submit_button";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_BTN_BODY_PHONE_ERROR_BUTTON = "btn_body_phone_error_button";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_BTN_BODY_OTP_RESEND_BUTTON = "btn_body_otp_resend_button";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_BTN_BODY_CANCEL_BUTTON = "btn_body_cancel_button";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_PHONE_ERROR_BODY_MESSAGE = "phone_error_body_message";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_PHONE_ERROR_BODY_SYMBOL = "phone_error_body_symbol";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_FOOTER_LABEL_1 = "footer_label_1";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_FOOTER_MESSAGE_1 = "footer_message_1";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_FOOTER_LABEL_2 = "footer_label_2";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_FOOTER_MESSAGE_2 = "footer_message_2";
	public static final String COL_CHALLENGE_VIEW_MESSAGE_VERIFY_FAILED_MESSAGE = "verify_failed_message";
    public static final String COL_CHALLENGE_VIEW_MESSAGE_RESEND_MESSAGE = "resend_message";
    public static final String COL_CHALLENGE_VIEW_MESSAGE_NOT_RESEND_MESSAGE = "not_resend_message";


	// Table challenge_view_error_message
	public static final String TABLE_CHALLENGE_VIEW_ERROR_MESSAGE = "challenge_view_error_message";
	public static final String COL_CHALLENGE_VIEW_ERROR_MESSAGE_ID = "id";
	public static final String COL_CHALLENGE_VIEW_ERROR_MESSAGE_LANGUAGE_CODE = "language_code";
    public static final String COL_CHALLENGE_VIEW_ERROR_MESSAGE_ERROR_CATEGORY = "error_category";
	public static final String COL_CHALLENGE_VIEW_ERROR_MESSAGE_ERROR_TITLE = "error_title";
	public static final String COL_CHALLENGE_VIEW_ERROR_MESSAGE_ERROR_MESSAGE = "error_message";

	// Table challenge_view_otp_setting
	public static final String TABLE_CHALLENGE_VIEW_OTP_SETTING = "challenge_view_otp_setting";
	public static final String COL_CHALLENGE_VIEW_OTP_SETTING_ID = "id";
	public static final String COL_CHALLENGE_VIEW_OTP_SETTING_ISSUER_BANK_ID = "issuer_bank_id";
	public static final String COL_CHALLENGE_VIEW_OTP_SETTING_MAX_RESEND_TIMES = "max_resend_times";
	public static final String COL_CHALLENGE_VIEW_OTP_SETTING_MAX_CHALLENGE_TIMES = "max_challenge_times";

    // Table error_code_mapping
    public static final String TABLE_ERROR_CODE_MAPPING = "error_code_mapping";
    public static final String COL_ERROR_CODE_MAPPING_ID = "id";
    public static final String COL_ERROR_CODE_MAPPING_ERROR_GROUP_ID = "error_group_id";
    public static final String COL_ERROR_CODE_MAPPING_CODE = "code";
    public static final String COL_ERROR_CODE_MAPPING_MSG = "msg";
    public static final String COL_ERROR_CODE_MAPPING_SYS_CREATOR = "sys_creator";
    public static final String COL_ERROR_CODE_MAPPING_CREATE_MILLIS = "create_millis";
    public static final String COL_ERROR_CODE_MAPPING_UPDATER = "updater";
    public static final String COL_ERROR_CODE_MAPPING_UPDATE_MILLIS = "update_millis";

    // Table error_issue_group
    public static final String TABLE_ERROR_ISSUE_GROUP = "error_issue_group";
    public static final String COL_ERROR_ISSUE_GROUP_ID = "id";
    public static final String COL_ERROR_ISSUE_GROUP_NAME = "name";

    // Table issuer_bank
    public static final String TABLE_ISSUER_BANK = "issuer_bank";
    public static final String COL_ISSUER_BANK_ID = "id";
    public static final String COL_ISSUER_BANK_CODE = "code";
    public static final String COL_ISSUER_BANK_NAME = "name";
    public static final String COL_ISSUER_BANK_SYMMETRIC_KEY_ID = "symmetric_key_id";
    public static final String COL_ISSUER_BANK_SENSITIVE_DATA_KEY_ID = "sensitive_data_key_id";

    // Table issuer_brand_logo
    public static final String TABLE_ISSUER_BRAND_LOGO = "issuer_brand_logo";
    public static final String COL_ISSUER_BRAND_LOGO_ID = "id";
    public static final String COL_ISSUER_BRAND_LOGO_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_ISSUER_BRAND_LOGO_IMAGE_NAME = "image_name";
    public static final String COL_ISSUER_BRAND_LOGO_IMAGE_CONTENT = "image_content";
    public static final String COL_ISSUER_BRAND_LOGO_IMAGE_SIZE = "image_size";

    // Table issuer_handing_fee
    public static final String TABLE_ISSUER_HANDING_FEE = "issuer_handing_fee";
    public static final String COL_ISSUER_HANDING_FEE_ID = "id";
    public static final String COL_ISSUER_HANDING_FEE_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_ISSUER_HANDING_FEE_HANDING_FEE_TYPE = "handing_fee_type";
    public static final String COL_ISSUER_HANDING_FEE_FEE_PER_CARD = "fee_per_card";
    public static final String COL_ISSUER_HANDING_FEE_THRESHOLD_FEE = "threshold_fee";
    public static final String COL_ISSUER_HANDING_FEE_MINIMUM_FEE = "minimum_fee";
    public static final String COL_ISSUER_HANDING_FEE_FEE_PER_MONTH = "fee_per_month";

    // Table issuer_logo
    public static final String TABLE_ISSUER_LOGO = "issuer_logo";
    public static final String COL_ISSUER_LOGO_ID = "id";
    public static final String COL_ISSUER_LOGO_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_ISSUER_LOGO_IMAGE_CONTENT = "image_content";
    public static final String COL_ISSUER_LOGO_IMAGE_NAME = "image_name";
    public static final String COL_ISSUER_LOGO_IMAGE_SIZE = "image_size";

    // Table issuer_trading_channel
    public static final String TABLE_ISSUER_TRADING_CHANNEL = "issuer_trading_channel";
    public static final String COL_ISSUER_TRADING_CHANNEL_ID = "id";
    public static final String COL_ISSUER_TRADING_CHANNEL_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_ISSUER_TRADING_CHANNEL_ENABLED_CHANNEL_LIST = "enabled_channel_list";

    // Table key_store
    public static final String TABLE_KEY_STORE = "key_store";
    public static final String COL_KEY_STORE_ID = "id";
    public static final String COL_KEY_STORE_KEY_NAME = "key_name";
    public static final String COL_KEY_STORE_KEY_BODY = "key_body";
    public static final String COL_KEY_STORE_SYS_CREATOR = "sys_creator";
    public static final String COL_KEY_STORE_CREATE_MILLIS = "create_millis";

    // Table module_setting
    public static final String TABLE_MODULE_SETTING = "module_setting";
    public static final String COL_MODULE_SETTING_ID = "id";
    public static final String COL_MODULE_SETTING_FUNCTION_TYPE = "function_type";
    public static final String COL_MODULE_SETTING_AUDIT_DEMAND = "audit_demand";
    public static final String COL_MODULE_SETTING_CREATOR = "creator";
    public static final String COL_MODULE_SETTING_CREATE_MILLIS = "create_millis";
    public static final String COL_MODULE_SETTING_UPDATER = "updater";
    public static final String COL_MODULE_SETTING_UPDATE_MILLIS = "update_millis";

    // Table mcc
    public static final String TABLE_MCC = "mcc";
    public static final String COL_MCC_ID = "id";
    public static final String COL_MCC_MCC = "mcc";

    // Table otp_lock_log
    public static final String TABLE_OTP_LOCK_LOG = "otp_lock_log";
    public static final String COL_OTP_LOCK_LOG_ID = "id";
    public static final String COL_OTP_LOCK_LOG_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_OTP_LOCK_LOG_PAN_ID = "pan_id";
    public static final String COL_OTP_LOCK_LOG_ENABLED = "enabled";

    // Table otp_sending_setting
    public static final String TABLE_OTP_SENDING_SETTING = "otp_sending_setting";
    public static final String COL_OTP_SENDING_SETTING_ID = "id";
    public static final String COL_OTP_SENDING_SETTING_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_OTP_SENDING_SETTING_ORG_ENABLE = "org_enable";
    public static final String COL_OTP_SENDING_SETTING_BANK_ENABLE = "bank_enable";
    public static final String COL_OTP_SENDING_SETTING_BANK_URL = "bank_url";
    public static final String COL_OTP_SENDING_SETTING_JWE_RSA_PUBLIC_KEY = "jwe_rsa_public_key";
    public static final String COL_OTP_SENDING_SETTING_JWS_SECRET_KEY = "jws_secret_key";

	// Table pan_info
	public static final String TABLE_PAN_INFO = "pan_info";
	public static final String COL_PAN_INFO_ID = "id";
	public static final String COL_PAN_INFO_ISSUER_BANK_ID = "issuer_bank_id";
	public static final String COL_PAN_INFO_CARD_BRAND = "card_brand";
	public static final String COL_PAN_INFO_CARD_NUMBER = "card_number";
	public static final String COL_PAN_INFO_CARD_NUMBER_HASH = "card_number_hash";
	public static final String COL_PAN_INFO_CARD_NUMBER_EN = "card_number_en";
	public static final String COL_PAN_INFO_THREE_D_S_VERIFY_ENABLED = "three_d_s_verify_enabled";
	public static final String COL_PAN_INFO_PREVIOUS_TXN_DEVICE_ID = "previous_txn_device_id";
	public static final String COL_PAN_INFO_PREVIOUS_TXN_SUCCESS = "previous_txn_success";
    public static final String COL_PAN_INFO_CARD_STATUS = "card_status";

    // Table pan_otp_statistics
    public static final String TABLE_PAN_OTP_STATISTICS = "pan_otp_statistics";
    public static final String COL_PAN_OTP_STATISTICS_ID = "id";
    public static final String COL_PAN_OTP_STATISTICS_PAN_INFO_ID = "pan_info_id";
    public static final String COL_PAN_OTP_STATISTICS_VERIFY_OTP_COUNT = "verify_otp_count";
    public static final String COL_PAN_OTP_STATISTICS_AUDIT_STATUS = "audit_status";

    // Table report_attempt
    public static final String TABLE_REPORT_ATTEMPT = "report_attempt";
    public static final String COL_REPORT_ATTEMPT_ID = "id";
    public static final String COL_REPORT_ATTEMPT_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_REPORT_ATTEMPT_YEAR = "year";
    public static final String COL_REPORT_ATTEMPT_MONTH = "month";
    public static final String COL_REPORT_ATTEMPT_DAY_OF_MONTH = "day_of_month";
    public static final String COL_REPORT_ATTEMPT_PERMITTED_COUNT = "permitted_count";
    public static final String COL_REPORT_ATTEMPT_REAL_TRIES_COUNT = "real_tries_count";
    public static final String COL_REPORT_ATTEMPT_PERCENTAGE = "percentage";
    public static final String COL_REPORT_ATTEMPT_DATA_MILLIS = "data_millis";
    public static final String COL_REPORT_ATTEMPT_SYS_CREATOR = "sys_creator";
    public static final String COL_REPORT_ATTEMPT_CREATE_MILLIS = "create_millis";
    public static final String COL_REPORT_ATTEMPT_SYS_UPDATER = "sys_updater";
    public static final String COL_REPORT_ATTEMPT_UPDATE_MILLIS = "update_millis";

    // Table report_daily_error_reason
    public static final String TABLE_REPORT_DAILY_ERROR_REASON = "report_daily_error_reason";
    public static final String COL_REPORT_DAILY_ERROR_REASON_ID = "id";
    public static final String COL_REPORT_DAILY_ERROR_REASON_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_REPORT_DAILY_ERROR_REASON_YEAR = "year";
    public static final String COL_REPORT_DAILY_ERROR_REASON_MONTH = "month";
    public static final String COL_REPORT_DAILY_ERROR_REASON_DAY_OF_MONTH = "day_of_month";
    public static final String COL_REPORT_DAILY_ERROR_REASON_REASON_CODE_1 = "reason_code_1";
    public static final String COL_REPORT_DAILY_ERROR_REASON_REASON_COUNT_1 = "reason_count_1";
    public static final String COL_REPORT_DAILY_ERROR_REASON_REASON_PERCENTAGE_1 = "reason_percentage_1";
    public static final String COL_REPORT_DAILY_ERROR_REASON_REASON_CODE_2 = "reason_code_2";
    public static final String COL_REPORT_DAILY_ERROR_REASON_REASON_COUNT_2 = "reason_count_2";
    public static final String COL_REPORT_DAILY_ERROR_REASON_REASON_PERCENTAGE_2 = "reason_percentage_2";
    public static final String COL_REPORT_DAILY_ERROR_REASON_REASON_CODE_3 = "reason_code_3";
    public static final String COL_REPORT_DAILY_ERROR_REASON_REASON_COUNT_3 = "reason_count_3";
    public static final String COL_REPORT_DAILY_ERROR_REASON_REASON_PERCENTAGE_3 = "reason_percentage_3";
    public static final String COL_REPORT_DAILY_ERROR_REASON_DATA_MILLIS = "data_millis";
    public static final String COL_REPORT_DAILY_ERROR_REASON_SYS_CREATOR = "sys_creator";
    public static final String COL_REPORT_DAILY_ERROR_REASON_CREATE_MILLIS = "create_millis";
    public static final String COL_REPORT_DAILY_ERROR_REASON_SYS_UPDATER = "sys_updater";
    public static final String COL_REPORT_DAILY_ERROR_REASON_UPDATE_MILLIS = "update_millis";

    // Table report_month_error_reason
    public static final String TABLE_REPORT_MONTH_ERROR_REASON = "report_month_error_reason";
    public static final String COL_REPORT_MONTH_ERROR_REASON_ID = "id";
    public static final String COL_REPORT_MONTH_ERROR_REASON_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_REPORT_MONTH_ERROR_REASON_YEAR = "year";
    public static final String COL_REPORT_MONTH_ERROR_REASON_MONTH = "month";
    public static final String COL_REPORT_MONTH_ERROR_REASON_REASON_CODE_1 = "reason_code_1";
    public static final String COL_REPORT_MONTH_ERROR_REASON_REASON_COUNT_1 = "reason_count_1";
    public static final String COL_REPORT_MONTH_ERROR_REASON_REASON_PERCENTAGE_1 = "reason_percentage_1";
    public static final String COL_REPORT_MONTH_ERROR_REASON_REASON_CODE_2 = "reason_code_2";
    public static final String COL_REPORT_MONTH_ERROR_REASON_REASON_COUNT_2 = "reason_count_2";
    public static final String COL_REPORT_MONTH_ERROR_REASON_REASON_PERCENTAGE_2 = "reason_percentage_2";
    public static final String COL_REPORT_MONTH_ERROR_REASON_REASON_CODE_3 = "reason_code_3";
    public static final String COL_REPORT_MONTH_ERROR_REASON_REASON_COUNT_3 = "reason_count_3";
    public static final String COL_REPORT_MONTH_ERROR_REASON_REASON_PERCENTAGE_3 = "reason_percentage_3";
    public static final String COL_REPORT_MONTH_ERROR_REASON_DATA_MILLIS = "data_millis";
    public static final String COL_REPORT_MONTH_ERROR_REASON_SYS_CREATOR = "sys_creator";
    public static final String COL_REPORT_MONTH_ERROR_REASON_CREATE_MILLIS = "create_millis";
    public static final String COL_REPORT_MONTH_ERROR_REASON_SYS_UPDATER = "sys_updater";
    public static final String COL_REPORT_MONTH_ERROR_REASON_UPDATE_MILLIS = "update_millis";

    // Table report_tx_statistics
    public static final String TABLE_REPORT_TX_STATISTICS = "report_tx_statistics";
    public static final String COL_REPORT_TX_STATISTICS_ID = "id";
    public static final String COL_REPORT_TX_STATISTICS_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_REPORT_TX_STATISTICS_YEAR = "year";
    public static final String COL_REPORT_TX_STATISTICS_MONTH = "month";
    public static final String COL_REPORT_TX_STATISTICS_DAY_OF_MONTH = "day_of_month";
    public static final String COL_REPORT_TX_STATISTICS_TOTAL = "total";
    public static final String COL_REPORT_TX_STATISTICS_OTP_COUNT = "otp_count";
    public static final String COL_REPORT_TX_STATISTICS_N_COUNT = "n_count";
    public static final String COL_REPORT_TX_STATISTICS_A_COUNT = "a_count";
    public static final String COL_REPORT_TX_STATISTICS_Y_COUNT = "y_count";
    public static final String COL_REPORT_TX_STATISTICS_C_Y_COUNT = "c_y_count";
    public static final String COL_REPORT_TX_STATISTICS_C_N_COUNT = "c_n_count";
    public static final String COL_REPORT_TX_STATISTICS_R_COUNT = "r_count";
    public static final String COL_REPORT_TX_STATISTICS_U_COUNT = "u_count";
    public static final String COL_REPORT_TX_STATISTICS_DATA_MILLIS = "data_millis";
    public static final String COL_REPORT_TX_STATISTICS_SYS_CREATOR = "sys_creator";
    public static final String COL_REPORT_TX_STATISTICS_CREATE_MILLIS = "create_millis";
    public static final String COL_REPORT_TX_STATISTICS_SYS_UPDATER = "sys_updater";
    public static final String COL_REPORT_TX_STATISTICS_UPDATE_MILLIS = "update_millis";

    // Table report_tx_statistics_detail
    public static final String TABLE_REPORT_TX_STATISTICS_DETAIL = "report_tx_statistics_detail";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_ID = "id";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_YEAR = "year";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_MONTH = "month";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_DAY_OF_MONTH = "day_of_month";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_CARD_BRAND_NAME = "card_brand_name";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_U_RATE = "u_rate";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_N_RATE = "n_rate";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_ARES_Y_COUNT = "ares_y_count";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_ARES_N_COUNT = "ares_n_count";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_ARES_U_COUNT = "ares_u_count";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_ARES_A_COUNT = "ares_a_count";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_ARES_C_COUNT = "ares_c_count";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_ARES_R_COUNT = "ares_r_count";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_RREQ_Y_COUNT = "rreq_y_count";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_RREQ_N_COUNT = "rreq_n_count";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_RREQ_U_COUNT = "rreq_u_count";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_RREQ_A_COUNT = "rreq_a_count";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_RREQ_R_COUNT = "rreq_r_count";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_DATA_MILLIS = "data_millis";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_SYS_CREATOR = "sys_creator";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_CREATE_MILLIS = "create_millis";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_SYS_UPDATER = "sys_updater";
    public static final String COL_REPORT_TX_STATISTICS_DETAIL_UPDATE_MILLIS = "update_millis";

    // Table root_ca_certificate
    public static final String TABLE_ROOT_CA_CERTIFICATE = "root_ca_certificate";
    public static final String COL_ROOT_CA_CERTIFICATE_ID = "id";
    public static final String COL_ROOT_CA_CERTIFICATE_CERTIFICATE = "certificate";
    public static final String COL_ROOT_CA_CERTIFICATE_INFORMATION = "information";
    public static final String COL_ROOT_CA_CERTIFICATE_ISSUER = "issuer";
    public static final String COL_ROOT_CA_CERTIFICATE_APPLY_DATE = "apply_date";
    public static final String COL_ROOT_CA_CERTIFICATE_EXPIRY_DATE = "expiry_date";
    public static final String COL_ROOT_CA_CERTIFICATE_CREATE_MILLIS = "create_millis";

    // Table signing_certificate
    public static final String TABLE_SIGNING_CERTIFICATE = "signing_certificate";
    public static final String COL_SIGNING_CERTIFICATE_ID = "id";
    public static final String COL_SIGNING_CERTIFICATE_THREE_D_S_VERSION = "three_d_s_version";
    public static final String COL_SIGNING_CERTIFICATE_CARD_BRAND = "card_brand";
    public static final String COL_SIGNING_CERTIFICATE_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_SIGNING_CERTIFICATE_CURRENT_CERTIFICATE_ID = "current_certificate_id";
    public static final String COL_SIGNING_CERTIFICATE_CURRENT_SUB_CA_CERTIFICATE_ID = "current_sub_ca_certificate_id";
    public static final String COL_SIGNING_CERTIFICATE_CURRENT_ROOT_CA_CERTIFICATE_ID = "current_root_ca_certificate_id";
    public static final String COL_SIGNING_CERTIFICATE_CURRENT_SIGNING_RSA_KEY_ID = "current_signing_rsa_key_id";
    public static final String COL_SIGNING_CERTIFICATE_CREATE_MILLIS = "create_millis";
    public static final String COL_SIGNING_CERTIFICATE_UPDATE_MILLIS = "update_millis";

    // Table signing_rsa_key
    public static final String TABLE_SIGNING_RSA_KEY = "signing_rsa_key";
    public static final String COL_SIGNING_RSA_KEY_ID = "id";
    public static final String COL_SIGNING_RSA_KEY_KEY_ID = "key_id";
    public static final String COL_SIGNING_RSA_KEY_MODULUS_HEX = "modulus_hex";
    public static final String COL_SIGNING_RSA_KEY_PUBLIC_EXPONENT_HEX = "public_exponent_hex";
    public static final String COL_SIGNING_RSA_KEY_CREATE_MILLIS = "create_millis";
    public static final String COL_SIGNING_RSA_KEY_UPDATE_MILLIS = "update_millis";

    // Table sms_template
    public static final String TABLE_SMS_TEMPLATE = "sms_template";
    public static final String COL_SMS_TEMPLATE_ID = "id";
    public static final String COL_SMS_TEMPLATE_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_SMS_TEMPLATE_EXPIRE_MILLIS = "expire_millis";
    public static final String COL_SMS_TEMPLATE_VERIFY_MESSAGE = "verify_message";
    public static final String COL_SMS_TEMPLATE_EXCEED_LIMIT_MESSAGE = "exceed_limit_message";

    // Table ssl_client_certificate
    public static final String TABLE_SSL_CLIENT_CERTIFICATE = "ssl_client_certificate";
    public static final String COL_SSL_CLIENT_CERTIFICATE_ID = "id";
    public static final String COL_SSL_CLIENT_CERTIFICATE_CARD_BRAND = "card_brand";
    public static final String COL_SSL_CLIENT_CERTIFICATE_APPLY_MILLIS = "apply_millis";
    public static final String COL_SSL_CLIENT_CERTIFICATE_HAS_CERT = "has_cert";
    public static final String COL_SSL_CLIENT_CERTIFICATE_CERTIFICATE = "certificate";
    public static final String COL_SSL_CLIENT_CERTIFICATE_CERTIFICATE_INFORMATION = "certificate_information";
    public static final String COL_SSL_CLIENT_CERTIFICATE_EXPIRE_MILLIS = "expire_millis";
    public static final String COL_SSL_CLIENT_CERTIFICATE_HASH_ALGORITHM = "hash_algorithm";
    public static final String COL_SSL_CLIENT_CERTIFICATE_ISSUER = "issuer";
    public static final String COL_SSL_CLIENT_CERTIFICATE_PRIVATE_KEY = "private_key";
    public static final String COL_SSL_CLIENT_CERTIFICATE_PUBLIC_KEY = "public_key";
    public static final String COL_SSL_CLIENT_CERTIFICATE_KEY_STATUS = "key_status";
    public static final String COL_SSL_CLIENT_CERTIFICATE_ACTIVITY = "activity";

    // Table sub_ca_certificate
    public static final String TABLE_SUB_CA_CERTIFICATE = "sub_ca_certificate";
    public static final String COL_SUB_CA_CERTIFICATE_ID = "id";
    public static final String COL_SUB_CA_CERTIFICATE_CERTIFICATE = "certificate";
    public static final String COL_SUB_CA_CERTIFICATE_INFORMATION = "information";
    public static final String COL_SUB_CA_CERTIFICATE_ISSUER = "issuer";
    public static final String COL_SUB_CA_CERTIFICATE_APPLY_DATE = "apply_date";
    public static final String COL_SUB_CA_CERTIFICATE_EXPIRY_DATE = "expiry_date";
    public static final String COL_SUB_CA_CERTIFICATE_CREATE_MILLIS = "create_millis";

    // Table system_setting
    public static final String TABLE_SYSTEM_SETTING = "system_setting";
    public static final String COL_SYSTEM_SETTING_ID = "id";
    public static final String COL_SYSTEM_SETTING_CATEGORY = "category";
    public static final String COL_SYSTEM_SETTING_CLASS_NAME = "class_name";
    public static final String COL_SYSTEM_SETTING_GROUP_NAME = "group_name";
    public static final String COL_SYSTEM_SETTING_ITEM = "item";
    public static final String COL_SYSTEM_SETTING_KY = "ky";
    public static final String COL_SYSTEM_SETTING_VAL = "val";
    public static final String COL_SYSTEM_SETTING_SYS_CREATOR = "sys_creator";
    public static final String COL_SYSTEM_SETTING_CREATE_MILLIS = "create_millis";
    public static final String COL_SYSTEM_SETTING_UPDATER = "updater";
    public static final String COL_SYSTEM_SETTING_UPDATE_MILLIS = "update_millis";

    // Table three_d_s_verify_enabled_log
    public static final String TABLE_THREE_D_S_VERIFY_ENABLED_LOG = "three_d_s_verify_enabled_log";
    public static final String COL_THREE_D_S_VERIFY_ENABLED_LOG_ID = "id";
    public static final String COL_THREE_D_S_VERIFY_ENABLED_LOG_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_THREE_D_S_VERIFY_ENABLED_LOG_PAN_ID = "pan_id";
    public static final String COL_THREE_D_S_VERIFY_ENABLED_LOG_ENABLED = "enabled";

    // Table kernel_transaction_log
    public static final String TABLE_KERNEL_TRANSACTION_LOG = "kernel_transaction_log";
    public static final String COL_KERNEL_TRANSACTION_LOG_ID = "id";
    public static final String COL_KERNEL_TRANSACTION_LOG_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_ACS_TRANS_ID = "acs_trans_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_THREE_D_S_SERVER_TRANS_ID = "three_d_s_server_trans_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_THREE_D_S_SERVER_OPERATOR_ID = "three_d_s_server_operator_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_SDK_TRANS_ID = "sdk_trans_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_DS_TRANS_ID = "ds_trans_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_ACS_INTEGRATOR_TRANS_ID = "acs_integrator_trans_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_DEVICE_ID = "device_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_DEVICE_CHANNEL = "device_channel";
    public static final String COL_KERNEL_TRANSACTION_LOG_MESSAGE_VERSION = "message_version";
    public static final String COL_KERNEL_TRANSACTION_LOG_MESSAGE_CATEGORY = "message_category";
    public static final String COL_KERNEL_TRANSACTION_LOG_CARD_BRAND = "card_brand";
    public static final String COL_KERNEL_TRANSACTION_LOG_CARD_TYPE = "card_type";
    public static final String COL_KERNEL_TRANSACTION_LOG_PAN_INFO_ID = "pan_info_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_OTP_PHONE_NUMBER = "otp_phone_number";
    public static final String COL_KERNEL_TRANSACTION_LOG_AUTH_ID = "auth_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_CHALLENGE_ACS_COUNTER_A_TO_S = "challenge_acs_counter_a_to_s";
    public static final String COL_KERNEL_TRANSACTION_LOG_CHALLENGE_VERIFY_COUNT = "challenge_verify_count";
    public static final String COL_KERNEL_TRANSACTION_LOG_CHALLENGE_RESEND_COUNT = "challenge_resend_count";
    public static final String COL_KERNEL_TRANSACTION_LOG_CHALLENGE_PHONE_CORRECT = "challenge_phone_correct";
    public static final String COL_KERNEL_TRANSACTION_LOG_CHALLENGE_COMPLETED = "challenge_completed";
    public static final String COL_KERNEL_TRANSACTION_LOG_ATTEMPT_SETTING_ID = "attempt_setting_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_WHITE_LIST_ID = "white_list_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_MRES_TRANS_STATUS = "mres_trans_status";
    public static final String COL_KERNEL_TRANSACTION_LOG_ARES_RESULT_REASON_CODE = "ares_result_reason_code";
    public static final String COL_KERNEL_TRANSACTION_LOG_CHALLENGE_ERROR_REASON_CODE = "challenge_error_reason_code";
    public static final String COL_KERNEL_TRANSACTION_LOG_RESULT_ERROR_REASON_CODE = "result_error_reason_code";
    public static final String COL_KERNEL_TRANSACTION_LOG_SDK_SESSION_KEY = "sdk_session_key";
    public static final String COL_KERNEL_TRANSACTION_LOG_IP = "ip";
    public static final String COL_KERNEL_TRANSACTION_LOG_BLACK_LIST_IP_GROUP_ID = "black_list_ip_group_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_NOTIFICATION_URL = "notification_url";
    public static final String COL_KERNEL_TRANSACTION_LOG_DS_URL = "ds_url";
    public static final String COL_KERNEL_TRANSACTION_LOG_THREE_D_S_SESSION_DATA = "three_d_s_session_data";
    public static final String COL_KERNEL_TRANSACTION_LOG_THREE_D_S_METHOD_LOG_ID = "three_d_s_method_log_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_AUTHENTICATION_LOG_ID = "authentication_log_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_CHALLENGE_LOG_ID = "challenge_log_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_RESULT_LOG_ID = "result_log_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_ERROR_MESSAGE_LOG_ID = "error_message_log_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_RESPONSE_TIME = "response_time";
    public static final String COL_KERNEL_TRANSACTION_LOG_BROWSER_TYPE = "browser_type";
    public static final String COL_KERNEL_TRANSACTION_LOG_BROWSER_MAJOR_VERSION = "browser_major_version";
    public static final String COL_KERNEL_TRANSACTION_LOG_SMS_OTP_EXPIRE_MILLIS = "sms_otp_expire_millis";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_CHECKED = "rba_checked";
    public static final String COL_KERNEL_TRANSACTION_LOG_DDCA_LOG_ID = "ddca_log_id";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_PURCHASE_AMOUNT_RES = "rba_purchase_amount_res";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_CARDHOLDER_DATA_RES = "rba_cardholder_data_res";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_CUMULATIVE_AMOUNT_RES = "rba_cumulative_amount_res";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_CUMULATIVE_AMOUNT_LOG_AMOUNT = "rba_cumulative_amount_log_amt";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_CUMULATIVE_AMOUNT_LOG_COUNT = "rba_cumulative_amount_log_cnt";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_CUMULATIVE_TX_RES = "rba_cumulative_tx_res";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_LOCATION_CONSISTENCY_RES = "rba_location_consistency_res";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_BROWSER_LANGUAGE_RES = "rba_browser_language_res";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_VPN_RES = "rba_vpn_res";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_PROXY_RES = "rba_proxy_res";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_PRIVATE_BROWSING_RES = "rba_private_browsing_res";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_DEVICE_VARIATION_RES = "rba_device_variation_res";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_MCC_RES = "rba_mcc_res";
    public static final String COL_KERNEL_TRANSACTION_LOG_RBA_RECURRING_PAYMENT_RES = "rba_recurring_payment_res";
    public static final String COL_KERNEL_TRANSACTION_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_KERNEL_TRANSACTION_LOG_CREATE_MILLIS = "create_millis";
    public static final String COL_KERNEL_TRANSACTION_LOG_SYS_UPDATER = "sys_updater";
    public static final String COL_KERNEL_TRANSACTION_LOG_UPDATE_MILLIS = "update_millis";
    public static final String COL_KERNEL_TRANSACTION_LOG_CHALLENGE_OTP_CHANNEL = "challenge_otp_channel";
    public static final String COL_KERNEL_TRANSACTION_LOG_NEED_RESEND_RREQ = "need_resend_rreq";
    public static final String COL_KERNEL_TRANSACTION_LOG_RESEND_RREQ_TIME = "resend_rreq_time";

    // Table user_account
    public static final String TABLE_USER_ACCOUNT = "user_account";
    public static final String COL_USER_ACCOUNT_ID = "id";
    public static final String COL_USER_ACCOUNT_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_USER_ACCOUNT_ACCOUNT = "account";
    public static final String COL_USER_ACCOUNT_CHIP_CARD_NUMBER = "chip_card_number";
    public static final String COL_USER_ACCOUNT_USERNAME = "username";
    public static final String COL_USER_ACCOUNT_PHONE = "phone";
    public static final String COL_USER_ACCOUNT_EMAIL = "email";
    public static final String COL_USER_ACCOUNT_DEPARTMENT = "department";
    public static final String COL_USER_ACCOUNT_EXT = "ext";
    public static final String COL_USER_ACCOUNT_TRY_FAIL_COUNT = "try_fail_count";
    public static final String COL_USER_ACCOUNT_FORGET_MIMA_COUNT = "forget_mima_count";
    public static final String COL_USER_ACCOUNT_SEND_OTP_COUNT = "send_otp_count";
    public static final String COL_USER_ACCOUNT_VERIFY_OTP_COUNT = "verify_otp_count";
    public static final String COL_USER_ACCOUNT_LAST_LOGIN_MILLIS = "last_login_millis";

    // Table user_group
    public static final String TABLE_USER_GROUP = "user_group";
    public static final String COL_USER_GROUP_ID = "id";
    public static final String COL_USER_GROUP_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_USER_GROUP_NAME = "name";
    public static final String COL_USER_GROUP_TYPE = "type";
    public static final String COL_USER_GROUP_SCOPE = "scope";
    public static final String COL_USER_GROUP_MODULE_CAN_SEE_PAN = "moduleCanSeePan";
    public static final String COL_USER_GROUP_CAN_SEE_PAN_QUERY = "canSeePanQuery";
    public static final String COL_USER_GROUP_MODULE_REPORT = "module_report";
    public static final String COL_USER_GROUP_RT_QUERY = "rt_query";
    public static final String COL_USER_GROUP_MODULE_HEALTH_CHECK = "module_health_check";
    public static final String COL_USER_GROUP_HEALTH_CHECK_QUERY = "health_check_query";
    public static final String COL_USER_GROUP_MODULE_TX = "module_tx";
    public static final String COL_USER_GROUP_TX_QUERY = "tx_query";
    public static final String COL_USER_GROUP_MODULE_CARD = "module_card";
    public static final String COL_USER_GROUP_CARD_QUERY = "card_query";
    public static final String COL_USER_GROUP_CARD_MODIFY = "card_modify";
    public static final String COL_USER_GROUP_CARD_AUDIT = "card_audit";
    public static final String COL_USER_GROUP_MODULE_RISK_BLACK_LIST = "module_risk_black_list";
    public static final String COL_USER_GROUP_BLACK_LIST_QUERY = "black_list_query";
    public static final String COL_USER_GROUP_BLACK_LIST_MODIFY = "black_list_modify";
    public static final String COL_USER_GROUP_BLACK_LIST_AUDIT = "black_list_audit";
    public static final String COL_USER_GROUP_MODULE_RISK_WHITE_LIST = "module_risk_white_list";
    public static final String COL_USER_GROUP_WHITE_LIST_QUERY = "white_list_query";
    public static final String COL_USER_GROUP_WHITE_LIST_MODIFY = "white_list_modify";
    public static final String COL_USER_GROUP_WHITE_LIST_AUDIT = "white_list_audit";
    public static final String COL_USER_GROUP_MODULE_RISK_CONTROL = "module_risk_control";
    public static final String COL_USER_GROUP_RISK_CONTROL_QUERY = "risk_control_query";
    public static final String COL_USER_GROUP_RISK_CONTROL_MODIFY = "risk_control_modify";
    public static final String COL_USER_GROUP_RISK_CONTROL_AUDIT = "risk_control_audit";
    public static final String COL_USER_GROUP_MODULE_USER_GROUP = "module_user_group";
    public static final String COL_USER_GROUP_USER_GROUP_QUERY = "user_group_query";
    public static final String COL_USER_GROUP_USER_GROUP_MODIFY = "user_group_modify";
    public static final String COL_USER_GROUP_USER_GROUP_AUDIT = "user_group_audit";
    public static final String COL_USER_GROUP_MODULE_USER_UNLOCK = "module_user_unlock";
    public static final String COL_USER_GROUP_UNLOCK_QUERY = "unlock_query";
    public static final String COL_USER_GROUP_UNLOCK_MODIFY = "unlock_modify";
    public static final String COL_USER_GROUP_UNLOCK_AUDIT = "unlock_audit";
    public static final String COL_USER_GROUP_MODULE_USER_AUDIT_LOG = "module_user_audit_log";
    public static final String COL_USER_GROUP_AUDIT_LOG_QUERY = "audit_log_query";
    public static final String COL_USER_GROUP_MODULE_BANK_MANAGE = "module_bank_manage";
    public static final String COL_USER_GROUP_BANK_MANAGE_QUERY = "bank_manage_query";
    public static final String COL_USER_GROUP_BANK_MANAGE_MODIFY = "bank_manage_modify";
    public static final String COL_USER_GROUP_BANK_MANAGE_AUDIT = "bank_manage_audit";
    public static final String COL_USER_GROUP_MODULE_BANK_LOGO = "module_bank_logo";
    public static final String COL_USER_GROUP_BANK_LOGO_QUERY = "bank_logo_query";
    public static final String COL_USER_GROUP_BANK_LOGO_MODIFY = "bank_logo_modify";
    public static final String COL_USER_GROUP_BANK_LOGO_AUDIT = "bank_logo_audit";
    public static final String COL_USER_GROUP_MODULE_BANK_CHANNEL = "module_bank_channel";
    public static final String COL_USER_GROUP_BANK_CHANNEL_QUERY = "bank_channel_query";
    public static final String COL_USER_GROUP_BANK_CHANNEL_MODIFY = "bank_channel_modify";
    public static final String COL_USER_GROUP_BANK_CHANNEL_AUDIT = "bank_channel_audit";
    public static final String COL_USER_GROUP_MODULE_BANK_FEE = "module_bank_fee";
    public static final String COL_USER_GROUP_BANK_FEE_QUERY = "bank_fee_query";
    public static final String COL_USER_GROUP_BANK_FEE_MODIFY = "bank_fee_modify";
    public static final String COL_USER_GROUP_BANK_FEE_AUDIT = "bank_fee_audit";
    public static final String COL_USER_GROUP_MODULE_BANK_OTP_SENDING = "module_bank_otp_sending";
    public static final String COL_USER_GROUP_BANK_OTP_SENDING_QUERY = "bank_otp_sending_query";
    public static final String COL_USER_GROUP_BANK_OTP_SENDING_MODIFY = "bank_otp_sending_modify";
    public static final String COL_USER_GROUP_BANK_OTP_SENDING_AUDIT = "bank_otp_sending_audit";
    public static final String COL_USER_GROUP_MODULE_SYS_BIN_RANGE = "module_sys_bin_range";
    public static final String COL_USER_GROUP_SYS_BIN_RANGE_QUERY = "sys_bin_range_query";
    public static final String COL_USER_GROUP_SYS_BIN_RANGE_MODIFY = "sys_bin_range_modify";
    public static final String COL_USER_GROUP_SYS_BIN_RANGE_AUDIT = "sys_bin_range_audit";
    public static final String COL_USER_GROUP_MODULE_GENERAL_SETTING = "module_general_setting";
    public static final String COL_USER_GROUP_GENERAL_SETTING_QUERY = "general_setting_query";
    public static final String COL_USER_GROUP_GENERAL_SETTING_MODIFY = "general_setting_modify";
    public static final String COL_USER_GROUP_MODULE_SYS_CARD_LOGO = "module_sys_card_logo";
    public static final String COL_USER_GROUP_SYS_CARD_LOGO_QUERY = "sys_card_logo_query";
    public static final String COL_USER_GROUP_SYS_CARD_LOGO_MODIFY = "sys_card_logo_modify";
    public static final String COL_USER_GROUP_SYS_CARD_LOGO_AUDIT = "sys_card_logo_audit";
    public static final String COL_USER_GROUP_MODULE_SYS_CHALLENGE_VIEW = "module_sys_challenge_view";
    public static final String COL_USER_GROUP_SYS_CHALLENGE_VIEW_QUERY = "sys_challenge_view_query";
    public static final String COL_USER_GROUP_SYS_CHALLENGE_VIEW_MODIFY = "sys_challenge_view_modify";
    public static final String COL_USER_GROUP_SYS_CHALLENGE_VIEW_AUDIT = "sys_challenge_view_audit";
    public static final String COL_USER_GROUP_MODULE_SYS_CHALLENGE_SMS_MSG = "module_sys_challenge_sms_msg";
    public static final String COL_USER_GROUP_SYS_CHALLENGE_SMS_MSG_QUERY = "sys_challenge_sms_msg_query";
    public static final String COL_USER_GROUP_SYS_CHALLENGE_SMS_MSG_MODIFY = "sys_challenge_sms_msg_modify";
    public static final String COL_USER_GROUP_SYS_CHALLENGE_SMS_MSG_AUDIT = "sys_challenge_sms_msg_audit";
    public static final String COL_USER_GROUP_MODULE_SYS_ACS_OPERATOR_ID = "module_sys_acs_operator_id";
    public static final String COL_USER_GROUP_SYS_ACS_OPERATOR_ID_QUERY = "sys_acs_operator_id_query";
    public static final String COL_USER_GROUP_SYS_ACS_OPERATOR_ID_MODIFY = "sys_acs_operator_id_modify";
    public static final String COL_USER_GROUP_SYS_ACS_OPERATOR_ID_AUDIT = "sys_acs_operator_id_audit";
    public static final String COL_USER_GROUP_MODULE_SYS_TIMEOUT = "module_sys_timeout";
    public static final String COL_USER_GROUP_SYS_TIMEOUT_QUERY = "sys_timeout_query";
    public static final String COL_USER_GROUP_SYS_TIMEOUT_MODIFY = "sys_timeout_modify";
    public static final String COL_USER_GROUP_SYS_TIMEOUT_AUDIT = "sys_timeout_audit";
    public static final String COL_USER_GROUP_MODULE_SYS_ERROR_CODE = "module_sys_error_code";
    public static final String COL_USER_GROUP_SYS_ERROR_CODE_QUERY = "sys_error_code_query";
    public static final String COL_USER_GROUP_SYS_ERROR_CODE_MODIFY = "sys_error_code_modify";
    public static final String COL_USER_GROUP_SYS_ERROR_CODE_AUDIT = "sys_error_code_audit";
    public static final String COL_USER_GROUP_MODULE_SYS_KEY = "module_sys_key";
    public static final String COL_USER_GROUP_SYS_KEY_QUERY = "sys_key_query";
    public static final String COL_USER_GROUP_SYS_KEY_MODIFY = "sys_key_modify";
    public static final String COL_USER_GROUP_SYS_KEY_AUDIT = "sys_key_audit";
    public static final String COL_USER_GROUP_MODULE_CERT = "module_cert";
    public static final String COL_USER_GROUP_CERT_QUERY = "cert_query";
    public static final String COL_USER_GROUP_CERT_MODIFY = "cert_modify";
    public static final String COL_USER_GROUP_CERT_AUDIT = "cert_audit";
    public static final String COL_USER_GROUP_MODULE_CLASSIC_RBA = "module_classic_rba";
    public static final String COL_USER_GROUP_CLASSIC_RBA_QUERY = "classic_rba_query";
    public static final String COL_USER_GROUP_CLASSIC_RBA_MODIFY = "classic_rba_modify";
    public static final String COL_USER_GROUP_CLASSIC_RBA_AUDIT = "classic_rba_audit";
    public static final String COL_USER_GROUP_ACCESS_MULTI_BANK = "access_multi_bank";

    public static final String COL_USER_GROUP_MODULE_PLUGIN_ISSUER_PROPERTY = "module_plugin_issuer_property";
    public static final String COL_USER_GROUP_PLUGIN_ISSUER_PROPERTY_QUERY = "plugin_issuer_property_query";
    public static final String COL_USER_GROUP_PLUGIN_ISSUER_PROPERTY_MODIFY = "plugin_issuer_property_modify";
    public static final String COL_USER_GROUP_MODULE_MIMA_POLICY = "module_mima_policy";
    public static final String COL_USER_GROUP_MIMA_POLICY_QUERY = "mima_policy_query";
    public static final String COL_USER_GROUP_MIMA_POLICY_MODIFY = "mima_policy_modify";
    public static final String COL_USER_GROUP_MODULE_ACQUIRER_BANK = "module_acquirer_bank";
    public static final String COL_USER_GROUP_ACQUIRER_BANK_QUERY = "acquirer_bank_query";
    public static final String COL_USER_GROUP_ACQUIRER_BANK_MODIFY = "acquirer_bank_modify";
    public static final String COL_USER_GROUP_MODULE_BANK_DATA_KEY = "module_bank_data_key";
    public static final String COL_USER_GROUP_BANK_DATA_KEY_QUERY = "bank_data_key_query";
    public static final String COL_USER_GROUP_BANK_DATA_KEY_MODIFY = "bank_data_key_modify";
    public static final String COL_USER_GROUP_MODULE_VELOG = "module_velog";
    public static final String COL_USER_GROUP_VELOG_QUERY = "velog_query";

    // Table white_list_attempt_authorize
    public static final String TABLE_WHITE_LIST_ATTEMPT_AUTHORIZE = "white_list_attempt_authorize";
    public static final String COL_WHITE_LIST_ATTEMPT_AUTHORIZE_ID = "id";
    public static final String COL_WHITE_LIST_ATTEMPT_AUTHORIZE_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_WHITE_LIST_ATTEMPT_AUTHORIZE_LDAP_GROUP = "ldap_group";
    public static final String COL_WHITE_LIST_ATTEMPT_AUTHORIZE_MAX_AMOUNT = "max_amount";

    // Table white_list_attempt_setting
    public static final String TABLE_WHITE_LIST_ATTEMPT_SETTING = "white_list_attempt_setting";
    public static final String COL_WHITE_LIST_ATTEMPT_SETTING_ID = "id";
    public static final String COL_WHITE_LIST_ATTEMPT_SETTING_PAN_ID = "pan_id";
    public static final String COL_WHITE_LIST_ATTEMPT_SETTING_AMOUNT = "amount";
    public static final String COL_WHITE_LIST_ATTEMPT_SETTING_CURRENCY = "currency";
    public static final String COL_WHITE_LIST_ATTEMPT_SETTING_TRIES_PERMITTED = "tries_permitted";
    public static final String COL_WHITE_LIST_ATTEMPT_SETTING_TRIES_QUOTA = "tries_quota";
    public static final String COL_WHITE_LIST_ATTEMPT_SETTING_ATTEMPT_EXPIRE_TIME = "attempt_expire_time";

    // Table white_list_pan
    public static final String TABLE_WHITE_LIST_PAN = "white_list_pan";
    public static final String COL_WHITE_LIST_PAN_ID = "id";
    public static final String COL_WHITE_LIST_PAN_PAN_ID = "pan_id";

    // Table authentication_log
    public static final String TABLE_AUTHENTICATION_LOG = "authentication_log";
    public static final String COL_AUTHENTICATION_LOG_ID = "id";
    public static final String COL_AUTHENTICATION_LOG_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_AUTHENTICATION_LOG_THREE_D_S_COMP_IND = "three_d_s_comp_ind";
    public static final String COL_AUTHENTICATION_LOG_THREE_D_S_REQ_AUTH_IND = "three_d_s_req_auth_ind";
    public static final String COL_AUTHENTICATION_LOG_TDSRAI_THR_D_S_REQ_A_DATA = "tdsrai_thr_d_s_req_a_data";
    public static final String COL_AUTHENTICATION_LOG_TDSRAI_THR_D_S_REQ_A_METHOD = "tdsrai_thr_d_s_req_a_method";
    public static final String COL_AUTHENTICATION_LOG_TDSRAI_THR_D_S_REQ_A_TIMESTAMP = "tdsrai_thr_d_s_req_a_timestamp";
    public static final String COL_AUTHENTICATION_LOG_THREE_D_S_REQ_AUTH_METHOD_IND = "three_d_s_req_auth_method_ind";
    public static final String COL_AUTHENTICATION_LOG_THREE_D_S_REQ_CHALLENGE_IND = "three_d_s_req_challenge_ind";
    public static final String COL_AUTHENTICATION_LOG_THREE_D_S_REQ_DEC_MAX_TIME = "three_d_s_req_dec_max_time";
    public static final String COL_AUTHENTICATION_LOG_THREE_D_S_REQ_DEC_REQ_IND = "three_d_s_req_dec_req_ind";
    public static final String COL_AUTHENTICATION_LOG_THREE_D_S_REQ_ID = "three_d_s_req_id";
    public static final String COL_AUTHENTICATION_LOG_THREE_D_S_REQ_NAME = "three_d_s_req_name";
    public static final String COL_AUTHENTICATION_LOG_TDSRPAI_THR_D_S_REQ_PA_DATA = "tdsrpai_thr_d_s_req_pa_data";
    public static final String COL_AUTHENTICATION_LOG_TDSRPAI_THR_D_S_REQ_PA_METHOD = "tdsrpai_thr_d_s_req_pa_method";
    public static final String COL_AUTHENTICATION_LOG_TDSRPAI_THR_D_S_REQ_PA_T_STAMP = "tdsrpai_thr_d_s_req_pa_t_stamp";
    public static final String COL_AUTHENTICATION_LOG_TDSRPAI_THR_D_S_REQ_P_REF = "tdsrpai_thr_d_s_req_p_ref";
    public static final String COL_AUTHENTICATION_LOG_THREE_D_S_REQ_URL = "three_d_s_req_url";
    public static final String COL_AUTHENTICATION_LOG_THREE_D_S_SERVER_REF_NUMBER = "three_d_s_server_ref_number";
    public static final String COL_AUTHENTICATION_LOG_THREE_D_S_SERVER_URL = "three_d_s_server_url";
    public static final String COL_AUTHENTICATION_LOG_ACCT_TYPE = "acct_type";
    public static final String COL_AUTHENTICATION_LOG_ACQUIRER_B_I_N = "acquirer_b_i_n";
    public static final String COL_AUTHENTICATION_LOG_ACQUIRER_MERCHANT_ID = "acquirer_merchant_id";
    public static final String COL_AUTHENTICATION_LOG_ADDR_MATCH = "addr_match";
    public static final String COL_AUTHENTICATION_LOG_BROAD_INFO = "broad_info";
    public static final String COL_AUTHENTICATION_LOG_CARD_EXPIRY_DATE = "card_expiry_date";
    public static final String COL_AUTHENTICATION_LOG_AIF_CH_ACC_AGE_IND = "aif_ch_acc_age_ind";
    public static final String COL_AUTHENTICATION_LOG_AIF_CH_ACC_CHANGE = "aif_ch_acc_change";
    public static final String COL_AUTHENTICATION_LOG_AIF_CH_ACC_CHANGE_IND = "aif_ch_acc_change_ind";
    public static final String COL_AUTHENTICATION_LOG_AIF_CH_ACC_DATE = "aif_ch_acc_date";
    public static final String COL_AUTHENTICATION_LOG_AIF_CH_ACC_PW_CHANGE = "aif_ch_acc_pw_change";
    public static final String COL_AUTHENTICATION_LOG_AIF_CH_ACC_PW_CHANGE_IND = "aif_ch_acc_pw_change_ind";
    public static final String COL_AUTHENTICATION_LOG_AIF_NB_PURCHASE_ACCOUNT = "aif_nb_purchase_account";
    public static final String COL_AUTHENTICATION_LOG_AIF_PROVISION_ATTEMPTS_DAY = "aif_provision_attempts_day";
    public static final String COL_AUTHENTICATION_LOG_AIF_TXN_ACTIVITY_DAY = "aif_txn_activity_day";
    public static final String COL_AUTHENTICATION_LOG_AIF_TXN_ACTIVITY_YEAR = "aif_txn_activity_year";
    public static final String COL_AUTHENTICATION_LOG_AIF_PAYMENT_ACC_AGE = "aif_payment_acc_age";
    public static final String COL_AUTHENTICATION_LOG_AIF_PAYMENT_ACC_IND = "aif_payment_acc_ind";
    public static final String COL_AUTHENTICATION_LOG_AIF_SHIP_ADDRESS_USAGE = "aif_ship_address_usage";
    public static final String COL_AUTHENTICATION_LOG_AIF_SHIP_ADDRESS_USAGE_IND = "aif_ship_address_usage_ind";
    public static final String COL_AUTHENTICATION_LOG_AIF_SHIP_NAME_INDICATOR = "aif_ship_name_indicator";
    public static final String COL_AUTHENTICATION_LOG_AIF_SUSPICIOUS_ACC_ACTIVITY = "aif_suspicious_acc_activity";
    public static final String COL_AUTHENTICATION_LOG_ACCT_ID = "acct_id";
    public static final String COL_AUTHENTICATION_LOG_BILL_ADDR_CITY = "bill_addr_city";
    public static final String COL_AUTHENTICATION_LOG_BILL_ADDR_COUNTRY = "bill_addr_country";
    public static final String COL_AUTHENTICATION_LOG_BILL_ADDR_LINE_1 = "bill_addr_line_1";
    public static final String COL_AUTHENTICATION_LOG_BILL_ADDR_LINE_1_EN = "bill_addr_line_1_en";
    public static final String COL_AUTHENTICATION_LOG_BILL_ADDR_LINE_2 = "bill_addr_line_2";
    public static final String COL_AUTHENTICATION_LOG_BILL_ADDR_LINE_2_EN = "bill_addr_line_2_en";
    public static final String COL_AUTHENTICATION_LOG_BILL_ADDR_LINE_3 = "bill_addr_line_3";
    public static final String COL_AUTHENTICATION_LOG_BILL_ADDR_LINE_3_EN = "bill_addr_line_3_en";
    public static final String COL_AUTHENTICATION_LOG_BILL_ADDR_POST_CODE = "bill_addr_post_code";
    public static final String COL_AUTHENTICATION_LOG_BILL_ADDR_STATE = "bill_addr_state";
    public static final String COL_AUTHENTICATION_LOG_EMAIL = "email";
    public static final String COL_AUTHENTICATION_LOG_HP_CC = "hp_cc";
    public static final String COL_AUTHENTICATION_LOG_HP_SUBSCRIBER = "hp_subscriber";
    public static final String COL_AUTHENTICATION_LOG_HP_SUBSCRIBER_EN = "hp_subscriber_en";
    public static final String COL_AUTHENTICATION_LOG_WP_CC = "wp_cc";
    public static final String COL_AUTHENTICATION_LOG_WP_SUBSCRIBER = "wp_subscriber";
    public static final String COL_AUTHENTICATION_LOG_WP_SUBSCRIBER_EN = "wp_subscriber_en";
    public static final String COL_AUTHENTICATION_LOG_MP_CC = "mp_cc";
    public static final String COL_AUTHENTICATION_LOG_MP_SUBSCRIBER = "mp_subscriber";
    public static final String COL_AUTHENTICATION_LOG_MP_SUBSCRIBER_EN = "mp_subscriber_en";
    public static final String COL_AUTHENTICATION_LOG_CARDHOLDER_NAME = "cardholder_name";
    public static final String COL_AUTHENTICATION_LOG_CARDHOLDER_NAME_EN = "cardholder_name_en";
    public static final String COL_AUTHENTICATION_LOG_SHIP_ADDR_CITY = "ship_addr_city";
    public static final String COL_AUTHENTICATION_LOG_SHIP_ADDR_COUNTRY = "ship_addr_country";
    public static final String COL_AUTHENTICATION_LOG_SHIP_ADDR_LINE_1 = "ship_addr_line_1";
    public static final String COL_AUTHENTICATION_LOG_SHIP_ADDR_LINE_1_EN = "ship_addr_line_1_en";
    public static final String COL_AUTHENTICATION_LOG_SHIP_ADDR_LINE_2 = "ship_addr_line_2";
    public static final String COL_AUTHENTICATION_LOG_SHIP_ADDR_LINE_2_EN = "ship_addr_line_2_en";
    public static final String COL_AUTHENTICATION_LOG_SHIP_ADDR_LINE_3 = "ship_addr_line_3";
    public static final String COL_AUTHENTICATION_LOG_SHIP_ADDR_LINE_3_EN = "ship_addr_line_3_en";
    public static final String COL_AUTHENTICATION_LOG_SHIP_ADDR_POST_CODE = "ship_addr_post_code";
    public static final String COL_AUTHENTICATION_LOG_SHIP_ADDR_STATE = "ship_addr_state";
    public static final String COL_AUTHENTICATION_LOG_DS_REFERENCE_NUMBER = "ds_reference_number";
    public static final String COL_AUTHENTICATION_LOG_PAY_TOKEN_IND = "pay_token_ind";
    public static final String COL_AUTHENTICATION_LOG_PAY_TOKEN_SOURCE = "pay_token_source";
    public static final String COL_AUTHENTICATION_LOG_PURCHASE_INSTAL_DATA = "purchase_instal_data";
    public static final String COL_AUTHENTICATION_LOG_MCC = "mcc";
    public static final String COL_AUTHENTICATION_LOG_MERCHANT_COUNTRY_CODE = "merchant_country_code";
    public static final String COL_AUTHENTICATION_LOG_MERCHANT_NAME = "merchant_name";
    public static final String COL_AUTHENTICATION_LOG_MRI_DELIVERY_EMAIL_ADDRESS = "mri_delivery_email_address";
    public static final String COL_AUTHENTICATION_LOG_MRI_DELIVERY_TIMEFRAME = "mri_delivery_timeframe";
    public static final String COL_AUTHENTICATION_LOG_MRI_GIFT_CARD_AMOUNT = "mri_gift_card_amount";
    public static final String COL_AUTHENTICATION_LOG_MRI_GIFT_CARD_COUNT = "mri_gift_card_count";
    public static final String COL_AUTHENTICATION_LOG_MRI_GIFT_CARD_CURR = "mri_gift_card_curr";
    public static final String COL_AUTHENTICATION_LOG_MRI_PRE_ORDER_DATE = "mri_pre_order_date";
    public static final String COL_AUTHENTICATION_LOG_MRI_PRE_ORDER_PURCHASE_IND = "mri_pre_order_purchase_ind";
    public static final String COL_AUTHENTICATION_LOG_MRI_REORDER_ITEMS_IND = "mri_reorder_items_ind";
    public static final String COL_AUTHENTICATION_LOG_MRI_SHIP_INDICATOR = "mri_ship_indicator";
    public static final String COL_AUTHENTICATION_LOG_MESSAGE_CATEGORY = "message_category";
    public static final String COL_AUTHENTICATION_LOG_MESSAGE_VERSION = "message_version";
    public static final String COL_AUTHENTICATION_LOG_PURCHASE_CURRENCY = "purchase_currency";
    public static final String COL_AUTHENTICATION_LOG_PURCHASE_AMOUNT = "purchase_amount";
    public static final String COL_AUTHENTICATION_LOG_PURCHASE_EXPONENT = "purchase_exponent";
    public static final String COL_AUTHENTICATION_LOG_PURCHASE_DATE = "purchase_date";
    public static final String COL_AUTHENTICATION_LOG_RECURRING_EXPIRY = "recurring_expiry";
    public static final String COL_AUTHENTICATION_LOG_RECURRING_FREQUENCY = "recurring_frequency";
    public static final String COL_AUTHENTICATION_LOG_TRANS_TYPE = "trans_type";
    public static final String COL_AUTHENTICATION_LOG_WHITE_LIST_STATUS = "white_list_status";
    public static final String COL_AUTHENTICATION_LOG_WHITE_LIST_STATUS_SOURCE = "white_list_status_source";
    public static final String COL_AUTHENTICATION_LOG_ACS_CHALLENGE_MANDATED = "acs_challenge_mandated";
    public static final String COL_AUTHENTICATION_LOG_ACS_DEC_CON_IND = "acs_dec_con_ind";
    public static final String COL_AUTHENTICATION_LOG_ACS_OPERATOR_ID = "acs_operator_id";
    public static final String COL_AUTHENTICATION_LOG_ACS_REFERENCE_NUMBER = "acs_reference_number";
    public static final String COL_AUTHENTICATION_LOG_ART_ACS_INTERFACE = "art_acs_interface";
    public static final String COL_AUTHENTICATION_LOG_ART_ACS_UI_TEMPLATE = "art_acs_ui_template";
    public static final String COL_AUTHENTICATION_LOG_ACS_SIGNED_CONTENT = "acs_signed_content";
    public static final String COL_AUTHENTICATION_LOG_ACS_URL = "acs_url";
    public static final String COL_AUTHENTICATION_LOG_AUTHENTICATION_TYPE = "authentication_type";
    public static final String COL_AUTHENTICATION_LOG_AUTHENTICATION_VALUE_EN = "authentication_value_en";
    public static final String COL_AUTHENTICATION_LOG_CARDHOLDER_INFO = "cardholder_info";
    public static final String COL_AUTHENTICATION_LOG_ECI = "eci";
    public static final String COL_AUTHENTICATION_LOG_TRANS_STATUS = "trans_status";
    public static final String COL_AUTHENTICATION_LOG_TRANS_STATUS_REASON = "trans_status_reason";
    public static final String COL_AUTHENTICATION_LOG_DEVICE_CHANNEL = "device_channel";
    public static final String COL_AUTHENTICATION_LOG_BROWSER_ACCEPT_HEADER = "browser_accept_header";
    public static final String COL_AUTHENTICATION_LOG_BROWSER_IP = "browser_ip";
    public static final String COL_AUTHENTICATION_LOG_BROWSER_JAVA_ENABLED = "browser_java_enabled";
    public static final String COL_AUTHENTICATION_LOG_BROWSER_JAVASCRIPT_ENABLED = "browser_javascript_enabled";
    public static final String COL_AUTHENTICATION_LOG_BROWSER_LANGUAGE = "browser_language";
    public static final String COL_AUTHENTICATION_LOG_BROWSER_COLOR_DEPTH = "browser_color_depth";
    public static final String COL_AUTHENTICATION_LOG_BROWSER_SCREEN_HEIGHT = "browser_screen_height";
    public static final String COL_AUTHENTICATION_LOG_BROWSER_SCREEN_WIDTH = "browser_screen_width";
    public static final String COL_AUTHENTICATION_LOG_BROWSER_TZ = "browser_tz";
    public static final String COL_AUTHENTICATION_LOG_BROWSER_PRIVATE_MODE = "browser_private_mode";
    public static final String COL_AUTHENTICATION_LOG_BROWSER_USER_AGENT = "browser_user_agent";
    public static final String COL_AUTHENTICATION_LOG_DEVICE_INFO_EN = "device_info_en";
    public static final String COL_AUTHENTICATION_LOG_DRO_SDK_INTERFACE = "dro_sdk_interface";
    public static final String COL_AUTHENTICATION_LOG_SDK_APP_ID = "sdk_app_id";
    public static final String COL_AUTHENTICATION_LOG_SDK_ENC_DATA = "sdk_enc_data";
    public static final String COL_AUTHENTICATION_LOG_SDK_MAX_TIMEOUT = "sdk_max_timeout";
    public static final String COL_AUTHENTICATION_LOG_SDK_REFERENCE_NUMBER = "sdk_reference_number";
    public static final String COL_AUTHENTICATION_LOG_THREE_R_I_IND = "three_r_i_ind";
    public static final String COL_AUTHENTICATION_LOG_ARES_BROAD_INFO = "ares_broad_info";
    public static final String COL_AUTHENTICATION_LOG_ARES_WHITE_LIST_STATUS = "ares_white_list_status";
    public static final String COL_AUTHENTICATION_LOG_ARES_WHITE_LIST_STATUS_SOURCE = "ares_white_list_status_source";
    public static final String COL_AUTHENTICATION_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_AUTHENTICATION_LOG_CREATE_MILLIS = "create_millis";
    public static final String COL_AUTHENTICATION_LOG_SYS_UPDATER = "sys_updater";
    public static final String COL_AUTHENTICATION_LOG_UPDATE_MILLIS = "update_millis";

    // Table authentication_me_log
    public static final String TABLE_AUTHENTICATION_ME_LOG = "authentication_me_log";
    public static final String COL_AUTHENTICATION_ME_LOG_ID = "id";
    public static final String COL_AUTHENTICATION_ME_LOG_AUTHENTICATION_LOG_ID = "authentication_log_id";
    public static final String COL_AUTHENTICATION_ME_LOG_MESSAGE_TYPE = "message_type";
    public static final String COL_AUTHENTICATION_ME_LOG_CRITICALITY_INDICATOR = "criticality_indicator";
    public static final String COL_AUTHENTICATION_ME_LOG_DATA_BODY = "data_body";
    public static final String COL_AUTHENTICATION_ME_LOG_ME_ID = "me_id";
    public static final String COL_AUTHENTICATION_ME_LOG_NAME = "name";
    public static final String COL_AUTHENTICATION_ME_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_AUTHENTICATION_ME_LOG_CREATE_MILLIS = "create_millis";

    // Table challenge_code_log
    public static final String TABLE_CHALLENGE_CODE_LOG = "challenge_code_log";
    public static final String COL_CHALLENGE_CODE_LOG_ID = "id";
    public static final String COL_CHALLENGE_CODE_LOG_CHALLENGE_LOG_ID = "challenge_log_id";
    public static final String COL_CHALLENGE_CODE_LOG_VERIFY_CODE = "verify_code";
    public static final String COL_CHALLENGE_CODE_LOG_VERIFY_STATUS = "verify_status";
    public static final String COL_CHALLENGE_CODE_LOG_AUTH_ID = "auth_id";
    public static final String COL_CHALLENGE_CODE_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_CHALLENGE_CODE_LOG_CREATE_MILLIS = "create_millis";

    public static final String TABLE_TRANSACTION_TIMER = "transaction_timer";
    public static final String COL_TRANSACTION_TIMER_ID = "id";
    public static final String COL_TRANSACTION_TIMER_TIMER_TYPE = "timer_type";
    public static final String COL_TRANSACTION_TIMER_KERNEL_TRANSACTION_LOG_ID = "kernel_transaction_log_id";
    public static final String COL_TRANSACTION_TIMER_EXPIRE_MILLIS = "expire_millis";
    public static final String COL_TRANSACTION_TIMER_SYS_CREATOR = "sys_creator";
    public static final String COL_TRANSACTION_TIMER_CREATE_MILLIS = "create_millis";
    public static final String COL_TRANSACTION_TIMER_SYS_UPDATER = "sys_updater";
    public static final String COL_TRANSACTION_TIMER_UPDATE_MILLIS = "update_millis";

    // Table challenge_log
    public static final String TABLE_CHALLENGE_LOG = "challenge_log";
    public static final String COL_CHALLENGE_LOG_ID = "id";
    public static final String COL_CHALLENGE_LOG_KERNEL_TRANSACTION_LOG_ID = "kernel_transaction_log_id";
    public static final String COL_CHALLENGE_LOG_CHALLENGE_CANCEL = "challenge_cancel";
    public static final String COL_CHALLENGE_LOG_CHALLENGE_NO_ENTRY = "challenge_no_entry";
    public static final String COL_CHALLENGE_LOG_CHALLENGE_WINDOW_SIZE = "challenge_window_size";
    public static final String COL_CHALLENGE_LOG_MESSAGE_VERSION = "message_version";
    public static final String COL_CHALLENGE_LOG_TRANS_STATUS = "trans_status";
    public static final String COL_CHALLENGE_LOG_THREE_D_S_REQUESTOR_APP_URL = "three_d_s_requestor_app_url";
    public static final String COL_CHALLENGE_LOG_OOB_CONTINUE = "oob_continue";
    public static final String COL_CHALLENGE_LOG_RESEND_CHALLENGE = "resend_challenge";
    public static final String COL_CHALLENGE_LOG_ACS_COUNTER_A_TO_S = "acs_counter_a_to_s";
    public static final String COL_CHALLENGE_LOG_SDK_COUNTER_S_TO_A = "sdk_counter_s_to_a";
    public static final String COL_CHALLENGE_LOG_ACS_HTML = "acs_html";
    public static final String COL_CHALLENGE_LOG_ACS_UI_TYPE = "acs_ui_type";
    public static final String COL_CHALLENGE_LOG_CHALLENGE_ADD_INFO = "challenge_add_info";
    public static final String COL_CHALLENGE_LOG_CHALLENGE_COMPLETION_IND = "challenge_completion_ind";
    public static final String COL_CHALLENGE_LOG_CHALLENGE_INFO_HEADER = "challenge_info_header";
    public static final String COL_CHALLENGE_LOG_CHALLENGE_INFO_LABEL = "challenge_info_label";
    public static final String COL_CHALLENGE_LOG_CHALLENGE_INFO_TEXT = "challenge_info_text";
    public static final String COL_CHALLENGE_LOG_CHALLENGE_INFO_TEXT_INDICATOR = "challenge_info_text_indicator";
    public static final String COL_CHALLENGE_LOG_EXPAND_INFO_LABEL = "expand_info_label";
    public static final String COL_CHALLENGE_LOG_EXPAND_INFO_TEXT = "expand_info_text";
    public static final String COL_CHALLENGE_LOG_II_MEDIUM = "ii_medium";
    public static final String COL_CHALLENGE_LOG_II_HIGH = "ii_high";
    public static final String COL_CHALLENGE_LOG_II_EXTRA_HIGH = "ii_extra_high";
    public static final String COL_CHALLENGE_LOG_OOB_APP_URL = "oob_app_url";
    public static final String COL_CHALLENGE_LOG_OOB_APP_LABEL = "oob_app_label";
    public static final String COL_CHALLENGE_LOG_OOB_CONTINUE_LABEL = "oob_continue_label";
    public static final String COL_CHALLENGE_LOG_PI_MEDIUM = "pi_medium";
    public static final String COL_CHALLENGE_LOG_PI_HIGH = "pi_high";
    public static final String COL_CHALLENGE_LOG_PI_EXTRA_HIGH = "pi_extra_high";
    public static final String COL_CHALLENGE_LOG_RESEND_INFORMATION_LABEL = "resend_information_label";
    public static final String COL_CHALLENGE_LOG_SUBMIT_AUTHENTICATION_LABEL = "submit_authentication_label";
    public static final String COL_CHALLENGE_LOG_WHY_INFO_LABEL = "why_info_label";
    public static final String COL_CHALLENGE_LOG_WHY_INFO_TEXT = "why_info_text";
    public static final String COL_CHALLENGE_LOG_WHITELISTING_DATA_ENTRY = "whitelisting_data_entry";
    public static final String COL_CHALLENGE_LOG_WHITELISTING_INFO_TEXT = "whitelisting_info_text";
    public static final String COL_CHALLENGE_LOG_CRES_MESSAGE_VERSION = "cres_message_version";
    public static final String COL_CHALLENGE_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_CHALLENGE_LOG_CREATE_MILLIS = "create_millis";
    public static final String COL_CHALLENGE_LOG_SYS_UPDATER = "sys_updater";
    public static final String COL_CHALLENGE_LOG_UPDATE_MILLIS = "update_millis";

    // Table challenge_me_log
    public static final String TABLE_CHALLENGE_ME_LOG = "challenge_me_log";
    public static final String COL_CHALLENGE_ME_LOG_ID = "id";
    public static final String COL_CHALLENGE_ME_LOG_CHALLENGE_LOG_ID = "challenge_log_id";
    public static final String COL_CHALLENGE_ME_LOG_MESSAGE_TYPE = "message_type";
    public static final String COL_CHALLENGE_ME_LOG_CRITICALITY_INDICATOR = "criticality_indicator";
    public static final String COL_CHALLENGE_ME_LOG_DATA_BODY = "data_body";
    public static final String COL_CHALLENGE_ME_LOG_ME_ID = "me_id";
    public static final String COL_CHALLENGE_ME_LOG_NAME = "name";
    public static final String COL_CHALLENGE_ME_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_CHALLENGE_ME_LOG_CREATE_MILLIS = "create_millis";

    // Table challenge_select_info_log
    public static final String TABLE_CHALLENGE_SELECT_INFO_LOG = "challenge_select_info_log";
    public static final String COL_CHALLENGE_SELECT_INFO_LOG_ID = "id";
    public static final String COL_CHALLENGE_SELECT_INFO_LOG_CHALLENGE_LOG_ID = "challenge_log_id";
    public static final String COL_CHALLENGE_SELECT_INFO_LOG_KY = "ky";
    public static final String COL_CHALLENGE_SELECT_INFO_LOG_VAL = "val";
    public static final String COL_CHALLENGE_SELECT_INFO_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_CHALLENGE_SELECT_INFO_LOG_CREATE_MILLIS = "create_millis";

    // Table error_message_log
    public static final String TABLE_ERROR_MESSAGE_LOG = "error_message_log";
    public static final String COL_ERROR_MESSAGE_LOG_ID = "id";
    public static final String COL_ERROR_MESSAGE_LOG_ERROR_CODE = "error_code";
    public static final String COL_ERROR_MESSAGE_LOG_ERROR_COMPONENT = "error_component";
    public static final String COL_ERROR_MESSAGE_LOG_ERROR_DESCRIPTION = "error_description";
    public static final String COL_ERROR_MESSAGE_LOG_ERROR_DETAIL = "error_detail";
    public static final String COL_ERROR_MESSAGE_LOG_ERROR_MESSAGE_TYPE = "error_message_type";
    public static final String COL_ERROR_MESSAGE_LOG_MESSAGE_TYPE = "message_type";
    public static final String COL_ERROR_MESSAGE_LOG_MESSAGE_VERSION = "message_version";
    public static final String COL_ERROR_MESSAGE_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_ERROR_MESSAGE_LOG_CREATE_MILLIS = "create_millis";
    public static final String COL_ERROR_MESSAGE_LOG_UPDATER = "updater";
    public static final String COL_ERROR_MESSAGE_LOG_UPDATE_MILLIS = "update_millis";

    // Table result_log
    public static final String TABLE_RESULT_LOG = "result_log";
    public static final String COL_RESULT_LOG_ID = "id";
    public static final String COL_RESULT_LOG_ART_ACS_INTERFACE = "art_acs_interface";
    public static final String COL_RESULT_LOG_ART_ACS_UI_TEMPLATE = "art_acs_ui_template";
    public static final String COL_RESULT_LOG_AUTHENTICATION_METHOD = "authentication_method";
    public static final String COL_RESULT_LOG_AUTHENTICATION_TYPE = "authentication_type";
    public static final String COL_RESULT_LOG_AUTHENTICATION_VALUE_EN = "authentication_value_en";
    public static final String COL_RESULT_LOG_CHALLENGE_CANCEL = "challenge_cancel";
    public static final String COL_RESULT_LOG_ECI = "eci";
    public static final String COL_RESULT_LOG_INTERACTION_COUNTER = "interaction_counter";
    public static final String COL_RESULT_LOG_MESSAGE_CATEGORY = "message_category";
    public static final String COL_RESULT_LOG_MESSAGE_VERSION = "message_version";
    public static final String COL_RESULT_LOG_TRANS_STATUS = "trans_status";
    public static final String COL_RESULT_LOG_TRANS_STATUS_REASON = "trans_status_reason";
    public static final String COL_RESULT_LOG_RESULTS_STATUS = "results_status";
    public static final String COL_RESULT_LOG_WHITE_LIST_STATUS = "white_list_status";
    public static final String COL_RESULT_LOG_WHITE_LIST_STATUS_SOURCE = "white_list_status_source";
    public static final String COL_RESULT_LOG_RRES_MESSAGE_VERSION = "rres_message_version";
    public static final String COL_RESULT_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_RESULT_LOG_CREATE_MILLIS = "create_millis";
    public static final String COL_RESULT_LOG_SYS_UPDATER = "sys_updater";
    public static final String COL_RESULT_LOG_UPDATE_MILLIS = "update_millis";

    // Table result_me_log
    public static final String TABLE_RESULT_ME_LOG = "result_me_log";
    public static final String COL_RESULT_ME_LOG_ID = "id";
    public static final String COL_RESULT_ME_LOG_RESULT_LOG_ID = "result_log_id";
    public static final String COL_RESULT_ME_LOG_MESSAGE_TYPE = "message_type";
    public static final String COL_RESULT_ME_LOG_CRITICALITY_INDICATOR = "criticality_indicator";
    public static final String COL_RESULT_ME_LOG_DATA_BODY = "data_body";
    public static final String COL_RESULT_ME_LOG_ME_ID = "me_id";
    public static final String COL_RESULT_ME_LOG_NAME = "name";
    public static final String COL_RESULT_ME_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_RESULT_ME_LOG_CREATE_MILLIS = "create_millis";

    // Table sdk_ui_type_log
    public static final String TABLE_SDK_UI_TYPE_LOG = "sdk_ui_type_log";
    public static final String COL_SDK_UI_TYPE_LOG_ID = "id";
    public static final String COL_SDK_UI_TYPE_LOG_AUTHENTICATION_LOG_ID = "authentication_log_id";
    public static final String COL_SDK_UI_TYPE_LOG_SDK_UI_TYPE = "sdk_ui_type";
    public static final String COL_SDK_UI_TYPE_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_SDK_UI_TYPE_LOG_CREATE_MILLIS = "create_millis";

    // Table three_d_s_method_log
    public static final String TABLE_THREE_D_S_METHOD_LOG = "three_d_s_method_log";
    public static final String COL_THREE_D_S_METHOD_LOG_ID = "id";
    public static final String COL_THREE_D_S_METHOD_LOG_THREE_D_S_METHOD_NOTIFY_URL = "three_d_s_method_notify_url";
    public static final String COL_THREE_D_S_METHOD_LOG_BROWSER_ACCEPT_HEADER = "browser_accept_header";
    public static final String COL_THREE_D_S_METHOD_LOG_BROWSER_ACCEPT_AGENT = "browser_accept_agent";
    public static final String COL_THREE_D_S_METHOD_LOG_BROWSER_JAVA_ENABLED = "browser_java_enabled";
    public static final String COL_THREE_D_S_METHOD_LOG_BROWSER_LANGUAGE = "browser_language";
    public static final String COL_THREE_D_S_METHOD_LOG_BROWSER_COLOR_DEPTH = "browser_color_depth";
    public static final String COL_THREE_D_S_METHOD_LOG_BROWSER_SCREEN_HEIGHT = "browser_screen_height";
    public static final String COL_THREE_D_S_METHOD_LOG_BROWSER_SCREEN_WIDTH = "browser_screen_width";
    public static final String COL_THREE_D_S_METHOD_LOG_BROWSER_T_Z = "browser_t_z";
    public static final String COL_THREE_D_S_METHOD_LOG_BROWSER_PRIVATE_MODE = "browser_private_mode";
    public static final String COL_THREE_D_S_METHOD_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_THREE_D_S_METHOD_LOG_CREATE_MILLIS = "create_millis";
    public static final String COL_THREE_D_S_METHOD_LOG_SYS_UPDATER = "sys_updater";
    public static final String COL_THREE_D_S_METHOD_LOG_UPDATE_MILLIS = "update_millis";

    // Table ddca_log
    public static final String TABLE_DDCA_LOG = "ddca_log";
    public static final String COL_DDCA_LOG_ID = "id";
    public static final String COL_DDCA_LOG_TX_REPORT = "tx_report";
    public static final String COL_DDCA_LOG_DEVICE_ID = "device_id";
    public static final String COL_DDCA_LOG_IS_IN_PRIVATE_BROWSING_MODE = "is_in_private_browsing_mode";
    public static final String COL_DDCA_LOG_BROWSER_TIMEZONE_COUNTRY = "browser_timezone_country";
    public static final String COL_DDCA_LOG_IP_COUNTRY = "ip_country";
    public static final String COL_DDCA_LOG_BROWSER_LANGUAGE_CODE = "browser_language_code";
    public static final String COL_DDCA_LOG_IS_VPN_ENABLED = "is_vpn_enabled";
    public static final String COL_DDCA_LOG_IS_PROXY_ENABLED = "is_proxy_enabled";
    public static final String COL_DDCA_LOG_ADDITIONAL_DATA = "additional_data";
    public static final String COL_DDCA_LOG_RESPONSE_TIME = "response_time";
    public static final String COL_DDCA_LOG_ERROR_STATUS = "error_status";
    public static final String COL_DDCA_LOG_ERROR_REASON = "error_reason";
    public static final String COL_DDCA_LOG_SYS_CREATOR = "sys_creator";
    public static final String COL_DDCA_LOG_CREATE_MILLIS = "create_millis";
    public static final String COL_DDCA_LOG_SYS_UPDATER = "sys_updater";
    public static final String COL_DDCA_LOG_UPDATE_MILLIS = "update_millis";

    // Table classic_rba_check
    public static final String TABLE_CLASSIC_RBA_CHECK = "classic_rba_check";
    public static final String COL_CLASSIC_RBA_CHECK_ID = "id";
    public static final String COL_CLASSIC_RBA_CHECK_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_CLASSIC_RBA_CHECK_CLASSIC_RBA_ENABLED = "classic_rba_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_ENABLED_BANK_IDS = "enabled_bank_ids";
    public static final String COL_CLASSIC_RBA_CHECK_PURCHASE_AMOUNT_ENABLED = "purchase_amount_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_PURCHASE_AMOUNT_AMOUNT = "purchase_amount_amount";
    public static final String COL_CLASSIC_RBA_CHECK_PURCHASE_AMOUNT_MIN_AMOUNT = "purchase_amount_min_amount";
    public static final String COL_CLASSIC_RBA_CHECK_PURCHASE_AMOUNT_CURRENCY_CODE = "purchase_amount_currency_code";
    public static final String COL_CLASSIC_RBA_CHECK_CARDHOLDER_DATA_ENABLED = "cardholder_data_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_CARDHOLDER_DATA_NAME = "cardholder_data_name";
    public static final String COL_CLASSIC_RBA_CHECK_CARDHOLDER_DATA_EMAIL = "cardholder_data_email";
    public static final String COL_CLASSIC_RBA_CHECK_CARDHOLDER_DATA_POSTCODE = "cardholder_data_postcode";
    public static final String COL_CLASSIC_RBA_CHECK_CARDHOLDER_DATA_PHONE = "cardholder_data_phone";
    public static final String COL_CLASSIC_RBA_CHECK_CUMULATIVE_AMOUNT_ENABLED = "cumulative_amount_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_CUMULATIVE_AMOUNT_PERIOD = "cumulative_amount_period";
    public static final String COL_CLASSIC_RBA_CHECK_CUMULATIVE_AMOUNT_AMOUNT = "cumulative_amount_amount";
    public static final String COL_CLASSIC_RBA_CHECK_CUMULATIVE_AMOUNT_CURRENCYCODE = "cumulative_amount_currencycode";
    public static final String COL_CLASSIC_RBA_CHECK_CUMULATIVE_AMOUNT_TX_ENABLED = "cumulative_amount_tx_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_CUMULATIVE_AMOUNT_TX_COUNT = "cumulative_amount_tx_count";
    public static final String COL_CLASSIC_RBA_CHECK_CUMULATIVE_TRANSACTION_ENABLED = "cumulative_transaction_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_CUMULATIVE_TRANSACTION_COUNT = "cumulative_transaction_count";
    public static final String COL_CLASSIC_RBA_CHECK_LOCATION_CONSISTENCY_ENABLED = "location_consistency_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_LOCATION_CONSISTENCY_IP = "location_consistency_ip";
    public static final String COL_CLASSIC_RBA_CHECK_LOCATION_CONSISTENCY_BRW_TZ = "location_consistency_brw_tz";
    public static final String COL_CLASSIC_RBA_CHECK_LOCATION_CONSISTENCY_BILLING = "location_consistency_billing";
    public static final String COL_CLASSIC_RBA_CHECK_LOCATION_CONSISTENCY_SHIPPING = "location_consistency_shipping";
    public static final String COL_CLASSIC_RBA_CHECK_BROWSER_LANGUAGE_ENABLED = "browser_language_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_BROWSER_LANGUAGE_LIST = "browser_language_list";
    public static final String COL_CLASSIC_RBA_CHECK_VPN_ENABLED = "vpn_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_PROXY_ENABLED = "proxy_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_PRIVATE_BROWSING_ENABLED = "private_browsing_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_DEVICE_VARIATION_ENABLED = "device_variation_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_MCC_ENABLED = "mcc_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_MCC_LIST = "mcc_list";
    public static final String COL_CLASSIC_RBA_CHECK_RECURRING_PAYMENT_ENABLED = "recurring_payment_enabled";
    public static final String COL_CLASSIC_RBA_CHECK_RECURRING_PAYMENT_FREQUENCY = "recurring_payment_frequency";
    public static final String COL_CLASSIC_RBA_CHECK_RECURRING_PAYMENT_EXPIRATION = "recurring_payment_expiration";

    // Table classic_rba_report
    public static final String TABLE_CLASSIC_RBA_REPORT = "classic_rba_report";
    public static final String COL_CLASSIC_RBA_REPORT_ID = "id";
    public static final String COL_CLASSIC_RBA_REPORT_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_CLASSIC_RBA_REPORT_YEAR = "year";
    public static final String COL_CLASSIC_RBA_REPORT_MONTH = "month";
    public static final String COL_CLASSIC_RBA_REPORT_DAY_OF_MONTH = "day_of_month";
    public static final String COL_CLASSIC_RBA_REPORT_BIN_RANGE_F = "bin_range_f";
    public static final String COL_CLASSIC_RBA_REPORT_BIN_RANGE_C = "bin_range_c";
    public static final String COL_CLASSIC_RBA_REPORT_BIN_RANGE_R = "bin_range_r";
    public static final String COL_CLASSIC_RBA_REPORT_BLOCK_CODE_F = "block_code_f";
    public static final String COL_CLASSIC_RBA_REPORT_BLOCK_CODE_C = "block_code_c";
    public static final String COL_CLASSIC_RBA_REPORT_BLOCK_CODE_R = "block_code_r";
    public static final String COL_CLASSIC_RBA_REPORT_PURCHASE_AMOUNT_F = "purchase_amount_f";
    public static final String COL_CLASSIC_RBA_REPORT_PURCHASE_AMOUNT_C = "purchase_amount_c";
    public static final String COL_CLASSIC_RBA_REPORT_PURCHASE_AMOUNT_R = "purchase_amount_r";
    public static final String COL_CLASSIC_RBA_REPORT_CARDHOLDER_DATA_F = "cardholder_data_f";
    public static final String COL_CLASSIC_RBA_REPORT_CARDHOLDER_DATA_C = "cardholder_data_c";
    public static final String COL_CLASSIC_RBA_REPORT_CARDHOLDER_DATA_R = "cardholder_data_r";
    public static final String COL_CLASSIC_RBA_REPORT_CUMULATIVE_AMOUNT_F = "cumulative_amount_f";
    public static final String COL_CLASSIC_RBA_REPORT_CUMULATIVE_AMOUNT_C = "cumulative_amount_c";
    public static final String COL_CLASSIC_RBA_REPORT_CUMULATIVE_AMOUNT_R = "cumulative_amount_r";
    public static final String COL_CLASSIC_RBA_REPORT_CUMULATIVE_TRANSACTION_F = "cumulative_transaction_f";
    public static final String COL_CLASSIC_RBA_REPORT_CUMULATIVE_TRANSACTION_C = "cumulative_transaction_c";
    public static final String COL_CLASSIC_RBA_REPORT_CUMULATIVE_TRANSACTION_R = "cumulative_transaction_r";
    public static final String COL_CLASSIC_RBA_REPORT_LOCATION_CONSISTENCY_F = "location_consistency_f";
    public static final String COL_CLASSIC_RBA_REPORT_LOCATION_CONSISTENCY_C = "location_consistency_c";
    public static final String COL_CLASSIC_RBA_REPORT_LOCATION_CONSISTENCY_R = "location_consistency_r";
    public static final String COL_CLASSIC_RBA_REPORT_BROWSER_LANGUAGE_F = "browser_language_f";
    public static final String COL_CLASSIC_RBA_REPORT_BROWSER_LANGUAGE_C = "browser_language_c";
    public static final String COL_CLASSIC_RBA_REPORT_BROWSER_LANGUAGE_R = "browser_language_r";
    public static final String COL_CLASSIC_RBA_REPORT_VPN_F = "vpn_f";
    public static final String COL_CLASSIC_RBA_REPORT_VPN_C = "vpn_c";
    public static final String COL_CLASSIC_RBA_REPORT_VPN_R = "vpn_r";
    public static final String COL_CLASSIC_RBA_REPORT_PROXY_F = "proxy_f";
    public static final String COL_CLASSIC_RBA_REPORT_PROXY_C = "proxy_c";
    public static final String COL_CLASSIC_RBA_REPORT_PROXY_R = "proxy_r";
    public static final String COL_CLASSIC_RBA_REPORT_PRIVATE_BROWSING_F = "private_browsing_f";
    public static final String COL_CLASSIC_RBA_REPORT_PRIVATE_BROWSING_C = "private_browsing_c";
    public static final String COL_CLASSIC_RBA_REPORT_PRIVATE_BROWSING_R = "private_browsing_r";
    public static final String COL_CLASSIC_RBA_REPORT_DEVICE_VARIATION_F = "device_variation_f";
    public static final String COL_CLASSIC_RBA_REPORT_DEVICE_VARIATION_C = "device_variation_c";
    public static final String COL_CLASSIC_RBA_REPORT_DEVICE_VARIATION_R = "device_variation_r";
    public static final String COL_CLASSIC_RBA_REPORT_MCC_F = "mcc_f";
    public static final String COL_CLASSIC_RBA_REPORT_MCC_C = "mcc_c";
    public static final String COL_CLASSIC_RBA_REPORT_MCC_R = "mcc_r";
    public static final String COL_CLASSIC_RBA_REPORT_RECURRING_PAYMENT_F = "recurring_payment_f";
    public static final String COL_CLASSIC_RBA_REPORT_RECURRING_PAYMENT_C = "recurring_payment_c";
    public static final String COL_CLASSIC_RBA_REPORT_RECURRING_PAYMENT_R = "recurring_payment_r";
    public static final String COL_CLASSIC_RBA_REPORT_BLACK_LIST_CARD_F = "black_list_card_f";
    public static final String COL_CLASSIC_RBA_REPORT_BLACK_LIST_CARD_C = "black_list_card_c";
    public static final String COL_CLASSIC_RBA_REPORT_BLACK_LIST_CARD_R = "black_list_card_r";
    public static final String COL_CLASSIC_RBA_REPORT_BLACK_LIST_IP_F = "black_list_ip_f";
    public static final String COL_CLASSIC_RBA_REPORT_BLACK_LIST_IP_C = "black_list_ip_c";
    public static final String COL_CLASSIC_RBA_REPORT_BLACK_LIST_IP_R = "black_list_ip_r";
    public static final String COL_CLASSIC_RBA_REPORT_BLACK_LIST_MERCHANT_URL_F = "black_list_merchant_url_f";
    public static final String COL_CLASSIC_RBA_REPORT_BLACK_LIST_MERCHANT_URL_C = "black_list_merchant_url_c";
    public static final String COL_CLASSIC_RBA_REPORT_BLACK_LIST_MERCHANT_URL_R = "black_list_merchant_url_r";
    public static final String COL_CLASSIC_RBA_REPORT_BLACK_LIST_DEVICE_ID_F = "black_list_device_id_f";
    public static final String COL_CLASSIC_RBA_REPORT_BLACK_LIST_DEVICE_ID_C = "black_list_device_id_c";
    public static final String COL_CLASSIC_RBA_REPORT_BLACK_LIST_DEVICE_ID_R = "black_list_device_id_r";
    public static final String COL_CLASSIC_RBA_REPORT_WHITE_LIST_F = "white_list_f";
    public static final String COL_CLASSIC_RBA_REPORT_WHITE_LIST_C = "white_list_c";
    public static final String COL_CLASSIC_RBA_REPORT_WHITE_LIST_R = "white_list_r";
    public static final String COL_CLASSIC_RBA_REPORT_ATTEMPT_F = "attempt_f";
    public static final String COL_CLASSIC_RBA_REPORT_ATTEMPT_C = "attempt_c";
    public static final String COL_CLASSIC_RBA_REPORT_ATTEMPT_R = "attempt_r";
    public static final String COL_CLASSIC_RBA_REPORT_SYS_CREATOR = "sys_creator";
    public static final String COL_CLASSIC_RBA_REPORT_CREATE_MILLIS = "create_millis";
    public static final String COL_CLASSIC_RBA_REPORT_SYS_UPDATER = "sys_updater";
    public static final String COL_CLASSIC_RBA_REPORT_UPDATE_MILLIS = "update_millis";
    public static final String COL_CLASSIC_RBA_REPORT_DELETE_FLAG = "delete_flag";
    public static final String COL_CLASSIC_RBA_REPORT_DELETER = "deleter";
    public static final String COL_CLASSIC_RBA_REPORT_DELETE_MILLIS = "delete_millis";

    // Table card_brand
    public static final String TABLE_CARD_BRAND = "card_brand";
    public static final String COL_CARD_BRAND_NAME = "name";
    public static final String COL_CARD_BRAND_PATTERN = "pattern";
    public static final String COL_CARD_BRAND_DISPLAY_ORDER = "display_order";
    public static final String COL_CARD_BRAND_DISPLAY_NAME = "display_name";

    // Table currency
    public static final String TABLE_CURRENCY = "currency";
    public static final String COL_CURRENCY_CODE = "code";
    public static final String COL_CURRENCY_ALPHA = "alpha";
    public static final String COL_CURRENCY_EXPONENT = "exponent";
    public static final String COL_CURRENCY_USD_EXCHANGE_RATE = "usd_exchange_rate";

    // Table plugin_issuer_runtime_property
    public static final String TABLE_PLUGIN_ISSUER_RUNTIME_PROPERTY = "plugin_issuer_runtime_property";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_ID = "id";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_PLUGIN_ID = "plugin_id";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_PROPERTY_ID = "property_id";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_NAME = "name";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_VALUE = "value";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_VALUE_ENCRYPTED = "value_encrypted";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_CREATOR = "creator";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_CREATE_MILLIS = "create_millis";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_UPDATER = "updater";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_UPDATE_MILLIS = "update_millis";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_DELETE_FLAG = "delete_flag";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_DELETER = "deleter";
    public static final String COL_PLUGIN_ISSUER_RUNTIME_PROPERTY_DELETE_MILLIS = "delete_millis";

    // Table transaction_amount
    public static final String TABLE_TRANSACTION_AMOUNT = "transaction_amount";
    public static final String COL_TRANSACTION_AMOUNT_ID = "id";
    public static final String COL_TRANSACTION_AMOUNT_PAN_INFO_ID = "pan_info_id";
    public static final String COL_TRANSACTION_AMOUNT_PURCHASE_AMOUNT = "purchase_amount";
    public static final String COL_TRANSACTION_AMOUNT_PURCHASE_CURRENCY = "purchase_currency";
    public static final String COL_TRANSACTION_AMOUNT_PURCHASE_EXPONENT = "purchase_exponent";
    public static final String COL_TRANSACTION_AMOUNT_SYS_CREATOR = "sys_creator";
    public static final String COL_TRANSACTION_AMOUNT_CREATE_MILLIS = "create_millis";

    // Table mima_policy
    public static final String TABLE_MIMA_POLICY = "mima_policy";
    public static final String COL_MIMA_POLICY_ID = "id";
    public static final String COL_MIMA_POLICY_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_MIMA_POLICY_LOGIN_RETRY_COUNT = "login_retry_count";
    public static final String COL_MIMA_POLICY_MIMA_FRESH_INTERVAL = "mima_fresh_interval";
    public static final String COL_MIMA_POLICY_ACCOUNT_MAX_IDLE_DAY = "account_max_idle_day";
    public static final String COL_MIMA_POLICY_MIMA_MAX_LENGTH = "mima_max_length";
    public static final String COL_MIMA_POLICY_MIMA_MIN_LENGTH = "mima_min_length";
    public static final String COL_MIMA_POLICY_MIMA_ALPHABET_COUNT = "mima_alphabet_count";
    public static final String COL_MIMA_POLICY_MIMA_MIN_LOWER_CASE = "mima_min_lower_case";
    public static final String COL_MIMA_POLICY_MIMA_MIN_UPPER_CASE = "mima_min_upper_case";
    public static final String COL_MIMA_POLICY_MIMA_MIN_NUMERIC = "mima_min_numeric";
    public static final String COL_MIMA_POLICY_MIMA_MIN_SPECIAL_CHAR = "mima_min_special_char";
    public static final String COL_MIMA_POLICY_MIMA_HISTORY_DUP_COUNT = "mima_history_dup_count";
    public static final String COL_MIMA_POLICY_CREATE_TIME = "create_time";
    public static final String COL_MIMA_POLICY_UPDATE_TIME = "update_time";

    // Table mima_record
    public static final String TABLE_MIMA_RECORD = "mima_record";
    public static final String COL_MIMA_RECORD_ID = "id";
    public static final String COL_MIMA_RECORD_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_MIMA_RECORD_ACCOUNT = "account";
    public static final String COL_MIMA_RECORD_MIMA = "mima";
    public static final String COL_MIMA_RECORD_CREATE_TIME = "create_time";

    // Table bank_data_key
    public static final String TABLE_BANK_DATA_KEY = "bank_data_key";
    public static final String COL_BANK_DATA_KEY_ID = "id";
    public static final String COL_BANK_DATA_KEY_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_BANK_DATA_KEY_RSA_PRIVATE_KEY_ID = "rsa_private_key_id";
    public static final String COL_BANK_DATA_KEY_RSA_PUBLIC_KEY = "rsa_public_key";
    public static final String COL_BANK_DATA_KEY_CREATOR = "creator";
    public static final String COL_BANK_DATA_KEY_CREATE_MILLIS = "create_millis";
    public static final String COL_BANK_DATA_KEY_UPDATER = "updater";
    public static final String COL_BANK_DATA_KEY_UPDATE_MILLIS = "update_millis";

    // Table MfaInfo
    public static final String TABLE_MFA_INFO = "mfa_info";
    public static final String COL_MFA_INFO_ID = "id";
    public static final String COL_MFA_INFO_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_MFA_INFO_ACCOUNT = "account";
    public static final String COL_MFA_INFO_SECRET_KEY = "secret_key";

    // Table MfaOtpRecord
    public static final String TABLE_MFA_OTP_RECORD = "mfa_otp_record";
    public static final String COL_MFA_OTP_RECORD_ID = "id";
    public static final String COL_MFA_OTP_RECORD_ISSUER_BANK_ID = "issuer_bank_id";
    public static final String COL_MFA_OTP_RECORD_ACCOUNT = "account";
    public static final String COL_MFA_OTP_RECORD_OTP = "otp";
    public static final String COL_MFA_OTP_RECORD_CREATE_MILLIS = "create_millis";
    public static final String COL_MFA_OTP_RECORD_IS_ACTIVE = "is_active";

}
