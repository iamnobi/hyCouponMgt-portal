package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Get3ds1AttemptGrantedOriginDataResDto extends BaseResDTO {

    private Long id;
    private String cardNumberEn;
    private Double maxMoney;
    private Long triesPermitted;
    private Long activeTime;

}
