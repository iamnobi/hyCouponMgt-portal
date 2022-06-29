package com.cherri.acs_portal.dto.transactionLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.models.data_object.entity.ResultLogDO;
import ocean.acs.models.data_object.entity.ResultMeLogDO;
import ocean.acs.models.data_object.entity.TransactionLogDO;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@JsonInclude
public class ResultRequestDto {

    private String threeDSServerTransID; // from transaction log
    private String acsTransID; // from transaction log
    // acsRenderingType
    private String artAcsInterface;
    private String artAcsUiTemplate;
    private String authenticationMethod;
    private String authenticationType;
    private String authenticationValue;
    private String challengeCancel;
    private String dsTransID; // from transaction log
    private String eci;
    private String interactionCounter;
    private List<MessageExtensionDto> messageExtension; // list from resultMeLog
    private String messageType = MessageType.RReq.name(); // fix RReq
    private String messageVersion;
    private String transStatus;
    private String transStatusReason;


    public static ResultRequestDto valueOf(
      ResultLogDO resultLog, List<ResultMeLogDO> resultMeLogs, TransactionLogDO transactionLog) {
        ResultRequestDto resultRequestDto = new ResultRequestDto();
        BeanUtils.copyProperties(resultLog, resultRequestDto);
        resultRequestDto
          .setMessageExtension(MessageExtensionDto.valueOfResultMeLogList(resultMeLogs));
        resultRequestDto.setThreeDSServerTransID(transactionLog.getThreeDSServerTransID());
        resultRequestDto.setAcsTransID(transactionLog.getAcsTransID());
        resultRequestDto.setDsTransID(transactionLog.getDsTransID());
        return resultRequestDto;
    }
}
