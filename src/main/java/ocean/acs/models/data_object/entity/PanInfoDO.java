package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.CardStatus;
import ocean.acs.commons.utils.MaskUtils;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PanInfoDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private String cardBrand;
    private String cardNumber;
    private String cardNumberHash;
    private String cardNumberEn;
    private Boolean threeDSVerifyEnable;
    private String previousTransactionDeviceId;
    private Boolean previousTransactionSuccess;
    private CardStatus cardStatus;
    private String auditStatus;

    public PanInfoDO(
            Long id,
            Long issuerBankId,
            String cardBrand,
            String cardNumber,
            String cardNumberHash,
            String cardNumberEn,
            Boolean threeDSVerifyEnable,
            String previousTransactionDeviceId,
            Boolean previousTransactionSuccess,
            CardStatus cardStatus,
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
        this.cardNumber = cardNumber;
        this.cardNumberHash = cardNumberHash;
        this.cardNumberEn = cardNumberEn;
        this.threeDSVerifyEnable = threeDSVerifyEnable;
        this.previousTransactionDeviceId = previousTransactionDeviceId;
        this.previousTransactionSuccess = previousTransactionSuccess;
        this.cardStatus = cardStatus;
        this.auditStatus = auditStatus;
    }

    public PanInfoDO(Long issuerBankId, String cardNumber, String cardBrand) {
        super();
        this.issuerBankId = issuerBankId;
        this.threeDSVerifyEnable = true;
        this.cardNumber = MaskUtils.acctNumberMask(cardNumber);
        this.cardBrand = cardBrand;
        this.cardStatus = CardStatus.NORMAL;
    }

    public static PanInfoDO valueOf(ocean.acs.models.oracle.entity.PanInfo e) {
        return new PanInfoDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getCardBrand(),
                e.getCardNumber(),
                e.getCardNumberHash(),
                e.getCardNumberEn(),
                e.getThreeDSVerifyEnable(),
                e.getPreviousTransactionDeviceId(),
                e.getPreviousTransactionSuccess(),
                CardStatus.getByCode(e.getCardStatus()),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }

    public static PanInfoDO valueOf(ocean.acs.models.sql_server.entity.PanInfo e) {
        return new PanInfoDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getCardBrand(),
                e.getCardNumber(),
                e.getCardNumberHash(),
                e.getCardNumberEn(),
                e.getThreeDSVerifyEnable(),
                e.getPreviousTransactionDeviceId(),
                e.getPreviousTransactionSuccess(),
                CardStatus.getByCode(e.getCardStatus()),
                e.getAuditStatus(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }

    public static PanInfoDO newInstance(Long issuerBankId, String acctNumber, String cardBrand) {
        return PanInfoDO.builder()
                .issuerBankId(issuerBankId)
                .threeDSVerifyEnable(true)
                .cardNumber(MaskUtils.acctNumberMask(acctNumber))
                .cardBrand(cardBrand)
                .cardStatus(CardStatus.NORMAL)
                .auditStatus("P") // 放行
                .build();
    }
}
