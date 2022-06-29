package com.cherri.acs_kernel.plugin.dto.cardholder.result;

import com.cherri.acs_kernel.plugin.dto.AuthenticationChannel;
import com.cherri.acs_kernel.plugin.dto.ResultDTO;
import java.util.List;
import lombok.Data;

@Data
public class CardholderGetCardholderInfoResultDTO extends ResultDTO {
    private boolean isCardholderExists;
    private String cardHolderId;
    private String cardHolderName;
    /** Postal Code */
    private String zipCode;

    /**
     * OTP authentication channel info
     * The channel information would take as different option and display in challenge page
     * Template:
     * {name} : {description}
     * - {connection key} : {MASKED connection value}
     * sample:
     * SMS : sending otp code by sms
     * - phone: 10xx xxx xxx 23
     * Email: sending otp code to user mail box
     * - email: xxx@xxx.com
     * P.S. the connection value must be masked with PCI compliance
     */
    private List<AuthenticationChannel> otpAuthChannelList;

    private CardholderGetCardholderInfoResultDTO(
      boolean isSuccess, Exception exception, boolean isCardholderExists, String cardHolderId, String cardHolderName,
      String zipCode) {
        super(isSuccess, exception);
        this.isCardholderExists = isCardholderExists;
        this.cardHolderId = cardHolderId;
        this.cardHolderName = cardHolderName;
        this.zipCode = zipCode;
    }

    private CardholderGetCardholderInfoResultDTO(
      boolean isSuccess, Exception exception, boolean isCardholderExists, String cardHolderId, String cardHolderName,
      String zipCode, List<AuthenticationChannel> list) {
        super(isSuccess, exception);
        this.isCardholderExists = isCardholderExists;
        this.cardHolderId = cardHolderId;
        this.cardHolderName = cardHolderName;
        this.zipCode = zipCode;
        this.otpAuthChannelList = list;
    }

    public static CardholderGetCardholderInfoResultDTO newInstanceOfSuccess(
      boolean isCardholderExist, String cardHolderId, String cardHolderName, String zipCode,
      List<AuthenticationChannel> list) {
        return new CardholderGetCardholderInfoResultDTO(true, null, isCardholderExist, cardHolderId, cardHolderName,
          zipCode, list);
    }

    public static CardholderGetCardholderInfoResultDTO newInstanceOfSuccess(
      boolean isCardholderExist, String cardHolderId, String cardHolderName, String zipCode) {
        return new CardholderGetCardholderInfoResultDTO(true, null, isCardholderExist, cardHolderId, cardHolderName,
          zipCode);
    }

    public static CardholderGetCardholderInfoResultDTO newInstanceOfFailure() {
        return new CardholderGetCardholderInfoResultDTO(false, null, false, null, null, null);
    }

    public static CardholderGetCardholderInfoResultDTO newInstanceOfExceptionHappened(Exception e) {
        return new CardholderGetCardholderInfoResultDTO(false, e, false, null, null, null);
    }
}
