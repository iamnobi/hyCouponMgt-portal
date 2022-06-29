package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.dto.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogoutController extends ContextProvider {

  @GetMapping("/logout/{issuerBankId}")
  @ResponseBody
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.LOGOUT)
  public ApiResponse logout(@PathVariable Long issuerBankId) {
    getHttpSession().invalidate();
    return ApiResponse.SUCCESS_API_RESPONSE;
  }
}
