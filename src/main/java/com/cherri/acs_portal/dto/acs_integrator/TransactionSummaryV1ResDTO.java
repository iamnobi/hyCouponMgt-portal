package com.cherri.acs_portal.dto.acs_integrator;

import com.cherri.acs_portal.util.AcsPortalUtil;
import com.cherri.acs_portal.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ocean.acs.models.oracle.entity.PALogPortal;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude
public class TransactionSummaryV1ResDTO extends BaseResDTO {

  /** IP */
  private String ip;
  /** 購物時間 */
  private Long shoppingTime;
  /** 商家名稱 */
  private String merchantName;
  /** 商家國家代碼, ex: 158 (TW) */
  private String merchantCountryCode;
  /** VERES Transaction Status */
  private String veres;
  /** PARES Transaction Status */
  private String pares;
  private String otpResult;
  private String challengeOtpChannel;
  /** User-Agent（瀏覽器） */
  private String userAgent;
  private String enCardNumber;
  private long createMillis;
  private String xid;

  public static TransactionSummaryV1ResDTO valueOf(PALogPortal paLogDO) {
    TransactionSummaryV1ResDTO result = new TransactionSummaryV1ResDTO();
    result.setIp(paLogDO.getIp());
    result.setShoppingTime(
        DateUtil.threeDS1PurchaseDateToMillis(paLogDO.getPurchaseDate()));
    result.setMerchantName(paLogDO.getMerchantName());
    result.setMerchantCountryCode(
        AcsPortalUtil.countryCodeAndNameFormatString(paLogDO.getMerchantCountry()));
    result.setVeres("Y"); // VERes is always Y is PAReq exists
    result.setPares(paLogDO.getTxStatus());
    result.setOtpResult(
        paLogDO.getChallengeErrorReason() == null
            ? ""
            : paLogDO.getChallengeErrorReason().getMessage());
    result.setChallengeOtpChannel(paLogDO.getChallengeOtpChannel());
    result.setUserAgent(paLogDO.getBrowserUserAgent());
    result.setEnCardNumber(
        paLogDO.getPanInfo() == null ? "" : paLogDO.getPanInfo().getCardNumberEn());
    result.setCreateMillis(paLogDO.getCreateMillis());
    result.setXid(paLogDO.getPurchaseXid());
    return result;
  }
}
