package com.cherri.acs_portal.aspect;

import com.cherri.acs_portal.constant.SessionAttributes;
import com.cherri.acs_portal.dto.ApiPageResponse;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.util.IpUtils;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditLogAction;
import ocean.acs.commons.utils.AuditLogToStringStyle;
import ocean.acs.models.dao.AuditLogDAO;
import ocean.acs.models.data_object.entity.AuditLogDO;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
@Log4j2
public class AuditLogAspect {

    public static final AuditLogToStringStyle AUDIT_LOG_STYLE = new AuditLogToStringStyle();
    private static final Set<Class<?>> WRAPPER_TYPES = getDefaultObjectTypes();
    @Autowired
    private AuditLogDAO repo;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    public static boolean isDefaultObjectType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getDefaultObjectTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(Array.class);
        ret.add(List.class);
        ret.add(ArrayList.class);
        ret.add(String.class);
        return ret;
    }

    @Around(value = "@annotation(com.cherri.acs_portal.aspect.AuditLogHandler)")
    public Object auditLogHandle(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        AuditLogMethodNameEnum methodName = signature.getMethod()
          .getAnnotation(AuditLogHandler.class).methodName();
        Object[] args = proceedingJoinPoint.getArgs();
        String ip = IpUtils.getIPFromRequest(request);
        String user = (String) session.getAttribute(SessionAttributes.ACCOUNT);
        Long issuerBankId = (Long) session.getAttribute(SessionAttributes.ISSUER_BANK_ID);

        String value;
        Object returnObject = proceedingJoinPoint.proceed();
        int status = 0;
        if (methodName.equals(AuditLogMethodNameEnum.LOGOUT)) {
            value = "account=" + user;
        } else {
            value = generateParamString(args, signature.getParameterNames());
            status = getStatus(returnObject);
        }

        // 判斷匯出類型的API執行狀態：成功/失敗，因為這類型的API Response是直接輸出內容的
        String methodNam = methodName.name().toUpperCase();
        boolean isExportMethod = methodNam.contains("EXPORT") || methodNam.contains("DOWNLOAD");
        if (returnObject == null && isExportMethod) {
            status = 0; // success
        }

        String action = AuditLogAction.getByBoolean(status == 0).name();

        if (null == issuerBankId) {
            log.info("[auditLogHandle] no save audit, because issuerBankId is unknown.");
            return returnObject;
        }
        if (null == ip) {
            log.info("[auditLogHandle] no save audit, because ip is unknown.");
            return returnObject;
        }
        if (null == user) {
            log.info("[auditLogHandle] no save audit, because user is unknown.");
            return returnObject;
        }

        AuditLogDO auditLog =
          AuditLogDO.createInstance(
            issuerBankId, ip, methodName.toString(), value, action, status + "", user);
        log.debug(
          "[auditLogHandle] Audit Log - issuerBankId={}, methodName={}, value={}, action={}, errorCode={}, user={}, ip:{}",
          issuerBankId,
          StringUtils.normalizeSpace(methodName.name()),
          StringUtils.normalizeSpace(value),
          StringUtils.normalizeSpace(action),
          status,
          StringUtils.normalizeSpace(user),
          StringUtils.normalizeSpace(ip));
        repo.save(auditLog);

        return returnObject;
    }

    /**
     * AUDIT_LOG.ERROR_CODE
     */
    private Integer getStatus(Object returnObject) throws Throwable {
        int status = 1;
        if (returnObject != null) {
            if (returnObject instanceof ApiResponse) {
                status = ((ApiResponse) returnObject).getStatus();
            } else if (returnObject instanceof ApiPageResponse) {
                status = ((ApiPageResponse) returnObject).getStatus();
            } else {
                status = 0;
            }
        }
        return status;
    }

    private String generateParamString(Object[] args, String[] parameterNames) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append("|");
            }
            if (isDefaultObjectType(args[i].getClass())) {
                sb.append(parameterNames[i]).append("=").append(args[i]);
            } else if (args[i] instanceof MultipartFile) {
                // multipart file do nothing
            } else if (args[i] != null) {
                sb.append(args[i]);
            }
        }
        return sb.toString();
    }
}
