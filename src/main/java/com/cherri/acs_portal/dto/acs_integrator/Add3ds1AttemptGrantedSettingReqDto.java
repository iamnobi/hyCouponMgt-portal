package com.cherri.acs_portal.dto.acs_integrator;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.whitelist.AttemptGrantedDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 向ACS Integrator 設定 3DS 1.0 人工彈性授權請求
 */
@AllArgsConstructor
@Data
public class Add3ds1AttemptGrantedSettingReqDto {

    @JsonProperty("cardNumberEn")
    private String cardNumberEn;

    @JsonProperty("bankCode")
    private String bankCode;

    @JsonProperty("maxMoney")
    private Double maxMoney;

    @JsonProperty("triesPermitted")
    private Long triesPermitted;

    @JsonProperty("activeTime")
    private Long activeTime; // 單位：分鐘

    @JsonProperty("updater")
    private String updater;

    public static Add3ds1AttemptGrantedSettingReqDto valueOf(AttemptGrantedDTO grantedDTO,
      String cardNumberEn, String bankCode) {
        return new Add3ds1AttemptGrantedSettingReqDto(
          cardNumberEn,
          bankCode,
          grantedDTO.getMaxMoney(),
          grantedDTO.getTriesPermitted(),
          EnvironmentConstants.WHITELIST_ATTEMPT_AVAILABLE_DURATION,
          grantedDTO.getCreator()
        );
    }

}
