package com.cherri.acs_kernel.plugin.dto.cardholder.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;

public class CardholderInitializeResultDTO extends ResultDTO {

    private CardholderInitializeResultDTO(boolean isSuccess, Exception exception) {
        super(isSuccess, exception);
    }

    public static CardholderInitializeResultDTO newInstanceOfSuccess() {
        return new CardholderInitializeResultDTO(true, null);
    }

    public static CardholderInitializeResultDTO newInstanceOfFailure() {
        return new CardholderInitializeResultDTO(false, null);
    }

    public static CardholderInitializeResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new CardholderInitializeResultDTO(false, e);
    }
}
