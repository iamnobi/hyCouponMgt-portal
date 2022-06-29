package com.cherri.acs_portal.job;

import com.cherri.acs_portal.dto.report.SystemHealthResponseDTO;
import com.cherri.acs_portal.service.SystemHealthService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
public class SystemHealthJob extends BaseJob {

  public static final String JOB_NAME = SystemHealthJob.class.getSimpleName();

  @Autowired SystemHealthService service;

  @Override
  public String getJobName() {
    return JOB_NAME;
  }

  @Override
  public boolean runJob() {
//    SystemHealthResponseDTO result = service.doSystemHealthCheck();
//    log.debug("[runJob] health check result={}", result);
    return true;
  }
}
