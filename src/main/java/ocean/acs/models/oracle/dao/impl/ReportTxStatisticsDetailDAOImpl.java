package ocean.acs.models.oracle.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.dao.ReportTxStatisticsDetailDAO;
import ocean.acs.models.data_object.entity.ReportTxStatisticsDetailDO;
import ocean.acs.models.oracle.entity.ReportTxStatisticsDetail;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class ReportTxStatisticsDetailDAOImpl implements ReportTxStatisticsDetailDAO {

    private final ocean.acs.models.oracle.repository.ReportTxStatisticsDetailRepository repo;

    @Override
    public Optional<ReportTxStatisticsDetailDO> findByCardBrandAndYearAndMonthAndDay(
            Long issuerBankId, String cardBrand, int year, int month, int day) {
        try {
            return repo.findByIssuerBankIdAndCardBrandNameAndYearAndMonthAndDayOfMonth(issuerBankId,
              cardBrand, year, month, day).map(ReportTxStatisticsDetailDO::valueOf);
        } catch (Exception e) {
            log.error(
                    "[findByCardBrandAndYearAndMonthAndDay] unknown exception, cardBrand={}, year={}, month={}, day={}",
                    cardBrand, year, month, day, e);
            return Optional.empty();
        }
    }

    @Override
    public List<ReportTxStatisticsDetailDO> statisticByDataMillisBetween(Long issuerBankId,
            long startMillis, long endMillis) {
        try {
            List<ReportTxStatisticsDetailDO> resultList =
                    repo.statisticByDataMillisBetween(issuerBankId, startMillis, endMillis);
            if (resultList == null || resultList.isEmpty()) {
                return Collections.emptyList();
            }

            return resultList;
        } catch (Exception e) {
            log.error(
                    "[statisticByDataMillisBetween] unknown exception, issuerBankId={}, startMillis={}, endMillis={}",
                    issuerBankId, startMillis, endMillis, e);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<ReportTxStatisticsDetailDO> save(
            ReportTxStatisticsDetailDO reportTxStatisticsDetailDO) {
        try {
            ReportTxStatisticsDetail reportTxStatisticsDetail =
                    ReportTxStatisticsDetail.valueOf(reportTxStatisticsDetailDO);
            return Optional
                    .of(ReportTxStatisticsDetailDO.valueOf(repo.save(reportTxStatisticsDetail)));
        } catch (Exception e) {
            log.error("[save] unknown exception, request params={}", reportTxStatisticsDetailDO, e);
            return Optional.empty();
        }
    }

    @Override
    public List<ReportTxStatisticsDetailDO> saveAll(
            List<ReportTxStatisticsDetailDO> reportTxStatisticsDetailDoList) {
        try {
            List<ReportTxStatisticsDetail> reportTxStatisticsDetailList =
                    reportTxStatisticsDetailDoList.stream().map(ReportTxStatisticsDetail::valueOf)
                            .collect(Collectors.toList());
            Iterable<ReportTxStatisticsDetail> reportTxStatisticsDetailIter =
                    repo.saveAll(reportTxStatisticsDetailList);
            return StreamSupport.stream(reportTxStatisticsDetailIter.spliterator(), false)
                    .map(ReportTxStatisticsDetailDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[saveAll] unknown exception, request params={}",
                    reportTxStatisticsDetailDoList, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<ReportTxStatisticsDetailDO> statisticByIssuerBankIdAndYearAndMonth(
            long issuerBankId, int year, int month) {
        try {
            List<ReportTxStatisticsDetailDO> resultList =
                    repo.statisticByIssuerBankIdAndYearAndMonth(issuerBankId, year, month);
            if (resultList == null || resultList.isEmpty()) {
                return Collections.emptyList();
            }

            return resultList;
        } catch (Exception e) {
            log.error(
                    "[statisticByIssuerBankIdAndYearAndMonth] unknown exception, issuerBankId={}, year={}, month={}",
                    issuerBankId, year, month, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<ReportTxStatisticsDetailDO> statisticByIssuerBankIdAndYearAndMonthAndDay(
            long issuerBankId, int year, int month, int day) {
        try {
            List<ReportTxStatisticsDetailDO> resultList =
                    repo.statisticByIssuerBankIdAndYearAndMonthAndDay(issuerBankId, year, month, day);
            if (resultList == null || resultList.isEmpty()) {
                return Collections.emptyList();
            }

            return resultList;
        } catch (Exception e) {
            log.error(
                    "[statisticByIssuerBankIdAndYearAndMonthAndDay] unknown exception, issuerBankId={}, year={}, month={}, day={}",
                    issuerBankId, year, month, day, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<ReportTxStatisticsDetailDO> statisticByYearAndMonth(int year, int month) {
        try {
            List<ReportTxStatisticsDetailDO> resultList =
                repo.statisticByYearAndMonth(year, month);
            if (resultList == null || resultList.isEmpty()) {
                return Collections.emptyList();
            }

            return resultList;
        } catch (Exception e) {
            log.error(
                "[statisticByYearAndMonth] unknown exception, year={}, month={}",
                year, month, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<ReportTxStatisticsDetailDO> statisticByYearAndMonthAndDay(int year, int month, int day) {
        try {
            List<ReportTxStatisticsDetailDO> resultList =
                repo.statisticByYearAndMonthAndDay(year, month, day);
            if (resultList == null || resultList.isEmpty()) {
                return Collections.emptyList();
            }

            return resultList;
        } catch (Exception e) {
            log.error(
                "[statisticByYearAndMonth] unknown exception, year={}, month={}, day={}",
                year, month, day, e);
            return Collections.emptyList();
        }
    }
}
