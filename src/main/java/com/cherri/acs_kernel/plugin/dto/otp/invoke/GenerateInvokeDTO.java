package com.cherri.acs_kernel.plugin.dto.otp.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
public class GenerateInvokeDTO extends InvokeDTO {
    private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;
    private String acsTransID;
    private String integratorId;        // for backward compatible

    @Builder
    public GenerateInvokeDTO(String acsTransID, String integratorId,
                             Map<String, String> systemPropertiesMap, Map<IssuerPropertyDefinition, String> issuerPropertiesMap) {
        super(systemPropertiesMap);
        this.acsTransID = acsTransID;
        this.integratorId = integratorId;
        this.issuerPropertiesMap = issuerPropertiesMap;
    }
}
