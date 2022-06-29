package com.cherri.acs_portal.config.security;

import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.util.GetBeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ocean.acs.commons.enumerator.ResultStatus;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AcsPortalAuthFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = GetBeanUtil.getBean(ObjectMapper.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        // TAPPF-1853 - Account Enumertation, all login error messages should be the same
        if (exception.getMessage() != null && exception.getMessage().equalsIgnoreCase("LOGIN_FAILED_BAD_RECAPTCHA_TOKEN")) {
            response.getOutputStream().println(objectMapper.writeValueAsString(
                    new ApiResponse<>(ResultStatus.FORBIDDEN, exception.getMessage())));
        } else {
            response.getOutputStream().println(objectMapper.writeValueAsString(
                    new ApiResponse<>(ResultStatus.FORBIDDEN, "LOGIN_FAILED_BAD_CREDENTIAL")));
        }
    }

}
