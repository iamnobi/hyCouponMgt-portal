package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AllCertificateDetailDTO {
  private CardCertificateDTO visa;
  private CardCertificateDTO mastercard;
  private CardCertificateDTO jcb;
  private CardCertificateDTO ae;
}
