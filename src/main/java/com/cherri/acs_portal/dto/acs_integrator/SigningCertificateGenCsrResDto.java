package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SigningCertificateGenCsrResDto extends BaseResDTO {
  private String csr;
}
