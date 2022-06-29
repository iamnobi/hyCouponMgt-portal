package com.cherri.acs_portal.controller.request;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeyImportVerifyReqDTO {

    @NotNull(message = "{column.notempty}")
    private List<String> keyComponents;

    @Override
    public String toString() {
        //        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
        // NOTE: 機敏資料不寫入 audit log
        return "keyComponents="
          + (keyComponents == null
          ? ""
          : keyComponents.stream().map(s -> "****").collect(Collectors.joining(",")));
    }
}
