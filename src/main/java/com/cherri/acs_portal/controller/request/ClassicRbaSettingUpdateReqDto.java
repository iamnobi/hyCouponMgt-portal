package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.rba.ClassicRbaSettingDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ClassicRbaSettingUpdateReqDto extends AuditableDTO {

    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;
    @NotNull(message = "{column.notempty}")
    private Long id;
    @NotNull(message = "{column.notempty}")
    private Boolean classicRbaEnabled;

    private ClassicRbaSettingDto.PurchaseAmount purchaseAmount;

    private ClassicRbaSettingDto.CardholderData cardholderData;

    private ClassicRbaSettingDto.CumulativeAmount cumulativeAmount;

    private ClassicRbaSettingDto.CumulativeTransaction cumulativeTransaction;

    private ClassicRbaSettingDto.LocationConsistency locationConsistency;

    private ClassicRbaSettingDto.BrowserLanguage browserLanguage;

    private ClassicRbaSettingDto.Vpn vpn;

    private ClassicRbaSettingDto.Proxy proxy;

    private ClassicRbaSettingDto.PrivateBrowsing privateBrowsing;

    private ClassicRbaSettingDto.DeviceVariation deviceVariation;

    private ClassicRbaSettingDto.Mcc mcc;

    private ClassicRbaSettingDto.RecurringPayment recurringPayment;

    private AuditStatus auditStatus;

    private String user;

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.CLASSIC_RBA;
    }

    public static ClassicRbaSettingUpdateReqDto valueOf(ClassicRbaSettingDto c) {
        return ClassicRbaSettingUpdateReqDto.builder()
          .id(c.getId())
          .issuerBankId(c.getIssuerBankId())
          .classicRbaEnabled(c.getClassicRbaEnabled())
          .purchaseAmount(c.getPurchaseAmount())
          .cardholderData(c.getCardholderData())
          .cumulativeAmount(c.getCumulativeAmount())
          .cumulativeTransaction(c.getCumulativeTransaction())
          .locationConsistency(c.getLocationConsistency())
          .browserLanguage(c.getBrowserLanguage())
          .vpn(c.getVpn())
          .proxy(c.getProxy())
          .privateBrowsing(c.getPrivateBrowsing())
          .deviceVariation(c.getDeviceVariation())
          .mcc(c.getMcc())
          .recurringPayment(c.getRecurringPayment())
          .auditStatus(AuditStatus.getStatusBySymbol(c.getAuditStatus()))
          .build();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE)
            // remove 不需要 show 出來的變數：periodInterval, amountInterval
            .replaceAll("periodInterval=[^)]+\\), ", "")
            .replaceAll("amountInterval=[^)]+\\), ", "")
            // 移除多餘的字串, ex:
            // ClassicRbaSettingDto.PurchaseAmount(enabled=true, amount=2000, minAmount=1000)
            // -> enabled=true, amount=2000, minAmount=1000
            .replaceAll("ClassicRbaSettingDto[^(]+\\(([^)]+)\\)", "$1");
    }
}
