package com.cherri.acs_portal.dto.blackList.output;

import lombok.Getter;
import ocean.acs.models.data_object.portal.ComplexBlackListDeviceDO;

@Getter
public class BlackListDeviceInfoDTO {

    private Long id;
    private Long issuerBankId;
    private String deviceId;
    private String deviceChannel;
    private String browserUserAgent;
    private String ip;
    private String transStatus;
    private Integer blockTimes;
    private String operator; // entity.BlackListDeviceInfo's creator.
    private String auditStatus;
    private Long createMillis;

    public BlackListDeviceInfoDTO(BlackListDeviceInfoDTO.Builder builder) {
        this.id = builder.id;
        this.issuerBankId = builder.issuerBankId;
        this.deviceId = builder.deviceId;
        this.deviceChannel = builder.deviceChannel;
        this.browserUserAgent = builder.browserUserAgent;
        this.ip = builder.ip;
        this.transStatus = builder.transStatus;
        this.blockTimes = builder.blockTimes;
        this.operator = builder.operator;
        this.auditStatus = builder.auditStatus;
        this.createMillis = builder.createMillis;
    }

    public static BlackListDeviceInfoDTO valueOf(ComplexBlackListDeviceDO d) {
        return BlackListDeviceInfoDTO.builder()
          .id(d.getId())
          .issuerBankId(d.getIssuerBankId())
          .deviceId(d.getDeviceId())
          .deviceChannel(d.getDeviceChannel())
          .browserUserAgent(d.getBrowserUserAgent())
          .ip(d.getIp())
          .blockTimes(d.getBlockTimes())
          .operator(d.getOperator())
          .transStatus(d.getTransStatus())
          .auditStatus(d.getAuditStatus())
          .createMillis(d.getCreateMillis())
          .build();
    }

    public static BlackListDeviceInfoDTO.Builder builder() {
        return new BlackListDeviceInfoDTO.Builder();
    }

    public static final class Builder {

        private Long id;
        private Long issuerBankId;
        private String deviceId;
        private String deviceChannel;
        private String browserUserAgent;
        private String ip;
        private String transStatus;
        private Integer blockTimes;
        private String operator;
        private String auditStatus;
        private Long createMillis;

        public BlackListDeviceInfoDTO.Builder id(Long id) {
            this.id = id;
            return this;
        }

        public BlackListDeviceInfoDTO.Builder issuerBankId(Long issuerBankId) {
            this.issuerBankId = issuerBankId;
            return this;
        }

        public BlackListDeviceInfoDTO.Builder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public BlackListDeviceInfoDTO.Builder deviceChannel(String deviceChannel) {
            this.deviceChannel = deviceChannel;
            return this;
        }

        public BlackListDeviceInfoDTO.Builder browserUserAgent(String browserUserAgent) {
            this.browserUserAgent = browserUserAgent == null ? "" : browserUserAgent;
            return this;
        }

        public BlackListDeviceInfoDTO.Builder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public BlackListDeviceInfoDTO.Builder transStatus(String transStatus) {
            this.transStatus = transStatus;
            return this;
        }

        public BlackListDeviceInfoDTO.Builder blockTimes(Integer blockTimes) {
            this.blockTimes = blockTimes;
            return this;
        }

        public BlackListDeviceInfoDTO.Builder operator(String operator) {
            this.operator = operator;
            return this;
        }

        public BlackListDeviceInfoDTO.Builder auditStatus(String auditStatus) {
            this.auditStatus = auditStatus;
            return this;
        }

        public BlackListDeviceInfoDTO.Builder createMillis(Long createMillis) {
            this.createMillis = createMillis;
            return this;
        }

        public BlackListDeviceInfoDTO build() {
            return new BlackListDeviceInfoDTO(this);
        }
    }

}
