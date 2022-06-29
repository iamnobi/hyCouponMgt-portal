package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BlackListAuthStatusDO extends OperatorInfoDO {

    private long id;
    private Long issuerBankId;
    private Integer category;
    private String transStatus;
    private String auditStatus;

    public BlackListAuthStatusDO(long id, Long issuerBankId, Integer category, String transStatus,
            String auditStatus, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.category = category;
        this.transStatus = transStatus;
        this.auditStatus = auditStatus;
    }

    public static BlackListAuthStatusDO valueOf(
            ocean.acs.models.oracle.entity.BlackListAuthStatus e) {
        return new BlackListAuthStatusDO(e.getId(), e.getIssuerBankId(), e.getCategory(),
                e.getTransStatus(), e.getAuditStatus(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }
    
    public static BlackListAuthStatusDO valueOf(
            ocean.acs.models.sql_server.entity.BlackListAuthStatus e) {
        return new BlackListAuthStatusDO(e.getId(), e.getIssuerBankId(), e.getCategory(),
                e.getTransStatus(), e.getAuditStatus(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }

}
