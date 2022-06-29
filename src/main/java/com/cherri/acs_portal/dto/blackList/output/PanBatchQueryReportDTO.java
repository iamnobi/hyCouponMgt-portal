package com.cherri.acs_portal.dto.blackList.output;

import lombok.Data;

@Data
public class PanBatchQueryReportDTO {
    private String batchID = "";
    private String batchName = "";
    private int challengeCardTotal = 0;
    private int rejectCardTotal = 0;
    private long createTime = 0L;
}
