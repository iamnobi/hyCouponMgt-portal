package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.CsrDO;
import ocean.acs.models.data_object.entity.CsrDO.CertType;
import ocean.acs.models.entity.OperatorInfo;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

@Data
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Csr extends OperatorInfo {

  private static final long serialVersionUID = 1L;

  @Id
  @GenericGenerator(
      name = "csr_seq_generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_csr"),
        @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
      })
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "csr_seq_generator")
  @Column(name = "ID")
  private Long id;

  @Column(name = "THREE_D_S_VERSION")
  private int threeDSVersion;

  @Column(name = "ISSUER_BANK_ID")
  private Long issuerBankId;

  @Enumerated(EnumType.STRING)
  @Column(name = "CERT_TYPE")
  private CertType certType;

  @Column(name = "CARD_BRAND")
  private String cardBrand;

  @Column(name = "CSR")
  @Lob
  private String csr;

  @Column(name = "CERTIFICATE")
  private byte[] certificate;

  @Column(name = "CERT_UPLOAD_MILLIS")
  private Long certUploadMillis;

  @Column(name = "CERT_EXPIRE_MILLIS")
  private Long certExpireMillis;

  @Column(name = "KEY_ID")
  private String keyId;

  @Column(name = "ENC_PRIVATE_KEY")
  @Lob
  private String encPrivateKey;

  @Column(name = "PUBLIC_KEY")
  @Lob
  private String publicKey;

  @Column(name = "MODULUS_SHA256_HEX")
  private String modulusSha256Hex;

  public static Csr valueOf(CsrDO e) {
    Csr csr =
        new Csr(
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
    csr.setOperatorInfo(e);
    return csr;
  }
}
