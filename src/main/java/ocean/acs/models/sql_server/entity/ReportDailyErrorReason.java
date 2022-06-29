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
import ocean.acs.models.data_object.entity.ReportDailyErrorReasonDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_REPORT_DAILY_ERROR_REASON)
public class ReportDailyErrorReason implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_YEAR)
    private Integer year;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_MONTH)
    private Integer month;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_DAY_OF_MONTH)
    private Integer dayOfMonth;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_REASON_CODE_1)
    private Integer reasonCode1;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_REASON_COUNT_1)
    private Integer reasonCount1;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_REASON_PERCENTAGE_1)
    private Double reasonPercentage1;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_REASON_CODE_2)
    private Integer reasonCode2;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_REASON_COUNT_2)
    private Integer reasonCount2;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_REASON_PERCENTAGE_2)
    private Double reasonPercentage2;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_REASON_CODE_3)
    private Integer reasonCode3;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_REASON_COUNT_3)
    private Integer reasonCount3;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_REASON_PERCENTAGE_3)
    private Double reasonPercentage3;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_DATA_MILLIS)
    private Long dataMillis;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_SYS_UPDATER)
    private String sysUpdater;

    @Column(name = DBKey.COL_REPORT_DAILY_ERROR_REASON_UPDATE_MILLIS)
    private Long updateMillis;

    public static ReportDailyErrorReason valueOf(ReportDailyErrorReasonDO d) {
        return new ReportDailyErrorReason(d.getId(), d.getIssuerBankId(), d.getYear(), d.getMonth(),
                d.getDayOfMonth(), d.getReasonCode1(), d.getReasonCount1(),
                d.getReasonPercentage1(), d.getReasonCode2(), d.getReasonCount2(),
                d.getReasonPercentage2(), d.getReasonCode3(), d.getReasonCount3(),
                d.getReasonPercentage3(), d.getDataMillis(), d.getSysCreator(), d.getCreateMillis(),
                d.getSysUpdater(), d.getUpdateMillis());
    }

    // /**
    // * @param issuerBankId
    // * @param queryTimeRange
    // * @param errorReasonList 傳入此list時，須先按照失敗次數降冪排序
    // * @param operator
    // * @param createMillis
    // * @return
    // */
    // public static ReportDailyErrorReason newInstance(Long issuerBankId, QueryTimeRange
    // queryTimeRange,
    // List<TxLogErrorReasonDTO> errorReasonList, String operator, long createMillis)
    // {
    // ReportDailyErrorReason entity = new ReportDailyErrorReason();
    // entity.setIssuerBankId(issuerBankId);
    // entity.setYear(queryTimeRange.getStartZonedDateTime().getYear());
    // entity.setMonth(queryTimeRange.getStartZonedDateTime().getMonthValue());
    // entity.setDayOfMonth(queryTimeRange.getStartZonedDateTime().getDayOfMonth());
    //
    // TxLogErrorReasonDTO reason1 = errorReasonList.get(0);
    // TxLogErrorReasonDTO reason2 = errorReasonList.get(1);
    // TxLogErrorReasonDTO reason3 = errorReasonList.get(2);
    //
    // final int reasonCode1 = reason1.getErrorReasonCode();
    // final int reasonCode2 = reason2.getErrorReasonCode();
    // final int reasonCode3 = reason3.getErrorReasonCode();
    // entity.setReasonCode1(reasonCode1);
    // entity.setReasonCode2(reasonCode2);
    // entity.setReasonCode3(reasonCode3);
    // entity.setReasonCount1(reason1.getErrorReasonCount());
    // entity.setReasonCount2(reason2.getErrorReasonCount());
    // entity.setReasonCount3(reason3.getErrorReasonCount());
    //
    // final int errorTotal =
    // errorReasonList.stream().mapToInt(TxLogErrorReasonDTO::getErrorReasonCount).sum();
    //
    // Double reasonPercentage1 = (double) reason1.getErrorReasonCount() / errorTotal;
    // Double reasonPercentage2 = (double) reason2.getErrorReasonCount() / errorTotal;
    // Double reasonPercentage3 = (double) reason3.getErrorReasonCount() / errorTotal;
    //
    // reasonPercentage1 = reasonPercentage1.isNaN() ? 0.0 : reasonPercentage1 * 100;
    // reasonPercentage2 = reasonPercentage2.isNaN() ? 0.0 : reasonPercentage2 * 100;
    // reasonPercentage3 = reasonPercentage3.isNaN() ? 0.0 : reasonPercentage3 * 100;
    //
    // final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    // reasonPercentage1 = Double.parseDouble(decimalFormat.format(reasonPercentage1));
    // reasonPercentage2 = Double.parseDouble(decimalFormat.format(reasonPercentage2));
    // reasonPercentage3 = Double.parseDouble(decimalFormat.format(reasonPercentage3));
    //
    // entity.setReasonPercentage1(reasonPercentage1);
    // entity.setReasonPercentage2(reasonPercentage2);
    // entity.setReasonPercentage3(reasonPercentage3);
    //
    // entity.setDataMillis(queryTimeRange.getStartMillis());
    // entity.setSysCreator(operator);
    // entity.setCreateMillis(createMillis);
    // return entity;
    // }

}
