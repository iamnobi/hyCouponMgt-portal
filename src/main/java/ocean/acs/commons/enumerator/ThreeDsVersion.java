package ocean.acs.commons.enumerator;

import lombok.Getter;

public enum ThreeDsVersion {

    ONE("1.0", 1), TWO("2.0", 2), UNKNOWN("Unknown", 0);

    @Getter
    private String description;
    @Getter
    private Integer code;

    ThreeDsVersion(String description, Integer code) {
        this.description = description;
        this.code = code;
    }

    public static ThreeDsVersion getByCodeStr(String codeStr) {
        try {
            return getByCode(Integer.parseInt(codeStr));
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public static ThreeDsVersion getByCode(Integer code) {
        if (code == null) {
            return ThreeDsVersion.UNKNOWN;
        }

        for (ThreeDsVersion version : ThreeDsVersion.values()) {
            if (version.getCode().equals(code)) {
                return version;
            }
        }
        return ThreeDsVersion.UNKNOWN;
    }

}
