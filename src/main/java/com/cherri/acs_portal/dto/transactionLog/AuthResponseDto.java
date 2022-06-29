package com.cherri.acs_portal.dto.transactionLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.models.data_object.entity.AuthenticationLogDO;
import ocean.acs.models.data_object.entity.AuthenticationMeLogDO;
import ocean.acs.models.data_object.entity.TransactionLogDO;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@JsonInclude
public class AuthResponseDto {

    private String threeDSServerTransID; // from transaction log
    private String acsChallengeMandated;
    private String acsOperatorID; // from transaction log
    private String acsReferenceNumber;
    // acsRenderingType
    private String artAcsInterface;
    private String artAcsUiTemplate;
    private String acsSignedContent;
    private String acsTransID; // from transaction log
    private String acsURL;
    private String authenticationType;
    private String authenticationValue;
    private String aresBroadInfo;
    private String cardholderInfo;
    private String aresDsReferenceNumber;
    private String dsTransID; // from transaction log
    private String eci;
    private List<MessageExtensionDto> messageExtension; // list from authencationMeLog
    private String messageType = MessageType.ARes.name(); // fix ARes
    private String messageVersion; // aresMessageVersion
    private String sdkTransID; // from transaction log
    private String transStatus;
    private String transStatusReason;

    public static AuthResponseDto valueOf(
      AuthenticationLogDO authenticationLog,
      List<AuthenticationMeLogDO> aresAuthenticationMeLogs,
      TransactionLogDO transactionLog) {
        AuthResponseDto authResponseDto = new AuthResponseDto();
        BeanUtils.copyProperties(authenticationLog, authResponseDto);
        authResponseDto.setMessageExtension(
          MessageExtensionDto.valueOfAuthenticationMeLogList(aresAuthenticationMeLogs));
        authResponseDto.setThreeDSServerTransID(transactionLog.getThreeDSServerTransID());
        authResponseDto.setAcsOperatorID(authenticationLog.getAcsOperatorID());
        authResponseDto.setAcsTransID(transactionLog.getAcsTransID());
        authResponseDto.setDsTransID(transactionLog.getDsTransID());
        authResponseDto.setSdkTransID(transactionLog.getSdkTransID());
        return authResponseDto;
    }
}
