package com.cherri.acs_kernel.plugin.hsm.impl.exception;

public class AwsSecretManagerRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 955330830357110833L;

    public AwsSecretManagerRuntimeException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
