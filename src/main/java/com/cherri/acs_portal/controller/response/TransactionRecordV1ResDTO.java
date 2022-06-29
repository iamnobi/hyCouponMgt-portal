package com.cherri.acs_portal.controller.response;

import com.cherri.acs_portal.util.AcsPortalUtil;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.utils.MaskUtils;
import ocean.acs.models.data_object.kernel.PALogDO;
import ocean.acs.models.oracle.entity.PALogPortal;
import org.apache.commons.lang3.StringUtils;

@Log4j2
@Builder
@Data
public class TransactionRecordV1ResDTO {

    public enum VerifyResult {
      SUCCESS, // 驗證成功
      FAIL, // 驗證失敗
      SYSTEM_ERROR, // 系統異常
      IN_PROGRESS // 驗證未完成
    }

    private String id;

    /**
     * 驗證時間
     */
    private Long transTime;
    /**
     * 卡號
     */
    private String pan;
    /**
     * 金額
     */
    private String amount;
    /**
     * 幣別
     */
    private String currency;

    private VerifyResult verifyResult;

    private String failureReason;

    private String issuerName;

  public static TransactionRecordV1ResDTO valueOf(PALogPortal paLogDO, String pan, String amount, Map<Long, String> issuerBankIdNameMap) {
    VerifyResult verifyResult;
    if ("Y".equals(paLogDO.getTxStatus())) {
      verifyResult = VerifyResult.SUCCESS;

    } else if ("N".equals(paLogDO.getTxStatus())) {
      verifyResult = VerifyResult.FAIL;

    } else if ("U".equals(paLogDO.getTxStatus())) {
      verifyResult = VerifyResult.SYSTEM_ERROR;

    } else if (StringUtils.isBlank(paLogDO.getTxStatus())) {
      verifyResult = VerifyResult.IN_PROGRESS;
    } else {
      log.error("[valueOf] unexpected paLogDO.getTxStatus() = {}", paLogDO.getTxStatus());
      verifyResult = null;
    }

    return TransactionRecordV1ResDTO.builder()
        .id(paLogDO.getId())
        .transTime(paLogDO.getCreateMillis())
        .pan(pan)
        .amount(amount)
        .currency(AcsPortalUtil.currencyCodeAndNameFormatString(paLogDO.getPurchaseCurrency()))
        .verifyResult(verifyResult)
        .failureReason(paLogDO.getPaResErrorReason() == null ? "" : paLogDO.getPaResErrorReason().getMessage())
        .issuerName(issuerBankIdNameMap.get(paLogDO.getIssuerBankID()))
        .build();
  }
}
