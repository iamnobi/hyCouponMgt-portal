package com.cherri.acs_portal.dto.bank;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class IssuerTotalCardsQueryDto {
  private String issuerBankCode;
  private List<IssuerBinRangeQueryDto> issuerBinRangeQueryArray;

  public IssuerTotalCardsQueryDto(
      String issuerBankCode, List<IssuerBinRangeQueryDto> issuerBinRangeQueryArray) {
    this.issuerBankCode = issuerBankCode;
    this.issuerBinRangeQueryArray = issuerBinRangeQueryArray;
  }
}
