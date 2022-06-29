package ocean.acs.models.oracle.entity;

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
import org.hibernate.annotations.GenericGenerator;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_REPORT_TX_STATISTICS_DETAIL)
public class ReportTxStatisticsDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "report_tx_statistics_detail_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "RT_TX_STATISTICS_DETAIL_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "report_tx_statistics_detail_seq_generator")
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
    private String cardBrandName;

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
}
