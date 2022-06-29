package com.cherri.acs_portal.dto.transactionLog;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.AuthenticationMeLogDO;
import ocean.acs.models.data_object.entity.ChallengeMeLogDO;
import ocean.acs.models.data_object.entity.ResultMeLogDO;

@Data
@NoArgsConstructor
public class MessageExtensionDto {

    private String data;

    private String id;

    private String name;

    private Boolean criticalityIndicator;

    private MessageExtensionDto(String data, String id, String name, Boolean criticalityIndicator) {
        this.data = data;
        this.id = id;
        this.name = name;
        this.criticalityIndicator = criticalityIndicator;
    }

    public static List<MessageExtensionDto> valueOfAuthenticationMeLogList(
      List<AuthenticationMeLogDO> authenticationMeLogList) {
        List<MessageExtensionDto> result = new ArrayList<>();
        authenticationMeLogList.stream()
          .forEach(
            authenticationMeLog ->
              result.add(
                new MessageExtensionDto(
                  authenticationMeLog.getData(),
                  authenticationMeLog.getMeID(),
                  authenticationMeLog.getName(),
                  authenticationMeLog.getCriticalityIndicator())));
        return result;
    }

    public static List<MessageExtensionDto> valueOfResultMeLogList(
      List<ResultMeLogDO> resultMeLogList) {
        List<MessageExtensionDto> result = new ArrayList<>();
        resultMeLogList.stream()
          .forEach(
            resultMeLog ->
              result.add(
                new MessageExtensionDto(
                  resultMeLog.getData(),
                  resultMeLog.getMeID(),
                  resultMeLog.getName(),
                  resultMeLog.getCriticalityIndicator())));
        return result;
    }

    public static List<MessageExtensionDto> valueOfChallengeMeLogList(
      List<ChallengeMeLogDO> challengeMeLogList) {
        List<MessageExtensionDto> result = new ArrayList<>();
        challengeMeLogList.stream()
          .forEach(
            challengeMeLog ->
              result.add(
                new MessageExtensionDto(
                  challengeMeLog.getData(),
                  challengeMeLog.getMeID(),
                  challengeMeLog.getName(),
                  challengeMeLog.getCriticalityIndicator())));
        return result;
    }
}
