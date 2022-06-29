package com.cherri.acs_portal.dto.blackList.input;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class BlackListAuthStatusUpdateDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull(message = "{column.notempty}")
  private Long id;

  @NotNull(message = "{column.notempty}")
  private Long issuerBankId;

  @NotBlank(message = "{column.notempty}")
  @Pattern(regexp = "[CN]", message = "{unaccepted.value}")
  private String authStatus;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
