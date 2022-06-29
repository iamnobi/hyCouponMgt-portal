package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.PageQueryDTO;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BinRangeQueryDTO extends PageQueryDTO {

    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;

    private String cardBrand;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
