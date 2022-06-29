package com.cherri.acs_portal.dto.audit;

import com.cherri.acs_portal.util.StringCustomizedUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Optional;
import lombok.Data;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
public abstract class AuditableV1DTO implements Serializable {

    protected Long id;
    protected Long issuerBankId;

    @JsonIgnore
    protected String fileName;

    @JsonIgnore
    protected byte[] fileContent;

    public abstract void setAuditStatus(AuditStatus auditStatus);

    @JsonIgnore
    public abstract AuditFunctionType getFunctionType();

    @JsonIgnore
    public Optional<AuditFileDTO> getAuditFile() {
        if (fileContent == null || StringCustomizedUtils.isEmpty(fileName)) {
            return Optional.empty();
        }

        AuditFileDTO fileDTO = new AuditFileDTO();
        fileDTO.setContent(fileContent);
        fileDTO.setName(fileName);

        return Optional.of(fileDTO);
    }
}
