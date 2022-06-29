package com.cherri.acs_portal.dto.usermanagement;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.entity.AuditLogDO;

@Data
@ToString
@NoArgsConstructor
public class AuditLogDto {

    private Long id;
    private String account;
    private String ip;
    private String methodName;
    private String action;
    private String errorCode;
    private String value;
    private Long createMillis;

    public AuditLogDto(
      Long id,
      String account,
      String ip,
      String methodName,
      String action,
      String errorCode,
      String value,
      Long createMillis) {
        this.id = id;
        this.account = account;
        this.ip = ip;
        this.methodName = methodName;
        this.action = action;
        this.errorCode = errorCode;
        this.value = value;
        this.createMillis = createMillis;
    }

    public static AuditLogDto valueOf(AuditLogDO auditLog) {
        return new AuditLogDto(
          auditLog.getId(),
          auditLog.getSysCreator(),
          auditLog.getIp(),
          auditLog.getMethodName(),
          auditLog.getAction(),
          auditLog.getErrorCode(),
          auditLog.getValue(),
          auditLog.getCreateMillis());
    }
}
