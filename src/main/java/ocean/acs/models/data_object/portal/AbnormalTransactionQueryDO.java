package ocean.acs.models.data_object.portal;

import java.time.LocalDate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.constant.PortalEnvironmentConstants;

@Log4j2
@Data
@EqualsAndHashCode(callSuper = true)
public class AbnormalTransactionQueryDO extends PageQueryDO {

    private Long issuerBankId;
    private Integer year;
    private Integer month;
    private String merchantId;
    private String merchantName;

    public AbnormalTransactionQueryDO(Long issuerBankId, Integer year, Integer month,
            String merchantId, String merchantName, Integer page, Integer pageSize) {
        this.issuerBankId = issuerBankId;
        LocalDate localDate = null;
        try {
            localDate = LocalDate.of(year.intValue(), month.intValue(), 01);
        } catch (Exception e) {
            log.error("[AbnormalTransactionQueryDO] Invalid parameters, year={}, month={}", year, month, e);
            localDate = LocalDate.now().minusMonths(1);  //will use last month as default value.
        }
        this.year = localDate.getYear();
        this.month = localDate.getMonthValue();
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        if (null != page && page.intValue() > 0) {
            super.setPage(page);
        }
        if (null != pageSize && pageSize.intValue() > 0) {
            super.setPageSize(pageSize);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, PortalEnvironmentConstants.AUDIT_LOG_STYLE);
    }
}
