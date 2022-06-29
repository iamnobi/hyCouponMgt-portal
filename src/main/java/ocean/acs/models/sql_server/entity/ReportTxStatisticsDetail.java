package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.ReportTxStatisticsDetailDO;
import ocean.acs.models.entity.DBKey;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_REPORT_TX_STATISTICS_DETAIL)
public class ReportTxStatisticsDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_ID)
    private Long id;

    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_YEAR)
    private Integer year;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_MONTH)
    private Integer month;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_DAY_OF_MONTH)
    private Integer dayOfMonth;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_CARD_BRAND_NAME)
    private String cardBrand;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_U_RATE)
    private Double uRate = 0.0;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_N_RATE)
    private Double nRate = 0.0;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_ARES_Y_COUNT)
    private Long aresYCount = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_ARES_N_COUNT)
    private Long aresNCount = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_ARES_U_COUNT)
    private Long aresUCount = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_ARES_A_COUNT)
    private Long aresACount = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_ARES_C_COUNT)
    private Long aresCCount = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_ARES_R_COUNT)
    private Long aresRCount = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_RREQ_Y_COUNT)
    private Long rreqYCount = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_RREQ_N_COUNT)
    private Long rreqNCount = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_RREQ_U_COUNT)
    private Long rreqUCount = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_RREQ_A_COUNT)
    private Long rreqACount = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_RREQ_R_COUNT)
    private Long rreqRCount = 0L;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_DATA_MILLIS)
    private Long dataMillis;

    @NonNull
    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_CREATE_MILLIS)
    private Long createMillis = System.currentTimeMillis();

    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_SYS_UPDATER)
    private String sysUpdater;

    @Column(name = DBKey.COL_REPORT_TX_STATISTICS_DETAIL_UPDATE_MILLIS)
    private Long updateMillis;

    public static ReportTxStatisticsDetail valueOf(ReportTxStatisticsDetailDO d) {
        return new ReportTxStatisticsDetail(d.getId(), d.getIssuerBankId(), d.getYear(),
                d.getMonth(), d.getDayOfMonth(), d.getCardBrand(), d.getURate(), d.getNRate(),
                d.getAresYCount(), d.getAresNCount(), d.getAresUCount(), d.getAresACount(),
                d.getAresCCount(), d.getAresRCount(), d.getRreqYCount(), d.getRreqNCount(),
                d.getRreqUCount(), d.getRreqACount(), d.getRreqRCount(), d.getSysCreator(),
                d.getDataMillis(), d.getCreateMillis(), d.getSysUpdater(), d.getUpdateMillis());
    }

    // public static ReportTxStatisticsDetail newInstance(Long issuerBankId, CardBrand cardBrand,
    // QueryTimeRange queryTimeRange, String operator)
    // {
    // final ZonedDateTime startDateTime = queryTimeRange.getStartZonedDateTime();
    // ReportTxStatisticsDetail reportTxStatisticsDetail = new ReportTxStatisticsDetail();
    // reportTxStatisticsDetail.setIssuerBankId(issuerBankId);
    // reportTxStatisticsDetail.setCardBrand(cardBrand);
    // reportTxStatisticsDetail.setYear(startDateTime.getYear());
    // reportTxStatisticsDetail.setMonth(startDateTime.getMonthValue());
    // reportTxStatisticsDetail.setDayOfMonth(startDateTime.getDayOfMonth());
    // reportTxStatisticsDetail.setDataMillis(queryTimeRange.getStartMillis());
    // reportTxStatisticsDetail.setSysCreator(operator);
    // reportTxStatisticsDetail.setCreateMillis(queryTimeRange.getNowMillis());
    // return reportTxStatisticsDetail;
    // }
    //
    // public static ReportTxStatisticsDetail appendData(ReportTxStatisticsDetail entity,
    // CardBrandTransStatusDTO cardBrandTransStatusDetailDto, Double nRate, Double uRate)
    // {
    // CardBrandTransStatusDTO.TransStatus aRes = cardBrandTransStatusDetailDto.getARes();
    // entity.setAresYCount(aRes.getY());
    // entity.setAresNCount(aRes.getN());
    // entity.setAresUCount(aRes.getU());
    // entity.setAresACount(aRes.getA());
    // entity.setAresCCount(aRes.getC());
    // entity.setAresRCount(aRes.getR());
    //
    // CardBrandTransStatusDTO.TransStatus rreq = cardBrandTransStatusDetailDto.getRReq();
    // entity.setRreqYCount(rreq.getY());
    // entity.setRreqNCount(rreq.getN());
    // entity.setRreqUCount(rreq.getU());
    // entity.setRreqACount(rreq.getA());
    // entity.setRreqRCount(rreq.getR());
    //
    // entity.setNRate(nRate);
    // entity.setURate(uRate);
    // return entity;
    // }

}
