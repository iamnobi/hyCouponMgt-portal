package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HealthCheckDTO extends BaseResDTO {
  private Boolean acsDatabaseStatus = false;
  private Boolean ccDcDatabaseStatus = false;
  private Boolean otpDatabaseStatus = false;
  private Boolean hsmStatus = false;
}
