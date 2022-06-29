package com.cherri.acs_portal.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HttpHeaderInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    response.setHeader("X-Frame-Options", "DENY");
    response.setHeader("Content-Security-Policy", "default-src 'self' www.google.com; style-src 'unsafe-inline' 'self'; script-src 'unsafe-eval' 'unsafe-inline' 'self' www.google.com www.gstatic.com; img-src 'self' blob: data:;");
    response.setHeader("X-XSS-Protection", "1; mode=block");
    response.setHeader("X-Content-Type-Options", "nosniff");
    response.setHeader("Strict-Transport-Security","max-age=31536000 ; includeSubDomains");

    return true;
  }
}