package com.cherri.acs_kernel.plugin.dto.hsm.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;

public class HSMInitializeResultDTO extends ResultDTO {

    private HSMInitializeResultDTO(boolean isSuccess, Exception exception) {
        super(isSuccess, exception);
    }

    public static HSMInitializeResultDTO newInstanceOfSuccess() {
        return new HSMInitializeResultDTO(true, null);
    }

    public static HSMInitializeResultDTO newInstanceOfFailure() {
        return new HSMInitializeResultDTO(false, null);
    }

    public static HSMInitializeResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new HSMInitializeResultDTO(false, e);
    }

}
