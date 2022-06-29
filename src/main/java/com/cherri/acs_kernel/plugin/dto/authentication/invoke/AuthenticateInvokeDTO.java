package com.cherri.acs_kernel.plugin.dto.authentication.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
public class AuthenticateInvokeDTO extends InvokeDTO {
    private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;
    private String account;
    private String password;

    @Builder
    public AuthenticateInvokeDTO(String account, String password,
                                 Map<String, String> systemPropertiesMap, Map<IssuerPropertyDefinition, String> issuerPropertiesMap) {
        super(systemPropertiesMap);
        this.issuerPropertiesMap = issuerPropertiesMap;
        this.account = account;
        this.password = password;
    }
}
