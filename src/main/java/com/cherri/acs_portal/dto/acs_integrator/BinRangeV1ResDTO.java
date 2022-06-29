package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class BinRangeV1ResDTO extends BaseResDTO {
  private Long id;

  private String bankCode;

  private String cardBrand;

  private String cardType;

  private String startRange;

  private String endRange;

  private String auditStatus;
}
