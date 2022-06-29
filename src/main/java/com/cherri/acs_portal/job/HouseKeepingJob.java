package com.cherri.acs_portal.job;

import com.cherri.acs_portal.service.HouseKeepingJobService;
import org.springframework.beans.factory.annotation.Autowired;

public class HouseKeepingJob extends BaseJob {

    private static final String JOB_NAME = HouseKeepingJob.class.getSimpleName();

    @Autowired
    private HouseKeepingJobService houseKeepingJobService;

    @Override
    public String getJobName() {
        return JOB_NAME;
    }

    @Override
    public boolean runJob() {
        return true;
//        return houseKeepingJobService.housekeeping(JOB_NAME);
    }
}
