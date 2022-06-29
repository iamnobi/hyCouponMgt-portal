package com.cherri.acs_portal.job;

import com.cherri.acs_portal.service.StatisticsReportJobService;
import org.springframework.beans.factory.annotation.Autowired;

/** 人工彈性授權統計排程 */
public class StatisticsAttemptAuthorizeJob extends BaseJob {

  public static final String JOB_NAME = StatisticsAttemptAuthorizeJob.class.getSimpleName();

  @Autowired StatisticsReportJobService service;

  @Override
  public String getJobName() {
    return JOB_NAME;
  }

  @Override
  public boolean runJob() {
    return service.statisticsAttemptAuth(getYesterdayQueryTimeRange(), JOB_NAME);
  }
}
