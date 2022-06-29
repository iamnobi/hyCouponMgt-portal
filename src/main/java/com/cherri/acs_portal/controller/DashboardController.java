package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.facade.DashboardFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/dashboard")
public class DashboardController extends ContextProvider {

  private final DashboardFacade facade;

  @Autowired
  public DashboardController(DashboardFacade facade) {
    this.facade = facade;
  }

  @GetMapping("/system-health")
  @Secured("ROLE_HEALTH_CHECK_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.DASHBOARD)
  public ApiResponse systemHealth() {
    return new ApiResponse<>(facade.doSystemHealthCheck());
  }
}
