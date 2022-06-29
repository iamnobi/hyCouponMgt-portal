package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class HsmTacResDTO extends BaseResDTO {
    
    private String tac;

}
