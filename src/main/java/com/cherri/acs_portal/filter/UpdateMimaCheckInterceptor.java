package com.cherri.acs_portal.filter;

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
public class UpdateMimaCheckInterceptor implements HandlerInterceptor {
  private final HttpSession httpSession;
  private final ObjectMapper objectMapper;

  public UpdateMimaCheckInterceptor(HttpSession httpSession) {
    this.httpSession = httpSession;
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // check needUpdateMima
    boolean needUpdateMima = (Boolean) httpSession
        .getAttribute(SessionAttributes.NEED_UPDATE_MIMA);
    if (needUpdateMima) {
      log.warn("Need to update password");
      // response error
      ApiResponse apiResponse = new ApiResponse(ResultStatus.FORBIDDEN, "Need to update password");
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
      return false;
    }

    return true;
  }
}
