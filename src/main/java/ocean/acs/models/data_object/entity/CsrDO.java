package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ocean.acs.models.oracle.entity.Csr;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
public class CsrDO extends OperatorInfoDO {
  private Long id;
  private int threeDSVersion;
  private Long issuerBankId;
  private CertType certType;
  private String cardBrand;
  private String csr;
  private byte[] certificate;
  private Long certUploadMillis;
  private Long certExpireMillis;
  private String keyId;
  private String encPrivateKey;
  private String publicKey;
  private String modulusSha256Hex;

  public enum CertType {
    SIGNING,
    SSL_CLIENT
  }

  public static CsrDO valueOf(Csr e) {
    CsrDO csrDO =
        new CsrDO(
            e.getId(),
            e.getThreeDSVersion(),
            e.getIssuerBankId(),
            e.getCertType(),
            e.getCardBrand(),
            e.getCsr(),
            e.getCertificate(),
            e.getCertUploadMillis(),
            e.getCertExpireMillis(),
            e.getKeyId(),
            e.getEncPrivateKey(),
            e.getPublicKey(),
            e.getModulusSha256Hex());
    csrDO.setOperatorInfo(e);
    return csrDO;
  }
}
