package ocean.acs.commons.enumerator;

public enum HandingFeeType {

    CARD(0), MONTH(1);

    private final int code;

    HandingFeeType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
