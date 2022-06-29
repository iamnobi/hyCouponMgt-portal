package com.cherri.acs_portal.dto.bank;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.entity.IssuerHandingFeeDO;

@Data
@NoArgsConstructor
@ToString
public class IssuerHandingFeeQueryResultDto {

    private Long id;
    private String auditStatus;
    private Integer handlingFeeType;
    private ChargePerCard chargePerCard = new ChargePerCard();
    private ChargePerMonth chargePerMonth = new ChargePerMonth();

    @Data
    @ToString
    @NoArgsConstructor
    public static class ChargePerCard {

        private Double feePerCard;
        private Double thresholdFee;
        private Double minimumFee;
    }

    @Data
    @ToString
    @NoArgsConstructor
    public static class ChargePerMonth {

        private Double feePerMonth;
    }

    public static IssuerHandingFeeQueryResultDto valueOf(IssuerHandingFeeDO issuerHandingFee) {
        IssuerHandingFeeQueryResultDto dto = new IssuerHandingFeeQueryResultDto();
        dto.setId(issuerHandingFee.getId());
        dto.setHandlingFeeType(issuerHandingFee.getHandingFeeType());

        Double feePerCard =
          issuerHandingFee.getFeePerCard() == null ? 0.0 : issuerHandingFee.getFeePerCard();
        Double thresholdFee =
          issuerHandingFee.getThresholdFee() == null ? 0.0 : issuerHandingFee.getThresholdFee();
        Double minimumFee =
          issuerHandingFee.getMinimumFee() == null ? 0.0 : issuerHandingFee.getMinimumFee();

        ChargePerCard chargePerCard = new ChargePerCard();
        chargePerCard.setFeePerCard(feePerCard);
        chargePerCard.setThresholdFee(thresholdFee);
        chargePerCard.setMinimumFee(minimumFee);
        dto.setChargePerCard(chargePerCard);

        Double feePerMonth =
          issuerHandingFee.getFeePerMonth() == null ? 0.0 : issuerHandingFee.getFeePerMonth();
        ChargePerMonth chargePerMonth = new ChargePerMonth();
        chargePerMonth.setFeePerMonth(feePerMonth);
        dto.setChargePerMonth(chargePerMonth);

        dto.setAuditStatus(issuerHandingFee.getAuditStatus());
        return dto;
    }
}
