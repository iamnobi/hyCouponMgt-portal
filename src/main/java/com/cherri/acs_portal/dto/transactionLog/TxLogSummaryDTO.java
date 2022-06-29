package com.cherri.acs_portal.dto.transactionLog;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 查詢交易紀錄摘要
 * Transaction record summary response object
 *
 * @author Vance, Alan
 */
@Getter
@ToString
@NoArgsConstructor
public class TxLogSummaryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 持卡人姓名 */
    private String cardholderName;
    /** 持卡人手機 */
    private String mobilePhone;
    /** 帳單地址 */
    private String billingAddress;
    /** 持卡人IP */
    private String ip;
    /** 購物時間 */
    private Long shoppingTime;
    /** 商家名稱 */
    private String merchantName;
    /** 商家國家代碼 */
    private String merchantCountryCode;
    /** 貨幣代碼 */
    private String currencyCode;
    /** 裝置通道 */
    private String deviceChannel;
    /** ARes */
    private String ares;
    /** RRES */
    private String rres;
    /** User-Agent（瀏覽器） */
    private String userAgent;
    /** OTP結果 */
    private String otpResult;
    /** Challenge reason */
    private String challengeReason;
    /** Encryption card number */
    private String enCardNumber;
    /** 風險評估(RBA) 說明 */
    private String rbaDescription;
    /** Transaction time */
    private Long createMillis;
    /** Challenge OTP channel name */
    private String challengeOtpChannel;

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName == null ? "" : cardholderName;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? "" : mobilePhone;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress == null ? "" : billingAddress;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? "" : ip;
    }

    public void setShoppingTime(Long shoppingTime) {
        this.shoppingTime = shoppingTime == null ? 0 : shoppingTime;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName == null ? "" : merchantName;
    }

    public void setMerchantCountryCode(String merchantCountryCode) {
        this.merchantCountryCode = merchantCountryCode == null ? "" : merchantCountryCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode == null ? "" : currencyCode;
    }

    public void setDeviceChannel(String deviceChannel) {
        this.deviceChannel = deviceChannel == null ? "" : deviceChannel;
    }

    public void setAres(String ares) {
        this.ares = ares;
    }

    public void setRres(String rres) {
        this.rres = rres;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent == null ? "" : userAgent;
    }

    public void setOtpResult(String otpResult) {
        this.otpResult = otpResult == null ? "" : otpResult;
    }

    public void setChallengeReason(String challengeReason) {
        this.challengeReason = challengeReason == null ? "" : challengeReason;
    }

    public void setEnCardNumber(String enCardNumber) {
        this.enCardNumber = enCardNumber == null ? "" : enCardNumber;
    }

    public void setRbaDescription(String rbaDescription) {
        this.rbaDescription = rbaDescription == null ? "" : rbaDescription;
    }

    public void setCreateMillis(long createMillis) {
        this.createMillis = createMillis;
    }

    public void setChallengeOtpChannel(String challengeOtpChannel) {
        this.challengeOtpChannel = challengeOtpChannel;
    }
}
