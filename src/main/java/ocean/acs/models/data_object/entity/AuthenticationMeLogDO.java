package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ocean.acs.commons.enumerator.MessageType;

@Data
@Builder
@AllArgsConstructor
public class AuthenticationMeLogDO {

    private Long id;
    private Boolean criticalityIndicator;
    private Long authenticationLogId;
    private String messageType;
    private String data;
    private String meID;
    private String name;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();

    public static AuthenticationMeLogDO valueOf(ocean.acs.models.oracle.entity.AuthenticationMeLog e) {
        return new AuthenticationMeLogDO(e.getId(), e.getCriticalityIndicator(), e.getAuthenticationLogId(), e.getMessageType(),
                e.getData(), e.getMeID(), e.getName(), e.getSysCreator(), e.getCreateMillis());
    }
    
    public static AuthenticationMeLogDO valueOf(ocean.acs.models.sql_server.entity.AuthenticationMeLog e) {
        return new AuthenticationMeLogDO(e.getId(), e.getCriticalityIndicator(), e.getAuthenticationLogId(), e.getMessageType(),
                e.getData(), e.getMeID(), e.getName(), e.getSysCreator(), e.getCreateMillis());
    }

    public static AuthenticationMeLogDO newInstance(MessageType aReq) {
        return AuthenticationMeLogDO.builder().sysCreator(aReq.name())
                .createMillis(System.currentTimeMillis())
                .build();
    }
}
