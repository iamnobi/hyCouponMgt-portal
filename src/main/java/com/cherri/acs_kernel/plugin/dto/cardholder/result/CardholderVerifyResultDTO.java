package com.cherri.acs_kernel.plugin.dto.cardholder.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;

@Data
public class CardholderVerifyResultDTO extends ResultDTO {

    private boolean isValidity;
    private String integratorId;        // for backward compatible

    private CardholderVerifyResultDTO(boolean isSuccess, Exception exception, boolean isValidity, String integratorId) {
        super(isSuccess, exception);
        this.isValidity = isValidity;
        this.integratorId = integratorId;
    }

    public static CardholderVerifyResultDTO newInstanceOfSuccess(boolean isValidity, String integratorId) {

        return new CardholderVerifyResultDTO(true, null, isValidity, integratorId);
    }

    public static CardholderVerifyResultDTO newInstanceOfFailure() {
        return new CardholderVerifyResultDTO(false, null, false, "");
    }

    public static CardholderVerifyResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new CardholderVerifyResultDTO(false, e, false, "");
    }
}
