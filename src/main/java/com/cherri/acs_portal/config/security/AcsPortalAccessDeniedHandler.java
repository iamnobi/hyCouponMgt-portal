package com.cherri.acs_portal.config.security;

import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.constant.SessionAttributes;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.util.IpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditLogAction;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.AuditLogDAO;
import ocean.acs.models.data_object.entity.AuditLogDO;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class AcsPortalAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HttpSession session;
    @Autowired
    private AuditLogDAO auditLogRepository;

    @Override
    public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {

        HttpStatus httpStatus = null;
        ResultStatus resultStatus = null;
        if (accessDeniedException instanceof MissingCsrfTokenException) {
            httpStatus = HttpStatus.UNAUTHORIZED;
            resultStatus = ResultStatus.UNAUTHORIZED;
        } else {
            httpStatus = HttpStatus.FORBIDDEN;
            resultStatus = ResultStatus.FORBIDDEN;
        }

        doAuditingLogging(request, httpStatus);

        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setStatus(httpStatus.value());
        response.getOutputStream()
          .println(objectMapper.writeValueAsString(new ApiResponse(resultStatus)));
    }

    private void doAuditingLogging(HttpServletRequest request, HttpStatus httpStatus) {

        String ip = IpUtils.getIPFromRequest(request);
        String user = (String) session.getAttribute(SessionAttributes.ACCOUNT);
        Long issuerBankId = (Long) session.getAttribute(SessionAttributes.ISSUER_BANK_ID);
        String pathInfo = request.getPathInfo();

        if (null == issuerBankId) {
            log.info("[doAuditingLogging] no save audit, because issuerBankId is unknown.");
            return;
        }
        if (null == ip) {
            log.info("[doAuditingLogging] no save audit, because ip is unknown.");
            return;
        }
        if (null == user) {
            log.info("[AudidoAuditingLoggingtLog] no save audit, because user is unknown.");
            return;
        }

        String val = "issuerBankId=" + issuerBankId + "|" +
          "account=" + user + "|" +
          "url=" + pathInfo;

        AuditLogDO auditLog =
          AuditLogDO.createInstance(
            issuerBankId,
            ip,
            AuditLogMethodNameEnum.NO_PERMISSION.name(),
            val,
            AuditLogAction.N.name(),
            httpStatus.value() + "",
            user);

        try {
            auditLogRepository.save(auditLog);
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
        }
    }
}
