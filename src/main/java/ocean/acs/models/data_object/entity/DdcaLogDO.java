package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.MessageType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DdcaLogDO {

    public static final int STATE_USED = 1;
    public static final int STATE_NOT_USED = 0;
    public static final int STATE_NOT_DETECTED = -1;

    private Long id;
    private Long txReport;
    private String deviceId;
    private Integer isInPrivateBrowsingMode;
    private String browserTimeZoneCountry;
    private String ipCountry;
    private String browserLanguageCode;
    private Integer isVpnEnabled;
    private Integer isProxyEnabled;
    private String additionalData;
    private Long responseTime;
    private String errorStatus;
    private String errorReason;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;

    public static DdcaLogDO valueOf(ocean.acs.models.oracle.entity.DdcaLog e) {
        return new DdcaLogDO(
                e.getId(),
                e.getTxReport(),
                e.getDeviceId(),
                e.getIsInPrivateBrowsingMode(),
                e.getBrowserTimezoneCountry(),
                e.getIpCountry(),
                e.getBrowserLanguageCode(),
                e.getIsVpnEnabled(),
                e.getIsProxyEnabled(),
                e.getAdditionalData(),
                e.getResponseTime(),
                e.getErrorStatus(),
                e.getErrorReason(),
                e.getSysCreator(),
                e.getCreateMillis(),
                e.getSysUpdater(),
                e.getUpdateMillis());
    }
    
    public static DdcaLogDO valueOf(ocean.acs.models.sql_server.entity.DdcaLog e) {
        return new DdcaLogDO(
                e.getId(),
                e.getTxReport(),
                e.getDeviceId(),
                e.getIsInPrivateBrowsingMode(),
                e.getBrowserTimezoneCountry(),
                e.getIpCountry(),
                e.getBrowserLanguageCode(),
                e.getIsVpnEnabled(),
                e.getIsProxyEnabled(),
                e.getAdditionalData(),
                e.getResponseTime(),
                e.getErrorStatus(),
                e.getErrorReason(),
                e.getSysCreator(),
                e.getCreateMillis(),
                e.getSysUpdater(),
                e.getUpdateMillis());
    }

    public static DdcaLogDO newInstanceOf(
            MessageType messageType,
            Long txReport,
            String deviceId,
            Boolean isInPrivateBrowsingMode,
            String browserTimeZoneCountry,
            String ipCountry,
            String browserLanguageCode,
            Boolean isVpnEnabled,
            Boolean isProxyEnabled,
            String additionalData,
            Long responseTime,
            String errorStatus,
            String errorReason) {

        DdcaLogDO ddcaLogDO = new DdcaLogDO();
        ddcaLogDO.setTxReport(txReport);
        ddcaLogDO.setDeviceId(deviceId);
        ddcaLogDO.setIsInPrivateBrowsingMode(convertDdcaBooleanState(isInPrivateBrowsingMode));
        ddcaLogDO.setBrowserTimeZoneCountry(browserTimeZoneCountry);
        ddcaLogDO.setIpCountry(ipCountry);
        ddcaLogDO.setBrowserLanguageCode(browserLanguageCode);
        ddcaLogDO.setIsVpnEnabled(convertDdcaBooleanState(isVpnEnabled));
        ddcaLogDO.setIsProxyEnabled(convertDdcaBooleanState(isProxyEnabled));
        ddcaLogDO.setAdditionalData(additionalData);
        ddcaLogDO.setResponseTime(responseTime);
        ddcaLogDO.setErrorStatus(errorStatus);
        ddcaLogDO.setErrorReason(errorReason);
        ddcaLogDO.setSysCreator(messageType.name());
        return ddcaLogDO;
    }

    private static Integer convertDdcaBooleanState(Boolean result) {
        if (result == null) {
            return STATE_NOT_DETECTED;
        }
        return result ? STATE_USED : STATE_NOT_USED;
    }

    public boolean isDdcaFailed() {
        return null != errorStatus || null != errorReason;
    }

}
