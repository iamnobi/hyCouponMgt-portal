package com.cherri.acs_kernel.plugin.dto.hsm.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import com.cherri.acs_kernel.plugin.enumerator.HSMSignMechanism;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
public class HSMSignInvokeDTO extends InvokeDTO {
    private HSMSignMechanism hsmMechanism;
    private String keyId;
    private byte[] message;
    private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;

    @Builder
    public HSMSignInvokeDTO(
            HSMSignMechanism hsmMechanism, String keyId,
            byte[] message, Map<String, String> systemPropertiesMap, Map<IssuerPropertyDefinition, String> issuerPropertiesMap) {
        super(systemPropertiesMap);
        this.hsmMechanism = hsmMechanism;
        this.keyId = keyId;
        this.message = message;
        this.issuerPropertiesMap = issuerPropertiesMap;
    }
}
