package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ocean.acs.models.data_object.portal.ThreeDSVerifyDO;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ThreeDSVerifiedOperationLogDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private Long panId;
    private Boolean threeDSVerifiedEnabled;

    public ThreeDSVerifiedOperationLogDO(Long id, Long issuerBankId, Long panId,
            Boolean threeDSVerifiedEnabled, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.panId = panId;
        this.threeDSVerifiedEnabled = threeDSVerifiedEnabled;
    }

    public static ThreeDSVerifiedOperationLogDO valueOf(
            ocean.acs.models.oracle.entity.ThreeDSVerifiedOperationLog e) {
        return new ThreeDSVerifiedOperationLogDO(e.getId(), e.getIssuerBankId(), e.getPanId(),
                e.getThreeDSVerifiedEnabled(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

    public static ThreeDSVerifiedOperationLogDO valueOf(
            ocean.acs.models.sql_server.entity.ThreeDSVerifiedOperationLog e) {
        return new ThreeDSVerifiedOperationLogDO(e.getId(), e.getIssuerBankId(), e.getPanId(),
                e.getThreeDSVerifiedEnabled(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

    public static ThreeDSVerifiedOperationLogDO newInstance(ThreeDSVerifyDO verifyDto) {
        return ThreeDSVerifiedOperationLogDO.builder().issuerBankId(verifyDto.getIssuerBankId())
                .panId(verifyDto.getPanId()).threeDSVerifiedEnabled(verifyDto.getVerifyEnabled())
                .creator(verifyDto.getUser()).createMillis(System.currentTimeMillis()).build();
    }

}
