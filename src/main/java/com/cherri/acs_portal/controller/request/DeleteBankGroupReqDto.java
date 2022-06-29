package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
public class DeleteBankGroupReqDto {

    @NotNull(message = "{column.notempty}")
    @JsonProperty("issuerBankId")
    private Long issuerBankId;

    @NotNull(message = "{column.notempty}")
    @JsonProperty("groupId")
    private Long groupId;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

}