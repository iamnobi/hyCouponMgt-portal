package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ocean.acs.commons.enumerator.MessageType;

@Data
@Builder
@AllArgsConstructor
public class SdkUiTypeLogDO {

    private Long id;
    private Long authenticationLogID;
    private String sdkUiType;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();

    public static SdkUiTypeLogDO valueOf(ocean.acs.models.oracle.entity.SdkUiTypeLog e) {
        return new SdkUiTypeLogDO(e.getId(), e.getAuthenticationLogID(), e.getSdkUiType(),
                e.getSysCreator(), e.getCreateMillis());
    }
    
    public static SdkUiTypeLogDO valueOf(ocean.acs.models.sql_server.entity.SdkUiTypeLog e) {
        return new SdkUiTypeLogDO(e.getId(), e.getAuthenticationLogID(), e.getSdkUiType(),
                e.getSysCreator(), e.getCreateMillis());
    }

    public static SdkUiTypeLogDO newInstance(MessageType messageType) {
        return SdkUiTypeLogDO.builder().sysCreator(messageType.name())
                .createMillis(System.currentTimeMillis()).build();
    }
}
