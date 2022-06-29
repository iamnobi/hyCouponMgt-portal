package com.cherri.acs_portal.job;

import com.cherri.acs_portal.service.BankHandlingFeeJobService;
import org.springframework.beans.factory.annotation.Autowired;

/** FISC銀行手續費上傳 - Month 每月的23號，11:05:00開始執行批次，計算一個月份的手續費(上個月份) */
public class BankHandlingFeeJob extends BaseJob {

  public static final String JOB_NAME = BankHandlingFeeJob.class.getSimpleName();

  @Autowired private BankHandlingFeeJobService service;

  @Override
  public String getJobName() {
    return JOB_NAME;
  }

  @Override
  public boolean runJob() {
    return true;
//    return service.exportAndUploadBankHandlingFee(JOB_NAME);
  }
}
