package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class IdsQueryReqDTO {
  private String bankCode;
  private String ids;
}
