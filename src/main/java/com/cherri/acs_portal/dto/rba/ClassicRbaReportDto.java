package com.cherri.acs_portal.dto.rba;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.cherri.acs_portal.enumeration.ClassicRbaReportName;
import ocean.acs.models.data_object.entity.ClassicRbaReportDO;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClassicRbaReportDto {

    List<ClassicRbaStatus> classicRbaStatusList;

    public static ClassicRbaReportDto valueOf(ClassicRbaReportDO e) {

        Long totalChallengeCount = e.getBinRangeChallenge() + e.getBinRangeReject();

        return ClassicRbaReportDto.builder()
          .classicRbaStatusList(Arrays.asList(
            calculateClassicRbaStatus(
              ClassicRbaReportName.BIN_RANGE.getName(),
              e.getBinRangeFrictionless(),
              e.getBinRangeChallenge(),
              e.getBinRangeReject(),
              totalChallengeCount,
              false),
            calculateClassicRbaStatus(
              ClassicRbaReportName.BLOCK_CODE.getName(),
              e.getBlockCodeFrictionless(),
              e.getBlockCodeChallenge(),
              e.getBlockCodeReject(),
              totalChallengeCount,
              false),
            calculateClassicRbaStatus(
              ClassicRbaReportName.PURCHASE_AMOUNT.getName(),
              e.getPurchaseAmountFrictionless(),
              e.getPurchaseAmountChallenge(),
              e.getPurchaseAmountReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.CARDHOLDER_DATA.getName(),
              e.getCardholderDataFrictionless(),
              e.getCardholderDataChallenge(),
              e.getCardholderDataReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.CUMULATIVE_AMOUNT.getName(),
              e.getCumulativeAmountFrictionless(),
              e.getCumulativeAmountChallenge(),
              e.getCumulativeAmountReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.CUMULATIVE_TRANSACTION.getName(),
              e.getCumulativeTransactionFrictionless(),
              e.getCumulativeTransactionChallenge(),
              e.getCumulativeTransactionReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.LOCATION_CONSISTENCY.getName(),
              e.getLocationConsistencyFrictionless(),
              e.getLocationConsistencyChallenge(),
              e.getLocationConsistencyReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.BROWSER_LANGUAGE.getName(),
              e.getBrowserLanguageFrictionless(),
              e.getBrowserLanguageChallenge(),
              e.getBrowserLanguageReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.VPN.getName(),
              e.getVpnFrictionless(),
              e.getVpnChallenge(),
              e.getVpnReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.PROXY.getName(),
              e.getProxyFrictionless(),
              e.getProxyChallenge(),
              e.getProxyReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.PRIVATE_BROWSING.getName(),
              e.getPrivateBrowsingFrictionless(),
              e.getPrivateBrowsingChallenge(),
              e.getPrivateBrowsingReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.DEVICE_VARIATION.getName(),
              e.getDeviceVariationFrictionless(),
              e.getDeviceVariationChallenge(),
              e.getDeviceVariationReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.MCC.getName(),
              e.getMccFrictionless(),
              e.getMccChallenge(),
              e.getMccReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.RECURRING_PAYMENT.getName(),
              e.getRecurringPaymentFrictionless(),
              e.getRecurringPaymentChallenge(),
              e.getRecurringPaymentReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.BLACK_LIST_CARD.getName(),
              e.getBlackListCardFrictionless(),
              e.getBlackListCardChallenge(),
              e.getBlackListCardReject(),
              totalChallengeCount,
              false),
            calculateClassicRbaStatus(
              ClassicRbaReportName.BLACK_LIST_IP.getName(),
              e.getBlackListIpFrictionless(),
              e.getBlackListIpChallenge(),
              e.getBlackListIpReject(),
              totalChallengeCount,
              false),
            calculateClassicRbaStatus(
              ClassicRbaReportName.BLACK_LIST_MERCHANT_URL.getName(),
              e.getBlackListMerchantUrlFrictionless(),
              e.getBlackListMerchantUrlChallenge(),
              e.getBlackListDeviceIdReject(),
              totalChallengeCount,
              false),
            calculateClassicRbaStatus(
              ClassicRbaReportName.BLACK_LIST_DEVICE_ID.getName(),
              e.getBlackListDeviceIdFrictionless(),
              e.getBlackListDeviceIdChallenge(),
              e.getBlackListDeviceIdReject(),
              totalChallengeCount,
              false),
            calculateClassicRbaStatus(
              ClassicRbaReportName.WHITE_LIST.getName(),
              e.getWhiteListFrictionless(),
              e.getWhiteListChallenge(),
              e.getWhiteListReject(),
              totalChallengeCount,
              true),
            calculateClassicRbaStatus(
              ClassicRbaReportName.ATTEMPT.getName(),
              e.getAttemptFrictionless(),
              e.getAttemptChallenge(),
              e.getAttemptReject(),
              totalChallengeCount,
              true)
          ))
          .build();
    }

    private static ClassicRbaStatus calculateClassicRbaStatus(
      String name,
      Long currentCheckpointFrictionlessCount,
      Long currentCheckpointChallengeCount,
      Long currentCheckpointRejectCount,
      Long totalChallengeCount,
      boolean positiveOrNegative) {
        ClassicRbaStatus.ClassicRbaStatusBuilder builder = ClassicRbaStatus.builder();
        builder.name(name);
        builder
          .challenge(calculatePercentage(currentCheckpointChallengeCount, totalChallengeCount));
        if (positiveOrNegative) {
            return builder
              .frictionless(
                calculatePercentage(currentCheckpointFrictionlessCount, totalChallengeCount))
              .reject("0%")
              .build();
        } else {
            return builder
              .frictionless("0%")
              .reject(calculatePercentage(currentCheckpointRejectCount, totalChallengeCount))
              .build();
        }
    }

    private static String calculatePercentage(long numerator, long denominator) {
        if (numerator == 0 || denominator == 0) {
            return "0%";
        }
        return (numerator * 100 / denominator) + "%";
    }

}
