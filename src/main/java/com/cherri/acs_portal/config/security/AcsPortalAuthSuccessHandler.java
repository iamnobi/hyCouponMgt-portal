package com.cherri.acs_portal.config.security;

import com.cherri.acs_portal.constant.SessionAttributes;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.service.AuditLogService;
import com.cherri.acs_portal.service.MimaPolicyService;
import com.cherri.acs_portal.util.GetBeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;

@Log4j2
public class AcsPortalAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = GetBeanUtil.getBean(ObjectMapper.class);
    private final AuditLogService auditLogService;
    private final MimaPolicyService mimaPolicyService;

    public AcsPortalAuthSuccessHandler(
        AuditLogService auditLogService,
        MimaPolicyService mimaPolicyService) {
        this.auditLogService = auditLogService;
        this.mimaPolicyService = mimaPolicyService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException {

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        Long issuerBankId = (Long) request.getSession().getAttribute(SessionAttributes.ISSUER_BANK_ID);
        String account = (String) request.getSession().getAttribute(SessionAttributes.ACCOUNT);

        boolean mimaIsExpired = mimaPolicyService.checkMimaIsExpired(issuerBankId, account);
        if (mimaIsExpired) {
            log.info("[onAuthenticationSuccess] password is expired");
        }

        boolean needUpdatePassword = mimaIsExpired;

        Map<String, Object> data = new HashMap<>();
        data.put("csrf_token", csrfToken.getToken());
        data.put("issuerBankId", issuerBankId);
        data.put("account", account);
        data.put("needUpdatePassword", needUpdatePassword);
        request.getSession()
            .setAttribute(SessionAttributes.NEED_UPDATE_MIMA, needUpdatePassword);

        ApiResponse responseData = ApiResponse.valueOf(data);
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.getOutputStream().println(objectMapper.writeValueAsString(responseData));

    }
}
