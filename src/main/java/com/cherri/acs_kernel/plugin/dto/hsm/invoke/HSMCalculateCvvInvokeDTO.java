package com.cherri.acs_kernel.plugin.dto.hsm.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
public class HSMCalculateCvvInvokeDTO extends InvokeDTO {
  private String cardNumber;
  private String atnLastFour;
  private String threeDSecurityResult;
  private String keyA;
  private String keyB;
  private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;

  @Builder
  public HSMCalculateCvvInvokeDTO(
      String cardNumber,
      String atnLastFour,
      String threeDSecurityResult,
      String keyA,
      String keyB,
      Map<String, String> systemPropertiesMap,
      Map<IssuerPropertyDefinition, String> issuerPropertiesMap) {
    super(systemPropertiesMap);
    this.cardNumber = cardNumber;
    this.atnLastFour = atnLastFour;
    this.threeDSecurityResult = threeDSecurityResult;
    this.keyA = keyA;
    this.keyB = keyB;
    this.issuerPropertiesMap = issuerPropertiesMap;
  }
}
