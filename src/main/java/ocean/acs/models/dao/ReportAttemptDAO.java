package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.data_object.entity.ReportAttemptDO;
import ocean.acs.models.data_object.portal.PortalReportAttemptDO;

public interface ReportAttemptDAO {

    /**
     * The original report query method before time zone problem.
     */
    @Deprecated
    List<PortalReportAttemptDO> findByIssuerBankIdAndDataMillisBetween(Long issuerBankId,
            long startMillis, long endMillis);

    /**
     * The original report query method before time zone problem.
     */
    @Deprecated
    List<PortalReportAttemptDO> findByDataMillisBetween(long startMillis, long endMillis);

    Optional<ReportAttemptDO> findByIssuerBankIdAndYearAndMonthAndDayOfMonth(Long issuerBankId,
            int year, int month, int day);

    ReportAttemptDO save(ReportAttemptDO reportAttemptDO);

    List<PortalReportAttemptDO> findByYearAndMonth(int year, int month);

    List<PortalReportAttemptDO> findByYearAndMonthAndDay(int year, int month, int day);

    List<PortalReportAttemptDO> findByIssuerBankIdAndYearAndMonth(long issuerBankId, int year,
            int month);

    List<PortalReportAttemptDO> findByIssuerBankIdAndYearAndMonthAndDay(long issuerBankId, int year,
            int month, int day);
}
