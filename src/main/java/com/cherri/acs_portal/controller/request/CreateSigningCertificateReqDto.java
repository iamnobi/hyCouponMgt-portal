package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.SigningCertificateDO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateSigningCertificateReqDto extends AuditableDTO {

    int threeDSVersion;
    String cardBrand;
    String issuerBankCode;

    private AuditStatus auditStatus;

    private String user;

    public CreateSigningCertificateReqDto(int version, String cardBrand, String issuerBankCode,
      byte[] fileContent, String fileName, Long issuerBankId) throws IOException {
        this.version = version;
        this.cardBrand = cardBrand;
        this.issuerBankCode = issuerBankCode;
        this.fileContent = fileContent;
        this.fileName = fileName;
        this.issuerBankId = issuerBankId;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_SIGNING_CERT;
    }

    public static CreateSigningCertificateReqDto valueOf(SigningCertificateDO signingCertificate) {
        CreateSigningCertificateReqDto dto = new CreateSigningCertificateReqDto();
        dto.setVersion(signingCertificate.getThreeDSVersion());
        dto.setId(signingCertificate.getId());
        dto.setCardBrand(signingCertificate.getCardBrand());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(signingCertificate.getAuditStatus()));
        return dto;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

}
