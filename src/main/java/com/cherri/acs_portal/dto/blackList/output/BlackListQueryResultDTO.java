package com.cherri.acs_portal.dto.blackList.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.portal.BlackListQueryResultDO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlackListQueryResultDTO {

    private String panId;
    private String id;

    /**
     * 加入時間
     */
    private Long createMillis;
    /**
     * 卡組織
     */
    private String cardBrand;
    /**
     * 卡號
     */
    private String cardNumber;
    /**
     * 阻檔次數
     */
    private Integer blockTimes = 0;

    @JsonIgnore
    private Long blackListPanBatchId;

    /**
     * 供還原卡號用，還原後將原始卡號放入cardNumber變數
     */
    @JsonIgnore
    private String enCardNumber;

    @JsonIgnore
    private String cardNumberHash;

    private AuditStatus auditStatus;

    public static BlackListQueryResultDTO valueOf(BlackListQueryResultDO d) {
        return BlackListQueryResultDTO.builder()
          .id(d.getId())
          .panId(d.getPanId())
          .cardNumber(d.getCardNumber())
          .cardBrand(d.getCardBrand())
          .auditStatus(d.getAuditStatus())
          .blackListPanBatchId(d.getBlackListPanBatchId())
          .blockTimes(d.getBlockTimes())
          .enCardNumber(d.getEnCardNumber())
          .cardNumberHash(d.getCardNumberHash())
          .createMillis(d.getCreateMillis())
          .build();
    }
}
