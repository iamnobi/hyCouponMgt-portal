package com.cherri.acs_portal.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class DisableOptionsMethodInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (request.getMethod().equals("OPTIONS")) {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      return false;
    } else {
      return true;
    }
  }
}
