package ocean.acs.commons.enumerator;

public enum TimerType {
    FIRST_TIMEOUT(0), TRANSACTION_TIMEOUT(1), UNKNOWN(99);

    private final int code;

    TimerType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static TimerType codeOf(int code){
        for (TimerType t : TimerType.values()) {
            if (t.getCode() == code) return t;
        }
        return TimerType.UNKNOWN;
    }
}
