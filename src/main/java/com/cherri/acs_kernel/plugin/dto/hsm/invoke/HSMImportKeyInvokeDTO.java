package com.cherri.acs_kernel.plugin.dto.hsm.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import com.cherri.acs_kernel.plugin.enumerator.HSMImportMechanism;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
public class HSMImportKeyInvokeDTO extends InvokeDTO {
    private HSMImportMechanism hsmMechanism;
    private String keyLabel;
    private String desKeyHex;
    private RSAPublicKey wrapKey;
    private String unwrapKey;
    private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;

    @Builder
    public HSMImportKeyInvokeDTO(
            HSMImportMechanism hsmMechanism,
            String keyLabel,
            String desKeyHex,
            RSAPublicKey wrapKey,
            String unwrapKey,
            Map<String, String> systemPropertiesMap,
            Map<IssuerPropertyDefinition, String> issuerPropertiesMap) {
        this.setSystemPropertiesMap(systemPropertiesMap);
        this.hsmMechanism = hsmMechanism;
        this.keyLabel = keyLabel;
        this.desKeyHex = desKeyHex;
        this.wrapKey = wrapKey;
        this.unwrapKey = unwrapKey;
        this.issuerPropertiesMap = issuerPropertiesMap;
    }
}
