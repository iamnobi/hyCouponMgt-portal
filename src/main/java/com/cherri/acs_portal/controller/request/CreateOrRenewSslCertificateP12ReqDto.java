package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.util.FileUtils;
import java.io.IOException;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.SslClientCertificateDO;
import ocean.acs.models.oracle.entity.SslClientCertificate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateOrRenewSslCertificateP12ReqDto extends AuditableDTO {

    String cardBrand;
    String password;

    // for AuditableDTO
    /**
     * USER_ACCOUNT.ID
     */
    private Long id;
    private Long issuerBankId;
    private AuditStatus auditStatus;

    private String user;

    public CreateOrRenewSslCertificateP12ReqDto(String cardBrand, String password,
      MultipartFile file) throws IOException {
        this.cardBrand = cardBrand;
        this.fileName = FileUtils
          .extractFilename(Objects.requireNonNull(file.getResource().getFilename()));
        log.debug("[CreateOrRenewSslCertificateP12ReqDto] filename={}", fileName);
        this.fileContent = file.getBytes();
        this.password = password;
    }

    public CreateOrRenewSslCertificateP12ReqDto(Long id, String cardBrand, String password,
      MultipartFile file) throws IOException {
        this.id = id;
        this.cardBrand = cardBrand;
        this.fileName = FileUtils
          .extractFilename(Objects.requireNonNull(file.getResource().getFilename()));
        log.debug("[CreateOrRenewSslCertificateP12ReqDto] filename={}", fileName);
        this.fileContent = file.getBytes();
        this.password = password;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_SSL_CERT;
    }

    public static CreateOrRenewSslCertificateP12ReqDto valueOf(
      SslClientCertificate sslClientCertificate) {
        CreateOrRenewSslCertificateP12ReqDto dto = new CreateOrRenewSslCertificateP12ReqDto();
        dto.id = sslClientCertificate.getId();
        dto.cardBrand = sslClientCertificate.getCardBrand();
        dto.auditStatus = AuditStatus.getStatusBySymbol(sslClientCertificate.getAuditStatus());
        return dto;
    }

    public static CreateOrRenewSslCertificateP12ReqDto valueOf(
      SslClientCertificateDO sslClientCertificate) {
        CreateOrRenewSslCertificateP12ReqDto dto = new CreateOrRenewSslCertificateP12ReqDto();
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
