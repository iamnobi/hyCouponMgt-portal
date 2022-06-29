package com.cherri.acs_portal.dto.transactionLog;

import com.cherri.acs_portal.config.ClassicRbaProperties.EnabledModules;
import com.cherri.acs_portal.serializer.ClassicRbaResultDtoSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.ThreeDsVersion;
import ocean.acs.models.data_object.entity.TransactionLogDO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
@JsonSerialize(using = ClassicRbaResultDtoSerializer.class)
public class ClassicRbaResultDto {

    private Boolean rbaChecked;
    private Boolean rbaPurchaseAmountResult;
    private Boolean rbaCardholderDataResult;
    private Boolean rbaCumulativeAmountResult;
    private String rbaCumulativeAmountLogAmount;
    private String rbaCumulativeAmountLogCount;
    private Boolean rbaCumulativeTransactionResult;
    private Boolean rbaLocationConsistencyResult;
    private Boolean rbaBrowserLanguageResult;
    private Boolean rbaVpnResult;
    private Boolean rbaProxyResult;
    private Boolean rbaPrivateBrowsingResult;
    private Boolean rbaDeviceVariationResult;
    private Boolean rbaMccResult;
    private Boolean rbaRecurringPaymentResult;
    private ThreeDsVersion threeDsVersion;

    /**
     * 用來提供 ClassicRbaResultDtoSerializer 資訊，以移除被 disabled 功能的資訊
     */
    private EnabledModules enabledModules;


    public static ClassicRbaResultDto valueOf(TransactionLogDO transactionLog,
      EnabledModules enabledModules) {
        return new ClassicRbaResultDto(
          transactionLog.getRbaChecked(),
          transactionLog.getRbaPurchaseAmountResult(),
          transactionLog.getRbaCardholderDataResult(),
          transactionLog.getRbaCumulativeAmountResult(),
          transactionLog.getRbaCumulativeAmountLogAmount(),
          transactionLog.getRbaCumulativeAmountLogCount(),
          transactionLog.getRbaCumulativeTransactionResult(),
          transactionLog.getRbaLocationConsistencyResult(),
          transactionLog.getRbaBrowserLanguageResult(),
          transactionLog.getRbaVpnResult(),
          transactionLog.getRbaProxyResult(),
          transactionLog.getRbaPrivateBrowsingResult(),
          transactionLog.getRbaDeviceVariationResult(),
          transactionLog.getRbaMccResult(),
          transactionLog.getRbaRecurringPaymentResult(),
          ThreeDsVersion.TWO,
          enabledModules);
    }
}
