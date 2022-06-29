package com.cherri.acs_portal.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.portal.BrowserErrorLogResultDO;

/**
 * 瀏覽器異常紀錄查詢結果回傳物件
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrowserErrorLogResultDTO {

    private Long issuerBankId;
    private String browserType;
    private Integer year;
    private Integer month;
    private Integer day;

    /**
     * Client端發起第一次驗證(Challenge)請求逾時 次數
     */
    private Integer clientFirstChallengeRequestTimeoutCount;
    /**
     * 使用者驗證(challenge)操作逾時 次數
     */
    private Integer challengeOperationTimeoutCount;
    /**
     * CReq傳入無效的參數 次數
     */
    private Integer creqInvalidArgsCount;
    /**
     * 驗證(Challenge)時系統異常 次數
     */
    private Integer challengeSystemAbnormalCount;

    public static BrowserErrorLogResultDTO valueOf(BrowserErrorLogResultDO d) {
        return BrowserErrorLogResultDTO.builder()
          .issuerBankId(d.getIssuerBankId())
          .browserType(d.getBrowserType())
          .year(d.getYear())
          .month(d.getMonth())
          .day(d.getDay())
          .clientFirstChallengeRequestTimeoutCount(d.getClientFirstChallengeRequestTimeoutCount())
          .challengeOperationTimeoutCount(d.getChallengeOperationTimeoutCount())
          .creqInvalidArgsCount(d.getCreqInvalidArgsCount())
          .challengeSystemAbnormalCount(d.getChallengeSystemAbnormalCount())
          .build();
    }

}
