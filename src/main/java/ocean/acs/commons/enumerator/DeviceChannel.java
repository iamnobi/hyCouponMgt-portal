package ocean.acs.commons.enumerator;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum DeviceChannel {

    APP("01", "app"), BROWSER("02", "browser"), THREERI("03", "3ri");

    private final String code;
    private final String desc;

    DeviceChannel(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean contains(String code) {
        DeviceChannel[] deviceList = values();
        for (DeviceChannel dt : deviceList) {
            if (dt.code.equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static DeviceChannel descOf(String reason) {
        DeviceChannel[] deviceList = values();
        for (DeviceChannel dt : deviceList) {
            if (dt.desc.equalsIgnoreCase(reason)) {
                return dt;
            }
        }
        return null;
    }

    public static DeviceChannel codeOf(String code) {
        DeviceChannel[] deviceList = values();
        for (DeviceChannel dt : deviceList) {
            if (dt.code.equals(code)) {
                return dt;
            }
        }
        return null;
    }

    public static Stream<DeviceChannel> stream() {
        return Stream.of(DeviceChannel.values());
    }

    @Override
    public String toString() {
        return this.desc;
    }

    @JsonValue
    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }

    public static boolean isApp(String deviceChannel) {
        return DeviceChannel.APP
                .getCode()
                .equals(deviceChannel);
    }

    public static boolean isBrowser(String deviceChannel) {
        return BROWSER
                .getCode()
                .equals(deviceChannel);
    }

    public static boolean is3Ri(String deviceChannel) {
        return THREERI
                .getCode()
                .equals(deviceChannel);
    }
}
