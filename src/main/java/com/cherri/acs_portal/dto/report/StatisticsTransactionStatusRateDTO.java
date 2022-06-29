package com.cherri.acs_portal.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class StatisticsTransactionStatusRateDTO {
  private String cardBrand;
    private Double rate;
}
