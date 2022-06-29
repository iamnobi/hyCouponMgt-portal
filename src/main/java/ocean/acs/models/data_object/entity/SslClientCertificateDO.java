package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.Activity;
import ocean.acs.commons.enumerator.KeyStatus;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class SslClientCertificateDO extends OperatorInfoDO {

    private Long id;
    private String cardBrand;
    private Boolean hasCert;
    private Long applyMillis;
    private byte[] certificate;
    private String certificateInformation;
    private Long expireMillis;
    private String hashAlgorithm;
    private String issuer;
    private String privateKey;
    private String publicKey;
    private String keyPassword;
    private Boolean manualPassword;
    private KeyStatus keyStatus;
    private Activity activity;
    private String auditStatus;

    public SslClientCertificateDO(Long id, String cardBrand, Boolean hasCert, Long applyMillis,
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

    public static SslClientCertificateDO valueOf(
            ocean.acs.models.oracle.entity.SslClientCertificate e) {
        return new SslClientCertificateDO(e.getId(), e.getCardBrand(), e.getHasCert(),
                e.getApplyMillis(), e.getCertificate(), e.getCertificateInformation(),
                e.getExpireMillis(), e.getHashAlgorithm(), e.getIssuer(), e.getPrivateKey(),
                e.getPublicKey(), e.getKeyPassword(), e.getManualPassword(), e.getKeyStatus(),
                e.getActivity(), e.getAuditStatus(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }

    public static SslClientCertificateDO valueOf(
            ocean.acs.models.sql_server.entity.SslClientCertificate e) {
        return new SslClientCertificateDO(e.getId(), e.getCardBrand(), e.getHasCert(),
                e.getApplyMillis(), e.getCertificate(), e.getCertificateInformation(),
                e.getExpireMillis(), e.getHashAlgorithm(), e.getIssuer(), e.getPrivateKey(),
                e.getPublicKey(), e.getKeyPassword(), e.getManualPassword(), e.getKeyStatus(),
                e.getActivity(), e.getAuditStatus(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }

    public Boolean isExpired() {
        if (this.expireMillis == null) {
            return false;
        }
        return expireMillis < System.currentTimeMillis();
    }

}
