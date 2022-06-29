package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.service.SystemHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemHealthFacade {

  private final SystemHealthService systemHealthService;

  @Autowired
  public SystemHealthFacade(SystemHealthService systemHealthService) {
    this.systemHealthService = systemHealthService;
  }

  public boolean checkDatabaseStatus() {
    return systemHealthService.checkDatabaseStatus();
  }
}
