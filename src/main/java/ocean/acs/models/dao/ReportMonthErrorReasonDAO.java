package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.ReportMonthErrorReasonDO;
import ocean.acs.models.data_object.portal.ComplexStatisticsErrorReasonDO;

public interface ReportMonthErrorReasonDAO {

    void saveAll(List<ReportMonthErrorReasonDO> reportDailyErrorReasonDoList);

    /**
     * The original report query method before time zone problem.
     */
    @Deprecated
    List<ComplexStatisticsErrorReasonDO> findByDataMillisBetween(long startMillis, long endMillis);

    /**
     * The original report query method before time zone problem.
     */
    @Deprecated
    List<ComplexStatisticsErrorReasonDO> findByIssuerBankIdAndDataMillisBetween(Long issuerBankId,
            long startMillis, long endMillis);

    Optional<ReportMonthErrorReasonDO> findByIssuerBankIdAndYearAndMonth(Long issuerBankId, int year,
            int month);

    List<ComplexStatisticsErrorReasonDO> findByYearAndMonth(int year, int month);

    List<ComplexStatisticsErrorReasonDO> findByIssuerBankIdAndYearAndMonth(long issuerBankId,
            int year, int month);
}
