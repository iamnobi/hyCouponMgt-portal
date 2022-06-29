package com.cherri.acs_portal.dto.cardholder;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class HolderQueryDTO {
  @NotNull(message = "{column.notempty}")
  private Long issuerBankId;
  private String identityNumber;
  private String realCardNumber;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
