package com.cherri.acs_portal.dto.bank;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.IssuerHandingFeeDO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IssuerHandingFeeUpdateDto extends AuditableDTO {

    @NotNull(message = "{column.notempty}")
    private Long id;

    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;

    @Range(min = 0, max = 1, message = "{unaccepted.value}")
    private Integer handlingFeeType;

    private ChargePerCard chargePerCard = new ChargePerCard();
    private ChargePerMonth chargePerMonth = new ChargePerMonth();

    private String user;
    private AuditStatus auditStatus;

    @Override
    public void setAuditStatus(ocean.acs.commons.enumerator.AuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.BANK_FEE;
    }

    @Data
    @NoArgsConstructor
    @ToString
    public static class ChargePerCard {

        private Double feePerCard = 0.0;
        private Double thresholdFee = 0.0;
        private Double minimumFee = 0.0;
    }

    @Data
    @NoArgsConstructor
    @ToString
    public static class ChargePerMonth {

        private Double feePerMonth = 0.0;
    }

    public static IssuerHandingFeeUpdateDto valueOf(IssuerHandingFeeDO entity) {
        IssuerHandingFeeUpdateDto dto = new IssuerHandingFeeUpdateDto();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        dto.setUser(entity.getUpdater());

        ChargePerCard chargePerCard = new ChargePerCard();
        chargePerCard.setFeePerCard(entity.getFeePerCard());
        chargePerCard.setMinimumFee(entity.getMinimumFee());
        chargePerCard.setThresholdFee(entity.getThresholdFee());

        ChargePerMonth chargePerMonth = new ChargePerMonth();
        chargePerMonth.setFeePerMonth(entity.getFeePerMonth());

        dto.setChargePerCard(chargePerCard);
        dto.setChargePerMonth(chargePerMonth);
        return dto;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
