package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.CardType;
import ocean.acs.commons.utils.StringCustomizedUtils;

import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BinRangeDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private String cardBrand;
    private Integer cardType;
    private BigInteger startBin;
    private BigInteger endBin;
    private String auditStatus;

    public BinRangeDO(
            Long id,
            Long issuerBankId,
            String cardBrand,
            Integer cardType,
            BigInteger startBin,
            BigInteger endBin,
            String auditStatus,
            String creator,
            Long createMillis,
            String updater,
            Long updateMillis,
            Boolean deleteFlag,
            String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.cardBrand = cardBrand;
        this.cardType = cardType;
        this.startBin = startBin;
        this.endBin = endBin;
        this.auditStatus = auditStatus;
    }

    public static BinRangeDO valueOf(ocean.acs.models.oracle.entity.BinRange e) {
        return new BinRangeDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getCardBrand(),
                e.getCardType(),
                e.getStartBin(),
                e.getEndBin(),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }
    
    public static BinRangeDO valueOf(ocean.acs.models.sql_server.entity.BinRange e) {
        return new BinRangeDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getCardBrand(),
                e.getCardType(),
                e.getStartBin(),
                e.getEndBin(),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }

    public static BinRangeDO newInstance(
            Long id,
            Long issuerBankId,
            String cardType,
            String endRange,
            String startRange,
            String cardBrand,
            AuditStatus auditStatus) {
        BinRangeDO binRange = BinRangeDO.builder().build();
        binRange.setIssuerBankId(issuerBankId);
        binRange.setId(id);
        binRange.setCardType(CardType.getByName(cardType).getCode());
        binRange.setEndBin(new BigInteger(endRange));
        binRange.setStartBin(new BigInteger(startRange));
        binRange.setCardBrand(cardBrand);

        if (auditStatus != AuditStatus.UNKNOWN) {
            binRange.setAuditStatus(auditStatus.getSymbol());
        }

        return binRange;
    }
}
