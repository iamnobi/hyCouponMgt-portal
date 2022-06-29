package ocean.acs.commons.enumerator;

public enum StorageType {

    PAN_BLACKLIST_BATCH("pan_blacklist"), BANK_MANAGEMENT("bank_management");

    private String pathToken;

    StorageType(String pathToken) {
        this.pathToken = pathToken;
    }

    public String getPathToken() {
        return pathToken;
    }
}
