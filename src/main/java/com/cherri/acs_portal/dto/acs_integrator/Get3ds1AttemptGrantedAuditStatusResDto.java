package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Get3ds1AttemptGrantedAuditStatusResDto extends BaseResDTO {

    /** 審核狀態 */
    private String auditStatus;

}
