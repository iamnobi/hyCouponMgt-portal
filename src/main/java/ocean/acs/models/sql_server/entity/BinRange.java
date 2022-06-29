package ocean.acs.models.sql_server.entity;

import java.math.BigInteger;
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
import lombok.NonNull;
import ocean.acs.commons.enumerator.CardType;
import ocean.acs.models.data_object.entity.BinRangeDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_BIN_RANGE)
public class BinRange extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_BIN_RANGE_ID)
    private Long id;

    @Column(name = DBKey.COL_BIN_RANGE_ISSUER_BANK_ID)
    private Long issuerBankId;

    @Column(name = DBKey.COL_BIN_RANGE_CARD_BRAND)
    private String cardBrand;

    @Column(name = DBKey.COL_BIN_RANGE_CARD_TYPE)
    private Integer cardType;

    @Column(name = DBKey.COL_BIN_RANGE_START_BIN)
    private BigInteger startBin;

    @NonNull
    @Column(name = DBKey.COL_BIN_RANGE_END_BIN)
    private BigInteger endBin;

    @NonNull
    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public BinRange(Long id, Long issuerBankId, String cardBrand, Integer cardType,
            BigInteger startBin, BigInteger endBin, String auditStatus, String creator,
            Long createMillis, String updater, Long updateMillis, Boolean deleteFlag,
            String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.cardBrand = cardBrand;
        this.cardType = cardType;
        this.startBin = startBin;
        this.endBin = endBin;
        this.auditStatus = auditStatus;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType.getCode();
    }

    public static BinRange valueOf(BinRangeDO d) {
        return new BinRange(d.getId(), d.getIssuerBankId(), d.getCardBrand(), d.getCardType(),
                d.getStartBin(), d.getEndBin(), d.getAuditStatus(), d.getCreator(),
                d.getCreateMillis(), d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(),
                d.getDeleter(), d.getDeleteMillis());
    }

}
