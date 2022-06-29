package com.cherri.acs_portal.job;

import com.cherri.acs_portal.service.StatisticsReportJobService;
import org.springframework.beans.factory.annotation.Autowired;

public class StatisticsTransactionStatusDetailJob extends BaseJob {

  public static final String JOB_NAME = StatisticsTransactionStatusDetailJob.class.getSimpleName();

  @Autowired StatisticsReportJobService service;

  @Override
  public String getJobName() {
    return JOB_NAME;
  }

  @Override
  public boolean runJob() {
    return service.statisticsTransactionStatusDetail(getYesterdayQueryTimeRange(), JOB_NAME);
  }
}
