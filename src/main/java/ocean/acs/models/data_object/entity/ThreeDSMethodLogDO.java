package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.MessageType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThreeDSMethodLogDO {

    private Long id;
    private String threeDSMethodNotificationURL;
    private String BrowserAcceptHeader;
    private String browserUserAgent;
    private Boolean browserJavaEnabled;
    private String browserLanguage;
    private String browserColorDepth;
    private String browserScreenHeight;
    private String browserScreenWidth;
    private String browserTZ;
    private Boolean browserPrivateMode;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;

    public static ThreeDSMethodLogDO newInstance(MessageType messageType) {
        ThreeDSMethodLogDO threeDSMethodLogDO = new ThreeDSMethodLogDO();
        threeDSMethodLogDO.setSysCreator(messageType.name());
        threeDSMethodLogDO.setCreateMillis(System.currentTimeMillis());
        return threeDSMethodLogDO;
    }

    public static ThreeDSMethodLogDO valueOf(ocean.acs.models.oracle.entity.ThreeDSMethodLog e) {
        return new ThreeDSMethodLogDO(e.getId(), e.getThreeDSMethodNotificationURL(),
                e.getBrowserAcceptHeader(), e.getBrowserUserAgent(), e.getBrowserJavaEnabled(),
                e.getBrowserLanguage(), e.getBrowserColorDepth(), e.getBrowserScreenHeight(),
                e.getBrowserScreenWidth(), e.getBrowserTZ(), e.getBrowserPrivateMode(),
                e.getSysCreator(), e.getCreateMillis(), e.getSysUpdater(), e.getUpdateMillis());
    }
    
    public static ThreeDSMethodLogDO valueOf(ocean.acs.models.sql_server.entity.ThreeDSMethodLog e) {
        return new ThreeDSMethodLogDO(e.getId(), e.getThreeDSMethodNotificationURL(),
                e.getBrowserAcceptHeader(), e.getBrowserUserAgent(), e.getBrowserJavaEnabled(),
                e.getBrowserLanguage(), e.getBrowserColorDepth(), e.getBrowserScreenHeight(),
                e.getBrowserScreenWidth(), e.getBrowserTZ(), e.getBrowserPrivateMode(),
                e.getSysCreator(), e.getCreateMillis(), e.getSysUpdater(), e.getUpdateMillis());
    }

}
