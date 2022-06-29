package com.cherri.acs_kernel.plugin.hsm.impl.exception;

public class CloudHsmException extends Exception {

    private static final long serialVersionUID = -4926563983532436437L;

    public CloudHsmException(String errorMessage) {
        super(errorMessage);
    }

    public CloudHsmException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
