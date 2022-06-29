package com.cherri.acs_portal.dto.system;

import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@NoArgsConstructor
public class SigningCertificateDetailDTO {

    private Long id;
    private AuditStatus auditStatus;
    private SigningCertificateDTO certificate;
    private SigningCertificateDTO rootCaCertificate;
    private SigningCertificateDTO subCaCertificate;
}
