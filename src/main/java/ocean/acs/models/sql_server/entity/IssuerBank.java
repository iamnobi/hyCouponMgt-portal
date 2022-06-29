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
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.portal.PortalIssuerBankDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_ISSUER_BANK)
public class IssuerBank extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_ISSUER_BANK_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_ISSUER_BANK_CODE)
    private String code;

    @NonNull
    @Column(name = DBKey.COL_ISSUER_BANK_NAME)
    private String name;

    @NonNull
    @Column(name = DBKey.COL_ISSUER_BANK_SYMMETRIC_KEY_ID)
    private String symmetricKeyId;

    @NonNull
    @Column(name = DBKey.COL_ISSUER_BANK_SENSITIVE_DATA_KEY_ID)
    private String sensitiveDataKeyId;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public IssuerBank(Long id, String code, String name, String symmetricKeyId, String sensitiveDataKeyId, String auditStatus,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.code = code;
        this.name = name;
        this.symmetricKeyId = symmetricKeyId;
        this.sensitiveDataKeyId = sensitiveDataKeyId;
        this.auditStatus = auditStatus;
    }

    public static IssuerBank valueOf(IssuerBankDO d) {
        return new IssuerBank(d.getId(), d.getCode(), d.getName(), d.getSymmetricKeyId(), d.getSensitiveDataKeyId(),
                d.getAuditStatus(), d.getCreator(), d.getCreateMillis(), d.getUpdater(),
                d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }

    public static IssuerBank valueOf(PortalIssuerBankDO issuerBankDO) {
        IssuerBank entity = new IssuerBank();
        entity.setCode(issuerBankDO.getCode());
        entity.setName(issuerBankDO.getName());
        entity.setSymmetricKeyId(issuerBankDO.getSymmetricKeyId());
        entity.setSensitiveDataKeyId(issuerBankDO.getSensitiveDataKeyId());
        entity.setCreator(issuerBankDO.getUser());
        entity.setAuditStatus(issuerBankDO.getAuditStatus().getSymbol());
        entity.setCreateMillis(System.currentTimeMillis());
        if (issuerBankDO.getAuditStatus() != null) {
            entity.setAuditStatus(issuerBankDO.getAuditStatus().getSymbol());
        }
        return entity;
    }

}
