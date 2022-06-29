package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.ReportDailyErrorReasonDO;
import ocean.acs.models.data_object.portal.ComplexStatisticsErrorReasonDO;

public interface ReportDailyErrorReasonDAO {

    void saveAll(List<ReportDailyErrorReasonDO> reportDailyErrorReasonDoList);

    Optional<ReportDailyErrorReasonDO> findByIssuerBankIdAndYearAndMonthAndDay(Long issuerBankId,
            int year, int month, int day);

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

    List<ComplexStatisticsErrorReasonDO> findByYearAndMonthAndDay(int year, int month, int day);

    List<ComplexStatisticsErrorReasonDO> findByIssuerBankIdAndYearAndMonthAndDay(long issuerBankId,
            int year, int month, int day);

}
