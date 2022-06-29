package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ocean.acs.commons.enumerator.MessageType;

@Data
@Builder
@AllArgsConstructor
public class ResultMeLogDO {

    private Long id;
    private Boolean criticalityIndicator;
    private Long resultLogId;
    private String messageType;
    private String data;
    private String meID;
    private String name;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();

    public static ResultMeLogDO valueOf(ocean.acs.models.oracle.entity.ResultMeLog e) {
        return new ResultMeLogDO(e.getId(), e.getCriticalityIndicator(), e.getResultLogId(), e.getMessageType(), e.getData(),
                e.getMeID(), e.getName(), e.getSysCreator(), e.getCreateMillis());
    }
    
    public static ResultMeLogDO valueOf(ocean.acs.models.sql_server.entity.ResultMeLog e) {
        return new ResultMeLogDO(e.getId(), e.getCriticalityIndicator(), e.getResultLogId(), e.getMessageType(), e.getData(),
                e.getMeID(), e.getName(), e.getSysCreator(), e.getCreateMillis());
    }

    public static ResultMeLogDO newInstance(MessageType creator) {
        return ResultMeLogDO.builder().sysCreator(creator.name())
            .createMillis(System.currentTimeMillis())
            .build();
    }

}
