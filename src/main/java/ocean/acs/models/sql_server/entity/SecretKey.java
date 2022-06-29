package ocean.acs.models.sql_server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
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
public class SecretKey extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
