package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.dto.audit.AuditableDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.ErrorCodeMappingDO;
import ocean.acs.models.data_object.entity.ErrorIssueGroupDO;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ErrorIssueGroupDTO extends AuditableDTO {

    private String groupName;
    private AuditStatus auditStatus = AuditStatus.UNKNOWN;
    private List<ErrorCodeMappingDTO> codeMappingList = new ArrayList<>();
    private String user;

    public static ErrorIssueGroupDTO valueOf(ErrorIssueGroupDO issueGroup) {
        ErrorIssueGroupDTO issueGroupDTO = new ErrorIssueGroupDTO();
        issueGroupDTO.setId(issueGroup.getId());
        issueGroupDTO.setGroupName(issueGroup.getName());
        issueGroupDTO.setAuditStatus(AuditStatus.getStatusBySymbol(issueGroup.getAuditStatus()));

        if (issueGroup.getCodeList() == null)
            return issueGroupDTO;

        for (ErrorCodeMappingDO codeMapping : issueGroup.getCodeList()) {
            ErrorCodeMappingDTO codeMappingDTO = ErrorCodeMappingDTO.valueOf(codeMapping);
            issueGroupDTO.getCodeMappingList().add(codeMappingDTO);
        }

        return issueGroupDTO;
    }

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_ERROR_CODE;
    }
}
