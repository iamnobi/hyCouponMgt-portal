package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ClassicRbaReportDO {

    private Long id;
    private Long issuerBankId;
    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private Long binRangeFrictionless;
    private Long binRangeChallenge;
    private Long binRangeReject;
    private Long blockCodeFrictionless;
    private Long blockCodeChallenge;
    private Long blockCodeReject;
    private Long purchaseAmountFrictionless;
    private Long purchaseAmountChallenge;
    private Long purchaseAmountReject;
    private Long cardholderDataFrictionless;
    private Long cardholderDataChallenge;
    private Long cardholderDataReject;
    private Long cumulativeAmountFrictionless;
    private Long cumulativeAmountChallenge;
    private Long cumulativeAmountReject;
    private Long cumulativeTransactionFrictionless;
    private Long cumulativeTransactionChallenge;
    private Long cumulativeTransactionReject;
    private Long locationConsistencyFrictionless;
    private Long locationConsistencyChallenge;
    private Long locationConsistencyReject;
    private Long browserLanguageFrictionless;
    private Long browserLanguageChallenge;
    private Long browserLanguageReject;
    private Long vpnFrictionless;
    private Long vpnChallenge;
    private Long vpnReject;
    private Long proxyFrictionless;
    private Long proxyChallenge;
    private Long proxyReject;
    private Long privateBrowsingFrictionless;
    private Long privateBrowsingChallenge;
    private Long privateBrowsingReject;
    private Long deviceVariationFrictionless;
    private Long deviceVariationChallenge;
    private Long deviceVariationReject;
    private Long mccFrictionless;
    private Long mccChallenge;
    private Long mccReject;
    private Long recurringPaymentFrictionless;
    private Long recurringPaymentChallenge;
    private Long recurringPaymentReject;
    private Long blackListCardFrictionless;
    private Long blackListCardChallenge;
    private Long blackListCardReject;
    private Long blackListIpFrictionless;
    private Long blackListIpChallenge;
    private Long blackListIpReject;
    private Long blackListMerchantUrlFrictionless;
    private Long blackListMerchantUrlChallenge;
    private Long blackListMerchantUrlReject;
    private Long blackListDeviceIdFrictionless;
    private Long blackListDeviceIdChallenge;
    private Long blackListDeviceIdReject;
    private Long whiteListFrictionless;
    private Long whiteListChallenge;
    private Long whiteListReject;
    private Long attemptFrictionless;
    private Long attemptChallenge;
    private Long attemptReject;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;
    private Boolean deleteFlag;
    private String deleter;
    private Long deleteMillis;

    public static ClassicRbaReportDO valueOf(ocean.acs.models.oracle.entity.ClassicRbaReport e) {
        return new ClassicRbaReportDO(e.getId(), e.getIssuerBankId(), e.getYear(), e.getMonth(),
                e.getDayOfMonth(), e.getBinRangeFrictionless(), e.getBinRangeChallenge(),
                e.getBinRangeReject(), e.getBlockCodeFrictionless(), e.getBlockCodeChallenge(),
                e.getBlockCodeReject(), e.getPurchaseAmountFrictionless(),
                e.getPurchaseAmountChallenge(), e.getPurchaseAmountReject(),
                e.getCardholderDataFrictionless(), e.getCardholderDataChallenge(),
                e.getCardholderDataReject(), e.getCumulativeAmountFrictionless(),
                e.getCumulativeAmountChallenge(), e.getCumulativeAmountReject(),
                e.getCumulativeTransactionFrictionless(), e.getCumulativeTransactionChallenge(),
                e.getCumulativeTransactionReject(), e.getLocationConsistencyFrictionless(),
                e.getLocationConsistencyChallenge(), e.getLocationConsistencyReject(),
                e.getBrowserLanguageFrictionless(), e.getBrowserLanguageChallenge(),
                e.getBrowserLanguageReject(), e.getVpnFrictionless(), e.getVpnChallenge(),
                e.getVpnReject(), e.getProxyFrictionless(), e.getProxyChallenge(),
                e.getProxyReject(), e.getPrivateBrowsingFrictionless(),
                e.getPrivateBrowsingChallenge(), e.getPrivateBrowsingReject(),
                e.getDeviceVariationFrictionless(), e.getDeviceVariationChallenge(),
                e.getDeviceVariationReject(), e.getMccFrictionless(), e.getMccChallenge(),
                e.getMccReject(), e.getRecurringPaymentFrictionless(),
                e.getRecurringPaymentChallenge(), e.getRecurringPaymentReject(),
                e.getBlackListCardFrictionless(), e.getBlackListCardChallenge(),
                e.getBlackListCardReject(), e.getBlackListIpFrictionless(),
                e.getBlackListIpChallenge(), e.getBlackListIpReject(),
                e.getBlackListMerchantUrlFrictionless(), e.getBlackListMerchantUrlChallenge(),
                e.getBlackListMerchantUrlReject(), e.getBlackListDeviceIdFrictionless(),
                e.getBlackListDeviceIdChallenge(), e.getBlackListDeviceIdReject(),
                e.getWhiteListFrictionless(), e.getWhiteListChallenge(), e.getWhiteListReject(),
                e.getAttemptFrictionless(), e.getAttemptChallenge(), e.getAttemptReject(),
                e.getSysCreator(), e.getCreateMillis(), e.getSysUpdater(), e.getUpdateMillis(),
                e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

    public static ClassicRbaReportDO valueOf(ocean.acs.models.sql_server.entity.ClassicRbaReport e) {
        return new ClassicRbaReportDO(e.getId(), e.getIssuerBankId(), e.getYear(), e.getMonth(),
                e.getDayOfMonth(), e.getBinRangeFrictionless(), e.getBinRangeChallenge(),
                e.getBinRangeReject(), e.getBlockCodeFrictionless(), e.getBlockCodeChallenge(),
                e.getBlockCodeReject(), e.getPurchaseAmountFrictionless(),
                e.getPurchaseAmountChallenge(), e.getPurchaseAmountReject(),
                e.getCardholderDataFrictionless(), e.getCardholderDataChallenge(),
                e.getCardholderDataReject(), e.getCumulativeAmountFrictionless(),
                e.getCumulativeAmountChallenge(), e.getCumulativeAmountReject(),
                e.getCumulativeTransactionFrictionless(), e.getCumulativeTransactionChallenge(),
                e.getCumulativeTransactionReject(), e.getLocationConsistencyFrictionless(),
                e.getLocationConsistencyChallenge(), e.getLocationConsistencyReject(),
                e.getBrowserLanguageFrictionless(), e.getBrowserLanguageChallenge(),
                e.getBrowserLanguageReject(), e.getVpnFrictionless(), e.getVpnChallenge(),
                e.getVpnReject(), e.getProxyFrictionless(), e.getProxyChallenge(),
                e.getProxyReject(), e.getPrivateBrowsingFrictionless(),
                e.getPrivateBrowsingChallenge(), e.getPrivateBrowsingReject(),
                e.getDeviceVariationFrictionless(), e.getDeviceVariationChallenge(),
                e.getDeviceVariationReject(), e.getMccFrictionless(), e.getMccChallenge(),
                e.getMccReject(), e.getRecurringPaymentFrictionless(),
                e.getRecurringPaymentChallenge(), e.getRecurringPaymentReject(),
                e.getBlackListCardFrictionless(), e.getBlackListCardChallenge(),
                e.getBlackListCardReject(), e.getBlackListIpFrictionless(),
                e.getBlackListIpChallenge(), e.getBlackListIpReject(),
                e.getBlackListMerchantUrlFrictionless(), e.getBlackListMerchantUrlChallenge(),
                e.getBlackListMerchantUrlReject(), e.getBlackListDeviceIdFrictionless(),
                e.getBlackListDeviceIdChallenge(), e.getBlackListDeviceIdReject(),
                e.getWhiteListFrictionless(), e.getWhiteListChallenge(), e.getWhiteListReject(),
                e.getAttemptFrictionless(), e.getAttemptChallenge(), e.getAttemptReject(),
                e.getSysCreator(), e.getCreateMillis(), e.getSysUpdater(), e.getUpdateMillis(),
                e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }
}
