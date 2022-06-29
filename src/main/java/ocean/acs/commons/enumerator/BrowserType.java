package ocean.acs.commons.enumerator;

import lombok.Getter;

/** 瀏覽器類型 */
public enum BrowserType {

    Chrome("chrome", 50, 0),
    IE("msie", 11,7),
    Edge("edge", 0, 0),
    Firefox("firefox", 52, 0),
    Safari("safari", 0, 0),
    Opera("opera", 0,0),
    Unknown("unknown", 0,0);

    /** 瀏覽器在User-Agent字串中的名稱 */
    @Getter
    private String name;
    /** 支援的瀏覽器最低大版本號 */
    @Getter
    private Integer supportMajorMinVersion;
    /** 支援的IE瀏覽器Trident engin最低版本號 */
    @Getter
    private Integer supportTridentMajorMinVersion;

    BrowserType(String name, Integer supportMajorMinVersion, Integer supportTridentMajorMinVersion) {
        this.name = name;
        this.supportMajorMinVersion = supportMajorMinVersion;
        this.supportTridentMajorMinVersion = supportTridentMajorMinVersion;
    }

    /** 以Request Header的 User-Agent字串取得BrowserType */
    public static BrowserType getBrowserType(String userAgent) {
        if (userAgent.contains(BrowserType.Edge.getName())) {
            return Edge;
        } else if (userAgent.contains(BrowserType.IE.getName())) {
            return IE;
        } else if (userAgent.contains(BrowserType.Chrome.getName())) {
            return Chrome;
        } else if (userAgent.contains(BrowserType.Firefox.getName())) {
            return Firefox;
        } else if (userAgent.contains(BrowserType.Safari.getName())) {
            return Safari;
        } else if (userAgent.contains(BrowserType.Opera.getName()) || userAgent.contains("opr")) {
            return Opera;
        } else {
            return Unknown;
        }

    }
}
