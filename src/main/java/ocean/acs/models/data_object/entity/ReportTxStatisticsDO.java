package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReportTxStatisticsDO {

    private Long id;
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
    private Long dataMillis;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;

    public static ReportTxStatisticsDO valueOf(
            ocean.acs.models.oracle.entity.ReportTxStatistics e) {
        return new ReportTxStatisticsDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getYear(),
                e.getMonth(),
                e.getDayOfMonth(),
                e.getTotal(),
                e.getOtpCount(),
                e.getNCount(),
                e.getACount(),
                e.getYCount(),
                e.getCyCount(),
                e.getCnCount(),
                e.getRCount(),
                e.getUCount(),
                e.getDataMillis(),
                e.getSysCreator(),
                e.getCreateMillis(),
                e.getSysUpdater(),
                e.getUpdateMillis());
    }
    
    public static ReportTxStatisticsDO valueOf(
            ocean.acs.models.sql_server.entity.ReportTxStatistics e) {
        return new ReportTxStatisticsDO(
                e.getId(),
                e.getIssuerBankId(),
                e.getYear(),
                e.getMonth(),
                e.getDayOfMonth(),
                e.getTotal(),
                e.getOtpCount(),
                e.getNCount(),
                e.getACount(),
                e.getYCount(),
                e.getCyCount(),
                e.getCnCount(),
                e.getRCount(),
                e.getUCount(),
                e.getDataMillis(),
                e.getSysCreator(),
                e.getCreateMillis(),
                e.getSysUpdater(),
                e.getUpdateMillis());
    }

    public static ReportTxStatisticsDO newInstance(
            Long issuerBankId,
            int year,
            int monthValue,
            int dayOfMonth,
            long startMillis,
            String operator,
            long now) {
        return ReportTxStatisticsDO.builder()
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
