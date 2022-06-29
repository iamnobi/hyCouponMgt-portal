package com.cherri.acs_portal.job;

import com.cherri.acs_portal.service.BrowserAbnormalJobService;
import org.springframework.beans.factory.annotation.Autowired;

/** 瀏覽器異常紀錄排程 */
public class StatisticsBrowserAbnormalJob extends BaseJob {

  private static final String JOB_NAME = StatisticsBrowserAbnormalJob.class.getSimpleName();

  @Autowired
  private BrowserAbnormalJobService browserAbnormalJobService;

  @Override
  public String getJobName() {
    return JOB_NAME;
  }

  @Override
  public boolean runJob() {
    return true;
    //return browserAbnormalJobService.staticBrowserErrorLog(JOB_NAME);
  }
}
