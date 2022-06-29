package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * BankDataKeyDO
 *
 * @author Alan Chen
 */
@Data
@Builder
@AllArgsConstructor
public class BankDataKeyDO {

    private Long id;
    private Long issuerBankId;
    private String rsaPrivateKyId;
    private String rsaPublicKy;
    private String creator;
    private Long createMillis;
    private String updater;
    private Long updateMillis;

    public BankDataKeyDO(Long issuerBankId, String kyId, String rsaPubKyBase64) {
        this.issuerBankId = issuerBankId;
        this.rsaPrivateKyId = kyId;
        this.rsaPublicKy = rsaPubKyBase64;
    }

    public static BankDataKeyDO valueOf(ocean.acs.models.oracle.entity.BankDataKey e) {
        return BankDataKeyDO.builder()
          .id(e.getId())
          .issuerBankId(e.getIssuerBankId())
          .rsaPrivateKyId(e.getRsaPrivateKyId())
          .rsaPublicKy(e.getRsaPublicKy())
          .creator(e.getCreator())
          .createMillis(e.getCreateMillis())
          .updater(e.getUpdater())
          .updateMillis(e.getUpdateMillis())
          .build();
    }
    
    public static BankDataKeyDO valueOf(ocean.acs.models.sql_server.entity.BankDataKey e) {
        return BankDataKeyDO.builder()
          .id(e.getId())
          .issuerBankId(e.getIssuerBankId())
          .rsaPrivateKyId(e.getRsaPrivateKyId())
          .rsaPublicKy(e.getRsaPublicKy())
          .creator(e.getCreator())
          .createMillis(e.getCreateMillis())
          .updater(e.getUpdater())
          .updateMillis(e.getUpdateMillis())
          .build();
    }
}
