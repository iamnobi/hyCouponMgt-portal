package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RootCaCertificateDO {

    private Long id;
    private byte[] certificate;
    private String information;
    private String issuer;
    private Long applyDate;
    private Long expiryDate;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();

    public static RootCaCertificateDO valueOf(ocean.acs.models.oracle.entity.RootCaCertificate e) {
        return new RootCaCertificateDO(e.getId(), e.getCertificate(), e.getInformation(),
                e.getIssuer(), e.getApplyDate(), e.getExpiryDate(), e.getCreateMillis());
    }
    
    public static RootCaCertificateDO valueOf(ocean.acs.models.sql_server.entity.RootCaCertificate e) {
        return new RootCaCertificateDO(e.getId(), e.getCertificate(), e.getInformation(),
                e.getIssuer(), e.getApplyDate(), e.getExpiryDate(), e.getCreateMillis());
    }

}
