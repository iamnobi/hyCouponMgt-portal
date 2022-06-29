package com.cherri.acs_portal.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.report.BankTransStatusDetailDTO;
import com.cherri.acs_portal.dto.report.CardBrandTransStatusDTO;
import com.cherri.acs_portal.dto.report.QueryTimeRange;
import com.cherri.acs_portal.dto.report.StatisticsTransactionStatusDTO;
import com.cherri.acs_portal.dto.system.CardBrandDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogErrorReasonDTO;
import com.cherri.acs_portal.manager.ReportManager;
import com.cherri.acs_portal.util.ThreeDsRateUtil;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.dao.ReportAttemptDAO;
import ocean.acs.models.dao.ReportDailyErrorReasonDAO;
import ocean.acs.models.dao.ReportMonthErrorReasonDAO;
import ocean.acs.models.dao.ReportTxStatisticsDAO;
import ocean.acs.models.dao.ReportTxStatisticsDetailDAO;
import ocean.acs.models.dao.WhiteListAttemptSettingDAO;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.entity.ReportAttemptDO;
import ocean.acs.models.data_object.entity.ReportDailyErrorReasonDO;
import ocean.acs.models.data_object.entity.ReportMonthErrorReasonDO;
import ocean.acs.models.data_object.entity.ReportTxStatisticsDO;
import ocean.acs.models.data_object.entity.ReportTxStatisticsDetailDO;

@Log4j2
@Service
public class StatisticsReportJobService {

    private final IssuerBankDAO bankDao;
    private final WhiteListAttemptSettingDAO attemptDao;
    private final ReportAttemptDAO reportAttemptDao;
    private final ReportDailyErrorReasonDAO reportDailyErrorReasonDao;
    private final ReportMonthErrorReasonDAO reportMonthErrorReasonDao;

    private final ReportManager reportManager;
    private final ReportTxStatisticsDAO reportTxStatisticsDao;
    private final ReportTxStatisticsDetailDAO reportTxStatisticsDetailDao;
    private final CardBrandConfigurationHelper cardBrandConfigurationHelper;

    @Autowired
    public StatisticsReportJobService(
      IssuerBankDAO bankDao,
      WhiteListAttemptSettingDAO attemptDao,
      ReportAttemptDAO reportAttemptDao,
      ReportDailyErrorReasonDAO reportDailyErrorReasonDao,
      ReportMonthErrorReasonDAO reportMonthErrorReasonDao,
      ReportManager reportManager,
      ReportTxStatisticsDAO reportTxStatisticsDao,
      ReportTxStatisticsDetailDAO reportTxStatisticsDetailDao,
      CardBrandConfigurationHelper cardBrandConfigurationHelper) {
        this.bankDao = bankDao;
        this.attemptDao = attemptDao;
        this.reportAttemptDao = reportAttemptDao;
        this.reportDailyErrorReasonDao = reportDailyErrorReasonDao;
        this.reportMonthErrorReasonDao = reportMonthErrorReasonDao;
        this.reportManager = reportManager;
        this.reportTxStatisticsDao = reportTxStatisticsDao;
        this.reportTxStatisticsDetailDao = reportTxStatisticsDetailDao;
        this.cardBrandConfigurationHelper = cardBrandConfigurationHelper;
    }

    /** 統計 - 人工彈性授權 */
    @Transactional(rollbackFor = Exception.class)
    public boolean statisticsAttemptAuth(final QueryTimeRange queryTimeRange, String operator) {
        final long startMillis = queryTimeRange.getStartMillis();
        final long endMillis = queryTimeRange.getEndMillis();
        final long now = System.currentTimeMillis();

        try {
            List<Long> allBankId = bankDao.findIdAll();

            for (final Long issuerBankId : allBankId) {
                // 開通次數
                final int triesPermitted =
                  attemptDao.sumByTriesPermitted(issuerBankId, startMillis, endMillis);
                if (triesPermitted == -1) {
                    return false;
                }
                // 剩多少次彈性授權次數
                final int triesQuota =
                  attemptDao.sumByTriesQuota(issuerBankId, startMillis, endMillis);
                if (triesQuota == -1) {
                    return false;
                }
                final int realTriesCount = triesPermitted - triesQuota;

                double percentage = (double) realTriesCount / triesPermitted;
                if (Double.isNaN(percentage)) {
                    percentage = 0.0;
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat("#.###");
                    percentage = Double.parseDouble(decimalFormat.format(percentage)) * 100;
                }
                int year = queryTimeRange.getStartZonedDateTime().getYear();
                int month = queryTimeRange.getStartZonedDateTime().getMonthValue();
                int day = queryTimeRange.getStartZonedDateTime().getDayOfMonth();
                ReportAttemptDO reportAttempt =
                  reportAttemptDao
                    .findByIssuerBankIdAndYearAndMonthAndDayOfMonth(
                      issuerBankId, year, month, day)
                    .map(
                      reportAttemptEntity -> {
                          reportAttemptEntity.setUpdateMillis(now);
                          reportAttemptEntity.setSysUpdater(operator);
                          return reportAttemptEntity;
                      })
                    .orElse(
                      ReportAttemptDO.newInstance(
                        issuerBankId,
                        queryTimeRange.getStartZonedDateTime().getYear(),
                        queryTimeRange
                          .getStartZonedDateTime()
                          .getMonthValue(),
                        queryTimeRange
                          .getStartZonedDateTime()
                          .getDayOfMonth(),
                        queryTimeRange.getStartMillis(),
                        operator,
                        now));
                reportAttempt.setPermittedCount(triesPermitted);
                reportAttempt.setRealTriesCount(realTriesCount);
                reportAttempt.setPercentage(percentage);
                reportAttemptDao.save(reportAttempt);
            }
            return true;
        } catch (Exception e) {
            log.error("[statisticsAttemptAuth] unknown exception", e);
        }
        return false;
    }

    /** 統計 - 各交易狀態統計數值報表 */
    public boolean statisticsTransactionStatus(
      final QueryTimeRange queryTimeRange, String operator) {
        final long startMillis = queryTimeRange.getStartMillis();
        final long endMillis = queryTimeRange.getEndMillis();

        List<StatisticsTransactionStatusDTO> resultList =
          reportManager.statisticsTransactionStatus(startMillis, endMillis);
        if (resultList == null || resultList.isEmpty()) {
            return false;
        }
        List<ReportTxStatisticsDO> newDataList = new ArrayList<>();
        long now = System.currentTimeMillis();
        for (StatisticsTransactionStatusDTO resultDto : resultList) {
            int year = queryTimeRange.getStartZonedDateTime().getYear();
            int month = queryTimeRange.getStartZonedDateTime().getMonthValue();
            int day = queryTimeRange.getStartZonedDateTime().getDayOfMonth();
            ReportTxStatisticsDO reportTxStatisticsDO =
              reportTxStatisticsDao
                .findByYearAndMonthAndDay(resultDto.getIssuerBankId(), year, month, day)
                .map(
                  entity -> {
                      entity.setSysUpdater(operator);
                      entity.setUpdateMillis(now);
                      return entity;
                  })
                .orElse(
                  ReportTxStatisticsDO.newInstance(
                    resultDto.getIssuerBankId(),
                    queryTimeRange.getStartZonedDateTime().getYear(),
                    queryTimeRange.getStartZonedDateTime().getMonthValue(),
                    queryTimeRange.getStartZonedDateTime().getDayOfMonth(),
                    queryTimeRange.getStartMillis(),
                    operator,
                    now));
            reportTxStatisticsDO =
              appendDataForReportTxStatisticsDO(reportTxStatisticsDO, resultDto);
            newDataList.add(reportTxStatisticsDO);
        }
        reportTxStatisticsDao.save(newDataList);
        return true;
    }

    /** 統計 - 各交易狀態統計數值報表 - by card-brand */
    public boolean statisticsTransactionStatusDetail(
      final QueryTimeRange queryTimeRange, String operator) {
        final long startMillis = queryTimeRange.getStartMillis();
        final long endMillis = queryTimeRange.getEndMillis();
        boolean isSaveSuccess = true;
        // Statistics N-Rate and U-Rate
        // Map key is cardBrand name
        try {
            List<IssuerBankDO> availableBank = bankDao.findAll();
            if (availableBank.isEmpty()) {
                return true;
            }
            // Statistics transStatus
            List<BankTransStatusDetailDTO> transStatusDetailList =
              reportManager.statisticsTransStatusDetail(startMillis, endMillis);

            for (IssuerBankDO issuerBankDO : availableBank) {
                if (!isSaveSuccess) {
                    break;
                }

                Optional<BankTransStatusDetailDTO> bankTransStatusDetailOpt =
                  filterByBankName(issuerBankDO.getName(), transStatusDetailList);
                if (!bankTransStatusDetailOpt.isPresent()) {
                    continue;
                }

                List<ReportTxStatisticsDetailDO> entityList = new ArrayList<>();

                List<CardBrandTransStatusDTO> cardBrandTransStatusList =
                  bankTransStatusDetailOpt.get().getCardBrandTransStatusDetailList();
                List<CardBrandDTO> cardBrandList = cardBrandConfigurationHelper.findCardBrandList();
                for (CardBrandDTO cardBrand : cardBrandList) {
                    CardBrandTransStatusDTO cardBrandTransStatus =
                      cardBrandTransStatusList.stream()
                        .filter(
                          cardTransStatus ->
                            cardBrand
                              .getName()
                              .equals(cardTransStatus.getCardBrand()))
                        .findFirst()
                        .orElse(null);
                    if (null == cardBrandTransStatus) {
                        cardBrandTransStatus = CardBrandTransStatusDTO.empty(cardBrand.getName());
                    }

                    // Calculate N/U Rate
                    Double nRate =
                      ThreeDsRateUtil.calculateNRate(
                        cardBrandTransStatus.getARes(),
                        cardBrandTransStatus.getRReq(),
                        ThreeDsRateUtil.ThreeDsVersion.TWO);
                    Double uRate =
                      ThreeDsRateUtil.calculateURate(
                        cardBrandTransStatus.getARes(),
                        cardBrandTransStatus.getRReq(),
                        ThreeDsRateUtil.ThreeDsVersion.TWO);
                    // Combine with N-Rate, U-Rate, ARes, RRes result
                    ReportTxStatisticsDetailDO entity =
                      createOrGetReportTxStatisticsDetail(
                        queryTimeRange,
                        issuerBankDO.getId(),
                        cardBrand.getName(),
                        operator);
                    entity =
                      appendDataForReportTxStatisticsDetailDO(
                        entity, cardBrandTransStatus, nRate, uRate);
                    entityList.add(entity);
                }
                List<ReportTxStatisticsDetailDO> resultList =
                  reportTxStatisticsDetailDao.saveAll(entityList);
                isSaveSuccess = resultList != null && !resultList.isEmpty();
            }
        } catch (Exception e) {
            log.error("[statisticsTransactionStatusDetail] unknown exception", e);
            return false;
        }
        return isSaveSuccess;
    }

    private Optional<BankTransStatusDetailDTO> filterByBankName(
      String bankName, List<BankTransStatusDetailDTO> transStatusDetailList) {
        return transStatusDetailList.stream()
          .filter(bankTransStatus -> bankName.equals(bankTransStatus.getBankName()))
          .findFirst();
    }

    private ReportTxStatisticsDetailDO createOrGetReportTxStatisticsDetail(
      QueryTimeRange queryTimeRange, Long issuerBankId, String cardBrand, String operator) {
        ZonedDateTime start = queryTimeRange.getStartZonedDateTime();
        ReportTxStatisticsDetailDO rtTxStatisticsDetailDO =
          reportTxStatisticsDetailDao
            .findByCardBrandAndYearAndMonthAndDay(
              issuerBankId,
              cardBrand,
              start.getYear(),
              start.getMonthValue(),
              start.getDayOfMonth())
            .map(
              reportTxStatisticsDetail -> {
                  reportTxStatisticsDetail.setSysUpdater(operator);
                  reportTxStatisticsDetail.setUpdateMillis(
                    queryTimeRange.getNowMillis());
                  return reportTxStatisticsDetail;
              })
            .orElse(
              ReportTxStatisticsDetailDO.newInstance(
                issuerBankId,
                cardBrand,
                queryTimeRange.getStartZonedDateTime().getYear(),
                queryTimeRange.getStartZonedDateTime().getMonthValue(),
                queryTimeRange.getStartZonedDateTime().getDayOfMonth(),
                queryTimeRange.getStartMillis(),
                queryTimeRange.getNowMillis(),
                operator));
        return rtTxStatisticsDetailDO;
    }

    /** 統計 - 日報表 - 失敗原因 */
    public boolean statisticsDailyErrorReason(
      final QueryTimeRange queryTimeRange, String operator) {
        final long startMillis = queryTimeRange.getStartMillis();
        final long endMillis = queryTimeRange.getEndMillis();
        final long now = System.currentTimeMillis();
        final int year = queryTimeRange.getStartZonedDateTime().getYear();
        final int month = queryTimeRange.getStartZonedDateTime().getMonthValue();
        final int day = queryTimeRange.getStartZonedDateTime().getDayOfMonth();
        List<TxLogErrorReasonDTO> resultList;
        List<ReportDailyErrorReasonDO> newDataList = new ArrayList<>();
        try {
            List<Long> allBankId = bankDao.findIdAll();
            for (final Long issuerBankId : allBankId) {
                resultList =
                  reportManager.statisticsErrorReason(issuerBankId, startMillis, endMillis);
                if (CollectionUtils.isEmpty(resultList)) {
                    resultList = new ArrayList<>();
                }
                // 若查詢結果<三筆，則填入空資料維持每間銀行的失敗原因最少都有三個
                for (int i = resultList.size(); i < 3; i++) {
                    resultList.add(TxLogErrorReasonDTO.newInstance(issuerBankId, 0, 0));
                }
                ReportDailyErrorReasonDO errorReason =
                  reportDailyErrorReasonDao
                    .findByIssuerBankIdAndYearAndMonthAndDay(
                      issuerBankId, year, month, day)
                    .map(
                      monthErrorReason -> {
                          monthErrorReason.setSysUpdater(operator);
                          monthErrorReason.setUpdateMillis(now);
                          return monthErrorReason;
                      })
                    .orElse(
                      ReportDailyErrorReasonDO.newInstance(
                        issuerBankId,
                        queryTimeRange.getStartZonedDateTime().getYear(),
                        queryTimeRange
                          .getStartZonedDateTime()
                          .getMonthValue(),
                        queryTimeRange
                          .getStartZonedDateTime()
                          .getDayOfMonth(),
                        queryTimeRange.getStartMillis(),
                        resultList.get(0).getErrorReasonCode(),
                        resultList.get(1).getErrorReasonCode(),
                        resultList.get(2).getErrorReasonCode(),
                        resultList.get(0).getErrorReasonCount(),
                        resultList.get(1).getErrorReasonCount(),
                        resultList.get(2).getErrorReasonCount(),
                        resultList.stream()
                          .mapToInt(
                            TxLogErrorReasonDTO
                              ::getErrorReasonCount)
                          .sum(),
                        operator,
                        now));
                newDataList.add(errorReason);
            }
            reportDailyErrorReasonDao.saveAll(newDataList);
            return true;
        } catch (Exception e) {
            log.error("[statisticsDailyErrorReason] unknown exception", e);
        }
        return false;
    }

    /** 統計 - 月報表 - 失敗原因 */
    public boolean statisticsMonthErrorReason(
      final QueryTimeRange queryTimeRange, String operator) {
        final long startMillis = queryTimeRange.getStartMillis();
        final long endMillis = queryTimeRange.getEndMillis();
        final long now = System.currentTimeMillis();
        final int year = queryTimeRange.getStartZonedDateTime().getYear();
        final int month = queryTimeRange.getStartZonedDateTime().getMonthValue();
        List<TxLogErrorReasonDTO> resultList;
        List<ReportMonthErrorReasonDO> newDataList = new ArrayList<>();
        try {
            List<Long> allBankId = bankDao.findIdAll();
            for (final Long issuerBankId : allBankId) {
                resultList =
                  reportManager.statisticsErrorReason(issuerBankId, startMillis, endMillis);
                if (CollectionUtils.isEmpty(resultList)) {
                    resultList = new ArrayList<>();
                }
                // 若查詢結果<三筆，則填入空資料維持每間銀行的失敗原因最少都有三個
                for (int i = resultList.size(); i < 3; i++) {
                    resultList.add(TxLogErrorReasonDTO.newInstance(issuerBankId, 0, 0));
                }
                ReportMonthErrorReasonDO errorReason =
                  reportMonthErrorReasonDao
                    .findByIssuerBankIdAndYearAndMonth(issuerBankId, year, month)
                    .map(
                      monthErrorReason -> {
                          monthErrorReason.setSysUpdater(operator);
                          monthErrorReason.setUpdateMillis(now);
                          return monthErrorReason;
                      })
                    .orElse(
                      ReportMonthErrorReasonDO.newInstance(
                        issuerBankId,
                        queryTimeRange.getStartZonedDateTime().getYear(),
                        queryTimeRange
                          .getStartZonedDateTime()
                          .getMonthValue(),
                        queryTimeRange.getStartMillis(),
                        resultList.get(0).getErrorReasonCode(),
                        resultList.get(1).getErrorReasonCode(),
                        resultList.get(2).getErrorReasonCode(),
                        resultList.get(0).getErrorReasonCount(),
                        resultList.get(1).getErrorReasonCount(),
                        resultList.get(2).getErrorReasonCount(),
                        resultList.stream()
                          .mapToInt(
                            TxLogErrorReasonDTO
                              ::getErrorReasonCount)
                          .sum(),
                        operator,
                        now));
                newDataList.add(errorReason);
            }
            reportMonthErrorReasonDao.saveAll(newDataList);
            return true;
        } catch (Exception e) {
            log.error("[statisticsMonthErrorReason] unknown exception", e);
        }
        return false;
    }

    public QueryTimeRange getTodayQueryTimeRange() {
        ZonedDateTime now = ZonedDateTime.now(EnvironmentConstants.ACS_TIMEZONE_ID);
        ZonedDateTime startLocalDateTime =
          ZonedDateTime.of(
            now.getYear(),
            now.getMonthValue(),
            now.getDayOfMonth(),
            0,
            0,
            0,
            0,
            EnvironmentConstants.ACS_TIMEZONE_ID);
        ZonedDateTime endLocalDateTime = startLocalDateTime.plusDays(1).minusNanos(1);
        return new QueryTimeRange(now, startLocalDateTime, endLocalDateTime);
    }

    public QueryTimeRange convertToTimeRange(String dateText) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDateTime = LocalDate.parse(dateText, formatter);
        ZonedDateTime zStartLocalDateTime = startLocalDateTime.atStartOfDay(EnvironmentConstants.ACS_TIMEZONE_ID);
        ZonedDateTime zEndLocalDateTime = zStartLocalDateTime.plusDays(1).minusNanos(1);
        return new QueryTimeRange(
          ZonedDateTime.now(EnvironmentConstants.ACS_TIMEZONE_ID), zStartLocalDateTime, zEndLocalDateTime);
    }

    private ReportTxStatisticsDO appendDataForReportTxStatisticsDO(
      ReportTxStatisticsDO reportTxStatisticsDO, StatisticsTransactionStatusDTO resultDto) {
        reportTxStatisticsDO.setIssuerBankId(resultDto.getIssuerBankId());
        reportTxStatisticsDO.setTotal(
          StringUtils.isNumeric(resultDto.getTotal())
            ? Long.parseLong(resultDto.getTotal())
            : 0);
        reportTxStatisticsDO.setOtpCount(
          StringUtils.isNumeric(resultDto.getOtpCount())
            ? Long.parseLong(resultDto.getOtpCount())
            : 0);
        reportTxStatisticsDO.setNCount(
          StringUtils.isNumeric(resultDto.getN()) ? Integer.parseInt(resultDto.getN()) : 0);
        reportTxStatisticsDO.setACount(
          StringUtils.isNumeric(resultDto.getA()) ? Integer.parseInt(resultDto.getA()) : 0);
        reportTxStatisticsDO.setYCount(
          StringUtils.isNumeric(resultDto.getY()) ? Integer.parseInt(resultDto.getY()) : 0);
        reportTxStatisticsDO.setCyCount(
          StringUtils.isNumeric(resultDto.getCy()) ? Integer.parseInt(resultDto.getCy()) : 0);
        reportTxStatisticsDO.setCnCount(
          StringUtils.isNumeric(resultDto.getCn()) ? Integer.parseInt(resultDto.getCn()) : 0);
        reportTxStatisticsDO.setRCount(
          StringUtils.isNumeric(resultDto.getR()) ? Integer.parseInt(resultDto.getR()) : 0);
        reportTxStatisticsDO.setUCount(
          StringUtils.isNumeric(resultDto.getU()) ? Integer.parseInt(resultDto.getU()) : 0);
        return reportTxStatisticsDO;
    }

    private ReportTxStatisticsDetailDO appendDataForReportTxStatisticsDetailDO(
      ReportTxStatisticsDetailDO reportTxStatisticsDetailDO,
      CardBrandTransStatusDTO cardBrandTransStatusDetailDto,
      Double nRate,
      Double uRate) {

        CardBrandTransStatusDTO.TransStatus aRes = cardBrandTransStatusDetailDto.getARes();
        reportTxStatisticsDetailDO.setAresYCount(aRes.getY());
        reportTxStatisticsDetailDO.setAresNCount(aRes.getN());
        reportTxStatisticsDetailDO.setAresUCount(aRes.getU());
        reportTxStatisticsDetailDO.setAresACount(aRes.getA());
        reportTxStatisticsDetailDO.setAresCCount(aRes.getC());
        reportTxStatisticsDetailDO.setAresRCount(aRes.getR());

        CardBrandTransStatusDTO.TransStatus rreq = cardBrandTransStatusDetailDto.getRReq();
        reportTxStatisticsDetailDO.setRreqYCount(rreq.getY());
        reportTxStatisticsDetailDO.setRreqNCount(rreq.getN());
        reportTxStatisticsDetailDO.setRreqUCount(rreq.getU());
        reportTxStatisticsDetailDO.setRreqACount(rreq.getA());
        reportTxStatisticsDetailDO.setRreqRCount(rreq.getR());

        reportTxStatisticsDetailDO.setNRate(nRate);
        reportTxStatisticsDetailDO.setURate(uRate);
        return reportTxStatisticsDetailDO;
    }
}
