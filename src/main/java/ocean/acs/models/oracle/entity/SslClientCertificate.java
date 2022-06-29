package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.commons.enumerator.Activity;
import ocean.acs.commons.enumerator.KeyStatus;
import ocean.acs.models.data_object.entity.SslClientCertificateDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_SSL_CLIENT_CERTIFICATE)
public class SslClientCertificate extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "ssl_client_certificate_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "SSL_CLIENT_CERTIFICATE_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ssl_client_certificate_seq_generator")
    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_ID)
    private Long id;

    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_CARD_BRAND)
    private String cardBrand;

    @NonNull
    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_HAS_CERT)
    private Boolean hasCert = false;

    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_APPLY_MILLIS)
    private Long applyMillis;

    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_CERTIFICATE)
    private byte[] certificate;

    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_CERTIFICATE_INFORMATION)
    private String certificateInformation;

    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_EXPIRE_MILLIS)
    private Long expireMillis;

    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_HASH_ALGORITHM)
    private String hashAlgorithm;

    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_ISSUER)
    private String issuer;

    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_PRIVATE_KEY)
    private String privateKey;

    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_PUBLIC_KEY)
    private String publicKey;

    @Column(name = "key_password")
    private String keyPassword;

    @Column(name = "manual_password")
    private Boolean manualPassword = false;

    @NonNull
    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_KEY_STATUS)
    private KeyStatus keyStatus = KeyStatus.INIT;

    @NonNull
    @Enumerated
    @Column(name = DBKey.COL_SSL_CLIENT_CERTIFICATE_ACTIVITY)
    private Activity activity = Activity.DISABLED;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public Boolean isExpired() {
        if (this.expireMillis == null) {
            return false;
        }
        return expireMillis < System.currentTimeMillis();
    }

    public SslClientCertificate(Long id, String cardBrand, Boolean hasCert, Long applyMillis,
            byte[] certificate, String certificateInformation, Long expireMillis,
            String hashAlgorithm, String issuer, String privateKey, String publicKey,
            String keyPassword, Boolean manualPassword, KeyStatus keyStatus, Activity activity,
            String auditStatus, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.cardBrand = cardBrand;
        this.hasCert = hasCert;
        this.applyMillis = applyMillis;
        this.certificate = certificate;
        this.certificateInformation = certificateInformation;
        this.expireMillis = expireMillis;
        this.hashAlgorithm = hashAlgorithm;
        this.issuer = issuer;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.keyPassword = keyPassword;
        this.manualPassword = manualPassword;
        this.keyStatus = keyStatus;
        this.activity = activity;
        this.auditStatus = auditStatus;
    }

    public static SslClientCertificate valueOf(SslClientCertificateDO d) {
        return new SslClientCertificate(d.getId(), d.getCardBrand(), d.getHasCert(),
                d.getApplyMillis(), d.getCertificate(), d.getCertificateInformation(),
                d.getExpireMillis(), d.getHashAlgorithm(), d.getIssuer(), d.getPrivateKey(),
                d.getPublicKey(), d.getKeyPassword(), d.getManualPassword(), d.getKeyStatus(),
                d.getActivity(), d.getAuditStatus(), d.getCreator(), d.getCreateMillis(),
                d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(),
                d.getDeleteMillis());
    }

}
