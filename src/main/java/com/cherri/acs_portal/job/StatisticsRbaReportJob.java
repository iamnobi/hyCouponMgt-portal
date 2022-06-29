package com.cherri.acs_portal.job;

import com.cherri.acs_portal.service.ClassicRbaService;
import org.springframework.beans.factory.annotation.Autowired;

public class StatisticsRbaReportJob extends BaseJob {

    private static final String JOB_NAME = StatisticsRbaReportJob.class.getSimpleName();

    @Autowired
    private ClassicRbaService classicRbaService;

    @Override
    public String getJobName() {
        return JOB_NAME;
    }

    @Override
    public boolean runJob() {
        return true;
        //return classicRbaService.collectRbaReportData(JOB_NAME);
    }
}
