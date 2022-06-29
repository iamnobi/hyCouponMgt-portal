package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SubCaCertificateDO {

    private Long id;
    private byte[] certificate;
    private String information;
    private String issuer;
    private Long applyDate;
    private Long expiryDate;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();

    public static SubCaCertificateDO valueOf(ocean.acs.models.oracle.entity.SubCaCertificate e) {
        return new SubCaCertificateDO(e.getId(), e.getCertificate(), e.getInformation(),
                e.getIssuer(), e.getApplyDate(), e.getExpiryDate(), e.getCreateMillis());
    }
    
    public static SubCaCertificateDO valueOf(ocean.acs.models.sql_server.entity.SubCaCertificate e) {
        return new SubCaCertificateDO(e.getId(), e.getCertificate(), e.getInformation(),
                e.getIssuer(), e.getApplyDate(), e.getExpiryDate(), e.getCreateMillis());
    }

}
