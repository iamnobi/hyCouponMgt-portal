package com.cherri.acs_portal.dto.transactionLog;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
public class TxLogExportDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String transTime = "";

  private Long kernelTransactionLogId;

  private String pan = "";

  private String ares = "";

  private String rres = "";

  private String deviceChannel = "";

  private String cardholderName = "";

  private String mobilePhone = "";

  private String billingAddress = "";

  private String shoppingTime = "";

  private String merchantName = "";

  private String merchantCountryCode = "";

  private String amount = "";

  private String currencyCode = "";

  private String userAgent = "";

  private String challengeReason = "";

  private String errorReason = "";

  private String issuerName = "";

  private Long issuerBankId;
  private String encCardNumber;
  private Long id;

  private String acqBIn = "";
}
