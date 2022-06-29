package com.cherri.acs_portal.dto.acs_integrator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/** 向ACS Integrator查詢卡號是否為3DS 1.0版本 */
@AllArgsConstructor
@ToString
@Data
public class Check3ds1ReqDto {

    @JsonProperty("cardNumber")
    private String cardNumber;
}
