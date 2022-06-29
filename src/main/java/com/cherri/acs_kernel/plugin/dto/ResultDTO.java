package com.cherri.acs_kernel.plugin.dto;

import lombok.Data;

@Data
public class ResultDTO {

    private boolean isSuccess;
    private Exception exception;

    protected ResultDTO() {
    }

    protected ResultDTO(boolean isSuccess, Exception exception) {
        this.isSuccess = isSuccess;
        this.exception = exception;
    }

    public boolean isExceptionHappened() {
        return exception != null;
    }
}
