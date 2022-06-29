package com.cherri.acs_portal.dto.acs_integrator;

import com.cherri.acs_portal.controller.response.TransactionRecordV1ResDTO;
import com.cherri.acs_portal.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZoneId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ocean.acs.models.oracle.entity.PALogPortal;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude
public class TransactionReportV1ResDTO extends BaseResDTO {

  /** 驗證時間 */
  private String transTime;
  /** 卡號 */
  private String pan;
  /** 金額 */
  private String amount;
  /** 幣別 */
  private String currencyCode;
  /** RBA result */
  private String rbaResult;
  /** 持卡人IP */
  private String ip;
  /** 購物時間 */
  private String purchaseTime;
  /** 商家名稱 */
  private String merchantName;
  /** 商家國家代碼 */
  private String merchantCountryCode;
  /** Authentication Channel */
  private String authenticationChannel;
  /** VERes, for 風險評估(RBA), 驗證註冊狀態 Y or N，決定於外部傳來的VReq，是否有設定binRange */
  private String veresTransStatus = "Y";
  /** PARes, for 風險評估(RBA), 交易狀態 ，決定於外部傳來的PAReq (Y, N, U, A) */
  private String paresTransStatus;
  /** Challenge Result */
  private String otpResult;
  private String xid;
  /** User-Agent（瀏覽器） */
  private String userAgent;
  private String issuerName;
  public static TransactionReportV1ResDTO valueOf(
      TransactionRecordV1ResDTO record, TransactionSummaryV1ResDTO summary, String timeZone) {
    TransactionReportV1ResDTO data = new TransactionReportV1ResDTO();
    data.setTransTime(DateUtil.millisToDateTimeStr(record.getTransTime(), ZoneId.of(timeZone)));
    data.setPan(record.getPan());
    data.setAmount(record.getAmount());
    data.setCurrencyCode(record.getCurrency());
    data.setRbaResult(record.getFailureReason());

    data.setIp(summary.getIp());
    data.setPurchaseTime(DateUtil.millisToDateTimeStr(summary.getShoppingTime(), ZoneId.of(timeZone)));
    data.setMerchantName(summary.getMerchantName());
    data.setMerchantCountryCode(summary.getMerchantCountryCode());
    data.setAuthenticationChannel(summary.getChallengeOtpChannel());
    data.setVeresTransStatus(summary.getVeres());
    data.setParesTransStatus(summary.getPares());
    data.setOtpResult(summary.getOtpResult());
    data.setXid(summary.getXid());
    data.setUserAgent(summary.getUserAgent());
    data.setIssuerName(record.getIssuerName());
    return data;
  }
}
