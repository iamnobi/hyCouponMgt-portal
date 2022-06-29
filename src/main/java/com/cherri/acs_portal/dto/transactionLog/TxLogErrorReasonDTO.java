package com.cherri.acs_portal.dto.transactionLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TxLogErrorReasonDTO {
  private Long issuerBankId;
  private Integer errorReasonCode;
  private Integer errorReasonCount;

  public static TxLogErrorReasonDTO newInstance(
      Long issuerBankId, Integer errorReasonCode, Integer errorReasonCount) {
    return new TxLogErrorReasonDTO(issuerBankId, errorReasonCode, errorReasonCount);
  }
}
