package com.cherri.acs_portal.dto.system;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SigningCertificateDTO {
  private String certificateInformation;
  private Long applyDate;
  private Long expireDate;
  private Boolean isExpire;
  private String issuer;
}
