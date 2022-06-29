package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.ReportTxStatisticsDetailDO;

public interface ReportTxStatisticsDetailDAO {

    Optional<ReportTxStatisticsDetailDO> findByCardBrandAndYearAndMonthAndDay(Long issuerBankId,
            String cardBrand, int year, int month, int day);

    /**
     * The original report query method before time zone problem.
     */
    @Deprecated
    List<ReportTxStatisticsDetailDO> statisticByDataMillisBetween(Long issuerBankId, long startMillis,
            long endMillis);

    Optional<ReportTxStatisticsDetailDO> save(ReportTxStatisticsDetailDO reportTxStatisticsDetailDO);

    List<ReportTxStatisticsDetailDO> saveAll(
            List<ReportTxStatisticsDetailDO> reportTxStatisticsDetailDoList);

    List<ReportTxStatisticsDetailDO> statisticByIssuerBankIdAndYearAndMonth(long issuerBankId,
            int year, int month);

    List<ReportTxStatisticsDetailDO> statisticByIssuerBankIdAndYearAndMonthAndDay(long issuerBankId,
            int year, int month, int day);

    List<ReportTxStatisticsDetailDO> statisticByYearAndMonth(int year, int month);

    List<ReportTxStatisticsDetailDO> statisticByYearAndMonthAndDay(int year, int month, int day);
}
