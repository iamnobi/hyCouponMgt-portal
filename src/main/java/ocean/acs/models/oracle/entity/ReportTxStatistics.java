package ocean.acs.models.oracle.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.ReportTxStatisticsDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_REPORT_TX_STATISTICS)
public class ReportTxStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "report_tx_statistics_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "REPORT_TX_STATISTICS_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "report_tx_statistics_seq_generator")
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_ID)
    private Long id;

    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_YEAR)
    private Integer year;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_MONTH)
    private Integer month;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DAY_OF_MONTH)
    private Integer dayOfMonth;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_TOTAL)
    private Long total = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_OTP_COUNT)
    private Long otpCount = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_N_COUNT)
    private Integer nCount = 0;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_A_COUNT)
    private Integer aCount = 0;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_Y_COUNT)
    private Integer yCount = 0;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_C_Y_COUNT)
    private Integer cyCount = 0;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_C_N_COUNT)
    private Integer cnCount = 0;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_R_COUNT)
    private Integer rCount = 0;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_U_COUNT)
    private Integer uCount = 0;

    /** 供查詢日期範圍使用 */
    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DATA_MILLIS)
    private Long dataMillis;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_SYS_UPDATER)
    private String sysUpdater;

    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_UPDATE_MILLIS)
    private Long updateMillis;

    public static ReportTxStatistics valueOf(ReportTxStatisticsDO d) {
        return new ReportTxStatistics(d.getId(), d.getIssuerBankId(), d.getYear(), d.getMonth(),
                d.getDayOfMonth(), d.getTotal(), d.getOtpCount(), d.getNCount(), d.getACount(),
                d.getYCount(), d.getCyCount(), d.getCnCount(), d.getRCount(), d.getUCount(),
                d.getDataMillis(), d.getSysCreator(), d.getCreateMillis(), d.getSysUpdater(),
                d.getUpdateMillis());
    }

    // public static ReportTxStatistics newInstance(Long issuerBankId, QueryTimeRange queryTime,
    // String operator,
    // long createMillis)
    // {
    // ReportTxStatistics reportTxStatistics = new ReportTxStatistics();
    // reportTxStatistics.setIssuerBankId(issuerBankId);
    // reportTxStatistics.setYear(queryTime.getStartZonedDateTime().getYear());
    // reportTxStatistics.setMonth(queryTime.getStartZonedDateTime().getMonthValue());
    // reportTxStatistics.setDayOfMonth(queryTime.getStartZonedDateTime().getDayOfMonth());
    // reportTxStatistics.setDataMillis(queryTime.getStartMillis());
    // reportTxStatistics.setSysCreator(operator);
    // reportTxStatistics.setCreateMillis(createMillis);
    // return reportTxStatistics;
    // }
    //
    // public static ReportTxStatistics appendData(ReportTxStatistics entity,
    // StatisticsTransactionStatusDTO dto)
    // {
    // entity.setIssuerBankId(dto.getIssuerBankId());
    // entity.setTotal(StringUtils.isNumeric(dto.getTotal()) ? Long.parseLong(dto.getTotal()) : 0);
    // entity.setOtpCount(StringUtils.isNumeric(dto.getOtpCount()) ?
    // Long.parseLong(dto.getOtpCount()) : 0);
    // entity.setNCount(StringUtils.isNumeric(dto.getN()) ? Integer.parseInt(dto.getN()) : 0);
    // entity.setACount(StringUtils.isNumeric(dto.getA()) ? Integer.parseInt(dto.getA()) : 0);
    // entity.setYCount(StringUtils.isNumeric(dto.getY()) ? Integer.parseInt(dto.getY()) : 0);
    // entity.setCyCount(StringUtils.isNumeric(dto.getCy()) ? Integer.parseInt(dto.getCy()) : 0);
    // entity.setCnCount(StringUtils.isNumeric(dto.getCn()) ? Integer.parseInt(dto.getCn()) : 0);
    // entity.setRCount(StringUtils.isNumeric(dto.getR()) ? Integer.parseInt(dto.getR()) : 0);
    // entity.setUCount(StringUtils.isNumeric(dto.getU()) ? Integer.parseInt(dto.getU()) : 0);
    // return entity;
    // }

}
