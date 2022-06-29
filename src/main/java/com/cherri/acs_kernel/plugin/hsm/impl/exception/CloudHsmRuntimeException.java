package com.cherri.acs_kernel.plugin.hsm.impl.exception;

public class CloudHsmRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 5840720611527811199L;

    public CloudHsmRuntimeException(String errorMessage) {
        super(errorMessage);
    }

    public CloudHsmRuntimeException(Throwable cause) {
        super(cause);
    }

    public CloudHsmRuntimeException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
