package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Check3ds1ResDto extends BaseResDTO{

    private Boolean is3ds1;

}
