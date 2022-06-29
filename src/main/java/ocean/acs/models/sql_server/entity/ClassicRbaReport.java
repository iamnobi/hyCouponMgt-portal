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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.ClassicRbaReportDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = DBKey.TABLE_CLASSIC_RBA_REPORT)
public class ClassicRbaReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_ID)
    private Long id;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_YEAR)
    private Integer year;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_MONTH)
    private Integer month;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_DAY_OF_MONTH)
    private Integer dayOfMonth;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BIN_RANGE_F)
    private Long binRangeFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BIN_RANGE_C)
    private Long binRangeChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BIN_RANGE_R)
    private Long binRangeReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLOCK_CODE_F)
    private Long blockCodeFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLOCK_CODE_C)
    private Long blockCodeChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLOCK_CODE_R)
    private Long blockCodeReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_PURCHASE_AMOUNT_F)
    private Long purchaseAmountFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_PURCHASE_AMOUNT_C)
    private Long purchaseAmountChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_PURCHASE_AMOUNT_R)
    private Long purchaseAmountReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_CARDHOLDER_DATA_F)
    private Long cardholderDataFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_CARDHOLDER_DATA_C)
    private Long cardholderDataChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_CARDHOLDER_DATA_R)
    private Long cardholderDataReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_CUMULATIVE_AMOUNT_F)
    private Long cumulativeAmountFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_CUMULATIVE_AMOUNT_C)
    private Long cumulativeAmountChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_CUMULATIVE_AMOUNT_R)
    private Long cumulativeAmountReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_CUMULATIVE_TRANSACTION_F)
    private Long cumulativeTransactionFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_CUMULATIVE_TRANSACTION_C)
    private Long cumulativeTransactionChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_CUMULATIVE_TRANSACTION_R)
    private Long cumulativeTransactionReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_LOCATION_CONSISTENCY_F)
    private Long locationConsistencyFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_LOCATION_CONSISTENCY_C)
    private Long locationConsistencyChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_LOCATION_CONSISTENCY_R)
    private Long locationConsistencyReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BROWSER_LANGUAGE_F)
    private Long browserLanguageFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BROWSER_LANGUAGE_C)
    private Long browserLanguageChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BROWSER_LANGUAGE_R)
    private Long browserLanguageReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_VPN_F)
    private Long vpnFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_VPN_C)
    private Long vpnChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_VPN_R)
    private Long vpnReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_PROXY_F)
    private Long proxyFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_PROXY_C)
    private Long proxyChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_PROXY_R)
    private Long proxyReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_PRIVATE_BROWSING_F)
    private Long privateBrowsingFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_PRIVATE_BROWSING_C)
    private Long privateBrowsingChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_PRIVATE_BROWSING_R)
    private Long privateBrowsingReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_DEVICE_VARIATION_F)
    private Long deviceVariationFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_DEVICE_VARIATION_C)
    private Long deviceVariationChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_DEVICE_VARIATION_R)
    private Long deviceVariationReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_MCC_F)
    private Long mccFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_MCC_C)
    private Long mccChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_MCC_R)
    private Long mccReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_RECURRING_PAYMENT_F)
    private Long recurringPaymentFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_RECURRING_PAYMENT_C)
    private Long recurringPaymentChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_RECURRING_PAYMENT_R)
    private Long recurringPaymentReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLACK_LIST_CARD_F)
    private Long blackListCardFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLACK_LIST_CARD_C)
    private Long blackListCardChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLACK_LIST_CARD_R)
    private Long blackListCardReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLACK_LIST_IP_F)
    private Long blackListIpFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLACK_LIST_IP_C)
    private Long blackListIpChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLACK_LIST_IP_R)
    private Long blackListIpReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLACK_LIST_MERCHANT_URL_F)
    private Long blackListMerchantUrlFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLACK_LIST_MERCHANT_URL_C)
    private Long blackListMerchantUrlChallenge;
    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLACK_LIST_MERCHANT_URL_R)
    private Long blackListMerchantUrlReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLACK_LIST_DEVICE_ID_F)
    private Long blackListDeviceIdFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLACK_LIST_DEVICE_ID_C)
    private Long blackListDeviceIdChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_BLACK_LIST_DEVICE_ID_R)
    private Long blackListDeviceIdReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_WHITE_LIST_F)
    private Long whiteListFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_WHITE_LIST_C)
    private Long whiteListChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_WHITE_LIST_R)
    private Long whiteListReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_ATTEMPT_F)
    private Long attemptFrictionless;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_ATTEMPT_C)
    private Long attemptChallenge;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_ATTEMPT_R)
    private Long attemptReject;

    @NotNull
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_SYS_CREATOR)
    private String sysCreator;

    @NotNull
    @Builder.Default
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_CREATE_MILLIS)
    private Long createMillis = System.currentTimeMillis();

    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_SYS_UPDATER)
    private String sysUpdater;

    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_UPDATE_MILLIS)
    private Long updateMillis;

    @Builder.Default
    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_DELETE_FLAG)
    private Boolean deleteFlag = Boolean.FALSE;

    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_DELETER)
    private String deleter;

    @Column(name = DBKey.COL_CLASSIC_RBA_REPORT_DELETE_MILLIS)
    private Long deleteMillis;

    public static ClassicRbaReport valueOf(ClassicRbaReportDO d) {
        return new ClassicRbaReport(d.getId(), d.getIssuerBankId(), d.getYear(), d.getMonth(),
                d.getDayOfMonth(), d.getBinRangeFrictionless(), d.getBinRangeChallenge(),
                d.getBinRangeReject(), d.getBlockCodeFrictionless(), d.getBlockCodeChallenge(),
                d.getBlockCodeReject(), d.getPurchaseAmountFrictionless(),
                d.getPurchaseAmountChallenge(), d.getPurchaseAmountReject(),
                d.getCardholderDataFrictionless(), d.getCardholderDataChallenge(),
                d.getCardholderDataReject(), d.getCumulativeAmountFrictionless(),
                d.getCumulativeAmountChallenge(), d.getCumulativeAmountReject(),
                d.getCumulativeTransactionFrictionless(), d.getCumulativeTransactionChallenge(),
                d.getCumulativeTransactionReject(), d.getLocationConsistencyFrictionless(),
                d.getLocationConsistencyChallenge(), d.getLocationConsistencyReject(),
                d.getBrowserLanguageFrictionless(), d.getBrowserLanguageChallenge(),
                d.getBrowserLanguageReject(), d.getVpnFrictionless(), d.getVpnChallenge(),
                d.getVpnReject(), d.getProxyFrictionless(), d.getProxyChallenge(),
                d.getProxyReject(), d.getPrivateBrowsingFrictionless(),
                d.getPrivateBrowsingChallenge(), d.getPrivateBrowsingReject(),
                d.getDeviceVariationFrictionless(), d.getDeviceVariationChallenge(),
                d.getDeviceVariationReject(), d.getMccFrictionless(), d.getMccChallenge(),
                d.getMccReject(), d.getRecurringPaymentFrictionless(),
                d.getRecurringPaymentChallenge(), d.getRecurringPaymentReject(),
                d.getBlackListCardFrictionless(), d.getBlackListCardChallenge(),
                d.getBlackListCardReject(), d.getBlackListIpFrictionless(),
                d.getBlackListIpChallenge(), d.getBlackListIpReject(),
                d.getBlackListMerchantUrlFrictionless(), d.getBlackListMerchantUrlChallenge(),
                d.getBlackListMerchantUrlReject(), d.getBlackListDeviceIdFrictionless(),
                d.getBlackListDeviceIdChallenge(), d.getBlackListDeviceIdReject(),
                d.getWhiteListFrictionless(), d.getWhiteListChallenge(), d.getWhiteListReject(),
                d.getAttemptFrictionless(), d.getAttemptChallenge(), d.getAttemptReject(),
                d.getSysCreator(), d.getCreateMillis(), d.getSysUpdater(), d.getUpdateMillis(),
                d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }

}
