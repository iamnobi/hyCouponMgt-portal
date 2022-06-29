package ocean.acs.models.oracle.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.portal.PortalIssuerBankDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_ISSUER_BANK)
public class IssuerBank extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "issuer_bank_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "ISSUER_BANK_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issuer_bank_seq_generator")
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

    @Column(name = "THREE_D_S_METHOD_URL")
    private String threeDSMethodUrl;

    @Column(name = "ACS_URL")
    private String acsUrl;

    @Column(name = "ACS_REF_NUMBER")
    private String acsRefNumber;

    @Column(name = "ACS_OPERATOR_ID")
    private String acsOperatorId;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public IssuerBank(Long id, String code, String name, String symmetricKeyId, String sensitiveDataKeyId,
            String threeDSMethodUrl, String acsUrl, String acsRefNumber,
            String auditStatus, String creator,
            Long createMillis, String updater, Long updateMillis, Boolean deleteFlag,
            String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.code = code;
        this.name = name;
        this.symmetricKeyId = symmetricKeyId;
        this.sensitiveDataKeyId = sensitiveDataKeyId;
        this.threeDSMethodUrl = threeDSMethodUrl;
        this.acsUrl = acsUrl;
        this.acsRefNumber = acsRefNumber;
        this.auditStatus = auditStatus;
    }

    public static IssuerBank valueOf(IssuerBankDO d) {
        return new IssuerBank(d.getId(), d.getCode(), d.getName(), d.getSymmetricKeyId(), d.getSensitiveDataKeyId(),
                d.getThreeDSMethodUrl(), d.getAcsUrl(), d.getAcsRefNumber(),
                d.getAuditStatus(),
                d.getCreator(), d.getCreateMillis(), d.getUpdater(), d.getUpdateMillis(),
                d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }

    public static IssuerBank valueOf(PortalIssuerBankDO issuerBankDO) {
        IssuerBank entity = new IssuerBank();
        entity.setCode(issuerBankDO.getCode());
        entity.setName(issuerBankDO.getName());
        entity.setSymmetricKeyId(issuerBankDO.getSymmetricKeyId());
        entity.setSensitiveDataKeyId(issuerBankDO.getSensitiveDataKeyId());
        entity.setThreeDSMethodUrl(issuerBankDO.getThreeDSMethodUrl());
        entity.setAcsUrl(issuerBankDO.getAcsUrl());
        entity.setAcsRefNumber(issuerBankDO.getAcsRefNumber());
        entity.setAcsOperatorId(issuerBankDO.getAcsOperatorId());
        entity.setAuditStatus(issuerBankDO.getAuditStatus().getSymbol());
        entity.setCreator(issuerBankDO.getUser());
        entity.setCreateMillis(System.currentTimeMillis());
        if (issuerBankDO.getAuditStatus() != null) {
            entity.setAuditStatus(issuerBankDO.getAuditStatus().getSymbol());
        }
        return entity;
    }

}
