package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BlackListPanDO extends OperatorInfoDO {

    private Long id;
    private Long panId;
    private String transStatus;
    private Long blackListPanBatchId;
    private String auditStatus;

    public BlackListPanDO(Long id, Long panId, String transStatus, Long blackListPanBatchId,
            String auditStatus, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.panId = panId;
        this.transStatus = transStatus;
        this.blackListPanBatchId = blackListPanBatchId;
        this.auditStatus = auditStatus;
    }

    public static BlackListPanDO valueOf(ocean.acs.models.oracle.entity.BlackListPan e) {
        return new BlackListPanDO(e.getId(), e.getPanId(), e.getTransStatus(),
                e.getBlackListPanBatchId(), e.getAuditStatus(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }
    
    public static BlackListPanDO valueOf(ocean.acs.models.sql_server.entity.BlackListPan e) {
        return new BlackListPanDO(e.getId(), e.getPanId(), e.getTransStatus(),
                e.getBlackListPanBatchId(), e.getAuditStatus(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }

}
