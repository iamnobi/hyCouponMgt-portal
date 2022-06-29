package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BinRangeV1ListResDTO {
  private Long id;

  private String bankCode;

  private String cardBrand;

  private String cardType;

  private String startRange;

  private String endRange;

  private String auditStatus;
}
