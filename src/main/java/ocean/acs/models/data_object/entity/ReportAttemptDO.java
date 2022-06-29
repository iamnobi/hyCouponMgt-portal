package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReportAttemptDO {

    private Long id;
    private Long issuerBankId;
    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private Integer permittedCount;
    private Integer realTriesCount;
    private Double percentage;
    private Long dataMillis;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;

    public static ReportAttemptDO valueOf(ocean.acs.models.oracle.entity.ReportAttempt e) {
        return new ReportAttemptDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getYear(),
                e.getMonth(),
                e.getDayOfMonth(),
                e.getPermittedCount(),
                e.getRealTriesCount(),
                e.getPercentage(),
                e.getDataMillis(),
                e.getSysCreator(),
                e.getCreateMillis(),
                e.getSysUpdater(),
                e.getUpdateMillis());
    }
    
    public static ReportAttemptDO valueOf(ocean.acs.models.sql_server.entity.ReportAttempt e) {
        return new ReportAttemptDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getYear(),
                e.getMonth(),
                e.getDayOfMonth(),
                e.getPermittedCount(),
                e.getRealTriesCount(),
                e.getPercentage(),
                e.getDataMillis(),
                e.getSysCreator(),
                e.getCreateMillis(),
                e.getSysUpdater(),
                e.getUpdateMillis());
    }

    public static ReportAttemptDO newInstance(
            Long issuerBankId,
            int year,
            int monthValue,
            int dayOfMonth,
            long startMillis,
            String operator,
            long now) {
        return ReportAttemptDO.builder()
                .issuerBankId(issuerBankId)
                .year(year)
                .month(monthValue)
                .dayOfMonth(dayOfMonth)
                .dataMillis(startMillis)
                .sysCreator(operator)
                .createMillis(now)
                .build();
    }
}
