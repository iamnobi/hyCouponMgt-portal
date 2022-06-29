package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PluginIssuerRuntimeUpdateReqDTO {

    private Long issuerBankId;

    private String user;

    private List<PluginRuntimePropertyCollectionDTO> propertyCollectionList;

    public PluginIssuerRuntimeUpdateReqDTO(Long issuerBankId) {
        this.issuerBankId = issuerBankId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
