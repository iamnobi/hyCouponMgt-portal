package com.cherri.acs_kernel.plugin.dto.hsm.invoke;


import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import com.cherri.acs_kernel.plugin.dto.IssuerPropertyDefinition;
import com.cherri.acs_kernel.plugin.enumerator.HSMEncryptDecryptMechanism;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HSMDecryptInvokeDTO extends InvokeDTO {
    private HSMEncryptDecryptMechanism hsmMechanism;
    private byte[] cipherText;
    private String keyId;
    private Map<IssuerPropertyDefinition, String> issuerPropertiesMap;

    @Builder
    public HSMDecryptInvokeDTO(HSMEncryptDecryptMechanism hsmMechanism
            , byte[] cipherText, String keyId, Map<String, String> systemPropertiesMap, Map<IssuerPropertyDefinition, String> issuerPropertiesMap) {
        super(systemPropertiesMap);
        this.hsmMechanism = hsmMechanism;
        this.cipherText = cipherText;
        this.keyId = keyId;
        this.issuerPropertiesMap = issuerPropertiesMap;
    }
}
