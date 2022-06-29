package com.cherri.acs_portal.dto.acs_integrator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/** 向ACS Integrator查詢3DS 1.0版本 人工彈性授權審核狀態 */
@AllArgsConstructor
@Data
public class Get3ds1AttemptGrantedAuditStatusReqDto {


    @JsonProperty("cardNumber")
    private String cardNumber;
}
