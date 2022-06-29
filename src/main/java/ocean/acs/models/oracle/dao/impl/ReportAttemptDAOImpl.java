package ocean.acs.models.oracle.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.dao.ReportAttemptDAO;
import ocean.acs.models.data_object.entity.ReportAttemptDO;
import ocean.acs.models.data_object.portal.PortalReportAttemptDO;
import ocean.acs.models.oracle.entity.ReportAttempt;

@Log4j2
@Repository
@AllArgsConstructor
public class ReportAttemptDAOImpl implements ReportAttemptDAO {

    private final ocean.acs.models.oracle.repository.ReportAttemptRepository repo;

    @Override
    public List<PortalReportAttemptDO> findByIssuerBankIdAndDataMillisBetween(Long issuerBankId,
            long startMillis, long endMillis) {
        try {
            return repo.findByIssuerBankIdAndDataMillisBetween(issuerBankId, startMillis,
                    endMillis);
        } catch (Exception e) {
            log.error(
                    "[findByIssuerBankIdAndDataMillisBetween] unknown exception, issuerBankId={}, startMillis={}, endMillis={}",
                    issuerBankId, startMillis, endMillis, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<PortalReportAttemptDO> findByDataMillisBetween(long startMillis, long endMillis) {
        try {
            return repo.findByDataMillisBetween(startMillis, endMillis);
        } catch (Exception e) {
            log.error("[findByDataMillisBetween] unknown exception, startMillis={}, endMillis={}",
                    startMillis, endMillis, e);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<ReportAttemptDO> findByIssuerBankIdAndYearAndMonthAndDayOfMonth(
            Long issuerBankId, int year, int month, int day) {
        return repo.findByIssuerBankIdAndYearAndMonthAndDayOfMonth(issuerBankId, year, month, day)
                .map(ReportAttemptDO::valueOf);
    }

    @Override
    public ReportAttemptDO save(ReportAttemptDO reportAttemptDO) {
        ReportAttempt reportAttempt = ReportAttempt.valueOf(reportAttemptDO);
        return ReportAttemptDO.valueOf(repo.save(reportAttempt));
    }

    @Override
    public List<PortalReportAttemptDO> findByYearAndMonth(int year, int month) {
        try {
            return repo.findByYearAndMonth(year, month);
        } catch (Exception e) {
            log.error("[findByYearAndMonth] unknown exception, year={}, month={}", year, month, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<PortalReportAttemptDO> findByYearAndMonthAndDay(int year, int month, int day) {
        try {
            return repo.findByYearAndMonthAndDay(year, month, day);
        } catch (Exception e) {
            log.error("[findByYearAndMonthAndDay] unknown exception, year={}, month={}, day={}",
                    year, month, day, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<PortalReportAttemptDO> findByIssuerBankIdAndYearAndMonth(long issuerBankId,
            int year, int month) {
        try {
            return repo.findByIssuerBankIdAndYearAndMonth(issuerBankId, year, month);
        } catch (Exception e) {
            log.error(
                    "[findByIssuerBankIdAndYearAndMonth] unknown exception, issuerBankId={}, year={}, month={}",
                    issuerBankId, year, month, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<PortalReportAttemptDO> findByIssuerBankIdAndYearAndMonthAndDay(long issuerBankId,
            int year, int month, int day) {
        try {
            return repo.findByIssuerBankIdAndYearAndMonthAndDay(issuerBankId, year, month, day);
        } catch (Exception e) {
            log.error(
                    "[findByIssuerBankIdAndYearAndMonthAndDay] unknown exception, issuerBankId={}, year={}, month={}, day={}",
                    issuerBankId, year, month, day, e);
            return Collections.emptyList();
        }
    }
}
