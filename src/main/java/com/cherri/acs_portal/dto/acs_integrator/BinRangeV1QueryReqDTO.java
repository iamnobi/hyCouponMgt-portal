package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class BinRangeV1QueryReqDTO {

    @NotNull private String bankCode;
    @NotNull private String cardNumber;

}
