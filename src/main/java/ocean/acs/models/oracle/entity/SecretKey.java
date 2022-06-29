package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import ocean.acs.models.data_object.portal.IssuerBankRelativeData;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.SecretKeyDO;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "secret_key")
public class SecretKey extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "secret_key_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "SECRET_KEY_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secret_key_seq_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "issuer_bank_id")
    private Long issuerBankId;

    @Column(name = "card_brand")
    private String cardBrand;

    @Column(name = "key_a")
    private String keyA;

    @Column(name = "key_b")
    private String KeyB;

    @Column(name = "audit_status")
    private String auditStatus;

    public SecretKey(Long id, Long issuerBankId, String cardBrand, String keyA, String keyB,
            String auditStatus, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.cardBrand = cardBrand;
        this.keyA = keyA;
        this.KeyB = keyB;
        this.auditStatus = auditStatus;
    }

    public static SecretKey valueOf(SecretKeyDO d) {
        return new SecretKey(d.getId(), d.getIssuerBankId(), d.getCardBrand(), d.getKeyA(),
                d.getKeyB(), d.getAuditStatus(), d.getCreator(), d.getCreateMillis(),
                d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(),
                d.getDeleteMillis());
    }

}
