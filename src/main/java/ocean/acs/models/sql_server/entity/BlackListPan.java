package ocean.acs.models.sql_server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import ocean.acs.models.data_object.entity.BlackListPanDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_BLACK_LIST_PAN)
public class BlackListPan extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_BLACK_LIST_PAN_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_BLACK_LIST_PAN_PAN_ID)
    private Long panId;

    @NonNull
    @Column(name = DBKey.COL_BLACK_LIST_PAN_TRANS_STATUS)
    private String transStatus;

    @Column(name = DBKey.COL_BLACK_LIST_PAN_BLACK_LIST_PAN_BATCH_ID)
    private Long blackListPanBatchId;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public BlackListPan(Long id, Long panId, String transStatus, Long blackListPanBatchId,
            String auditStatus, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.panId = panId;
        this.transStatus = transStatus;
        this.blackListPanBatchId = blackListPanBatchId;
        this.auditStatus = auditStatus;
    }

    public static BlackListPan valueOf(BlackListPanDO d) {
        return new BlackListPan(d.getId(), d.getPanId(), d.getTransStatus(),
                d.getBlackListPanBatchId(), d.getAuditStatus(), d.getCreator(), d.getCreateMillis(),
                d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(),
                d.getDeleteMillis());
    }

}
