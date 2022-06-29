package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CardBrandLogoDTO extends AuditableDTO {

    private String cardBrand;
    private AuditStatus auditStatus;

  @JsonIgnore
  @Override
  public AuditFunctionType getFunctionType() {
    return AuditFunctionType.SYS_CARD_BRAND_LOGO;
  }

}
