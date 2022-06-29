package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.ReportAttemptDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_REPORT_ATTEMPT)
public class ReportAttempt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_REPORT_ATTEMPT_ID)
    private Long id;

    @Column(name = DBKey.COL_REPORT_ATTEMPT_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_REPORT_ATTEMPT_YEAR)
    private Integer year;

    @NonNull
    @Column(name = DBKey.COL_REPORT_ATTEMPT_MONTH)
    private Integer month;

    @NonNull
    @Column(name = DBKey.COL_REPORT_ATTEMPT_DAY_OF_MONTH)
    private Integer dayOfMonth;

    @NonNull
    @Column(name = DBKey.COL_REPORT_ATTEMPT_PERMITTED_COUNT)
    private Integer permittedCount = 0;

    @NonNull
    @Column(name = DBKey.COL_REPORT_ATTEMPT_REAL_TRIES_COUNT)
    private Integer realTriesCount = 0;

    @NonNull
    @Column(name = DBKey.COL_REPORT_ATTEMPT_PERCENTAGE)
    private Double percentage = 0.0;

    @NonNull
    @Column(name = DBKey.COL_REPORT_ATTEMPT_DATA_MILLIS)
    private Long dataMillis;

    @NonNull
    @Column(name = DBKey.COL_REPORT_ATTEMPT_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_REPORT_ATTEMPT_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_REPORT_ATTEMPT_SYS_UPDATER)
    private String sysUpdater;

    @Column(name = DBKey.COL_REPORT_ATTEMPT_UPDATE_MILLIS)
    private Long updateMillis;

    public static ReportAttempt valueOf(ReportAttemptDO d) {
        return new ReportAttempt(d.getId(), d.getIssuerBankId(), d.getYear(), d.getMonth(),
                d.getDayOfMonth(), d.getPermittedCount(), d.getRealTriesCount(), d.getPercentage(),
                d.getDataMillis(), d.getSysCreator(), d.getCreateMillis(), d.getSysUpdater(),
                d.getUpdateMillis());
    }

    // public static ReportAttempt newInstance(Long issuerBankId, QueryTimeRange queryTimeRange,
    // String operator,
    // long createMillis)
    // {
    // ReportAttempt reportAttempt = new ReportAttempt();
    // reportAttempt.setIssuerBankId(issuerBankId);
    // reportAttempt.setYear(queryTimeRange.getStartZonedDateTime().getYear());
    // reportAttempt.setMonth(queryTimeRange.getStartZonedDateTime().getMonthValue());
    // reportAttempt.setDayOfMonth(queryTimeRange.getStartZonedDateTime().getDayOfMonth());
    // reportAttempt.setDataMillis(queryTimeRange.getStartMillis());
    // reportAttempt.setSysCreator(operator);
    // reportAttempt.setCreateMillis(createMillis);
    // return reportAttempt;
    // }

}
