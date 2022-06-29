package com.cherri.acs_kernel.plugin.dto.hsm.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import java.security.interfaces.RSAPublicKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HSMGenerateKeyResultDTO extends ResultDTO {

    private String keyId;
    private RSAPublicKey rsaPublicKey;

    private HSMGenerateKeyResultDTO(
            boolean isSuccess, Exception exception, String keyId, RSAPublicKey rsaPublicKey) {
        super(isSuccess, exception);
        this.keyId = keyId;
        this.rsaPublicKey = rsaPublicKey;
    }

    public static HSMGenerateKeyResultDTO newInstanceOfSuccessWithData(String keyId, RSAPublicKey rsaPublicKey) {
        return new HSMGenerateKeyResultDTO(true, null, keyId, rsaPublicKey);
    }

    public static HSMGenerateKeyResultDTO newInstanceOfFailure() {
        return new HSMGenerateKeyResultDTO(false, null, null, null);
    }

    public static HSMGenerateKeyResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new HSMGenerateKeyResultDTO(false, e, null, null);
    }
}
