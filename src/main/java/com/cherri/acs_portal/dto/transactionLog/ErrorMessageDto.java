package com.cherri.acs_portal.dto.transactionLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.ErrorMessageLogDO;

@Data
@NoArgsConstructor
@JsonInclude
public class ErrorMessageDto {

  private String errorCode;
  private String errorComponent;
  private String errorDescription;
  private String errorDetail;
  private String errorMessageType;
  private String messageType;
  private String messageVersion;

    public ErrorMessageDto(
      String errorCode,
      String errorComponent,
      String errorDescription,
      String errorDetail,
      String errorMessageType,
      String messageType,
      String messageVersion) {
        this.errorCode = errorCode;
        this.errorComponent = errorComponent;
        this.errorDescription = errorDescription;
        this.errorDetail = errorDetail;
        this.errorMessageType = errorMessageType;
        this.messageType = messageType;
        this.messageVersion = messageVersion;
    }

    public static ErrorMessageDto valueOf(ErrorMessageLogDO errorMessageLog) {
        return new ErrorMessageDto(
          errorMessageLog.getErrorCode(),
          errorMessageLog.getErrorComponent(),
          errorMessageLog.getErrorDescription(),
          errorMessageLog.getErrorDetail(),
          errorMessageLog.getErrorMessageType().name(),
          errorMessageLog.getMessageVersion(),
          errorMessageLog.getMessageType());
    }
}
