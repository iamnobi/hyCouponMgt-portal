package com.cherri.acs_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ocean.acs.models.data_object.entity.MimaRecordDO;

/**
 * 密碼異動紀錄DTO
 *
 * @author Alan Chen
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MimaRecordDto {

    /**
     * 銀行代碼
     */
    private long issuerBankId;
    /**
     * 使用者帳號
     */
    private String account;
    /**
     * 密碼
     */
    private String mima;
    /**
     * 建立時間
     */
    private long createTime;

    /**
     * Transfer entity to dto
     *
     * @param record Mima record entity
     * @return Mima record dto
     */
    public static MimaRecordDto valueOf(MimaRecordDO record) {
        MimaRecordDto dto = new MimaRecordDto();
        dto.setIssuerBankId(record.getIssuerBankId());
        dto.setAccount(record.getAccount());
        dto.setMima(record.getEncryptedMima());
        dto.setCreateTime(record.getCreateTime());
        return dto;
    }
}
