package com.cherri.acs_portal.dto.blackList.input;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.TimePageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WhiteListPanQueryDTO extends TimePageQueryDTO {

  private String cardBrand;

  private String realCardNumber;

  private Long issuerBankId;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
