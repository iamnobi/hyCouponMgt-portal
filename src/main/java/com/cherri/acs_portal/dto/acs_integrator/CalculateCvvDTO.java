package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalculateCvvDTO {

    private String cardNumber;
    private String atnLastFour;
    private String threeDSecurityResult;
    private String keyA;
    private String keyB;
}
