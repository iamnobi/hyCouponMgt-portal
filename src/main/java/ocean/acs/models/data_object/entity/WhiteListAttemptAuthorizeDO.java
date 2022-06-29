package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class WhiteListAttemptAuthorizeDO extends OperatorInfoDO {

    private String id;
    private Long issuerBankId;
    private String ldapGroup;
    private Long maxAmount;

    public WhiteListAttemptAuthorizeDO(String id, Long issuerBankId, String ldapGroup,
            Long maxAmount, String creator, Long createMillis, String updater, Long updateMillis,
            Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.ldapGroup = ldapGroup;
        this.maxAmount = maxAmount;
    }

    public static WhiteListAttemptAuthorizeDO valueOf(
            ocean.acs.models.oracle.entity.WhiteListAttemptAuthorize e) {
        return new WhiteListAttemptAuthorizeDO(e.getId(), e.getIssuerBankId(), e.getLdapGroup(),
                e.getMaxAmount(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }
    
    public static WhiteListAttemptAuthorizeDO valueOf(
            ocean.acs.models.sql_server.entity.WhiteListAttemptAuthorize e) {
        return new WhiteListAttemptAuthorizeDO(e.getId(), e.getIssuerBankId(), e.getLdapGroup(),
                e.getMaxAmount(), e.getCreator(), e.getCreateMillis(), e.getUpdater(),
                e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(), e.getDeleteMillis());
    }

}
