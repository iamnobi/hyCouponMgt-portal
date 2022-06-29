package com.cherri.acs_portal.dto.whitelist;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

@Data
@NoArgsConstructor
public class AttemptSettingLimitationDTO {
  private Long id;
  private Long triesPermittedLimit;
  private Long expireTime;

  private List<Currency> currencyList;

  @Data
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Currency {
    private String code;
    private String name;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
