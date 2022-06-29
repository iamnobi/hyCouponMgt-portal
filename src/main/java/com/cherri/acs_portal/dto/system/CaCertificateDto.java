package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.util.FileUtils;
import java.io.IOException;
import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.CaCertificateDO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CaCertificateDto extends AuditableDTO {

    private String cardBrand;

    private String certificateInformation;

    private Long expireMillis;

    private String issuer;

    private String hashAlgorithm;

    /**
     * USER_ACCOUNT.ID
     */
    private Long id;

    private Long issuerBankId;
    private AuditStatus auditStatus;

    private String user;

    public CaCertificateDto(String cardBrand, MultipartFile file)
      throws IOException, NullPointerException {
        this.cardBrand = cardBrand;
        this.fileName = FileUtils
          .extractFilename(Objects.requireNonNull(file.getResource().getFilename()));
        log.debug("[CaCertificateDto] filename={}", fileName);
        this.fileContent = file.getBytes();
    }

    private CaCertificateDto(
      Long id,
      String certificateInformation,
      Long expireMillis,
      String issuer,
      String hashAlgorithm,
      String auditStatus) {
        this.id = id;
        this.certificateInformation = certificateInformation;
        this.expireMillis = expireMillis;
        this.issuer = issuer;
        this.hashAlgorithm = hashAlgorithm;
        this.auditStatus = AuditStatus.getStatusBySymbol(auditStatus);
    }

    public static CaCertificateDto valueOf(CaCertificateDO caCertificate) {
        return new CaCertificateDto(
          caCertificate.getId(),
          caCertificate.getCertificateInformation(),
          caCertificate.getExpireMillis(),
          caCertificate.getIssuer(),
          caCertificate.getHashAlgorithm(),
          caCertificate.getAuditStatus());
    }

    public static CaCertificateDto parseToCaCertificateDto(CaCertificateDO caCertificate) {
        CaCertificateDto caCertificateDto = new CaCertificateDto();
        caCertificateDto.setId(caCertificate.getId());
        caCertificateDto.setCardBrand(caCertificate.getCardBrand());
        caCertificateDto.setId(caCertificate.getId());
        caCertificateDto.setCertificateInformation(caCertificate.getCertificateInformation());
        caCertificateDto.setExpireMillis(caCertificate.getExpireMillis());
        caCertificateDto.setIssuer(caCertificate.getIssuer());
        caCertificateDto.setHashAlgorithm(caCertificate.getHashAlgorithm());
        caCertificateDto
          .setAuditStatus(AuditStatus.getStatusBySymbol(caCertificate.getAuditStatus()));

        return caCertificateDto;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_CA_CERT;
    }
}
