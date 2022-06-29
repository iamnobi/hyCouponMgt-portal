package com.cherri.acs_kernel.plugin.dto.cardholder.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CardholderGetInfoInvokeDTO extends InvokeDTO {
    private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;
    private String bankCode;
    private String cardNumber;
    private String cardType;
    private String cardholderId;

    @Builder
    public CardholderGetInfoInvokeDTO(
            String bankCode,
            String cardNumber,
            String cardNumberHash,
            String cardType,
            String cardholderId,
            Map<String, String> systemPropertiesMap,
            Map<IssuerPropertyDefinition, String> issuerPropertiesMap) {
        super(systemPropertiesMap);
        this.bankCode = bankCode;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardholderId = cardholderId;
        this.issuerPropertiesMap = issuerPropertiesMap;
    }
}
