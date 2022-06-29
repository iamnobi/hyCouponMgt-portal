package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.SystemHealthInfo;
import com.cherri.acs_portal.facade.SystemHealthFacade;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class SystemHealthController {

  private final SystemHealthFacade facade;

  @Autowired
  public SystemHealthController(SystemHealthFacade facade) {
    this.facade = facade;
  }

    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<SystemHealthInfo>> ping(HttpServletRequest request) {
        SystemHealthInfo systemHealthInfo = new SystemHealthInfo(facade.checkDatabaseStatus());
        HttpStatus httpStatus =
                systemHealthInfo.getDatabaseStatus()
                        ? HttpStatus.OK
                        : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(new ApiResponse<>(systemHealthInfo), httpStatus);
    }
}
