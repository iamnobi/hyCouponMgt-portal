package com.cherri.acs_kernel.plugin.dto.hsm.invoke;

import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import com.cherri.acs_kernel.plugin.enumerator.HSMGenKeyMechanism;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
public class HSMGenerateKeyInvokeDTO extends InvokeDTO {
    private HSMGenKeyMechanism hsmMechanism;
    private String keyLabel;
    private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;

    @Builder
    public HSMGenerateKeyInvokeDTO(
            HSMGenKeyMechanism hsmMechanism, String keyLabel, Map<String, String> systemPropertiesMap, Map<IssuerPropertyDefinition, String> issuerPropertiesMap) {
        super(systemPropertiesMap);
        this.hsmMechanism = hsmMechanism;
        this.keyLabel = keyLabel;
        this.issuerPropertiesMap = issuerPropertiesMap;
    }
}
