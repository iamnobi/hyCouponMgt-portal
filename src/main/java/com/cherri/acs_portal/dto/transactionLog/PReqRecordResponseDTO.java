package com.cherri.acs_portal.dto.transactionLog;

import lombok.Data;
import ocean.acs.models.oracle.entity.PreparationLog;

@Data
public class PReqRecordResponseDTO {
    private String threeDSServerRefNumber;
    private String threeDSOperatorId;
    private String threeDSServerTransId;
    private String preqSerialNum;
    private Long preqMillis;
    private String dsTransId;
    private String presSerialNum;
    private Long presMillis;
    private String failureReason;

    public static PReqRecordResponseDTO valueOf(PreparationLog preparationLog) {
        PReqRecordResponseDTO dto = new PReqRecordResponseDTO();
        dto.setThreeDSServerRefNumber(preparationLog.getThreeDSServerRefNumber());
        dto.setThreeDSOperatorId(preparationLog.getThreeDSServerOperatorID());
        dto.setThreeDSServerTransId(preparationLog.getThreeDSServerTransID());
        dto.setPreqSerialNum(preparationLog.getSerialNum());
        dto.setPreqMillis(preparationLog.getCreateMillis());
        dto.setDsTransId(preparationLog.getDsTransID());
        dto.setPresSerialNum(preparationLog.getPresSerialNum());
        dto.setPresMillis(preparationLog.getUpdateMillis());
        if (preparationLog.getErrorMessageLog() != null) {
            dto.setFailureReason(preparationLog.getErrorMessageLog().getErrorDescription());
        }
        return dto;
    }
}
