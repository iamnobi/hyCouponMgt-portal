package com.cherri.acs_kernel.plugin.dto.otp.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GenerateResultDTO extends ResultDTO {
    //auth identity for server identify. length: 8
    private String authId;

    private String authCode;

    private GenerateResultDTO(
            boolean isSuccess, Exception exception, String authId, String authCode) {
        super(isSuccess, exception);
        this.authId = authId;
        this.authCode = authCode;
    }

    public static GenerateResultDTO newInstanceOfSuccessWithData(String authId, String authCode) {
        return new GenerateResultDTO(true, null, authId, authCode);
    }

    public static GenerateResultDTO newInstanceOfFailure() {
        return new GenerateResultDTO(false, null, null, null);
    }

    public static GenerateResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new GenerateResultDTO(false, e, null, null);
    }
}
