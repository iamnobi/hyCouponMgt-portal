package ocean.acs.commons.enumerator;

public enum EnrollStatus {

    ENROLLED("0"), LOCKED("C"), UNENROLLED(""), NONE("-");

    private String code;

    EnrollStatus(String code) {
        this.code = code;
    }

    public static EnrollStatus getByCode(String code) {
        for (EnrollStatus enrollStatus : EnrollStatus.values()) {
            if (enrollStatus.getCode().equalsIgnoreCase(code)) {
                return enrollStatus;
            }
        }
        return UNENROLLED;
    }

    public String getCode() {
        return code;
    }
}
