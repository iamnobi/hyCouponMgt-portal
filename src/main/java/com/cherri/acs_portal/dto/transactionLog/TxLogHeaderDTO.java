package com.cherri.acs_portal.dto.transactionLog;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ocean.acs.commons.enumerator.TransStatus;
import org.apache.commons.lang3.StringUtils;

/** 查詢交易紀錄Response物件 */
@Getter
@ToString
@NoArgsConstructor
public class TxLogHeaderDTO implements Serializable {

  public enum TransactionResult {
    SUCCESS, // 驗證成功
    FAIL, // 驗證失敗
  }

  private static final long serialVersionUID = 1L;

  @Setter
  private Long id;					//kernel_transaction_log.id
  @Setter
  private String acsTransId;
  @Setter
  private String acsIntegratorTransId;
  @Setter
  private String deviceId;
  @Setter
  private Long transTime;			//驗證時間
  @Setter
  private String pan;				//卡號
  @Setter
  private String issuerName;
  private String amount;			//金額
  private String currency;			//貨幣
  private String ares;				//ares and rres is used by front end for [驗證結果]
  private String rres;
  private String failureReason;		//RBA結果
  @Setter
  private Long issuerBankId;
  @Setter
  private String acqBin;

  public TransactionResult getTransactionResult() {
    // ARes = Y || (RReq = Y && no challengeErrorReasonCode)
    if (TransStatus.Authentication.getCode().equals(ares)
        || (TransStatus.Authentication.getCode().equals(rres)
            && StringUtils.isEmpty(challengeErrorReasonCode))) {
      return TransactionResult.SUCCESS;

    } else if (TransStatus.Challenge.getCode().equals(ares) && StringUtils.isEmpty(rres)) {
      // ARes = C && no RReq --> 未完成
      return null;

    } else {
      // 其他 -> 失敗
      return TransactionResult.FAIL;
    }
  }

  @JsonIgnore
  @Setter
  private String enCardNumber;		//加密的卡號
  @JsonIgnore
  @Setter
  private String challengeErrorReasonCode; // 用來判斷交易是否成功

  public void setAmount(String amount) {
    this.amount = amount == null ? "" : amount;
  }

  public void setCurrency(String currency) {
    this.currency = currency == null ? "" : currency;
  }

  public void setFailureReason(String failureReason) {
    this.failureReason = failureReason == null ? "" : failureReason;
  }

  public void setAres(String ares) {
    this.ares = ares == null ? "" : ares;
  }

  public void setRres(String rres) {
    this.rres = rres == null ? "" : rres;
  }
}
