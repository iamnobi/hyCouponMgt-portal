package com.cherri.acs_portal.dto.acs_integrator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/** 更新ACS Integrator 3DS 1.0版本 人工彈性授權 審核狀態 */
@AllArgsConstructor
@Data
public class Update3ds1AttemptGrantedAuditStatusRequestDto {

    @JsonProperty("cardNumberEn")
    private String cardNumberEn;

    @JsonProperty("auditStatus")
    private String auditStatus;

    @JsonProperty("updater")
    private String updater;
}
