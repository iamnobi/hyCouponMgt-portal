package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import ocean.acs.models.data_object.entity.CaCertificateDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_CA_CERTIFICATE)
public class CaCertificate extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "ca_certificate_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "CA_CERTIFICATE_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ca_certificate_seq_generator")
    @Column(name = DBKey.COL_CA_CERTIFICATE_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_CA_CERTIFICATE_CARD_BRAND)
    private String cardBrand;

    @Column(name = DBKey.COL_CA_CERTIFICATE_APPLY_MILLIS)
    private Long applyMillis;

    @NonNull
    @Column(name = DBKey.COL_CA_CERTIFICATE_CERTIFICATE)
    private byte[] certificate;

    @Column(name = DBKey.COL_CA_CERTIFICATE_CERTIFICATE_INFORMATION)
    private String certificateInformation;

    @Column(name = DBKey.COL_CA_CERTIFICATE_EXPIRE_MILLIS)
    private Long expireMillis;

    @Column(name = DBKey.COL_CA_CERTIFICATE_HASH_ALGORITHM)
    private String hashAlgorithm;

    @Column(name = DBKey.COL_CA_CERTIFICATE_ISSUER)
    private String issuer;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public CaCertificate(Long id, String cardBrand, Long applyMillis, byte[] certificate,
            String certificateInformation, Long expireMillis, String hashAlgorithm, String issuer,
            String auditStatus, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.cardBrand = cardBrand;
        this.applyMillis = applyMillis;
        this.certificate = certificate;
        this.certificateInformation = certificateInformation;
        this.expireMillis = expireMillis;
        this.hashAlgorithm = hashAlgorithm;
        this.issuer = issuer;
        this.auditStatus = auditStatus;
    }

    public static CaCertificate valueOf(CaCertificateDO d) {
        return new CaCertificate(d.getId(), d.getCardBrand(), d.getApplyMillis(),
                d.getCertificate(), d.getCertificateInformation(), d.getExpireMillis(),
                d.getHashAlgorithm(), d.getIssuer(), d.getAuditStatus(), d.getCreator(),
                d.getCreateMillis(), d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(),
                d.getDeleter(), d.getDeleteMillis());
    }

}
