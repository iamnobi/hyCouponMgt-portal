package com.cherri.acs_portal.dto.hsm;

import lombok.Data;

@Data
public class SignResultDTO {

    private byte[] signature;

    private SignResultDTO(byte[] signature) {
        this.signature = signature;
    }
}
