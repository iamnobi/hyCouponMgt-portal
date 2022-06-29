package com.cherri.acs_kernel.plugin.dto.hsm.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;

@Data
public class HSMImportKeyResultDTO extends ResultDTO {

    private String keyId;

    private HSMImportKeyResultDTO(
            boolean isSuccess, Exception exception, String keyId) {
        super(isSuccess, exception);
        this.keyId = keyId;
    }

    public static HSMImportKeyResultDTO newInstanceOfSuccessWithData(String keyId) {
        return new HSMImportKeyResultDTO(true, null, keyId);
    }

    public static HSMImportKeyResultDTO newInstanceOfFailure() {
        return new HSMImportKeyResultDTO(false, null, null);
    }

    public static HSMImportKeyResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new HSMImportKeyResultDTO(false, e, null);
    }
}
