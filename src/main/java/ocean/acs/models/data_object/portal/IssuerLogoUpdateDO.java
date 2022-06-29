package ocean.acs.models.data_object.portal;

import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.oracle.entity.IssuerBrandLogo;
import ocean.acs.models.oracle.entity.IssuerLogo;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class IssuerLogoUpdateDO extends AuditableDO {

    @JsonIgnore
    private MultipartFile image;

    private Integer imageSize;

    private String user;
    private AuditStatus auditStatus;

    public IssuerLogoUpdateDO(Long issuerBankId, MultipartFile file, String user) {
        setIssuerBankId(issuerBankId);
        this.image = file;
        this.user = user;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_ISSUER_LOGO;
    }

    public static IssuerLogoUpdateDO valueOf(IssuerLogo entity) {
        IssuerLogoUpdateDO dto = new IssuerLogoUpdateDO();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());
        dto.setImageSize(entity.getImageSize());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        dto.setFileName(entity.getImageName());
        dto.setFileContent(entity.getImageContent());
        return dto;
    }

    public static IssuerLogoUpdateDO valueOf(IssuerBrandLogo entity) {
        IssuerLogoUpdateDO dto = new IssuerLogoUpdateDO();
        dto.setId(entity.getId());
        dto.setIssuerBankId(entity.getIssuerBankId());
        dto.setImageSize(entity.getImageSize());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        dto.setFileName(entity.getImageName());
        dto.setFileContent(entity.getImageContent());
        return dto;
    }
}
