package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetOtpLockStatusResDto extends BaseResDTO {

    private Boolean otpLock;
    private String otpLockAuditStatus;

}
