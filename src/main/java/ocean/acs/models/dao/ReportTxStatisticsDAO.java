package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.ReportTxStatisticsDO;
import ocean.acs.models.data_object.portal.PortalReportTxStatisticsDO;

public interface ReportTxStatisticsDAO {

    Optional<ReportTxStatisticsDO> findByYearAndMonthAndDay(Long issuerBankId, int year, int month,
            int day);

    /**
     * The original report query method before time zone problem.
     */
    @Deprecated
    List<PortalReportTxStatisticsDO> findByIssuerBankIdAndDataMillisBetween(Long issuerBankId,
            long startMillis, long endMillis);

    /**
     * The original report query method before time zone problem.
     */
    @Deprecated
    List<PortalReportTxStatisticsDO> findByDataMillisBetween(long startMillis, long endMillis);

    List<ReportTxStatisticsDO> save(List<ReportTxStatisticsDO> reportTxStatisticsDoList);

    List<PortalReportTxStatisticsDO> findByYearAndMonth(int year, int month);

    List<PortalReportTxStatisticsDO> findByYearAndMonthAndDay(int year, int month, int day);

    List<PortalReportTxStatisticsDO> findByIssuerBankIdAndYearAndMonth(long issuerBankId, int year,
            int month);

    List<PortalReportTxStatisticsDO> findByIssuerBankIdAndYearAndMonthAndDay(long issuerBankId,
            int year, int month, int day);

}
