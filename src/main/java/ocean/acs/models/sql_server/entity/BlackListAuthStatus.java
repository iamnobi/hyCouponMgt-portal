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
import ocean.acs.models.data_object.entity.BlackListAuthStatusDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_BLACK_LIST_AUTH_STATUS)
public class BlackListAuthStatus extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_BLACK_LIST_AUTH_STATUS_ID)
    private long id;

    @Column(name = DBKey.COL_BLACK_LIST_AUTH_STATUS_ISSUER_BANK_ID)
    private Long issuerBankId;

    @Column(name = DBKey.COL_BLACK_LIST_AUTH_STATUS_CATEGORY)
    private Integer category;

    @Column(name = DBKey.COL_BLACK_LIST_AUTH_STATUS_TRANS_STATUS)
    private String transStatus;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public BlackListAuthStatus(long id, Long issuerBankId, Integer category, String transStatus,
            String auditStatus, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.category = category;
        this.transStatus = transStatus;
        this.auditStatus = auditStatus;
    }

    public static BlackListAuthStatus valueOf(BlackListAuthStatusDO d) {
        return new BlackListAuthStatus(d.getId(), d.getIssuerBankId(), d.getCategory(),
                d.getTransStatus(), d.getAuditStatus(), d.getCreator(), d.getCreateMillis(),
                d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(),
                d.getDeleteMillis());
    }

}
