package com.cherri.acs_portal.dto.acs_integrator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CardCertificateDTO {
  private String certificateApplyDate;
  private String certificateExpiryDate;
  private String certificateInformation;
  private String certificateIssuer;
  private String rootCaCertificateApplyDate;
  private String rootCaCertificateExpiryDate;
  private String rootCaCertificateInformation;
  private String rootCaCertificateIssuer;
  private String subCaCertificateApplyDate;
  private String subCaCertificateExpiryDate;
  private String subCaCertificateInformation;
  private String subCaCertificateIssuer;
}
