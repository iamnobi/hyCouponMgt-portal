package com.cherri.acs_portal.dto.acs_integrator;

import lombok.AllArgsConstructor;
import lombok.Data;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@AllArgsConstructor
public class OtpLockStatusDto {

    private Boolean otpLock;
    private AuditStatus otpLockAuditStatus;

    public static OtpLockStatusDto valueOf(GetOtpLockStatusResDto resDto) {
        return new OtpLockStatusDto(resDto.getOtpLock(),
          AuditStatus.getStatusBySymbol(resDto.getOtpLockAuditStatus()));
    }

}
