package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CertificateUploadResponseDTO extends BaseResDTO {
  private Long applyDate;
  private Long expiryDate;
  private String information;
  private String issuer;
}
