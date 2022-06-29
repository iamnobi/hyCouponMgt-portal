package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
public class UpdateBankGroupReqDto {

    @NotNull(message = "{column.notempty}")
    @JsonProperty("issuerBankId")
    private Long issuerBankId;

    @NotNull(message = "{column.notempty}")
    @JsonProperty("groupId")
    private Long groupId;

    @NotNull(message = "{column.notempty}")
    @JsonProperty("name")
    private String name;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

}
