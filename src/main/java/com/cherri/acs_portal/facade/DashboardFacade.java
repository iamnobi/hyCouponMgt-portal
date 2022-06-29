package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.dto.report.SystemHealthResponseDTO;
import com.cherri.acs_portal.service.SystemHealthService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class DashboardFacade {

    private final SystemHealthService systemHealthService;

    @Autowired
    public DashboardFacade(SystemHealthService systemHealthService) {
        this.systemHealthService = systemHealthService;
    }

    public SystemHealthResponseDTO doSystemHealthCheck() {
        return systemHealthService.doSystemHealthCheck();
    }
}
