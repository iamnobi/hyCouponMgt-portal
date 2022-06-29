package com.cherri.acs_portal.dto.usermanagement;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.AuditLogDO;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class AuditLogListResponse {

  private List<AuditLogDto> list;
  private Integer currentPage;
    private Integer totalPages;
    private Long total;

    public AuditLogListResponse(
      List<AuditLogDto> list, Integer currentPage, Integer totalPages, Long total) {
        this.list = list;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.total = total;
    }

    public static AuditLogListResponse valueOf(Page<AuditLogDO> auditLogPagingResult) {
        List<AuditLogDto> auditLogDtos = new ArrayList<>();
        auditLogPagingResult.getContent().stream()
          .forEach(
            auditLog -> {
                auditLogDtos.add(AuditLogDto.valueOf(auditLog));
            });
        return new AuditLogListResponse(
          auditLogDtos,
          auditLogPagingResult.getNumber() + 1,
          auditLogPagingResult.getTotalPages(),
          auditLogPagingResult.getTotalElements());
    }

  @Data
  private static class AuditLogDto {
    private String account;
    private String ip;
    private String methodName;
    private String action;
    private String errorCode;
    private String value;
    private Long createMillis;

      private AuditLogDto(
        String account,
        String ip,
        String methodName,
        String action,
        String errorCode,
        String value,
        Long createMillis) {
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
            auditLog.getSysCreator(),
            auditLog.getIp(),
            auditLog.getMethodName(),
            auditLog.getAction(),
            auditLog.getErrorCode(),
            auditLog.getValue(),
            auditLog.getCreateMillis());
      }
  }
}
