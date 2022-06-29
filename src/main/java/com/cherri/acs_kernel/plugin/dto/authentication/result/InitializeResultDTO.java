package com.cherri.acs_kernel.plugin.dto.authentication.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;

public class InitializeResultDTO extends ResultDTO {

    private InitializeResultDTO(boolean isSuccess, Exception exception) {
        super(isSuccess, exception);
    }

    public static InitializeResultDTO newInstanceOfSuccess() {
        return new InitializeResultDTO(true, null);
    }

    public static InitializeResultDTO newInstanceOfFailure() {
        return new InitializeResultDTO(false, null);
    }

    public static InitializeResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new InitializeResultDTO(false, e);
    }

}
