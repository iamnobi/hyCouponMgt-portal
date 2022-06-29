package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class WhiteListPanDO extends OperatorInfoDO {

    private Long id;
    private Long panId;
    private String auditStatus;

    public WhiteListPanDO(Long id, Long panId, String auditStatus, String creator,
            Long createMillis, String updater, Long updateMillis, Boolean deleteFlag,
            String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.panId = panId;
        this.auditStatus = auditStatus;
    }

    public static WhiteListPanDO valueOf(ocean.acs.models.oracle.entity.WhiteListPan e) {
        return new WhiteListPanDO(e.getId(), e.getPanId(), e.getAuditStatus(), e.getCreator(),
                e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(),
                e.getDeleter(), e.getDeleteMillis());
    }
    
    public static WhiteListPanDO valueOf(ocean.acs.models.sql_server.entity.WhiteListPan e) {
        return new WhiteListPanDO(e.getId(), e.getPanId(), e.getAuditStatus(), e.getCreator(),
                e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(),
                e.getDeleter(), e.getDeleteMillis());
    }

}
