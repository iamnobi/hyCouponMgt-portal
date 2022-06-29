package com.cherri.acs_portal.dto.hsm;

import java.security.interfaces.RSAPublicKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GenerateKeyResultDTO {

    private String keyId;
    private RSAPublicKey rsaPublicKey;

    private GenerateKeyResultDTO(String keyId, RSAPublicKey rsaPublicKey) {
        this.keyId = keyId;
        this.rsaPublicKey = rsaPublicKey;
    }
}
