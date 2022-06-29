package com.cherri.acs_kernel.plugin.dto.hsm.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;

@Data
public class HSMEncryptResultDTO extends ResultDTO {

    private byte[] ciphertext;

    private HSMEncryptResultDTO(
            boolean isSuccess, Exception exception, byte[] ciphertext) {
        super(isSuccess, exception);
        this.ciphertext = ciphertext;
    }

    public static HSMEncryptResultDTO newInstanceOfSuccessWithData(byte[] ciphertext) {
        return new HSMEncryptResultDTO(true, null, ciphertext);
    }

    public static HSMEncryptResultDTO newInstanceOfFailure() {
        return new HSMEncryptResultDTO(false, null, null);
    }

    public static HSMEncryptResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new HSMEncryptResultDTO(false, e, null);
    }
}
