package com.cherri.acs_portal.dto.hsm;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImportKeyResultDTO extends ResultDTO {

    private String keyId;

    private ImportKeyResultDTO(
      boolean isSuccess, Exception exception, String keyId) {
        super(isSuccess, exception);
        this.keyId = keyId;
    }

    public static ImportKeyResultDTO newInstanceOfSuccessWithData(String keyId) {
        return new ImportKeyResultDTO(true, null, keyId);
    }

    public static ImportKeyResultDTO newInstanceOfFailure() {
        return new ImportKeyResultDTO(false, null, null);
    }

    public static ImportKeyResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new ImportKeyResultDTO(false, e, null);
    }
}
