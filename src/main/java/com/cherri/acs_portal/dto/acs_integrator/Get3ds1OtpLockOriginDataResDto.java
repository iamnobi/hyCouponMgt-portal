package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Get3ds1OtpLockOriginDataResDto extends BaseResDTO{

    private Long id;
    private String auditStatus;
    private String creator;
    private String updater;

}
