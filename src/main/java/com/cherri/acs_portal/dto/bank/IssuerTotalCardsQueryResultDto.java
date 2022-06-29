package com.cherri.acs_portal.dto.bank;

import com.cherri.acs_portal.dto.acs_integrator.BaseResDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class IssuerTotalCardsQueryResultDto extends BaseResDTO {
  private List<TotalCardsArray> totalCardsArray;

  @Data
  public static class TotalCardsArray {
    private String cardBrand;
    private String cardType;
    private Long totalCards;
  }
}
