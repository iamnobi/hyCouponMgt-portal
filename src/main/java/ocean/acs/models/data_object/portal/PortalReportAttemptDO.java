package ocean.acs.models.data_object.portal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortalReportAttemptDO {

    private Long issuerBankId;
    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private Integer permittedCount;
    private Integer realTriesCount;
    private Double percentage;

}
