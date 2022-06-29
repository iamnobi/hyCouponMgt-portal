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
import ocean.acs.models.data_object.entity.WhiteListPanDO;
import ocean.acs.models.data_object.portal.WhiteListPanCreateDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_WHITE_LIST_PAN)
public class WhiteListPan extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_WHITE_LIST_PAN_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_WHITE_LIST_PAN_PAN_ID)
    private Long panId;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public WhiteListPan(Long id, Long panId, String auditStatus, String creator, Long createMillis,
            String updater, Long updateMillis, Boolean deleteFlag, String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.panId = panId;
        this.auditStatus = auditStatus;
    }

    public static WhiteListPan valueOf(WhiteListPanDO d) {
        return new WhiteListPan(d.getId(), d.getPanId(), d.getAuditStatus(), d.getCreator(),
                d.getCreateMillis(), d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(),
                d.getDeleter(), d.getDeleteMillis());
    }

    public static WhiteListPan valueOf(WhiteListPanCreateDO whiteListPanCreateDO, Long panInfoId) {
        return new WhiteListPan(null, panInfoId, whiteListPanCreateDO.getAuditStatus().getSymbol(),
                whiteListPanCreateDO.getUser(), System.currentTimeMillis(), null, null,
                Boolean.FALSE, null, null);
    }

}
