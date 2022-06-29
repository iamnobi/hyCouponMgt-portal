package ocean.acs.commons.enumerator;

public enum BlackListAuthStatusCategory {

    BLACK_LIST_PAN(0, "risk-control-setting.pan-black-list"),
    BLACK_LIST_DEVICE_ID(1, "risk-control-setting.device-id-black-list"),
    BLACK_LIST_IP(2, "risk-control-setting.ip-black-list"),
    BLACK_LIST_MERCHANT_URL(3, "risk-control-setting.merchant-url-black-list"),
    UNKNOWN(99, "N/A");

    private final Integer code;
    private final String desc;

    BlackListAuthStatusCategory(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BlackListAuthStatusCategory getByCode(Integer code) {
        BlackListAuthStatusCategory[] authStatusList = values();
        for (BlackListAuthStatusCategory authStatus : authStatusList) {
            if (code.equals(authStatus.code)) {
                return authStatus;
            }
        }
        return UNKNOWN;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getCode() {
        return code;
    }
}
