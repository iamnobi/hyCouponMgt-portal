package com.cherri.acs_kernel.plugin.dto.otp.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class VerifyResultDTO extends ResultDTO {

    private boolean isAuthenticated;

    /**
     * After Verify Message
     * <p>
     * This message is OTP verified fail message
     */
    private String verifyMessage;

    private VerifyResultDTO(
            boolean isSuccess, Exception exception, boolean isAuthenticated) {
        super(isSuccess, exception);
        this.isAuthenticated = isAuthenticated;
    }

    public static VerifyResultDTO newInstanceOfSuccessWithData(boolean isAuthenticated) {
        return new VerifyResultDTO(true, null, isAuthenticated);
    }

    public static VerifyResultDTO newInstanceOfFailure() {
        return new VerifyResultDTO(false, null, false);
    }

    public static VerifyResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new VerifyResultDTO(false, e, false);
    }
}
