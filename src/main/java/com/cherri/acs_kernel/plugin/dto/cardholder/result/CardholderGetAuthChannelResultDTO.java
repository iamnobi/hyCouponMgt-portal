package com.cherri.acs_kernel.plugin.dto.cardholder.result;

import com.cherri.acs_kernel.plugin.dto.AuthenticationChannel;
import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;

@Data
public class CardholderGetAuthChannelResultDTO extends ResultDTO {
    private AuthenticationChannel authChannel;
    private boolean isChannelExist;

    private CardholderGetAuthChannelResultDTO(boolean isSuccess, Exception exception, AuthenticationChannel authChannel, boolean isChannelExist) {
        super(isSuccess, exception);
        this.authChannel = authChannel;
        this.isChannelExist = isChannelExist;
    }

    public static CardholderGetAuthChannelResultDTO newInstanceOfSuccess(
        AuthenticationChannel authChannel, boolean isChannelExist) {
        return new CardholderGetAuthChannelResultDTO(true, null, authChannel, isChannelExist);
    }

    public static CardholderGetAuthChannelResultDTO newInstanceOfFailure() {
        return new CardholderGetAuthChannelResultDTO(false, null, null, false);
    }

    public static CardholderGetAuthChannelResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new CardholderGetAuthChannelResultDTO(false, e, null, false);
    }

}
