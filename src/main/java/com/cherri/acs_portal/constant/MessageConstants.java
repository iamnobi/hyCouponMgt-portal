package com.cherri.acs_portal.constant;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:messages.properties"})
public class MessageConstants {
    // Kernel
    public static String NOT_FOUND = "not.found";
    public static String COLUMN_NOT_EMPTY = "column.notempty";
    public static String MIMA_INCORRECT = "mima.incorrect";
    public static String NOT_SUPPORTED = "not.supported";
    public static String IO_LOAD_ERROR = "io.load.error";
    public static String IO_WRITE_ERROR = "io.write.error";
    public static String IO_CLOSE_ERROR = "io.close.error";
    public static String KEY_SPECIFICATIONS_INVALID = "key.specifications.invalid";
    public static String ENUM_NAME_INVALID = "enum.name.invalid";
    // Certificate
    public static String CERT_INVALID = "certificate.invalid";
    public static String PRIVATE_KEY_INVALID = "private.key.invalid";
    // RBA
    public static String RBA_AMOUNT_PER_TRANSACTION = "rba.amount-per-transaction";
    public static String RBA_CARDHOLDER_DATA_CHECK = "rba.cardholder-data-check";
    public static String RBA_CUMULATIVE_AMOUNT_CHECK = "rba.cumulative-amount-check";
    public static String RBA_LOCATION_CONSISTENCY_CHECK = "rba.location-consistency-check";
    public static String RBA_BROWSER_LANGUAGE_CHECK = "rba.browser-language-check";
    public static String RBA_VPN_CHECK = "rba.vpn-check";
    public static String RBA_PROXY_CHECK = "rba.proxy-check";
    public static String RBA_PRIVATE_BROWSING_CHECK = "rba.private-browsing-check";
    public static String RBA_DEVICE_VARIATION_CHECK = "rba.device-variation-check";
    public static String RBA_MCC_RISKY_MERCHANT = "rba.mcc-risky-merchant";
    public static String RBA_RECURRING_PAYMENT_RISK = "rba.recurring-payment-risk";
    public static String RBA_RESULT = "rba.result";
    public static String RBA_CLASSIC_RBA_ENABLED = "rba.classic-rba-enabled";
    public static String RBA_NOT_APPLICABLE = "rba.not-applicable";
    public static String RBA_PASS = "rba.pass";
    public static String RBA_FAIL = "rba.fail";
    // Risk Control Setting 風控設定
    public static String RISK_CONTROL_SETTING_PAN_BLACK_LIST = "risk-control-setting.pan-black-list";
    public static String RISK_CONTROL_SETTING_DEVICE_ID_BLACK_LIST = "risk-control-setting.device-id-black-list";
    public static String RISK_CONTROL_SETTING_IP_BLACK_LIST = "risk-control-setting.ip-black-list";
    public static String RISK_CONTROL_SETTING_MERCHANT_URL_BLACK_LIST = "risk-control-setting.merchant-url-black-list";
    // excel headers
    public static String EXCEL_BANK_NAME = "excel.bank-name";
    public static String EXCEL_YEAR = "excel.year";
    public static String EXCEL_MONTH = "excel.month";
    public static String EXCEL_DAY = "excel.day";
    // Excel - tx status
    public static String EXCEL_TX_STATUS = "excel.tx-status";
    public static String EXCEL_TX_STATUS_TOTAL_TRANSACTION_AMOUNT = "excel.tx-status.total-transaction-amount";
    public static String EXCEL_TX_STATUS_N_AUTHENTICATION_FAILED = "excel.tx-status.n.authentication-failed";
    public static String EXCEL_TX_STATUS_Y_AUTHENTICATION_SUCCEED = "excel.tx-status.y.authentication-succeed";
    public static String EXCEL_TX_STATUS_C_Y_OTP_AUTHENTICATION_SUCCEED = "excel.tx-status.c-y.otp-authentication-succeed";
    public static String EXCEL_TX_STATUS_C_N_OTP_AUTHENTICATION_FAILED = "excel.tx-status.c-n.otp-authentication-failed";
    public static String EXCEL_TX_STATUS_R_TRANSACTION_REJECT = "excel.tx-status.r.transaction-reject";
    public static String EXCEL_TX_STATUS_U_SYSTEM_EXCEPTION = "excel.tx-status.u.system_exception";
    // Excel - attempt
    public static String EXCEL_ATTEMPT = "excel.attempt";
    public static String EXCEL_ATTEMPT_ATTEMPT_TIMES = "excel.attempt.attempt-times";
    public static String EXCEL_ATTEMPT_AUTHORIZATION_TIMES = "excel.attempt.authorization-times";
    // Excel - abnormal_transaction
    public static String EXCEL_MERCHANT_EXCEPTION_TRANSACTION = "excel.merchant-exception-transaction";
    public static String EXCEL_MERCHANT_EXCEPTION_TRANSACTION_MERCHANT_ID = "excel.merchant-exception-transaction.merchant-id";
    public static String EXCEL_MERCHANT_EXCEPTION_TRANSACTION_MERCHANT_NAME = "excel.merchant-exception-transaction.merchant-name";
    // Excel - attempt
    public static String EXCEL_TRANSACTION_LOG = "excel.transaction-log";
    public static String EXCEL_TRANSACTION_LOG_AUTHENTICATION_DATE = "excel.transaction-log.authentication-date";
    public static String EXCEL_TRANSACTION_LOG_PAN = "excel.transaction-log.pan";
    public static String EXCEL_TRANSACTION_LOG_AMOUNT = "excel.transaction-log.amount";
    public static String EXCEL_TRANSACTION_LOG_CURRENCY = "excel.transaction-log.currency";
    public static String EXCEL_TRANSACTION_LOG_DEVICE_CHANNEL = "excel.transaction-log.device-channel";
    public static String EXCEL_TRANSACTION_LOG_PURCHASE_TIME = "excel.transaction-log.purchase-time";
    public static String EXCEL_TRANSACTION_LOG_MERCHANT_NAME = "excel.transaction-log.merchant-name";
    public static String EXCEL_TRANSACTION_LOG_MERCHANT_COUNTRY_CODE = "excel.transaction-log.merchant-country-code";
    public static String EXCEL_TRANSACTION_LOG_CHALLENGE_RESULT = "excel.transaction-log.challenge-result";
    public static String EXCEL_TRANSACTION_LOG_RBA_RESULT = "excel.transaction-log.rba-result";
    public static String EXCEL_TRANSACTION_LOG_USER_AGENT = "excel.transaction-log.user-agent";
    public static String EXCEL_TRANSACTION_LOG_AUTHENTICATION_CHANNEL = "excel.transaction-log.authentication-channel";
    // Excel - VE log
    public static String EXCEL_VE_LOG = "excel.ve-log";
    public static String EXCEL_VE_LOG_ISSUER_NAME = "excel.ve-log.issuer-name";
    public static String EXCEL_VE_LOG_VEREQ_TIME = "excel.ve-log.vereq-time";
    public static String EXCEL_VE_LOG_VERES = "excel.ve-log.veres";
    public static String EXCEL_VE_LOG_IS_PAREQ_RECEIVED = "excel.ve-log.is-pareq-received";
    public static String EXCEL_VE_LOG_PROXY_PAN = "excel.ve-log.proxy-pan";
    public static String EXCEL_VE_LOG_IREQ_CODE = "excel.ve-log.ireq-code";
    public static String EXCEL_VE_LOG_MERCHANT_ID = "excel.ve-log.merchant-id";
    // Excel - Blacklist
    public static String EXCEL_BLACK_LIST = "excel.black-list";
    public static String EXCEL_BLACK_LIST_ADDED_TIME = "excel.black-list.added-time";
    public static String EXCEL_BLACK_LIST_BLOCK_TIMES= "excel.black-list.block-times";
    // Excel - Blacklist - pan
    public static String EXCEL_BLACK_LIST_PAN = "excel.black-list.pan";
    public static String EXCEL_BLACK_LIST_PAN_BATCH_NAME = "excel.black-list.pan.batch-name";
    public static String EXCEL_BLACK_LIST_PAN_AUTHENTICATION_STATE = "excel.black-list.pan.authentication-state";
    // Excel - Blacklist - device id
    public static String EXCEL_BLACK_LIST_DEVICE_ID = "excel.black-list.device-id";
    public static String EXCEL_BLACK_LIST_DEVICE_ID_IP_ADDRESS = "excel.black-list.device-id.ip-address";
    public static String EXCEL_BLACK_LIST_DEVICE_ID_DEVICE_CHANNEL = "excel.black-list.device-id.device-channel";
    public static String EXCEL_BLACK_LIST_DEVICE_ID_BROWSER_USER_AGENT = "excel.black-list.device-id.browser-user-agent";
    // Excel - Blacklist - IP
    public static String EXCEL_BLACK_LIST_IP_IP_ADDRESS = "excel.black-list.ip.ip-address";
    // Excel - Blacklist - merchant url
    public static String EXCEL_BLACK_LIST_MERCHANT_URL = "excel.black-list.merchant-url";
    public static String EXCEL_BLACK_LIST_MERCHANT_UR_MERCHANT_URL_KEYWORDS = "excel.black-list.merchant-url.merchant-url-keywords";
    // Excel - white list pan
    public static String EXCEL_WHITE_LIST_PAN = "excel.white-list-pan";

    // account
    public static String EXCEED_MAX_LOGIN_ATTEMPT = "account.lock.EXCEED_MAX_LOGIN_ATTEMPT";
    public static String EXCEED_MAX_FORGET_MIMA = "account.lock.EXCEED_MAX_FORGET_MIMA";
    public static String EXCEED_MAX_UNUSED_PERIOD = "account.lock.EXCEED_MAX_UNUSED_PERIOD";
    public static String EXCEED_MAX_SEND_MFA_TIMES = "account.lock.EXCEED_MAX_SEND_MFA_TIMES";
    public static String EXCEED_MAX_VERIFY_MFA_ATTEMPT = "account.lock.EXCEED_MAX_VERIFY_MFA_ATTEMPT";

    private static MessageSource messageSource;

    public MessageConstants(MessageSource messageSource) {
        MessageConstants.messageSource = messageSource;
    }

    public static String get(String code) {
        return get(code, null);
    }

    public static String get(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}
