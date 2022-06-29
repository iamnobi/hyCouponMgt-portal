package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ocean.acs.commons.enumerator.MessageType;

@Data
@Builder
@AllArgsConstructor
public class ChallengeMeLogDO {

    private Long id;
    private Long challengeLogId;
    private String messageType;
    private Boolean criticalityIndicator;
    private String data;
    private String meID;
    private String name;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();

    public static ChallengeMeLogDO valueOf(ocean.acs.models.oracle.entity.ChallengeMeLog e) {
        return new ChallengeMeLogDO(e.getId(), e.getChallengeLogId(), e.getMessageType(), e.getCriticalityIndicator(),
                e.getData(), e.getMeID(), e.getName(), e.getSysCreator(), e.getCreateMillis());
    }
    
    public static ChallengeMeLogDO valueOf(ocean.acs.models.sql_server.entity.ChallengeMeLog e) {
        return new ChallengeMeLogDO(e.getId(), e.getChallengeLogId(), e.getMessageType(), e.getCriticalityIndicator(),
                e.getData(), e.getMeID(), e.getName(), e.getSysCreator(), e.getCreateMillis());
    }


    public static ChallengeMeLogDO newInstance(Long challengeLogId, MessageType messageType
    , String data, String meID, String name, Boolean criticalityIndicator) {
        return new ChallengeMeLogDO(null, challengeLogId,messageType.name(), criticalityIndicator,
                data, meID, name, messageType.name(), System.currentTimeMillis());
    }


}
