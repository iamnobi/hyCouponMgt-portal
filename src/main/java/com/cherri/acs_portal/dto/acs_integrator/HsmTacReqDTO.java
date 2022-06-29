package com.cherri.acs_portal.dto.acs_integrator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class HsmTacReqDTO {

    @Getter @Setter
    private String account;
    @Getter @Setter
    private String transNo;
    @Getter @Setter
    private String data;

}
