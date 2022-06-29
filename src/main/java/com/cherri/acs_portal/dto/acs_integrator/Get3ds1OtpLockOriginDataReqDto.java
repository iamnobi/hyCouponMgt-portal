package com.cherri.acs_portal.dto.acs_integrator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/** 向ACS Integrator查詢3DS 1.0版本 OTP lock更新前原始資料 */
@AllArgsConstructor
@Data
public class Get3ds1OtpLockOriginDataReqDto {

    @JsonProperty("cardNumberEn")
    private String cardNumberEn;

}
