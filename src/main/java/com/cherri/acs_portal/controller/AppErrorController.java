package com.cherri.acs_portal.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
  FSI-65: 解決瀏覽器打開 http://localhost:8008/ds-portal 會顯示 Whitelabel Error Page 的 issue
  打開 http://localhost:8008/ds-portal 現在會 302 到 http://localhost:8008/ds-portal/ 並且正確顯示 portal
 */
@Controller
public class AppErrorController implements ErrorController {
  private final static String ERROR_PATH = "/error";

  @RequestMapping(value = ERROR_PATH)
  public String error(HttpServletRequest request) {
    return "redirect:/";
  }
}
