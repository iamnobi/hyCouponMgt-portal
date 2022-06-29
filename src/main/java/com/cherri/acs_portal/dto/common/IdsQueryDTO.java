package com.cherri.acs_portal.dto.common;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.validator.validation.TimeZoneValidation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;


@Data
@NoArgsConstructor
public class IdsQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long issuerBankId;

    @NotNull(message = "{column.notempty}")
    private List<Long> ids;

    @NotBlank(message = "{column.notempty}")
    @TimeZoneValidation
    private String timeZone;

    public void addId(Long id) {
        if (ids == null) {
            ids = new ArrayList<>();
        }
        ids.add(id);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
}
