package com.cherri.acs_portal.dto.rba;

import com.cherri.acs_portal.config.ClassicRbaProperties.CumulativeAmountInterval;
import com.cherri.acs_portal.config.ClassicRbaProperties.CumulativePeriodInterval;
import com.cherri.acs_portal.validator.validation.DuplicatedListElementValidation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.ClassicRbaCheckDO;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClassicRbaSettingDto {

    private Boolean classicRbaEnabled;

    private PurchaseAmount purchaseAmount;
    private CardholderData cardholderData;
    private CumulativeAmount cumulativeAmount;
    private CumulativeTransaction cumulativeTransaction;
    private LocationConsistency locationConsistency;
    private BrowserLanguage browserLanguage;
    private Vpn vpn;
    private Proxy proxy;
    private PrivateBrowsing privateBrowsing;
    private DeviceVariation deviceVariation;
    private Mcc mcc;
    private RecurringPayment recurringPayment;

    private Long id;
    private Long issuerBankId;

    private String auditStatus;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class PurchaseAmount {

        @NotNull(message = "{column.notempty}")
        private Boolean enabled;
        @NotNull(message = "{column.notempty}")
        private String amount;
        @NotNull(message = "{column.notempty}")
        private String minAmount;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class CardholderData {

        @NotNull(message = "{column.notempty}")
        private Boolean enabled;
        @NotNull(message = "{column.notempty}")
        private Boolean name;
        @NotNull(message = "{column.notempty}")
        private Boolean email;
        @NotNull(message = "{column.notempty}")
        private Boolean postcode;
        @NotNull(message = "{column.notempty}")
        private Boolean phone;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class CumulativeAmount {

        @NotNull(message = "{column.notempty}")
        private Boolean enabled;
        @NotNull(message = "{column.notempty}")
        private Long period;
        @NotNull(message = "{column.notempty}")
        private PeriodInterval periodInterval;
        @NotNull(message = "{column.notempty}")
        private String amount;
        @NotNull(message = "{column.notempty}")
        private AmountInterval amountInterval;
        @NotNull(message = "{column.notempty}")
        private Boolean transactionCountEnabled;
        @NotNull(message = "{column.notempty}")
        private Long transactionCount;

        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Data
        public static class PeriodInterval {

            @NotNull(message = "{column.notempty}")
            private Long min;
            @NotNull(message = "{column.notempty}")
            private Long max;
        }

        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Data
        public static class AmountInterval {

            @NotNull(message = "{column.notempty}")
            private Long min;
            @NotNull(message = "{column.notempty}")
            private Long max;
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class CumulativeTransaction {

        @NotNull(message = "{column.notempty}")
        private Boolean enabled;
        @NotNull(message = "{column.notempty}")
        private Long transactionCount;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class LocationConsistency {

        @NotNull(message = "{column.notempty}")
        private Boolean enabled;
        @NotNull(message = "{column.notempty}")
        private Boolean ipCountry;
        @NotNull(message = "{column.notempty}")
        private Boolean browserTimeZone;
        @NotNull(message = "{column.notempty}")
        private Boolean billingCountry;
        @NotNull(message = "{column.notempty}")
        private Boolean shippingCountry;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BrowserLanguage {

        @NotNull(message = "{column.notempty}")
        private Boolean enabled;
        @DuplicatedListElementValidation
        private List<String> browserLanguageList;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Vpn {

        @NotNull(message = "{column.notempty}")
        private Boolean enabled;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Proxy {

        @NotNull(message = "{column.notempty}")
        private Boolean enabled;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class PrivateBrowsing {

        @NotNull(message = "{column.notempty}")
        private Boolean enabled;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class DeviceVariation {

        @NotNull(message = "{column.notempty}")
        private Boolean enabled;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Mcc {

        @NotNull(message = "{column.notempty}")
        private Boolean enabled;
        @NotNull(message = "{column.notempty}")
        @DuplicatedListElementValidation
        private List<String> mccList;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class RecurringPayment {

        @NotNull(message = "{column.notempty}")
        private Boolean enabled;
        @NotNull(message = "{column.notempty}")
        private Long frequency;
        @NotNull(message = "{column.notempty}")
        private Long expiration;
    }

    public static ClassicRbaSettingDto valueOf(
      ClassicRbaCheckDO classicRbaCheck,
      CumulativeAmountInterval cumulativeAmountInterval,
      CumulativePeriodInterval cumulativePeriodInterval) {

        return ClassicRbaSettingDto.builder()
          .id(classicRbaCheck.getId())
          .issuerBankId(classicRbaCheck.getIssuerBankId())
          .classicRbaEnabled(classicRbaCheck.getClassicRbaEnabled())
          .purchaseAmount(
            PurchaseAmount.builder()
              .enabled(classicRbaCheck.getPurchaseAmountEnabled())
              .amount(classicRbaCheck.getPurchaseAmountAmount())
              .minAmount(classicRbaCheck.getPurchaseAmountMinAmount())
              .build()
          )
          .cardholderData(
            CardholderData.builder()
              .enabled(classicRbaCheck.getCardholderDataEnabled())
              .name(classicRbaCheck.getCardholderDataName())
              .email(classicRbaCheck.getCardholderDataEmail())
              .postcode(classicRbaCheck.getCardholderDataPostcode())
              .phone(classicRbaCheck.getCardholderDataPhone())
              .build()
          )
          .cumulativeAmount(
            CumulativeAmount.builder()
              .enabled(classicRbaCheck.getCumulativeAmountEnabled())
              .period(classicRbaCheck.getCumulativeAmountPeriod())
              .periodInterval(
                CumulativeAmount.PeriodInterval.builder()
                  .min(cumulativePeriodInterval.getMin())
                  .max(cumulativePeriodInterval.getMax())
                  .build()
              )
              .amount(classicRbaCheck.getCumulativeAmountAmount())
              .amountInterval(
                CumulativeAmount.AmountInterval.builder()
                  .min(cumulativeAmountInterval.getMin())
                  .max(cumulativeAmountInterval.getMax())
                  .build()
              )
              .transactionCountEnabled(classicRbaCheck.getCumulativeAmountTxEnabled())
              .transactionCount(classicRbaCheck.getCumulativeAmountTxCount())
              .build()
          )
          .cumulativeTransaction(
            CumulativeTransaction.builder()
              .enabled(classicRbaCheck.getCumulativeTransactionEnabled())
              .transactionCount(classicRbaCheck.getCumulativeTransactionCount())
              .build()
          )
          .locationConsistency(
            LocationConsistency.builder()
              .enabled(classicRbaCheck.getLocationConsistencyEnabled())
              .ipCountry(classicRbaCheck.getLocationConsistencyIp())
              .billingCountry(classicRbaCheck.getLocationConsistencyBilling())
              .browserTimeZone(classicRbaCheck.getLocationConsistencyBrwTz())
              .shippingCountry(classicRbaCheck.getLocationConsistencyShipping())
              .build()
          )
          .browserLanguage(
            ClassicRbaSettingDto.BrowserLanguage.builder()
              .enabled(classicRbaCheck.getBrowserLanguageEnabled())
              .browserLanguageList(Optional.ofNullable(classicRbaCheck.getBrowserLanguageList())
                .map(e -> Arrays.asList(e.split(",")))
                .orElse(Collections.emptyList()))
              .build()
          )
          .vpn(ClassicRbaSettingDto.Vpn.builder().enabled(classicRbaCheck.getVpnEnabled()).build())
          .proxy(
            ClassicRbaSettingDto.Proxy.builder().enabled(classicRbaCheck.getProxyEnabled()).build())
          .privateBrowsing(ClassicRbaSettingDto.PrivateBrowsing.builder()
            .enabled(classicRbaCheck.getPrivateBrowsingEnabled()).build())
          .deviceVariation(ClassicRbaSettingDto.DeviceVariation.builder()
            .enabled(classicRbaCheck.getDeviceVariationEnabled()).build())
          .mcc(
            ClassicRbaSettingDto.Mcc.builder()
              .enabled(classicRbaCheck.getMccEnabled())
              .mccList(Optional.ofNullable(classicRbaCheck.getMccList())
                .map(e -> Arrays.asList(e.split(",")))
                .orElse(Collections.emptyList()))
              .build()
          )
          .recurringPayment(
            ClassicRbaSettingDto.RecurringPayment.builder()
              .enabled(classicRbaCheck.getRecurringPaymentEnabled())
              .frequency(classicRbaCheck.getRecurringPaymentFrequency())
              .expiration(classicRbaCheck.getRecurringPaymentExpiration())
              .build()
          )
          .auditStatus(classicRbaCheck.getAuditStatus()).build();
    }
}
