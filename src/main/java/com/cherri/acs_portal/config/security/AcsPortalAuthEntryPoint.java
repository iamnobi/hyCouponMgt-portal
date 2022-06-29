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
import org.springframework.security.web.AuthenticationEntryPoint;

public class AcsPortalAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = GetBeanUtil.getBean(ObjectMapper.class);

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {

      response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
      ApiResponse responseData = new ApiResponse(ResultStatus.UNAUTHORIZED);

      response.getOutputStream().println(objectMapper.writeValueAsString(responseData));
  }
}
