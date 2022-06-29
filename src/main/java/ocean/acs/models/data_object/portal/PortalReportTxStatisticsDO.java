package ocean.acs.models.data_object.portal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PortalReportTxStatisticsDO {

    private Long issuerBankId;
    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private Long total;
    private Long otpCount;
    private Integer nCount;
    private Integer aCount;
    private Integer yCount;
    private Integer cyCount;
    private Integer cnCount;
    private Integer rCount;
    private Integer uCount;


    public PortalReportTxStatisticsDO(Long issuerBankId, Integer year, Integer month,
      Integer dayOfMonth, Long total, Long otpCount, Integer nCount, Integer aCount,
      Integer yCount, Integer cyCount, Integer cnCount, Integer rCount, Integer uCount) {
        this.issuerBankId = issuerBankId;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.total = total;
        this.otpCount = otpCount;
        this.nCount = nCount;
        this.aCount = aCount;
        this.yCount = yCount;
        this.cyCount = cyCount;
        this.cnCount = cnCount;
        this.rCount = rCount;
        this.uCount = uCount;
    }

}
