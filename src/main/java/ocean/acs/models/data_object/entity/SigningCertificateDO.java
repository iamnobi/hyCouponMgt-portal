package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SigningCertificateDO {

    private Long id;
    private int threeDSVersion;
    private String cardBrand;
    private Long issuerBankId;
    private Long currentCertificateId;
    private Long currentSubCaCertificateId;
    private Long currentRootCaCertificateId;
    private Long currentRsaKeyId;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private Long updateMillis;
    private String auditStatus;

    public static SigningCertificateDO valueOf(
            ocean.acs.models.oracle.entity.SigningCertificate e) {
        return new SigningCertificateDO(e.getId(), e.getThreeDSVersion(), e.getCardBrand(), e.getIssuerBankId(),
                e.getCurrentCertificateId(), e.getCurrentSubCaCertificateId(),
                e.getCurrentRootCaCertificateId(), e.getCurrentRsaKeyId(), e.getCreateMillis(),
                e.getUpdateMillis(), e.getAuditStatus());
    }
    
    public static SigningCertificateDO valueOf(
            ocean.acs.models.sql_server.entity.SigningCertificate e) {
        return new SigningCertificateDO(e.getId(), e.getThreeDSVersion(), e.getCardBrand(), e.getIssuerBankId(),
                e.getCurrentCertificateId(), e.getCurrentSubCaCertificateId(),
                e.getCurrentRootCaCertificateId(), e.getCurrentRsaKeyId(), e.getCreateMillis(),
                e.getUpdateMillis(), e.getAuditStatus());
    }

}
