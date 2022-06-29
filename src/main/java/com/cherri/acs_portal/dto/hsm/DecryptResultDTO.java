package com.cherri.acs_portal.dto.hsm;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DecryptResultDTO {

    private byte[] plainText;

    public DecryptResultDTO(byte[] plainText) {
        this.plainText = plainText;
    }

    public String getString() {
        return new String(plainText, StandardCharsets.UTF_8);
    }

    /**
     * For Fix Privacy Violation: Heap Inspection
     */
    public void clearPlainText() {
        Arrays.fill(this.plainText, (byte)' ');
    }
}
