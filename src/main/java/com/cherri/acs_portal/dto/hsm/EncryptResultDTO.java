package com.cherri.acs_portal.dto.hsm;

import com.google.common.io.BaseEncoding;
import lombok.Data;

@Data
public class EncryptResultDTO {

    private byte[] ciphertext;

    public EncryptResultDTO(byte[] ciphertext) {
        this.ciphertext = ciphertext;
    }

    public String getBase64() {
        return BaseEncoding.base64().encode(ciphertext);
    }
}
