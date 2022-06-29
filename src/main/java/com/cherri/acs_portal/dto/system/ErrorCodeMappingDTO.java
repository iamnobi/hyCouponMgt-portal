package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.ErrorCodeMappingDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
public class ErrorCodeMappingDTO {

    private Long id;
    private String errorCode;
    private String errorMsg;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    public static ErrorCodeMappingDTO valueOf(ErrorCodeMappingDO codeMapping) {
        ErrorCodeMappingDTO codeMappingDTO = new ErrorCodeMappingDTO();
        codeMappingDTO.setId(codeMapping.getId());
        codeMappingDTO.setErrorCode(codeMapping.getCode());
        codeMappingDTO.setErrorMsg(codeMapping.getMessage());

        return codeMappingDTO;
    }
}
