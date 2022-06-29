package com.cherri.acs_kernel.plugin.dto.cardholder.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CardholderGetCardListInvokeDTO extends InvokeDTO {

  private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;

  /**
   * Bank Code
   */
  private String bankCode;

  /**
   * Card Type
   * Reference: Module {@code Common} enumerator {@code CardType}
   */
  private String cardType;

  /**
   * Cardholder ID or Identity number
   */
  private String cardholderId;

  @Builder
  public CardholderGetCardListInvokeDTO(
    String bankCode, String cardType, String cardholderId, Map<String, String> systemPropertiesMap,
    Map<IssuerPropertyDefinition, String> issuerPropertiesMap) {
    super(systemPropertiesMap);
    this.bankCode = bankCode;
    this.cardType = cardType;
    this.cardholderId = cardholderId;
    this.issuerPropertiesMap = issuerPropertiesMap;
  }
}
