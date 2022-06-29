package ocean.acs.models.data_object.portal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplexStatisticsErrorReasonDO {

    private String issuerBankName;
    private Integer year;
    private Integer month;
    private Integer day;
    private String reason1;
    private String reasonPercentage1;
    private String reason2;
    private String reasonPercentage2;
    private String reason3;
    private String reasonPercentage3;

    public static ComplexStatisticsErrorReasonDO newInstance(String issuerBankName, Integer year,
            Integer month, Integer day, String reason1, Double reasonPercentage1, String reason2,
            Double reasonPercentage2, String reason3, Double reasonPercentage3) {
        return new ComplexStatisticsErrorReasonDO(issuerBankName, year, month, day, reason1,
                reasonPercentage1 + "%", reason2, reasonPercentage2 + "%", reason3,
                reasonPercentage3 + "%");
    }

}
