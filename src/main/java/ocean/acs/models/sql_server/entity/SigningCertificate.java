package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.SigningCertificateDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_SIGNING_CERTIFICATE)
public class SigningCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_SIGNING_CERTIFICATE_ID)
    private Long id;

    @Column(name = DBKey.COL_SIGNING_CERTIFICATE_THREE_D_S_VERSION)
    private int threeDSVersion;

    @NotNull
    @Column(name = DBKey.COL_SIGNING_CERTIFICATE_CARD_BRAND)
    private String cardBrand;

    @NotNull
    @Column(name = DBKey.COL_SIGNING_CERTIFICATE_ISSUER_BANK_ID)
    private Long issuerBankId;

    @Column(name = DBKey.COL_SIGNING_CERTIFICATE_CURRENT_CERTIFICATE_ID)
    private Long currentCertificateId;

    @Column(name = DBKey.COL_SIGNING_CERTIFICATE_CURRENT_SUB_CA_CERTIFICATE_ID)
    private Long currentSubCaCertificateId;

    @Column(name = DBKey.COL_SIGNING_CERTIFICATE_CURRENT_ROOT_CA_CERTIFICATE_ID)
    private Long currentRootCaCertificateId;

    @Column(name = DBKey.COL_SIGNING_CERTIFICATE_CURRENT_SIGNING_RSA_KEY_ID)
    private Long currentRsaKeyId;

    @NotNull
    @Column(name = DBKey.COL_SIGNING_CERTIFICATE_CREATE_MILLIS)
    private Long createMillis;

    @NotNull
    @Column(name = DBKey.COL_SIGNING_CERTIFICATE_UPDATE_MILLIS)
    private Long updateMillis;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public static SigningCertificate valueOf(SigningCertificateDO d) {
        return new SigningCertificate(d.getId(), d.getThreeDSVersion(), d.getCardBrand(), d.getIssuerBankId(),
                d.getCurrentCertificateId(), d.getCurrentSubCaCertificateId(),
                d.getCurrentRootCaCertificateId(), d.getCurrentRsaKeyId(), d.getCreateMillis(),
                d.getUpdateMillis(), d.getAuditStatus());
    }

}
