package ocean.acs.models.oracle.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.dao.ReportTxStatisticsDAO;
import ocean.acs.models.data_object.entity.ReportTxStatisticsDO;
import ocean.acs.models.data_object.portal.PortalReportTxStatisticsDO;
import ocean.acs.models.oracle.entity.ReportTxStatistics;

@Log4j2
@Repository
@AllArgsConstructor
public class ReportTxStatisticsDAOImpl implements ReportTxStatisticsDAO {

    private final ocean.acs.models.oracle.repository.ReportTxStatisticsRepository repo;

    @Override
    public Optional<ReportTxStatisticsDO> findByYearAndMonthAndDay(Long issuerBankId, int year,
            int month, int day) {
        try {
            return repo
                    .findByIssuerBankIdAndYearAndMonthAndDayOfMonth(issuerBankId, year, month, day)
                    .map(ReportTxStatisticsDO::valueOf);
        } catch (Exception e) {
            log.error("[findByYearAndMonthAndDay] unknown exception", e);
            return Optional.empty();
        }
    }

    @Override
    public List<PortalReportTxStatisticsDO> findByIssuerBankIdAndDataMillisBetween(
            Long issuerBankId, long startMillis, long endMillis) {
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
    public List<PortalReportTxStatisticsDO> findByDataMillisBetween(long startMillis,
            long endMillis) {
        try {
            return repo.findByDataMillisBetween(startMillis, endMillis);
        } catch (Exception e) {
            log.error("[findByDataMillisBetween] unknown exception, startMillis={}, endMillis={}",
                    startMillis, endMillis, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<ReportTxStatisticsDO> save(List<ReportTxStatisticsDO> reportTxStatisticsDoList) {
        List<ReportTxStatistics> reportTxStatisticsList = reportTxStatisticsDoList.stream()
                .map(ReportTxStatistics::valueOf).collect(Collectors.toList());
        Iterable<ReportTxStatistics> reportTxStatisticsIter = repo.saveAll(reportTxStatisticsList);
        return StreamSupport.stream(reportTxStatisticsIter.spliterator(), false)
                .map(ReportTxStatisticsDO::valueOf).collect(Collectors.toList());
    }

    @Override
    public List<PortalReportTxStatisticsDO> findByYearAndMonth(int year, int month) {
        try {
            return repo.findByYearAndMonth(year, month);
        } catch (Exception e) {
            log.error("[findByYearAndMonth] unknown exception, year={}, month={}", year, month, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<PortalReportTxStatisticsDO> findByYearAndMonthAndDay(int year, int month, int day) {
        try {
            return repo.findByYearAndMonthAndDay(year, month, day);
        } catch (Exception e) {
            log.error("[findByYearAndMonthAndDay] unknown exception, year={}, month={}, day={}",
                    year, month, day, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<PortalReportTxStatisticsDO> findByIssuerBankIdAndYearAndMonth(long issuerBankId,
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
    public List<PortalReportTxStatisticsDO> findByIssuerBankIdAndYearAndMonthAndDay(
            long issuerBankId, int year, int month, int day) {
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
