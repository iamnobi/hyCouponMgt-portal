package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class OtpOperationLogDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private Long panId;
    private Boolean otpEnabled;

    public OtpOperationLogDO(Long id, Long issuerBankId, Long panId, Boolean otpEnabled,
      String creator, Long createMillis, String updater, Long updateMillis,
      Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.panId = panId;
        this.otpEnabled = otpEnabled;
    }

    public static OtpOperationLogDO valueOf(ocean.acs.models.oracle.entity.OtpOperationLog e) {
        return new OtpOperationLogDO(e.getId(), e.getIssuerBankId(), e.getPanId(),
          e.getOtpEnabled(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
          e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }
    
    public static OtpOperationLogDO valueOf(ocean.acs.models.sql_server.entity.OtpOperationLog e) {
        return new OtpOperationLogDO(e.getId(), e.getIssuerBankId(), e.getPanId(),
          e.getOtpEnabled(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
          e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

    public static OtpOperationLogDO valueOf(OtpOperationLogDO d) {
        return new OtpOperationLogDO(d.getId(), d.getIssuerBankId(), d.getPanId(),
          d.getOtpEnabled(), d.getCreator(), d.getCreateMillis(), d.getUpdater(),
          d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(), d.getDeleteMillis());
    }
}
