package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** [增刪改] 3ds 1.0 BinRange Response物件 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BinRangeV1ModifyResDTO extends BaseResDTO {
  private String id;
}
