package com.cherri.acs_portal.dto.audit;

import com.cherri.acs_portal.util.StringCustomizedUtils;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import ocean.acs.models.data_object.entity.AuditingDO;

@Data
public class AuditContentDTO {

    @NotBlank(message = "{column.notempty}")
    private Long auditId;

    private Long issuerBankId;

    private String functionType;

    private String actionType;

    private String draftContent;

    private String originalContent;

    private AuditFileDTO originalFile;

    private AuditFileDTO draftFile;

    private String applicant;

    private Long applyMillis;

    private String auditor;

    private String auditComment;

    private String issuerBankName;

    public AuditContentDTO(AuditingDO auditing) {
        auditId = auditing.getId();
        issuerBankId = auditing.getIssuerBankId();
        functionType = auditing.getFunctionType().getTypeSymbol();
        actionType = auditing.getActionType();
        applicant = auditing.getCreator();
        applyMillis = auditing.getCreateMillis();
        draftContent = auditing.getDraftContent();
        auditComment = auditing.getAuditComment();
        auditor = auditing.getUpdater();

        if (StringCustomizedUtils.isNotEmpty(auditing.getFileName())) {
            AuditFileDTO auditFile = new AuditFileDTO();
            auditFile.setContent(auditing.getFileContent());
            auditFile.setName(auditing.getFileName());

            this.draftFile = auditFile;
        }
    }
}
