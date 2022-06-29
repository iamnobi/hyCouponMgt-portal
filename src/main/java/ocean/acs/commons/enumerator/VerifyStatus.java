package ocean.acs.commons.enumerator;

public enum VerifyStatus {
    INIT(0), SUCCESS(1), FAIL(2);

    private final int code;

    VerifyStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    private static final VerifyStatus[] values = values();

    public static VerifyStatus getByCode(int code) {
        for (VerifyStatus sts : values) {
            if (sts.code == code) {
                return sts;
            }
        }
        return VerifyStatus.INIT;
    }
}
