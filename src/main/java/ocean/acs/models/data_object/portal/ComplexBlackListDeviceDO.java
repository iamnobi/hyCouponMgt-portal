package ocean.acs.models.data_object.portal;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ComplexBlackListDeviceDO {

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
    @lombok.Builder.Default
    private Long createMillis = System.currentTimeMillis();

    public ComplexBlackListDeviceDO(Builder builder) {
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

    public static Builder builder() {
        return new Builder();
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
        private Long createMillis = System.currentTimeMillis();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder issuerBankId(Long issuerBankId) {
            this.issuerBankId = issuerBankId;
            return this;
        }

        public Builder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder deviceChannel(String deviceChannel) {
            this.deviceChannel = deviceChannel;
            return this;
        }

        public Builder browserUserAgent(String browserUserAgent) {
            this.browserUserAgent = browserUserAgent == null ? "" : browserUserAgent;
            return this;
        }

        public Builder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder transStatus(String transStatus) {
            this.transStatus = transStatus;
            return this;
        }

        public Builder blockTimes(Integer blockTimes) {
            this.blockTimes = blockTimes;
            return this;
        }

        public Builder operator(String operator) {
            this.operator = operator;
            return this;
        }

        public Builder auditStatus(String auditStatus) {
            this.auditStatus = auditStatus;
            return this;
        }

        public Builder createMillis(Long createMillis) {
            this.createMillis = createMillis;
            return this;
        }

        public ComplexBlackListDeviceDO build() {
            return new ComplexBlackListDeviceDO(this);
        }
    }

}
