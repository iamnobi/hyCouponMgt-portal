package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class OtpSendingSettingDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private Boolean orgEnable;
    private Boolean bankEnable;
    private String bankUrl;
    private byte[] jweRsaPublicKey;
    private String jwsSecretKey;
    private String auditStatus;

    public OtpSendingSettingDO(Long id, Long issuerBankId, Boolean orgEnable, Boolean bankEnable,
            String bankUrl, byte[] jweRsaPublicKey, String jwsSecretKey, String auditStatus,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.orgEnable = orgEnable;
        this.bankEnable = bankEnable;
        this.bankUrl = bankUrl;
        this.jweRsaPublicKey = jweRsaPublicKey;
        this.jwsSecretKey = jwsSecretKey;
        this.auditStatus = auditStatus;
    }

    public static OtpSendingSettingDO valueOf(ocean.acs.models.oracle.entity.OtpSendingSetting e) {
        return new OtpSendingSettingDO(e.getId(), e.getIssuerBankId(), e.getOrgEnable(),
                e.getBankEnable(), e.getBankUrl(), e.getJweRsaPublicKey(), e.getJwsSecretKey(),
                e.getAuditStatus(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }
    
    public static OtpSendingSettingDO valueOf(ocean.acs.models.sql_server.entity.OtpSendingSetting e) {
        return new OtpSendingSettingDO(e.getId(), e.getIssuerBankId(), e.getOrgEnable(),
                e.getBankEnable(), e.getBankUrl(), e.getJweRsaPublicKey(), e.getJwsSecretKey(),
                e.getAuditStatus(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

}
