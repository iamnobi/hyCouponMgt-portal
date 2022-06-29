package com.cherri.acs_portal.dto.veLog;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.PageQueryDTO;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VELogQueryDTO extends PageQueryDTO {
  /** 銀行ID */
  @NotNull(message = "{column.notempty}")
  private Long issuerBankId;
  /** 查詢時間範圍 起始時間 */
  private Long startMillis;
  /** 查詢時間範圍 結束時間 */
  private Long endMillis;
  private String cardBrand;
  /** 卡號 */
  private String pan;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
