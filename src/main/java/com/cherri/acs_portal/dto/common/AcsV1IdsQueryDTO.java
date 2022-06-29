package com.cherri.acs_portal.dto.common;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.validator.validation.TimeZoneValidation;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class AcsV1IdsQueryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long issuerBankId;

  @NotNull(message = "{column.notempty}")
  private List<String> ids;

  @NotBlank(message = "{column.notempty}")
  @TimeZoneValidation
  private String timeZone;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
