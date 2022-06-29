package com.cherri.acs_portal.config.security;

import com.cherri.acs_portal.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ocean.acs.commons.enumerator.ResultStatus;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class AcsPortalLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication)
      throws IOException, ServletException {
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setStatus(HttpStatus.OK.value());
        response.getOutputStream()
          .println(objectMapper.writeValueAsString(new ApiResponse(ResultStatus.SUCCESS)));
    }
}
