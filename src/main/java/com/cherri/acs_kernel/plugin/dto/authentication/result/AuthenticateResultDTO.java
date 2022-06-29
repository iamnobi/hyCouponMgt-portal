package com.cherri.acs_kernel.plugin.dto.authentication.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuthenticateResultDTO extends ResultDTO {

    private boolean isAuthenticated;

    private AuthenticateResultDTO(
            boolean isSuccess, Exception exception, boolean isAuthenticated) {
        super(isSuccess, exception);
        this.isAuthenticated = isAuthenticated;
    }

    public static AuthenticateResultDTO newInstanceOfSuccessWithData(boolean isAuthenticated) {
        return new AuthenticateResultDTO(true, null, isAuthenticated);
    }

    public static AuthenticateResultDTO newInstanceOfFailure() {
        return new AuthenticateResultDTO(false, null, false);
    }

    public static AuthenticateResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new AuthenticateResultDTO(false, e, false);
    }
}
