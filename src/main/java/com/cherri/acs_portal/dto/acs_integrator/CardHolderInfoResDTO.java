package com.cherri.acs_portal.dto.acs_integrator;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CardHolderInfoResDTO extends BaseResDTO {
  private String name;
  private String identityNumber;
  private String birthday;
  private Credit credit;
  private Debit debit;
  private Map<String, String> cardEnrollStatusMap;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }

  @Data
  @NoArgsConstructor
  public static class Credit {
    private String phoneNumber;
    private String email;

    @Override
    public String toString() {
      return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
  }

  @Data
  @NoArgsConstructor
  public static class Debit {
    private String phoneNumber;
    private String email;

    @Override
    public String toString() {
      return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }
  }
}
