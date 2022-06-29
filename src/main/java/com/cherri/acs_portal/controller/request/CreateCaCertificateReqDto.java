package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateCaCertificateReqDto extends AuditableDTO {

    private String cardBrand;
    private MultipartFile caCertFile;

    // for AuditableDTO
    /**
     * USER_ACCOUNT.ID
     */
    private Long id;
    private Long issuerBankId;
    private AuditStatus auditStatus;

    private String user;

    public CreateCaCertificateReqDto(String cardBrand, MultipartFile caCertFile) {
        this.cardBrand = cardBrand;
        this.caCertFile = caCertFile;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_CA_CERT;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

}
