package com.cherri.acs_kernel.plugin.dto.cardholder.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CardholderGetAuthChannelInvokeDTO extends InvokeDTO {
    private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;
    private String authChannelId;
    private String cardNumber;
    private String bankCode;
    private String integratorId; // for backward compatible

    @Builder
    public CardholderGetAuthChannelInvokeDTO(
            String authChannelId,
            String cardNumber,
            String bankCode,
            String integratorId,
            Map<String, String> systemPropertiesMap,
            Map<IssuerPropertyDefinition, String> issuerPropertiesMap) {
        super(systemPropertiesMap);
        this.authChannelId = authChannelId;
        this.cardNumber = cardNumber;
        this.bankCode = bankCode;
        this.integratorId = integratorId;
        this.issuerPropertiesMap = issuerPropertiesMap;
    }
}
