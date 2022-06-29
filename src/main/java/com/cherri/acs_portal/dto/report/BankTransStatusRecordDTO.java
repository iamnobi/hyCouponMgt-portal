package com.cherri.acs_portal.dto.report;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class BankTransStatusRecordDTO {
  private final Long issuerBankId;
  private final String cardBrand;
  private final String transStatus;
  private final Long count;
}
