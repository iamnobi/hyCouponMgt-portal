package com.cherri.acs_portal.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class AttemptAuthorizeDTO {
  private String issuerBankName;
  private Integer year;
  private Integer month;
  private Integer dayOfMonth;
  private Integer permittedCount;
  private Integer realTriesCount;
  private String statistics;
}
