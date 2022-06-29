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
import ocean.acs.models.data_object.entity.SslClientCertificateDO;
import ocean.acs.models.oracle.entity.SslClientCertificate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateOrRenewSslCertificateReqDto extends AuditableDTO {

    String cardBrand;

    // for AuditableDTO
    /**
     * USER_ACCOUNT.ID
     */
    private Long id;
    private Long issuerBankId;
    private AuditStatus auditStatus;

    private String user;

    public CreateOrRenewSslCertificateReqDto(String cardBrand, MultipartFile file)
      throws IOException {
        this.cardBrand = cardBrand;
        this.fileContent = file.getBytes();
        this.fileName = file.getName();
    }

    public CreateOrRenewSslCertificateReqDto(Long id, String cardBrand, byte[] fileContent, String filename)
      throws IOException {
        this.id = id;
        this.cardBrand = cardBrand;
        this.fileContent = fileContent;
        this.fileName = filename;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_SSL_CERT;
    }

    public static CreateOrRenewSslCertificateReqDto valueOf(
      SslClientCertificate sslClientCertificate) {
        CreateOrRenewSslCertificateReqDto dto = new CreateOrRenewSslCertificateReqDto();
        dto.id = sslClientCertificate.getId();
        dto.cardBrand = sslClientCertificate.getCardBrand();
        dto.auditStatus = AuditStatus.getStatusBySymbol(sslClientCertificate.getAuditStatus());
        return dto;
    }

    public static CreateOrRenewSslCertificateReqDto valueOf(
      SslClientCertificateDO sslClientCertificate) {
        CreateOrRenewSslCertificateReqDto dto = new CreateOrRenewSslCertificateReqDto();
        dto.id = sslClientCertificate.getId();
        dto.cardBrand = sslClientCertificate.getCardBrand();
        dto.auditStatus = AuditStatus.getStatusBySymbol(sslClientCertificate.getAuditStatus());
        return dto;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

}
