package com.cherri.acs_kernel.plugin.dto.otp.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
public class VerifyInvokeDTO extends InvokeDTO {
    private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;
    private String acsTransID;
    private String authId;
    private String authCode;
    private String integratorId;        // for backward compatible

    @Builder
    public VerifyInvokeDTO(String acsTransID, String authId, String authCode, String integratorId,
                           Map<String, String> systemPropertiesMap, Map<IssuerPropertyDefinition, String> issuerPropertiesMap) {
        super(systemPropertiesMap);
        this.integratorId = integratorId;
        this.issuerPropertiesMap = issuerPropertiesMap;
        this.acsTransID = acsTransID;
        this.authId = authId;
        this.authCode = authCode;
    }
}
