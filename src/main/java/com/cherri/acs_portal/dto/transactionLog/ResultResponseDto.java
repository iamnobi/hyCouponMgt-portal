package com.cherri.acs_portal.dto.transactionLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.models.data_object.entity.ResultLogDO;
import ocean.acs.models.data_object.entity.ResultMeLogDO;
import ocean.acs.models.data_object.entity.TransactionLogDO;

@Data
@NoArgsConstructor
@JsonInclude
public class ResultResponseDto {

    private String threeDSServerTransID; // from transaction log
    private String acsTransID; // from transaction log
    private String dsTransID; // from transaction log
    private List<MessageExtensionDto> messageExtension; // array from resultMeLog
    private String messageType = MessageType.RRes.name(); // fix RRes
    private String messageVersion; // rresMessageVersion
    private String resultsStatus;

    public static ResultResponseDto valueOf(
      ResultLogDO resultLog, List<ResultMeLogDO> resultMeLogs, TransactionLogDO transactionLog) {
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        resultResponseDto
          .setMessageExtension(MessageExtensionDto.valueOfResultMeLogList(resultMeLogs));
        resultResponseDto.setThreeDSServerTransID(transactionLog.getThreeDSServerTransID());
        resultResponseDto.setAcsTransID(transactionLog.getAcsTransID());
        resultResponseDto.setDsTransID(transactionLog.getDsTransID());
        resultResponseDto.setMessageVersion(resultLog.getRresMessageVersion());
        resultResponseDto.setResultsStatus(resultLog.getResultsStatus());
        return resultResponseDto;
    }
}
