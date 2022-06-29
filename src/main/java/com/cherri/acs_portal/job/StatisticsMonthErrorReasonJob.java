package com.cherri.acs_portal.job;

import com.cherri.acs_portal.service.StatisticsReportJobService;
import org.springframework.beans.factory.annotation.Autowired;

public class StatisticsMonthErrorReasonJob extends BaseJob {

  public static final String JOB_NAME = StatisticsMonthErrorReasonJob.class.getSimpleName();

  @Autowired StatisticsReportJobService service;

  @Override
  public String getJobName() {
    return JOB_NAME;
  }

  @Override
  public boolean runJob() {
    return true;
    //return service.statisticsMonthErrorReason(getCurrentMonthQueryTimeRange(), JOB_NAME);
  }
}
