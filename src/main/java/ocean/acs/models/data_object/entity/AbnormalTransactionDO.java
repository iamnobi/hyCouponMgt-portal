package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AbnormalTransactionDO {

    private Long id;
    private Long issuerBankId;
    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private String merchantId;
    private String merchantName;
    private Double uRate;
    private Double nRate;
    private Long uCount;
    private Long nCount;
    private Long totalCount;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;

    public static AbnormalTransactionDO valueOf(
            ocean.acs.models.oracle.entity.AbnormalTransaction e) {
        return new AbnormalTransactionDO(e.getId(), e.getIssuerBankId(), e.getYear(), e.getMonth(),
                e.getDayOfMonth(), e.getMerchantId(), e.getMerchantName(), e.getURate(),
                e.getNRate(), e.getUCount(), e.getNCount(), e.getTotalCount(), e.getSysCreator(), e.getCreateMillis(), e.getSysUpdater(),
                e.getUpdateMillis());
    }

    public static AbnormalTransactionDO valueOf(
            ocean.acs.models.sql_server.entity.AbnormalTransaction e) {
        return new AbnormalTransactionDO(e.getId(), e.getIssuerBankId(), e.getYear(), e.getMonth(),
                e.getDayOfMonth(), e.getMerchantId(), e.getMerchantName(), e.getURate(),
                e.getNRate(), e.getUCount(), e.getNCount(), e.getTotalCount(), e.getSysCreator(), e.getCreateMillis(), e.getSysUpdater(),
                e.getUpdateMillis());
    }

}
