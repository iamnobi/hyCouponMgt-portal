package com.cherri.acs_portal.dto.report;

import lombok.Data;

import java.util.List;

@Data
public class BankTransStatusDetailDTO {

  private final String bankName;
  private final List<CardBrandTransStatusDTO> cardBrandTransStatusDetailList;

  private BankTransStatusDetailDTO(
      String bankName, List<CardBrandTransStatusDTO> cardBrandTransStatusDetailList) {
    this.bankName = bankName;
    this.cardBrandTransStatusDetailList = cardBrandTransStatusDetailList;
  }

  public static BankTransStatusDetailDTO newInstance(
      String bankName, List<CardBrandTransStatusDTO> cardBrandTransStatusDetailList) {
    return new BankTransStatusDetailDTO(bankName, cardBrandTransStatusDetailList);
  }
}
