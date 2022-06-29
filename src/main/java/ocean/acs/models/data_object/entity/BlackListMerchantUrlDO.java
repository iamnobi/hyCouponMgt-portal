package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BlackListMerchantUrlDO extends OperatorInfoDO {

    private Long id;
    private Long issuerBankId;
    private String url;
    private String transStatus;
    private String auditStatus;

    public BlackListMerchantUrlDO(Long id, Long issuerBankId, String url, String transStatus,
            String auditStatus, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.url = url;
        this.transStatus = transStatus;
        this.auditStatus = auditStatus;
    }

    public static BlackListMerchantUrlDO valueOf(ocean.acs.models.oracle.entity.BlackListMerchantUrl e) {
        return new BlackListMerchantUrlDO(e.getId(), e.getIssuerBankId(), e.getUrl(),
                e.getTransStatus(), e.getAuditStatus(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }
    
    public static BlackListMerchantUrlDO valueOf(ocean.acs.models.sql_server.entity.BlackListMerchantUrl e) {
        return new BlackListMerchantUrlDO(e.getId(), e.getIssuerBankId(), e.getUrl(),
                e.getTransStatus(), e.getAuditStatus(), e.getCreator(), e.getCreateMillis(),
                e.getUpdater(), e.getUpdateMillis(), e.getDeleteFlag(), e.getDeleter(),
                e.getDeleteMillis());
    }

}
