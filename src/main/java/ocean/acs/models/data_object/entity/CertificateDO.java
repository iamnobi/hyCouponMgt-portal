package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CertificateDO {

    private Long id;
    private byte[] certificate;
    private String information;
    private String issuer;
    private Long applyDate;
    private Long expiryDate;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();

    public static CertificateDO valueOf(ocean.acs.models.oracle.entity.Certificate e) {
        return new CertificateDO(e.getId(), e.getCertificate(), e.getInformation(), e.getIssuer(),
                e.getApplyDate(), e.getExpiryDate(), e.getCreateMillis());
    }

    public static CertificateDO valueOf(ocean.acs.models.sql_server.entity.Certificate e) {
        return new CertificateDO(e.getId(), e.getCertificate(), e.getInformation(), e.getIssuer(),
                e.getApplyDate(), e.getExpiryDate(), e.getCreateMillis());
    }

}
