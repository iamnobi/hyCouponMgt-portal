package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CaCertificateDO extends OperatorInfoDO {

    private Long id;
    private String cardBrand;
    private Long applyMillis;
    private byte[] certificate;
    private String certificateInformation;
    private Long expireMillis;
    private String hashAlgorithm;
    private String issuer;
    private String auditStatus;


    public CaCertificateDO(Long id, String cardBrand, Long applyMillis, byte[] certificate,
            String certificateInformation, Long expireMillis, String hashAlgorithm, String issuer,
            String auditStatus, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.cardBrand = cardBrand;
        this.applyMillis = applyMillis;
        this.certificate = certificate;
        this.certificateInformation = certificateInformation;
        this.expireMillis = expireMillis;
        this.hashAlgorithm = hashAlgorithm;
        this.issuer = issuer;
        this.auditStatus = auditStatus;
    }

    public static CaCertificateDO valueOf(ocean.acs.models.oracle.entity.CaCertificate e) {
        return new CaCertificateDO(e.getId(), e.getCardBrand(), e.getApplyMillis(),
                e.getCertificate(), e.getCertificateInformation(), e.getExpireMillis(),
                e.getHashAlgorithm(), e.getIssuer(), e.getAuditStatus(), e.getCreator(),
                e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(),
                e.getDeleter(), e.getDeleteMillis());
    }
    
    public static CaCertificateDO valueOf(ocean.acs.models.sql_server.entity.CaCertificate e) {
        return new CaCertificateDO(e.getId(), e.getCardBrand(), e.getApplyMillis(),
                e.getCertificate(), e.getCertificateInformation(), e.getExpireMillis(),
                e.getHashAlgorithm(), e.getIssuer(), e.getAuditStatus(), e.getCreator(),
                e.getCreateMillis(), e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(),
                e.getDeleter(), e.getDeleteMillis());
    }

}
