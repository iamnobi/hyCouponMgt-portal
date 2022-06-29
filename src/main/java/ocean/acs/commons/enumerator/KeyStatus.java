package ocean.acs.commons.enumerator;

public enum KeyStatus {

    INIT(0, false), PROCESSING(1, true), COMPLETED(2, false);

    private final int code;
    private final boolean hasTmpKey;

    KeyStatus(int code, boolean hasTmpKey) {
        this.code = code;
        this.hasTmpKey = hasTmpKey;
    }

    public int getCode() {
        return code;
    }

    public boolean hasTmpKey() {
        return hasTmpKey;
    }
}
