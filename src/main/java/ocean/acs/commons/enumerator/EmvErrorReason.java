package ocean.acs.commons.enumerator;

public enum EmvErrorReason {

    MESSAGE_RECEIVED_INVALID("101"),
    MESSAGE_VERSION_NUMBER_NOT_SUPPORTED("102"),
    SENT_MESSAGES_LIMIT_EXCEEDED("103"),
    REQUIRED_DATA_ELEMENT_MISSING("201"),
    CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED("202"),
    /** Format of one or more Data Elements is Invalid according to the Specification* */
    INVALID_FORMAT("203"),
    DUPLICATE_DATA_ELEMENT("204"),
    TRANSACTION_ID_NOT_RECOGNISED("301"),
    DATA_DECRYPTION_FAILURE("302"),
    ACCESS_DENIED_INVALID_Endpoint("303"),
    ISO_CODE_INVALID("304"),
    TRANSACTION_DATA_NOT_VALID("305"),
    MERCHANT_CATEGORY_CODE_NOT_VALID_FOR_PAYMENT_System("306"),
    SERIAL_NUMBER_NOT_VALID("307"),
    TRANSACTION_TIMED_OUT("402"),
    TRANSIENT_SYSTEM_FAILURE("403"),
    PERMANENT_SYSTEM_FAILURE("404"),
    SYSTEM_CONNECTION_FAILURE("405"),

    UNKNOWN("9999");
    
    private final String code;
    EmvErrorReason(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static EmvErrorReason codeOf(String code) {
        EmvErrorReason[] reasonList = values();
        for (EmvErrorReason err : reasonList) {
            if (err.code.equals(code)) {
                return err;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return this.getCode();
    }

}
