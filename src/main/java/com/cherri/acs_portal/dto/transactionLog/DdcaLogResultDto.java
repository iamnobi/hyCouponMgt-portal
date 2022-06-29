package com.cherri.acs_portal.dto.transactionLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.DdcaLogDO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
public class DdcaLogResultDto {


    private String deviceId;
    private Integer isInPrivateBrowsingMode;
    private String browserTimezoneCountry;
    private String ipCountry;
    private String browserLanguageCode;
    private Integer isVpnEnabled;
    private Integer isProxyEnabled;
    private String additionalData;
    private Long responseTime;
    private String errorStatus;
    private String errorReason;
    private String sysCreator;
    private Long createMillis;


    public static DdcaLogResultDto valueOf(DdcaLogDO ddcaLog) {
        return new DdcaLogResultDto(
          ddcaLog.getDeviceId(),
          ddcaLog.getIsInPrivateBrowsingMode(),
          ddcaLog.getBrowserTimeZoneCountry(),
          ddcaLog.getIpCountry(),
          ddcaLog.getBrowserLanguageCode(),
          ddcaLog.getIsVpnEnabled(),
          ddcaLog.getIsProxyEnabled(),
          ddcaLog.getAdditionalData(),
          ddcaLog.getResponseTime(),
          ddcaLog.getErrorStatus(),
          ddcaLog.getErrorReason(),
          ddcaLog.getSysCreator(),
          ddcaLog.getCreateMillis()
        );
    }

}
