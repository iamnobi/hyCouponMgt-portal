package ocean.acs.commons.enumerator;

public enum TransStatusReason {

    CARD_AUTHENTICATION_FAILED("01"),
    UNKNOWN_DEVICE("02"),
    UNSUPPORTED_DEVICE("03"),
    EXCEEDS_AUTHENTICATION_FREQUENCY_LIMIT("04"),
    EXPIRED_CARD("05"),
    INVALID_CARD_NUMBER("06"),
    INVALID_TRANSACTION("07"),
    NO_CARD_RECORD("08"),
    SECURITY_FAILURE("09"),
    STOLEN_CARD("10"),
    SUSPECTED_FRAUD("11"),
    TRANSACTION_NOT_PERMITTED_TO_CARDHOLDER("12"),
    CARDHOLDER_NOT_ENROLLED_IN_SERVICE("13"),
    TRANSACTION_TIMED_OUT_AT_THE_ACS("14"),
    LOW_CONFIDENCE("15"),
    MEDIUM_CONFIDENCE("16"),
    HIGH_CONFIDENCE("17"),
    VERY_HIGH_CONFIDENCE("18"),
    EXCEEDS_ACS_MAXIMUM_CHALLENGES("19"),
    NON_PAYMENT_TRANSACTION_NOT_SUPPORTED("20"),
    THREE_RI_TRANSACTION_NOT_SUPPORTED("21");

    // 22–79 = Reserved for EMVCo future use (values invalid until defined by EMVCo)
    // 80–99 = Reserved for DS use

    private final String code;

    TransStatusReason(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
