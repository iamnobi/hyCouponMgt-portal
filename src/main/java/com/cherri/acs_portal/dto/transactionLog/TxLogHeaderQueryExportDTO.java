package com.cherri.acs_portal.dto.transactionLog;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.PageQueryDTO;
import com.cherri.acs_portal.validator.validation.TimeZoneValidation;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** 查詢交易紀錄Request參數物件 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TxLogHeaderQueryExportDTO extends TxLogHeaderQueryDTO {

  @NotBlank(message = "{column.notempty}")
  @TimeZoneValidation
  private String timeZone;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
