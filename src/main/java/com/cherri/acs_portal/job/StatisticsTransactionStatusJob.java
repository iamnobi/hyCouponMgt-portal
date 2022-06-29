package com.cherri.acs_portal.job;

import com.cherri.acs_portal.service.StatisticsReportJobService;
import org.springframework.beans.factory.annotation.Autowired;

/** 各交易狀態統計排程 */
public class StatisticsTransactionStatusJob extends BaseJob {

  public static final String JOB_NAME = StatisticsTransactionStatusJob.class.getSimpleName();

  @Autowired StatisticsReportJobService service;

  @Override
  public String getJobName() {
    return JOB_NAME;
  }

  @Override
  public boolean runJob() {
    return service.statisticsTransactionStatus(getYesterdayQueryTimeRange(), JOB_NAME);
  }
}
