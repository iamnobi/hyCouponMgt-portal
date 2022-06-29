package ocean.acs.models.sql_server.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.PanInfoDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_PAN_INFO)
public class PanInfo extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_PAN_INFO_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_PAN_INFO_ISSUER_BANK_ID)
    private Long issuerBankId;

    @Column(name = DBKey.COL_PAN_INFO_CARD_BRAND)
    private String cardBrand;

    @NonNull
    @Column(name = DBKey.COL_PAN_INFO_CARD_NUMBER)
    private String cardNumber;

    @Column(name = DBKey.COL_PAN_INFO_CARD_NUMBER_HASH)
    private String cardNumberHash;

    @Column(name = DBKey.COL_PAN_INFO_CARD_NUMBER_EN)
    private String cardNumberEn;

    @Column(name = DBKey.COL_PAN_INFO_THREE_D_S_VERIFY_ENABLED)
    private Boolean threeDSVerifyEnable = Boolean.FALSE;

    @Column(name = DBKey.COL_PAN_INFO_PREVIOUS_TXN_DEVICE_ID)
    private String previousTransactionDeviceId;

    @Column(name = DBKey.COL_PAN_INFO_PREVIOUS_TXN_SUCCESS)
    private Boolean previousTransactionSuccess;

    @Column(name = DBKey.COL_PAN_INFO_CARD_STATUS)
    private String cardStatus;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public PanInfo(
            Long id,
            Long issuerBankId,
            String cardBrand,
            String cardNumber,
            String cardNumberHash,
            String cardNumberEn,
            Boolean threeDSVerifyEnable,
            String previousTransactionDeviceId,
            Boolean previousTransactionSuccess,
            String cardStatus,
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

    public static PanInfo valueOf(PanInfoDO d) {
        return new PanInfo(
                d.getId(),
                d.getIssuerBankId(),
                d.getCardBrand(),
                d.getCardNumber(),
                d.getCardNumberHash(),
                d.getCardNumberEn(),
                d.getThreeDSVerifyEnable(),
                d.getPreviousTransactionDeviceId(),
                d.getPreviousTransactionSuccess(),
                d.getCardStatus().getCode(),
                d.getAuditStatus(),
                d.getCreator(),
                d.getCreateMillis(),
                d.getUpdater(),
                d.getUpdateMillis(),
                d.getDeleteFlag(),
                d.getDeleter(),
                d.getDeleteMillis());
    }
}
