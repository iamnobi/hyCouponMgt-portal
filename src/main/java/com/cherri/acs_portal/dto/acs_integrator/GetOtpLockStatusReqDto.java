package com.cherri.acs_portal.dto.acs_integrator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/** 向ACS Integrator查詢卡號是否為3DS 1.0版本 OTP lock狀態 */
@AllArgsConstructor
@Data
public class GetOtpLockStatusReqDto {

    @JsonProperty("cardNumber")
    private String cardNumber;
}
