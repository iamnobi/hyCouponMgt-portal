package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.commons.enumerator.AuditLogAction;
import ocean.acs.commons.utils.IpUtils;
import ocean.acs.models.data_object.entity.AuditLogDO;
import ocean.acs.models.entity.DBKey;

@Builder
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_AUDIT_LOG)
public class AuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_AUDIT_LOG_ID)
    private Long id;

    @Column(name = DBKey.COL_AUDIT_LOG_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_AUDIT_LOG_IP)
    private String ip;

    @NonNull
    @Column(name = DBKey.COL_AUDIT_LOG_METHOD_NAME)
    private String methodName;

    @NonNull
    @Column(name = DBKey.COL_AUDIT_LOG_VAL)
    private String value;

    @NonNull
    @Column(name = DBKey.COL_AUDIT_LOG_ACTION)
    private String action;

    @NonNull
    @Column(name = DBKey.COL_AUDIT_LOG_ERROR_CODE)
    private String errorCode;

    @NonNull
    @Column(name = DBKey.COL_AUDIT_LOG_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_AUDIT_LOG_CREATE_MILLIS)
    private Long createMillis;

    private AuditLog(@NonNull Long issuerBankId, @NonNull String ip, @NonNull String methodName,
      @NonNull String value, @NonNull String action, @NonNull String errorCode,
      @NonNull String sysCreator) {
        this.issuerBankId = issuerBankId;
        this.ip = ip;
        this.methodName = methodName;
        this.value = value;
        this.action = action;
        this.errorCode = errorCode;
        this.sysCreator = sysCreator;
        this.createMillis = System.currentTimeMillis();
    }
    
    public void setErrorResult(String errorCode, String action) {
        this.errorCode = errorCode;
        this.action = action;
    }

    public static AuditLog createInstance(Long issuerBankId, String ip, String methodName,
      String value, String action, String errorCode, String creator) {
        return new AuditLog(issuerBankId, ip, methodName, value, action, errorCode, creator);
    }

    public static AuditLog initLoginAuditLogInstance(HttpServletRequest request, long issuerBankId,
      String account) {
        AuditLog auditLog = AuditLog.createInstance(issuerBankId, IpUtils.getIPFromRequest(request),
          "LOGIN", "account=" + account, AuditLogAction.Y.name(), "0", account);
        return auditLog;
    }

    public static AuditLog valueOf(AuditLogDO d) {
        return AuditLog.builder()
          .issuerBankId(d.getIssuerBankId())
          .ip(d.getIp())
          .methodName(d.getMethodName())
          .value(d.getValue())
          .action(d.getAction())
          .errorCode(d.getErrorCode())
          .sysCreator(d.getSysCreator())
          .createMillis(System.currentTimeMillis())
          .build();
    }
}
