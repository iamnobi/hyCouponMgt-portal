package ocean.acs.commons.enumerator;


public enum PluginType {
    UNKNOWN("0"),
    HSM("1"),
    CARDHOLDER("2"),
    MESSENGER("3"),
    OTP("4"),
//    OOB("5"),
//    DECOUPLED("6"),
    AUTHENTICATION("7");

    private String pluginId;

    PluginType(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getPluginId() {
        return pluginId;
    }

    public static PluginType getByCode(String pluginId) {
        for(PluginType type : values()) {
            if(type.getPluginId().equalsIgnoreCase(pluginId)) return type;
        }

        return PluginType.UNKNOWN;
    }
}
