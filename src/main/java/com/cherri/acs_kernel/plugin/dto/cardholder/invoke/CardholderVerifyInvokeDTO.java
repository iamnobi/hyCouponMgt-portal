package com.cherri.acs_kernel.plugin.dto.cardholder.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CardholderVerifyInvokeDTO extends InvokeDTO {
  private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;
  private String cardNumber;
  private String bankCode;
  private String cardType;
  private String expireDate;

  @Builder
  public CardholderVerifyInvokeDTO(
          String cardNumber, String bankCode, String cardType, String expireDate, Map<String, String> systemPropertiesMap, Map<IssuerPropertyDefinition, String> issuerPropertiesMap) {
    super(systemPropertiesMap);
    this.cardNumber = cardNumber;
    this.bankCode = bankCode;
    this.cardType = cardType;
    this.expireDate = expireDate;
    this.issuerPropertiesMap = issuerPropertiesMap;
  }
}
