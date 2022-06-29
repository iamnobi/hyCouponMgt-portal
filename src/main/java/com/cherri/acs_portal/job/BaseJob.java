package com.cherri.acs_portal.job;

import java.time.ZonedDateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.report.QueryTimeRange;
import lombok.extern.log4j.Log4j2;


@Log4j2
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class BaseJob implements Job {

  @Override
  public void execute(JobExecutionContext context) {
    long d1 = System.currentTimeMillis();
    boolean isSuccess = runJob();
    long d2 = System.currentTimeMillis();
    if (isSuccess) {
      log.debug("[execute ]success, jobName={}", getJobName());
    } else {
      log.error("===========================================");
      log.error("[execute] failed, jobName={}", getJobName());
      log.error("===========================================");
    }
    log.debug("[execute] execution time:{} seconds. jobName={}", (double) (d2 - d1) / 1000, getJobName());
  }

  public abstract String getJobName();

  public abstract boolean runJob();

  /**
   * 取得當前時間的前一天的起迄時間範圍<br>
   * ex:<br>
   * today：108-12-04 01:20:30<br>
   * startLocalDateTime: 108-12-03 0:0:0<br>
   * endLocalDateTime: 108-12-03 23:59:59<br>
   *
   * @return
   */
  public QueryTimeRange getYesterdayQueryTimeRange() {
    ZonedDateTime now = ZonedDateTime.now(EnvironmentConstants.ACS_TIMEZONE_ID);
    ZonedDateTime yesterday = now.minusDays(1);
    ZonedDateTime startLocalDateTime = ZonedDateTime.of(yesterday.getYear(), yesterday.getMonthValue(), 
            yesterday.getDayOfMonth(), 0, 0, 0, 0, EnvironmentConstants.ACS_TIMEZONE_ID);
    ZonedDateTime endLocalDateTime = startLocalDateTime.plusDays(1).minusNanos(1);

    log.debug(
        "[getYesterdayQueryTimeRange] daily query, jobName={}, startLocalDateTime={}, endLocalDateTime={}",
        getJobName(),
        startLocalDateTime,
        endLocalDateTime);
    return new QueryTimeRange(now, startLocalDateTime, endLocalDateTime);
  }

  /**
   * 取得前一天的月起迄時間範圍<br>
   * ex:<br>
   * today：108-12-01 01:20:30<br>
   * startLocalDateTime: 108-11-01 0:0:0<br>
   * endLocalDateTime: 108-11-30 23:59:59<br>
   *
   * @return
   */
  QueryTimeRange getCurrentMonthQueryTimeRange() {
    ZonedDateTime now = ZonedDateTime.now(EnvironmentConstants.ACS_TIMEZONE_ID);
    ZonedDateTime yesterday = now.minusDays(1);
    ZonedDateTime startLocalDateTime = ZonedDateTime.of(yesterday.getYear(), yesterday.getMonthValue(), 
            1, 0, 0, 0, 0, EnvironmentConstants.ACS_TIMEZONE_ID);
    ZonedDateTime endLocalDateTime = startLocalDateTime.plusMonths(1).minusNanos(1);

    log.debug(
        "[getCurrentMonthQueryTimeRange] month query, jobName={}, startLocalDateTime={}, endLocalDateTime={}",
        getJobName(),
        startLocalDateTime,
        endLocalDateTime);
    return new QueryTimeRange(now, startLocalDateTime, endLocalDateTime);
  }

}
