package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class IssuerBankDO extends OperatorInfoDO {

    private Long id;
    private String code;
    private String name;
    private String symmetricKeyId;
    private String sensitiveDataKeyId;
    private String threeDSMethodUrl;
    private String acsUrl;
    private String acsRefNumber;
    private String acsOperatorId;
    private String auditStatus;

    public IssuerBankDO(Long id, String code, String name, String symmetricKeyId, String sensitiveDataKeyId,
            String threeDSMethodUrl, String acsUrl, String acsRefNumber, String acsOperatorId,
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
        this.acsOperatorId = acsOperatorId;
        this.auditStatus = auditStatus;
    }

    public static IssuerBankDO valueOf(ocean.acs.models.oracle.entity.IssuerBank e) {
        return new IssuerBankDO(e.getId(), e.getCode(), e.getName(), e.getSymmetricKeyId(), e.getSensitiveDataKeyId(),
                e.getThreeDSMethodUrl(), e.getAcsUrl(), e.getAcsRefNumber(), e.getAcsOperatorId(),
                e.getAuditStatus(),
                e.getCreator(), e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(),
                e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }
    
    public static IssuerBankDO valueOf(ocean.acs.models.sql_server.entity.IssuerBank e) {
        return null;
//        return new IssuerBankDO(e.getId(), e.getCode(), e.getName(), e.getSymmetricKeyId(), e.getSensitiveDataKeyId(), e.getAuditStatus(),
//                e.getCreator(), e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(),
//                e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

}
