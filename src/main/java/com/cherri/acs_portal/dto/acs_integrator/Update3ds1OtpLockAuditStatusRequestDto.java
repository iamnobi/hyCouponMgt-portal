package com.cherri.acs_portal.dto.acs_integrator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/** 更新ACS Integrator 3DS 1.0版本 OTP lock 解鎖審核狀態 */
@AllArgsConstructor
@Data
public class Update3ds1OtpLockAuditStatusRequestDto {

    @JsonProperty("cardNumberEn")
    private String cardNumberEn;

    @JsonProperty("auditStatus")
    private String auditStatus;

    @JsonProperty("updater")
    private String updater;

}
