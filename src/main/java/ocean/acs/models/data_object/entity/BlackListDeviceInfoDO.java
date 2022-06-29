package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class BlackListDeviceInfoDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private String deviceID;
    private String deviceChannel;
    private String browserUserAgent;
    private String ip;
    private String transStatus;
    private String auditStatus;

    public BlackListDeviceInfoDO(Long id, Long issuerBankId, String deviceID, String deviceChannel,
            String browserUserAgent, String ip, String transStatus, String auditStatus,
            String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.deviceID = deviceID;
        this.deviceChannel = deviceChannel;
        this.browserUserAgent = browserUserAgent;
        this.ip = ip;
        this.transStatus = transStatus;
        this.auditStatus = auditStatus;
    }


    public static BlackListDeviceInfoDO valueOf(ocean.acs.models.oracle.entity.BlackListDeviceInfo e) {
        return new BlackListDeviceInfoDO(e.getId(), e.getIssuerBankId(), e.getDeviceID(),
                e.getDeviceChannel(), e.getBrowserUserAgent(), e.getIp(), e.getTransStatus(),
                e.getAuditStatus(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }
    
    public static BlackListDeviceInfoDO valueOf(ocean.acs.models.sql_server.entity.BlackListDeviceInfo e) {
        return new BlackListDeviceInfoDO(e.getId(), e.getIssuerBankId(), e.getDeviceID(),
                e.getDeviceChannel(), e.getBrowserUserAgent(), e.getIp(), e.getTransStatus(),
                e.getAuditStatus(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

}
