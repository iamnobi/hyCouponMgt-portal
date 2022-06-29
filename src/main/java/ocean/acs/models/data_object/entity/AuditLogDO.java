package ocean.acs.models.data_object.entity;

import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ocean.acs.commons.enumerator.AuditLogAction;
import ocean.acs.commons.utils.IpUtils;

/**
 * AuditLogDO
 *
 * @author Alan Chen
 */
@Data
@Builder
@AllArgsConstructor
public class AuditLogDO {

    private Long id;
    private Long issuerBankId;
    private String ip;
    private String methodName;
    private String value;
    private String action;
    private String errorCode;
    private String sysCreator;
    private Long createMillis;

    public static AuditLogDO createInstance(Long issuerBankId, String ip, String methodName,
      String value, String action, String errorCode, String creator) {
        return AuditLogDO.builder()
          .issuerBankId(issuerBankId)
          .ip(ip)
          .methodName(methodName)
          .value(value)
          .action(action)
          .errorCode(errorCode)
          .sysCreator(creator)
          .createMillis(System.currentTimeMillis())
          .build();
    }

    public static AuditLogDO initLoginAuditLogInstance(HttpServletRequest request,
      long issuerBankId,
      String account) {
        AuditLogDO auditLog = AuditLogDO
          .createInstance(issuerBankId, IpUtils.getIPFromRequest(request),
            "LOGIN", "account=" + account, AuditLogAction.Y.name(), "0", account);
        return auditLog;
    }

    public void setErrorResult(String errorCode, String action) {
        this.errorCode = errorCode;
        this.action = action;
    }

    public static AuditLogDO valueOf(ocean.acs.models.oracle.entity.AuditLog e) {
        return AuditLogDO.builder()
          .id(e.getId())
          .issuerBankId(e.getIssuerBankId())
          .ip(e.getIp())
          .methodName(e.getMethodName())
          .value(e.getValue())
          .action(e.getAction())
          .errorCode(e.getErrorCode())
          .sysCreator(e.getSysCreator())
          .createMillis(e.getCreateMillis())
          .build();
    }
    
    public static AuditLogDO valueOf(ocean.acs.models.sql_server.entity.AuditLog e) {
        return AuditLogDO.builder()
          .id(e.getId())
          .issuerBankId(e.getIssuerBankId())
          .ip(e.getIp())
          .methodName(e.getMethodName())
          .value(e.getValue())
          .action(e.getAction())
          .errorCode(e.getErrorCode())
          .sysCreator(e.getSysCreator())
          .createMillis(e.getCreateMillis())
          .build();
    }

}
