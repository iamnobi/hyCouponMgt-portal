package com.cherri.acs_portal.dto.bank;

import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.IssuerBrandLogoDO;
import ocean.acs.models.data_object.entity.IssuerLogoDO;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
public class IssuerLogoUpdateDto extends AuditableDTO {

  @JsonIgnore private MultipartFile image;

  private Integer imageSize;

    private String user;
    private AuditStatus auditStatus;

    public IssuerLogoUpdateDto(Long issuerBankId, MultipartFile file, String user) {
        setIssuerBankId(issuerBankId);
        this.image = file;
        this.user = user;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_ISSUER_LOGO;
    }

    public static IssuerLogoUpdateDto valueOf(IssuerLogoDO entity) {
        IssuerLogoUpdateDto dto = new IssuerLogoUpdateDto();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());
        dto.setImageSize(entity.getImageSize());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        dto.setFileName(entity.getImageName());
        dto.setFileContent(entity.getImageContent());
        return dto;
    }

    public static IssuerLogoUpdateDto valueOf(IssuerBrandLogoDO entity) {
        IssuerLogoUpdateDto dto = new IssuerLogoUpdateDto();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());
        dto.setImageSize(entity.getImageSize());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        dto.setFileName(entity.getImageName());
        dto.setFileContent(entity.getImageContent());
        return dto;
    }
}
