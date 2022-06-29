package com.cherri.acs_portal.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AbnormalTransactionMonthlyDTO {

  private Long issuerBankId;
  private String merchantId;
  private String merchantName;
  private Long uCount;
  private Long nCount;
  private Long totalCount;
}
