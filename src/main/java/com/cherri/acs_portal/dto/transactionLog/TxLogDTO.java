package com.cherri.acs_portal.dto.transactionLog;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class TxLogDTO implements Serializable{
  private static final long serialVersionUID = 1L;

  private Long id;

  private String acsTransID;

  private String threeDSServerTransID;

  private String sdkTransID;

  private String dsTransID;

  private String acsIntegratorTransID;

  private String deviceID;

  private String ip;

  private String notificationURL;

  private String dsURL;

  private String cardBrand;

  private String acctNumber;

  private String acctNumberHash;

  private String acctNumberEn;

  private String deviceChannel;

  private String mresTransStatus;

  private String messageVersion;

  private String otpPhoneNumber;

  private Integer challengeVerifyCount;

  private Integer challengeResendCount;

  private Boolean challengePhoneCorrect;

  private Boolean challengeCompleted;

  private String methodErrorReasonCode;

  private String authErrorReasonCode;

  private String challengeReasonCode;

  private String challengeErrorReasonCode;

  private String resultErrorReasonCode;

  private byte[] sdkSessionKey;

  private Long threeDSMethodLogId;

  private Long authenticationLogID;

  private Long challengeLogID;

  private Long resultLogID;

  private Long errorMessageLogID;

  private String sysCreator;

  private Long createMillis;

  private String sysUpdater;

  private Long updateMillis;

  private Long issuerBankId;
}
