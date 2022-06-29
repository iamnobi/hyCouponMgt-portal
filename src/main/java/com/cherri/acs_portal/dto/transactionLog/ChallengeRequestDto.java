package com.cherri.acs_portal.dto.transactionLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.models.data_object.entity.ChallengeLogDO;
import ocean.acs.models.data_object.entity.ChallengeMeLogDO;
import ocean.acs.models.data_object.entity.TransactionLogDO;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@JsonInclude
public class ChallengeRequestDto {

    private String threeDSServerTransID; // from transaction log
    private String acsTransID; // from transaction log
    private String sdkTransID; // from transaction log
    private String challengeCancel;
    //  private String challengeDataEntry;// DB不儲存
    //  private String challengeHTMLDataEntry;// DB不儲存
    private String challengeWindowSize;
    private List<MessageExtensionDto> messageExtension; // list from challengeMeLog
    private String messageType = MessageType.CReq.name(); // fix CReq
    private String messageVersion;
    private Boolean oobContinue;
    private String resendChallenge;
    private String sdkCounterStoA;


    public static ChallengeRequestDto valueOf(
      ChallengeLogDO challengeLog,
      List<ChallengeMeLogDO> challengeMeLogList,
      TransactionLogDO transactionLog) {
        ChallengeRequestDto challengeRequestDto = new ChallengeRequestDto();
        BeanUtils.copyProperties(challengeLog, challengeRequestDto);
        challengeRequestDto.setMessageExtension(
          MessageExtensionDto.valueOfChallengeMeLogList(challengeMeLogList));
        challengeRequestDto.setThreeDSServerTransID(transactionLog.getThreeDSServerTransID());
        challengeRequestDto.setAcsTransID(transactionLog.getAcsTransID());
        challengeRequestDto.setSdkTransID(transactionLog.getSdkTransID());
        return challengeRequestDto;
    }
}
