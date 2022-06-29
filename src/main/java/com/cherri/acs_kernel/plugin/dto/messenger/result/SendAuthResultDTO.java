package com.cherri.acs_kernel.plugin.dto.messenger.result;

import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SendAuthResultDTO
 *
 * @author William, Alan
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SendAuthResultDTO extends ResultDTO {

    /**
     * Authentication request id for server identify.(Optional)
     * <p>
     * String Max Length: 8
     * ex: if want query sms sending status can use this id
     */
    private String requestId;

    /**
     * Channel message when after sent OTP at OTP Verify page
     * <p>
     * ex: Please check OTP code by your phone. {phone}.
     * ex: Please check OTP code by your email. {mail}
     */
    private String channelMessage;

    private SendAuthResultDTO(boolean isSuccess, Exception exception, String requestId) {
        super(isSuccess, exception);
        this.requestId = requestId;
    }

    private SendAuthResultDTO(boolean isSuccess, Exception exception, String requestId, String channelMessage) {
        super(isSuccess, exception);
        this.requestId = requestId;
        this.channelMessage = channelMessage;
    }

    public static SendAuthResultDTO newInstanceOfSuccessWithData(String requestId) {
        return new SendAuthResultDTO(true, null, requestId);
    }

    public static SendAuthResultDTO newInstanceOfSuccessWithData(String requestId, String channelMessage) {
        return new SendAuthResultDTO(true, null, requestId, channelMessage);
    }

    public static SendAuthResultDTO newInstanceOfFailure() {
        return new SendAuthResultDTO(false, null, null);
    }

    public static SendAuthResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new SendAuthResultDTO(false, e, null);
    }
}
