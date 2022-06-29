package com.cherri.acs_portal.dto.acs_integrator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/** 向ACS Integrator 3DS 1.0版本 OTP lock 解鎖 */
@AllArgsConstructor
@Data
public class Unlock3ds1OtpReqDto {

    @JsonProperty("cardNumberEn")
    private String cardNumberEn;

    @JsonProperty("updater")
    private String updater;

}