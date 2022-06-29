package com.cherri.acs_portal.filter;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.SessionAttributes;
import com.cherri.acs_portal.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Log4j2
@Component
public class MfaCheckInterceptor implements HandlerInterceptor {
  private final HttpSession httpSession;
  private final ObjectMapper objectMapper;

  public MfaCheckInterceptor(HttpSession httpSession) {
    this.httpSession = httpSession;
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (!EnvironmentConstants.IS_MFA) {
     return true;
    }

    // check isMfaVerified
    boolean isMfaVerified = (Boolean) httpSession.getAttribute(SessionAttributes.IS_MFA_VERIFIED);
    if (!isMfaVerified) {
      log.warn("MFA not verified");
      // response error
      ApiResponse apiResponse = new ApiResponse(ResultStatus.FORBIDDEN, "MFA not verified");
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
      return false;
    }

    return true;
  }
}
