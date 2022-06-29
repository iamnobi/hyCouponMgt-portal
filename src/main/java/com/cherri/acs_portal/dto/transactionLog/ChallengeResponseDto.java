package com.cherri.acs_portal.dto.transactionLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.models.data_object.entity.ChallengeCodeLogDO;
import ocean.acs.models.data_object.entity.ChallengeLogDO;
import ocean.acs.models.data_object.entity.ChallengeMeLogDO;
import ocean.acs.models.data_object.entity.ChallengeSelectInfoLogDO;
import ocean.acs.models.data_object.entity.TransactionLogDO;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@JsonInclude
public class ChallengeResponseDto {

    private String threeDSServerTransID; // from transaction log
    private String acsTransID; // from transaction log
    private String sdkTransID; // from transaction log
    private String acsCounterAtoS;
    private String acsHTML;
    private String acsUiType;
    private String challengeCancel;
    private String challengeAddInfo;
    private String challengeCompletionInd;
    private String challengeInfoHeader;
    private String challengeInfoLabel;
    private String challengeInfoText;
    private String challengeInfoTextIndicator;
    private List<ChallengeSelectInfoDto> challengeSelectInfo; // from ChallengeSelectInfoLog
    private String expandInfoLabel;
    private String expandInfoText;
    private String iiMedium;
    private String iiHigh;
    private String iiExtraHigh;
    private String piMedium;
    private String piHigh;
    private String piExtraHigh;
    private List<MessageExtensionDto> messageExtension; // list from challengeMeLog
    private String messageType = MessageType.CRes.name(); // fix CRes
    private String messageVersion;
    private String oobAppURL;
    private String oobAppLabel;
    private String oobContinueLabel;
    private String resendInformationLabel;
    private String submitAuthenticationLabel;
    private String transStatus;
    private String whyInfoLabel;
    private String whyInfoText;
    private List<ChallengeCodeDto> challengeCode;

    public static ChallengeResponseDto valueOf(
      ChallengeLogDO challengeLog,
      List<ChallengeMeLogDO> challengeMeLogs,
      List<ChallengeSelectInfoLogDO> challengeSelectInfoLogList,
      List<ChallengeCodeLogDO> challengeCodeLogList,
      TransactionLogDO transactionLog) {
        ChallengeResponseDto challengeResponseDto = new ChallengeResponseDto();
        BeanUtils.copyProperties(challengeLog, challengeResponseDto);
        challengeResponseDto.setMessageExtension(
          MessageExtensionDto.valueOfChallengeMeLogList(challengeMeLogs));
        challengeResponseDto.setThreeDSServerTransID(transactionLog.getThreeDSServerTransID());
        challengeResponseDto.setAcsTransID(transactionLog.getAcsTransID());
        challengeResponseDto.setSdkTransID(transactionLog.getSdkTransID());
        challengeResponseDto.setMessageVersion(challengeLog.getCresMessageVersion());
        challengeResponseDto.setChallengeCode(ChallengeCodeDto.valueOf(challengeCodeLogList));

        challengeResponseDto.setChallengeSelectInfo(
          ChallengeSelectInfoDto.valueOfChallengeSelectInfoLogList(
            challengeSelectInfoLogList));
        return challengeResponseDto;
    }
}
