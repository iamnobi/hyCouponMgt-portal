package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.model.enumerator.CavvKeyType;
import com.cherri.acs_portal.validator.validation.CardBrandValidation;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeyImportReqDTO {

    @NotNull(message = "{column.notempty}")
    private Long issuerBankId;

    @CardBrandValidation
    private String cardBrand;

    @NotNull(message = "{column.notempty}")
    private CavvKeyType keyType;

    @NotNull(message = "{column.notempty}")
    private List<String> keyComponents;

    @Override
    public String toString() {
        //        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
        // NOTE: 機敏資料不寫入 audit log
        return String.format(
          "issuerBankId=%s|cardBrand=%s|keyType=%s|keyComponents=%s",
          issuerBankId,
          cardBrand,
          keyType,
          keyComponents == null
            ? ""
            : keyComponents.stream().map(s -> "****").collect(Collectors.joining(",")));
    }
}
