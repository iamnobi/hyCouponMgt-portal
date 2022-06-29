package com.cherri.acs_portal.job;

import com.cherri.acs_portal.dto.report.QueryTimeRange;
import com.cherri.acs_portal.service.AbnormalTransactionJobService;
import org.springframework.beans.factory.annotation.Autowired;

/** 商店異常交易統計排程 */
public class StatisticsAbnormalTransactionJob extends BaseJob {

  public static final String JOB_NAME = StatisticsAbnormalTransactionJob.class.getSimpleName();

  @Autowired AbnormalTransactionJobService service;

  @Override
  public String getJobName() {
    return JOB_NAME;
  }

  @Override
  public boolean runJob() {
    QueryTimeRange yesterdayQueryTimeRange = getYesterdayQueryTimeRange();
    service.statisticsAbnormalTransaction(yesterdayQueryTimeRange, JOB_NAME);
    service.statisticsAbnormalTransactionMonthly(
        yesterdayQueryTimeRange.getStartZonedDateTime().getYear(),
        yesterdayQueryTimeRange.getStartZonedDateTime().getMonthValue(),
        JOB_NAME
    );
    return true;
  }
}
