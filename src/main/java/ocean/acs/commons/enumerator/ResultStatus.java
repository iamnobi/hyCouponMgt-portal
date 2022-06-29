package ocean.acs.commons.enumerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum ResultStatus {
    SUCCESS(0),
    MESSAGE_RECEIVED_INVALID(101),
    NOT_SUPPORTED(102),
    COLUMN_NOT_EMPTY(201),
    CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED(202),
    INVALID_FORMAT(203),
    DUPLICATE_DATA_ELEMENT(204),
    TRANSACTION_ID_NOT_RECOGNISED(301),
    DATA_DECRYPTION_FAILURE(302),
    ACCESS_DENIED_INVALID_ENDPOINT(303),
    TRANSACTION_DATA_NOT_VALID(305),
    UNAUTHORIZED(401),
    TRANSACTION_TIMED_OUT(402),
    FORBIDDEN(403),
    NO_SUCH_DATA(404),
    SYSTEM_CONNECTION_FAILURE(405),
    CONNECTION_TIMEOUT(502),
    READ_TIMEOUT(503),
    CONNECTION_FAIL(504),
    ILLEGAL_ARGUMENT(505),
    ILLEGAL_URL(506),
    ILLEGAL_FILE_CHECKSUM(507),
    ILLEGAL_FILE_SIZE(508),
    ILLEGAL_KEY(509),
    ILLEGAL_KEY_SIZE(510),
    DB_SAVE_ERROR(800),
    DB_READ_ERROR(801),
    FILE_READ_ERROR(802),
    FILE_SAVE_ERROR(803),
    ACS_INTEGRATOR_CONNECT_ERROR(901),
    ACS_INTEGRATOR_RESPONSE_ERROR(902),
    ACS_KERNEL_CONNECT_ERROR(903),
    ACS_KERNEL_RESPONSE_ERROR(904),

    AUTHENTICATION_DEFAULT_FLOW(1000),
    AREQ_PARAMETER_INVALID(1001),
    THREE_DS_VERIFY_DISABLED(1002),
    THREE_RI_DEFAULT_FLOW(1003),
    BLACK_BIN_RANGE_U(1100),
    BLACK_BIN_RANGE_N(1101),
    TRADING_CHANNEL_U(1150),
    TRADING_CHANNEL_N(1151),
    VERIFY_CARDHOLDER_U(1200),
    VERIFY_CARDHOLDER_N(1201),
    BLACK_PAN_U(1300),
    BLACK_PAN_N(1301),
    BLACK_PAN_C(1302),
    BLACK_DEVICE_ID_U(1400),
    BLACK_DEVICE_ID_N(1401),
    BLACK_DEVICE_ID_C(1402),
    BLACK_IP_U(1500),
    BLACK_IP_N(1501),
    BLACK_IP_C(1502),
    BLACK_MERCHANT_U(1600),
    BLACK_MERCHANT_N(1601),
    BLACK_MERCHANT_C(1602),
    CALCULATE_CAVV_ERROR(1700),
    TWD_EXCHANGE_RATE_ERROR(1701),
    CALCULATE_JWS_ERROR(1702),
    THREE_DS_REQ_CHALLENGE_IND(1800),
    EMV_TOKEN_ID_AND_V(1801),
    WHITE_PAN_Y(1900),
    WHITE_PAN_U(1901),
    ATTEMPT_PAN_Y(1910),
    CLASSIC_RBA_Y(1911),
    CLASSIC_RBA_N(1912),
    CLASSIC_RBA_C(1913),
    CLASSIC_RBA_U(1914),

    /** 整個challenge過程中發生的ACS-kernel和ACS-integrator系統錯誤 */
    FIRST_CREQ_TIMEOUT(4000),
    TRANSACTION_TIMEOUT(4001),
    CREQ_PARAMETER_INVALID(4002),
    BROWSER_NOT_SUPPORT(4003),
    CARDHOLDER_AUTH_CHANNEL_NOT_SET(4004),
    CARDHOLDER_INFO_NOT_FOUND(4005),
    CHALLENGE_CANCEL(4100),
    GET_AUTH_CHANNEL_ERROR(4101),
    /** 指的是前端使用者按下"手機號碼錯誤 */
    PHONE_NUMBER_ERROR(4102),
    OTP_LOCK(4200),
    SEND_OTP_SYSTEM_ERROR(4201),
    OTP_VERIFY_ERROR(4202),
    OTP_VERIFY_SYSTEM_ERROR(4203),
    OTP_TIMEOUT(4204),
    /** 收到的RRes有錯誤時 */
    RESULT_RESPONSE_ERROR(4300),
    RESULT_RESPONSE_TIMEOUT(4301),
    RESULT_RESPONSE_FORMAT_ERROR(4302),
    RESULT_RESPONSE_CONNECT_ERROR(4303),
    UNSUPPORTED_EXCHANGE_RATE(4400),

    SERVER_ERROR(5000),
    NOT_RECOGNISED(5001),
    IO_EXCEPTION(5002),
    SYSTEM_KEYWORD(5003),
    DATABASE_ERROR(5004),
    HMAC_ERROR(5005),

    // BinRange
    ILLEGAL_BATCH_NAME_SIZE(5101),
    ILLEGAL_FILE_NAME_SIZE(5102),
    ILLEGAL_CARD(5103),
    /** 非自行卡號 */
    NON_SELF_BANK_CARD_NUMBER(5104),
    /** 無自行BinRange資料 */
    NO_SELF_BANK_BIN_RANGE(5105),

    // 批次黑名單
    /** 卡號筆數超過上限 */
    ITEMS_EXCEEDS_UPPER_LIMIT(5106),

    // Auditing Exception
    AUDITING_LOCK_UNAVAILABLE(6000),

    /* Mima Policy */
    DIFFERENT_WITH_MIMA_POLICY(6202), // 規則不符
    ILLEGAL_PASSWORD(6203), // 舊密碼輸入有錯
    EXPIRED_PASSWORD_RESET_LINK(6204), // 密碼重設連結失效

    /* MFA */
    MFA_NOT_REGISTER(6301),
    MFA_OTP_INCORRECT(6302),

    ACCOUNT_LOCKED(6400),
    ACCOUNT_ONLY_IN_ONE_GROUP(6401),

    /* CERTIFICATE */
    INVALID_CERTIFICATE(6500),
    EXCEEDS_MAX_CA_CERTIFICATE_LIMIT(6501),

    // Integrator Issue
    ENCRYPT_FAIL(7000),

    HSM_PLUGIN_EXCEPTION(8001),
    CARDHOLDER_PLUGIN_EXCEPTION(8002),
    MESSENGER_PLUGIN_EXCEPTION(8003),
    OTP_PLUGIN_EXCEPTION(8004),
    OOB_PLUGIN_EXCEPTION(8005),
    DECOUPLED_PLUGIN_EXCEPTION(8006),
    AUTHENTICATION_PLUGIN_EXCEPTION(8007),

    UNKNOWN(99999);

    private final Integer code;

    private static List<Integer> ARES_ERROR_REASON_CODE_LIST =
      Collections.synchronizedList(new ArrayList<>());

    ResultStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static ResultStatus codeOf(String code) {
        ResultStatus[] reasonList = values();
        for (ResultStatus err : reasonList) {
            if (String.valueOf(err.code).equals(code)) {
                return err;
            }
        }
        return null;
    }

    public boolean isSuccess() {
        return this.equals(ResultStatus.SUCCESS);
    }

    public static List<Integer> getAResErrorReasonCodeList() {
        if (!ARES_ERROR_REASON_CODE_LIST.isEmpty()) {
            return ARES_ERROR_REASON_CODE_LIST;
        }
        ARES_ERROR_REASON_CODE_LIST =
          Arrays.stream(values())
            .filter(resultStatus -> !ResultStatus.SUCCESS.equals(resultStatus))
            .filter(resultStatus -> !ResultStatus.AUTHENTICATION_DEFAULT_FLOW.equals(resultStatus))
            .filter(resultStatus -> !ResultStatus.THREE_RI_DEFAULT_FLOW.equals(resultStatus))
            .filter(resultStatus -> !ResultStatus.BLACK_PAN_C.equals(resultStatus))
            .filter(resultStatus -> !ResultStatus.BLACK_DEVICE_ID_C.equals(resultStatus))
            .filter(resultStatus -> !ResultStatus.BLACK_IP_C.equals(resultStatus))
            .filter(resultStatus -> !ResultStatus.BLACK_MERCHANT_C.equals(resultStatus))
            .filter(resultStatus -> !ResultStatus.WHITE_PAN_Y.equals(resultStatus))
            .filter(resultStatus -> !ResultStatus.ATTEMPT_PAN_Y.equals(resultStatus))
            .map(ResultStatus::getCode)
            .collect(Collectors.toList());
        return ARES_ERROR_REASON_CODE_LIST;
    }
}
