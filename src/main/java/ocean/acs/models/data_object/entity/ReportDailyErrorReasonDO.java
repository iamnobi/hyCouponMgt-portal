package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDailyErrorReasonDO {

    private Long id;
    private Long issuerBankId;
    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private Integer reasonCode1;
    private Integer reasonCount1;
    private Double reasonPercentage1;
    private Integer reasonCode2;
    private Integer reasonCount2;
    private Double reasonPercentage2;
    private Integer reasonCode3;
    private Integer reasonCount3;
    private Double reasonPercentage3;
    private Long dataMillis;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;

    public static ReportDailyErrorReasonDO valueOf(
            ocean.acs.models.oracle.entity.ReportDailyErrorReason e) {
        return new ReportDailyErrorReasonDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getYear(),
                e.getMonth(),
                e.getDayOfMonth(),
                e.getReasonCode1(),
                e.getReasonCount1(),
                e.getReasonPercentage1(),
                e.getReasonCode2(),
                e.getReasonCount2(),
                e.getReasonPercentage2(),
                e.getReasonCode3(),
                e.getReasonCount3(),
                e.getReasonPercentage3(),
                e.getDataMillis(),
                e.getSysCreator(),
                e.getCreateMillis(),
                e.getSysUpdater(),
                e.getUpdateMillis());
    }
    
    public static ReportDailyErrorReasonDO valueOf(
            ocean.acs.models.sql_server.entity.ReportDailyErrorReason e) {
        return new ReportDailyErrorReasonDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getYear(),
                e.getMonth(),
                e.getDayOfMonth(),
                e.getReasonCode1(),
                e.getReasonCount1(),
                e.getReasonPercentage1(),
                e.getReasonCode2(),
                e.getReasonCount2(),
                e.getReasonPercentage2(),
                e.getReasonCode3(),
                e.getReasonCount3(),
                e.getReasonPercentage3(),
                e.getDataMillis(),
                e.getSysCreator(),
                e.getCreateMillis(),
                e.getSysUpdater(),
                e.getUpdateMillis());
    }

    public static ReportDailyErrorReasonDO newInstance(
            Long issuerBankId,
            Integer year,
            Integer month,
            Integer dayOfMonth,
            Long startMillis,
            Integer errorReasonCode1,
            Integer errorReasonCode2,
            Integer errorReasonCode3,
            Integer errorReasonCount1,
            Integer errorReasonCount2,
            Integer errorReasonCount3,
            Integer errorTotal,
            String operator,
            long now) {
        ReportDailyErrorReasonDO entity = new ReportDailyErrorReasonDO();
        entity.setIssuerBankId(issuerBankId);
        entity.setYear(year);
        entity.setMonth(month);
        entity.setDayOfMonth(dayOfMonth);

        final int reasonCode1 = errorReasonCode1;
        final int reasonCode2 = errorReasonCode2;
        final int reasonCode3 = errorReasonCode3;
        entity.setReasonCode1(reasonCode1);
        entity.setReasonCode2(reasonCode2);
        entity.setReasonCode3(reasonCode3);
        entity.setReasonCount1(errorReasonCount1);
        entity.setReasonCount2(errorReasonCount2);
        entity.setReasonCount3(errorReasonCount3);

        Double reasonPercentage1 = (double) errorReasonCount1 / errorTotal;
        Double reasonPercentage2 = (double) errorReasonCount2 / errorTotal;
        Double reasonPercentage3 = (double) errorReasonCount3 / errorTotal;

        reasonPercentage1 = reasonPercentage1.isNaN() ? 0.0 : reasonPercentage1 * 100;
        reasonPercentage2 = reasonPercentage2.isNaN() ? 0.0 : reasonPercentage2 * 100;
        reasonPercentage3 = reasonPercentage3.isNaN() ? 0.0 : reasonPercentage3 * 100;

        final DecimalFormat decimalFormat = new DecimalFormat("#.##");
        reasonPercentage1 = Double.parseDouble(decimalFormat.format(reasonPercentage1));
        reasonPercentage2 = Double.parseDouble(decimalFormat.format(reasonPercentage2));
        reasonPercentage3 = Double.parseDouble(decimalFormat.format(reasonPercentage3));

        entity.setReasonPercentage1(reasonPercentage1);
        entity.setReasonPercentage2(reasonPercentage2);
        entity.setReasonPercentage3(reasonPercentage3);

        entity.setDataMillis(startMillis);
        entity.setSysCreator(operator);
        entity.setCreateMillis(now);
        return entity;
    }
}
