package com.cherri.acs_portal.service;

import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.dto.report.SimpleStatisticsTransactionStatusDTO;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.report.AbnormalTransactionQueryDTO;
import com.cherri.acs_portal.dto.report.AbnormalTransactionResultDTO;
import com.cherri.acs_portal.dto.report.AttemptAuthorizeDTO;
import com.cherri.acs_portal.dto.report.BrowserErrorLogResultDTO;
import com.cherri.acs_portal.dto.report.CardBrandTransactionRateKpiDTO;
import com.cherri.acs_portal.dto.report.ReportAttemptDTO;
import com.cherri.acs_portal.dto.report.ReportQueryDTO;
import com.cherri.acs_portal.dto.report.ReportTxStatisticsDTO;
import com.cherri.acs_portal.dto.report.StatisticsErrorReasonDTO;
import com.cherri.acs_portal.dto.report.StatisticsTransactionStatusDTO;
import com.cherri.acs_portal.dto.report.StatisticsTransactionStatusDetailDTO;
import com.cherri.acs_portal.dto.report.StatisticsTransactionStatusReportDTO;
import com.cherri.acs_portal.dto.report.StatisticsTransactionStatusReportDTO.CardBrandReport;
import com.cherri.acs_portal.dto.system.CardBrandDTO;
import com.cherri.acs_portal.util.ExcelBuildUtils;
import com.cherri.acs_portal.util.ReportUtil;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.AbnormalTransactionDAO;
import ocean.acs.models.dao.BrowserErrorLogDAO;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.dao.ReportAttemptDAO;
import ocean.acs.models.dao.ReportDailyErrorReasonDAO;
import ocean.acs.models.dao.ReportMonthErrorReasonDAO;
import ocean.acs.models.dao.ReportTxStatisticsDAO;
import ocean.acs.models.dao.ReportTxStatisticsDetailDAO;
import ocean.acs.models.data_object.entity.AbnormalTransactionDO;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.entity.ReportTxStatisticsDetailDO;
import ocean.acs.models.data_object.portal.AbnormalTransactionQueryDO;

@Log4j2
@Service
public class ReportService {

    private final IssuerBankDAO issuerBankDao;
    private final AbnormalTransactionDAO abnormalDao;
    private final ReportAttemptDAO reportAttemptDao;
    private final ReportTxStatisticsDAO reportTxStatisticsDao;
    private final ReportTxStatisticsDetailDAO reportTxStatisticsDetailDao;
    private final ReportDailyErrorReasonDAO reportDailyErrorReasonDao;
    private final ReportMonthErrorReasonDAO reportMonthErrorReasonDao;
    private final BrowserErrorLogDAO browserErrorLogDAO;
    private final CardBrandConfigurationHelper cardBrandConfigurationHelper;
    private final HttpServletResponse httpServletResponse;
    private final Scheduler scheduler;

    @Autowired
    public ReportService(
      IssuerBankDAO issuerBankDao,
      AbnormalTransactionDAO abnormalDao,
      ReportAttemptDAO reportAttemptDao,
      ReportTxStatisticsDAO reportTxStatisticsDao,
      ReportTxStatisticsDetailDAO reportTxStatisticsDetailDao,
      ReportDailyErrorReasonDAO reportDailyErrorReasonDao,
      ReportMonthErrorReasonDAO reportMonthErrorReasonDao,
      BrowserErrorLogDAO browserErrorLogDAO,
      CardBrandConfigurationHelper cardBrandConfigurationHelper,
      HttpServletResponse httpServletResponse,
      Scheduler scheduler) {
        this.issuerBankDao = issuerBankDao;
        this.abnormalDao = abnormalDao;
        this.reportAttemptDao = reportAttemptDao;
        this.reportTxStatisticsDao = reportTxStatisticsDao;
        this.reportTxStatisticsDetailDao = reportTxStatisticsDetailDao;
        this.reportDailyErrorReasonDao = reportDailyErrorReasonDao;
        this.reportMonthErrorReasonDao = reportMonthErrorReasonDao;
        this.browserErrorLogDAO = browserErrorLogDAO;
        this.cardBrandConfigurationHelper = cardBrandConfigurationHelper;
        this.httpServletResponse = httpServletResponse;
        this.scheduler = scheduler;
    }

    public Optional<PagingResultDTO<AbnormalTransactionResultDTO>> findAbnormalTransactionReport(
      AbnormalTransactionQueryDTO queryDto) {
        AbnormalTransactionQueryDO queryDO = new AbnormalTransactionQueryDO(
                queryDto.getIssuerBankId(), queryDto.getYear(), queryDto.getMonth(), 
                queryDto.getMerchantId(), queryDto.getMerchantName(), queryDto.getPage(), 
                queryDto.getPageSize());
        Optional<Page<AbnormalTransactionDO>> opt = abnormalDao.query(queryDO);
        if (opt.isPresent()) {
            Page<AbnormalTransactionDO> page = opt.get();
            List<AbnormalTransactionResultDTO> dtoData =
              page.getContent().stream()
                .map(AbnormalTransactionResultDTO::valueOf)
                .collect(Collectors.toList());
            PagingResultDTO<AbnormalTransactionResultDTO> result = PagingResultDTO.valueOf(page);
            result.setData(dtoData);
            return Optional.of(result);
        }
        log.debug("[findAbnormalTransactionReport] abnormal transaction not found");
        return Optional.empty();
    }

    public void exportAbnormalTransaction(AbnormalTransactionQueryDTO queryDto) throws IOException {
        queryDto.setPage(1);
        queryDto.setPageSize(EnvironmentConstants.PAGINATION_MAX_ROWS);
        Optional<PagingResultDTO<AbnormalTransactionResultDTO>> opt =
          findAbnormalTransactionReport(queryDto);

        List<AbnormalTransactionResultDTO> dataList;
        if (opt.isPresent()) {
            dataList = opt.get().getData();
        } else {
            dataList = Collections.emptyList();
        }
        String fileName = ExcelBuildUtils.getFileNameFormat(MessageConstants.get(MessageConstants.EXCEL_MERCHANT_EXCEPTION_TRANSACTION));
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();

            final List<String> header = Arrays.asList(
                MessageConstants.get(MessageConstants.EXCEL_MERCHANT_EXCEPTION_TRANSACTION_MERCHANT_ID),
                MessageConstants.get(MessageConstants.EXCEL_MERCHANT_EXCEPTION_TRANSACTION_MERCHANT_NAME),
                "U-Rate",
                "N-Rate");
            ExcelBuildUtils.createHeader(workbook, sheet, header);

            // Create data body
            for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
                Row row = sheet.createRow(rowNum);
                AbnormalTransactionResultDTO abnormalDto = dataList.get(rowNum - 1);
                row.createCell(0).setCellValue(abnormalDto.getMerchantID());
                row.createCell(1).setCellValue(abnormalDto.getMerchantName());
                row.createCell(2).setCellValue(abnormalDto.getURate());
                row.createCell(3).setCellValue(abnormalDto.getNRate());
            }
            ExcelBuildUtils.resizeColums(sheet, header.size());
            ExcelBuildUtils.doExport(httpServletResponse, fileName, workbook);
        } catch (Exception e) {
            log.error("[exportAbnormalTransaction] unknown exception, request params={}", queryDto);
            throw e;
        }
    }

    public List<AttemptAuthorizeDTO> findAttemptAuthReportByCreateMillis(ReportQueryDTO queryDto)
      throws IllegalArgumentException {
        try {
            List<ReportAttemptDTO> reportAttemptList = null;
            if (queryDto.isMonthlyReport()) { // 查詢月報表
                if (EnvironmentConstants.ORG_ISSUER_BANK_ID == queryDto.getIssuerBankId()) {
                    reportAttemptList = reportAttemptDao.findByYearAndMonth(
                            queryDto.getYear().intValue(), queryDto.getMonth().intValue()).stream()
                            .map(ReportAttemptDTO::valueOf).collect(Collectors.toList());
                } else {
                    reportAttemptList = reportAttemptDao.findByIssuerBankIdAndYearAndMonth(
                            queryDto.getIssuerBankId().longValue(), queryDto.getYear().intValue(), 
                            queryDto.getMonth().intValue()).stream().map(ReportAttemptDTO::valueOf)
                            .collect(Collectors.toList());
                }
            } else { // 查詢日報表
                if (EnvironmentConstants.ORG_ISSUER_BANK_ID == queryDto.getIssuerBankId()) {
                    reportAttemptList = reportAttemptDao.findByYearAndMonthAndDay(
                            queryDto.getYear().intValue(), queryDto.getMonth().intValue(), 
                            queryDto.getDay().intValue()).stream().map(ReportAttemptDTO::valueOf)
                            .collect(Collectors.toList());
                } else {
                    reportAttemptList = reportAttemptDao.findByIssuerBankIdAndYearAndMonthAndDay(
                            queryDto.getIssuerBankId().longValue(), queryDto.getYear().intValue(), 
                            queryDto.getMonth().intValue(), queryDto.getDay().intValue()).stream()
                            .map(ReportAttemptDTO::valueOf).collect(Collectors.toList());
                }
            }

            List<IssuerBankDO> allBank = issuerBankDao.findAll();
            if (queryDto.isMonthlyReport()) { // 查詢月報表
                reportAttemptList = doStatisticsAttemptForMonthReport(allBank, reportAttemptList);
            } else { // 查詢日報表
                // nothing...
            }

            /** 彈性授權次數(%) */
            List<AttemptAuthorizeDTO> resultList = new ArrayList<>();
            for (ReportAttemptDTO reportAttempt : reportAttemptList) {
                Long issuerBankId = reportAttempt.getIssuerBankId();
                String issuerBankName = getIssuerBankName(allBank, issuerBankId);
                String percentageStr = formatPercentage(reportAttempt.getPercentage()) + "%";
                AttemptAuthorizeDTO dto =
                  new AttemptAuthorizeDTO(
                    issuerBankName,
                    reportAttempt.getYear(),
                    reportAttempt.getMonth(),
                    reportAttempt.getDayOfMonth(),
                    reportAttempt.getPermittedCount(),
                    reportAttempt.getRealTriesCount(),
                    percentageStr);
                resultList.add(dto);
            }
            return resultList;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    private String getIssuerBankName(List<IssuerBankDO> allBank, Long issuerBankId) {
        return allBank.stream()
          .filter(bank -> issuerBankId.equals(bank.getId()))
          .findFirst()
          .map(IssuerBankDO::getName)
          .orElse("N/A");
    }

    private double formatPercentage(Double percentage) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(percentage));
    }

    public List<SimpleStatisticsTransactionStatusDTO> findTransactionStatusReport(
      ReportQueryDTO queryDto) {
        try {
            List<ReportTxStatisticsDTO> reportAttemptList = null;
            if (queryDto.isMonthlyReport()) { // 查詢月報表
                reportAttemptList = reportTxStatisticsDao.findByYearAndMonth(
                    queryDto.getYear().intValue(), queryDto.getMonth().intValue()).stream()
                    .map(ReportTxStatisticsDTO::valueOf).collect(Collectors.toList());
            } else { // 查詢日報表
                reportAttemptList = reportTxStatisticsDao.findByYearAndMonthAndDay(
                    queryDto.getYear().intValue(), queryDto.getMonth().intValue(),
                    queryDto.getDay().intValue()).stream()
                    .map(ReportTxStatisticsDTO::valueOf).collect(Collectors.toList());
            }
            log.debug("[findTransactionStatusReport] daily data size={}", reportAttemptList.size());

            List<IssuerBankDO> allBank = issuerBankDao.findAll();
            if (queryDto.isMonthlyReport()) { // 查詢月報表
                reportAttemptList = doStatisticsTxStatusForMonthReport(allBank, reportAttemptList);
            } else { // 查詢日報表
                // nothing...
            }

            if (reportAttemptList.isEmpty()) {
                return Collections.emptyList();
            }

            List<SimpleStatisticsTransactionStatusDTO> result = reportAttemptList.stream()
              .map(SimpleStatisticsTransactionStatusDTO::valueOf)
              .peek(
                statisticsTransactionStatusDto -> {
                    Long issuerBankId = statisticsTransactionStatusDto.getIssuerBankId();
                    String issuerBankName = getIssuerBankName(allBank, issuerBankId);
                    statisticsTransactionStatusDto.setIssuerBankName(issuerBankName);
                })
              .collect(Collectors.toList());

            return result;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 各交易狀態統計月報表：將各銀行的"每日"統計結果，加總起來變成"月"統計結果
     */
    private List<ReportTxStatisticsDTO> doStatisticsTxStatusForMonthReport(
      List<IssuerBankDO> allBank, List<ReportTxStatisticsDTO> reportTxStatisticsList) {
        List<ReportTxStatisticsDTO> monthResultList = new ArrayList<>();

        for (IssuerBankDO issuerBank : allBank) {
            final Long issuerBankId = issuerBank.getId();
            boolean isExists =
              reportTxStatisticsList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .map(ReportTxStatisticsDTO::getIssuerBankId)
                .findFirst()
                .isPresent();
            if (!isExists) {
                continue;
            }
            final Integer year =
              reportTxStatisticsList.stream()
                .map(ReportTxStatisticsDTO::getYear)
                .distinct()
                .findFirst()
                .get();
            final Integer month =
              reportTxStatisticsList.stream()
                .map(ReportTxStatisticsDTO::getMonth)
                .distinct()
                .findFirst()
                .get();
            final long total =
              reportTxStatisticsList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .mapToLong(ReportTxStatisticsDTO::getTotal)
                .sum();
            final long otpCount =
              reportTxStatisticsList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .mapToLong(ReportTxStatisticsDTO::getOtpCount)
                .sum();
            final int nCount =
              reportTxStatisticsList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .mapToInt(ReportTxStatisticsDTO::getNCount)
                .sum();
            final int aCount =
              reportTxStatisticsList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .mapToInt(ReportTxStatisticsDTO::getACount)
                .sum();
            final int yCount =
              reportTxStatisticsList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .mapToInt(ReportTxStatisticsDTO::getYCount)
                .sum();
            final int cyCount =
              reportTxStatisticsList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .mapToInt(ReportTxStatisticsDTO::getCyCount)
                .sum();
            final int cnCount =
              reportTxStatisticsList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .mapToInt(ReportTxStatisticsDTO::getCnCount)
                .sum();
            final int rCount =
              reportTxStatisticsList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .mapToInt(ReportTxStatisticsDTO::getRCount)
                .sum();
            final int uCount =
              reportTxStatisticsList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .mapToInt(ReportTxStatisticsDTO::getUCount)
                .sum();
            ReportTxStatisticsDTO txStatistics = new ReportTxStatisticsDTO();
            txStatistics.setIssuerBankId(issuerBankId);
            txStatistics.setYear(year);
            txStatistics.setMonth(month);
            txStatistics.setTotal(total);
            txStatistics.setOtpCount(otpCount);
            txStatistics.setNCount(nCount);
            txStatistics.setACount(aCount);
            txStatistics.setYCount(yCount);
            txStatistics.setCyCount(cyCount);
            txStatistics.setCnCount(cnCount);
            txStatistics.setRCount(rCount);
            txStatistics.setUCount(uCount);
            monthResultList.add(txStatistics);
        }
        return monthResultList;
    }

    public List<StatisticsTransactionStatusDetailDTO> findTransactionStatusDetailReportByDataMillis(
      ReportQueryDTO queryDto) {
        List<ReportTxStatisticsDetailDO> reportTxStatisticsDetailList = null;
        if (queryDto.isMonthlyReport()) { // 查詢月報表
            reportTxStatisticsDetailList =
                reportTxStatisticsDetailDao
                    .statisticByYearAndMonth(queryDto.getYear(), queryDto.getMonth());
        } else { // 查詢日報表
            reportTxStatisticsDetailList =
                reportTxStatisticsDetailDao
                    .statisticByYearAndMonthAndDay(queryDto.getYear(), queryDto.getMonth(),
                        queryDto.getDay());
        }

        if (reportTxStatisticsDetailList.isEmpty()) {
            return Collections.emptyList();
        }

        // 計算各卡組織的N-Rate, U-Rate
        doStatisticsTxStatusDetail(reportTxStatisticsDetailList);

        // 添加 N-Rate和U-Rate KPI
        List<CardBrandDTO> cardBrandList = cardBrandConfigurationHelper.findCardBrandList();
        CardBrandTransactionRateKpiDTO cardBrandTransactionRateKpiDTO = cardBrandConfigurationHelper
          .findCardBrandTransactionRateKpi();
        List<StatisticsTransactionStatusDetailDTO> dtoList =
          reportTxStatisticsDetailList.stream()
            .map(reportTxStatisticsDetail ->
              StatisticsTransactionStatusDetailDTO
                .valueOf(reportTxStatisticsDetail, cardBrandTransactionRateKpiDTO))
            .sorted(Comparator.comparingLong(dto -> {
                CardBrandDTO cardBrandDTO = cardBrandList.stream()
                  .filter(cardBrand -> cardBrand.getName().equals(dto.getCardBrand()))
                  .findFirst()
                  .orElse(null);
                if (cardBrandDTO == null) {
                    return Long.MAX_VALUE;
                } else {
                    return cardBrandDTO.getDisplayOrder();
                }
            }))
            .collect(Collectors.toList());

        return dtoList;
    }

    /**
     * 依據日報表結果，重新計算各卡組織的N-Rate, U-Rate<br> N-Rate = [(ARes=N or R) + (RReq=N or R)] /
     * [(ARes=Y+N+A+U+R) + (RReq=Y+N+A+U+R)] <br> U-Rate = [(ARes=U) + (RReq=U)] / [(ARes=Y+N+A+U+R)
     * + (RReq=Y+N+A+U+R)]
     */
    private List<ReportTxStatisticsDetailDO> doStatisticsTxStatusDetail(
      List<ReportTxStatisticsDetailDO> reportTxStatisticsDetailList) {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        List<CardBrandDTO> cardBrandList = cardBrandConfigurationHelper.findCardBrandList();
        for (CardBrandDTO cardBrand : cardBrandList) {
            final long aresYcount =
              reportTxStatisticsDetailList.stream()
                .filter(e -> cardBrand.getName().equals(e.getCardBrand()))
                .mapToLong(ReportTxStatisticsDetailDO::getAresYCount)
                .sum();
            final long aresNcount =
              reportTxStatisticsDetailList.stream()
                .filter(e -> cardBrand.getName().equals(e.getCardBrand()))
                .mapToLong(ReportTxStatisticsDetailDO::getAresNCount)
                .sum();
            final long aresUcount =
              reportTxStatisticsDetailList.stream()
                .filter(e -> cardBrand.getName().equals(e.getCardBrand()))
                .mapToLong(ReportTxStatisticsDetailDO::getAresUCount)
                .sum();
            final long aresAcount =
              reportTxStatisticsDetailList.stream()
                .filter(e -> cardBrand.getName().equals(e.getCardBrand()))
                .mapToLong(ReportTxStatisticsDetailDO::getAresACount)
                .sum();
            final long aresRcount =
              reportTxStatisticsDetailList.stream()
                .filter(e -> cardBrand.getName().equals(e.getCardBrand()))
                .mapToLong(ReportTxStatisticsDetailDO::getAresRCount)
                .sum();

            final long rreqYcount =
              reportTxStatisticsDetailList.stream()
                .filter(e -> cardBrand.getName().equals(e.getCardBrand()))
                .mapToLong(ReportTxStatisticsDetailDO::getRreqYCount)
                .sum();
            final long rreqNcount =
              reportTxStatisticsDetailList.stream()
                .filter(e -> cardBrand.getName().equals(e.getCardBrand()))
                .mapToLong(ReportTxStatisticsDetailDO::getRreqNCount)
                .sum();
            final long rreqUcount =
              reportTxStatisticsDetailList.stream()
                .filter(e -> cardBrand.getName().equals(e.getCardBrand()))
                .mapToLong(ReportTxStatisticsDetailDO::getRreqUCount)
                .sum();
            final long rreqAcount =
              reportTxStatisticsDetailList.stream()
                .filter(e -> cardBrand.getName().equals(e.getCardBrand()))
                .mapToLong(ReportTxStatisticsDetailDO::getRreqACount)
                .sum();
            final long rreqRcount =
              reportTxStatisticsDetailList.stream()
                .filter(e -> cardBrand.getName().equals(e.getCardBrand()))
                .mapToLong(ReportTxStatisticsDetailDO::getRreqRCount)
                .sum();

            final long aresYNAUR = aresYcount + aresNcount + aresAcount + aresUcount + aresRcount;
            final long rreqYNAUR = rreqYcount + rreqNcount + rreqAcount + rreqUcount + rreqRcount;

            final Double nRate =
              ((double) ((aresNcount + aresRcount) + (rreqNcount + rreqRcount))
                / (aresYNAUR + rreqYNAUR))
                * 100;

            final Double uRate =
              ((double) (aresUcount + rreqUcount) / (aresYNAUR + rreqYNAUR)) * 100;

            reportTxStatisticsDetailList.stream()
              .filter(e -> cardBrand.getName().equals(e.getCardBrand()))
              .forEach(
                e -> {
                    if (nRate.isNaN()) {
                        e.setNRate(0.0);
                    } else {
                        e.setNRate(Double.parseDouble(decimalFormat.format(nRate)));
                    }
                    if (uRate.isNaN()) {
                        e.setURate(0.0);
                    } else {
                        e.setURate(Double.parseDouble(decimalFormat.format(uRate)));
                    }
                });
        }
        return reportTxStatisticsDetailList;
    }

    /**
     * 匯出各交易狀態統計報表 - excel
     */
    public void exportTransactionStatusReportXls(ReportQueryDTO queryDto) throws IOException {
        String fileName = ExcelBuildUtils.getFileNameFormat(
            MessageConstants.get(MessageConstants.EXCEL_TX_STATUS));

        try {
            List<SimpleStatisticsTransactionStatusDTO> transStatusDataList =
              findTransactionStatusReport(queryDto);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();

            List<String> header;
            if (queryDto.isMonthlyReport()) {
                header = new ArrayList<>(ReportUtil.getTxStatusMonthlyReportColumn());
            } else {
                header = new ArrayList<>(ReportUtil.getTxStatusDailyReportColumn());
            }

            ExcelBuildUtils.createHeader(workbook, sheet, header);

            // Create data body
            for (int rowNum = 1; rowNum <= transStatusDataList.size(); rowNum++) {
                Row row = sheet.createRow(rowNum);
                SimpleStatisticsTransactionStatusDTO dto = transStatusDataList.get(rowNum - 1);
                // 交易狀態統計報表
                int columnIndex = 0;
                row.createCell(columnIndex).setCellValue(dto.getIssuerBankName());
                row.createCell(++columnIndex).setCellValue(dto.getYear());
                row.createCell(++columnIndex).setCellValue(dto.getMonth());
                if (!queryDto.isMonthlyReport()) {
                    row.createCell(++columnIndex).setCellValue(dto.getDay());
                }
                row.createCell(++columnIndex).setCellValue(dto.getTotal());
                // row.createCell(++columnIndex).setCellValue(dto.getOtpCount());
                row.createCell(++columnIndex).setCellValue(dto.getN());
                // row.createCell(++columnIndex).setCellValue(dto.getA());
                row.createCell(++columnIndex).setCellValue(dto.getY());
                row.createCell(++columnIndex).setCellValue(dto.getR());
                row.createCell(++columnIndex).setCellValue(dto.getU());
                row.createCell(++columnIndex).setCellValue(dto.getURate());
                row.createCell(++columnIndex).setCellValue(dto.getNRate());
            }
            ExcelBuildUtils.resizeColums(sheet, header.size());
            ExcelBuildUtils.doExport(httpServletResponse, fileName, workbook);
        } catch (Exception e) {
            log
              .error("[exportErrorReasonReportXls] unknown exception, request params={}",
                  StringUtils.normalizeSpace(queryDto.toString()));
            throw e;
        }
    }

    /**
     * 人工彈性授權月報表：將各銀行的"每日"統計結果，加總起來變成"月"統計結果
     */
    private List<ReportAttemptDTO> doStatisticsAttemptForMonthReport(
      List<IssuerBankDO> allBank, List<ReportAttemptDTO> reportAttemptList) {
        List<ReportAttemptDTO> monthResultList = new ArrayList<>();

        for (IssuerBankDO issuerBank : allBank) {
            final Long issuerBankId = issuerBank.getId();
            boolean isExists =
              reportAttemptList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .map(ReportAttemptDTO::getIssuerBankId)
                .findFirst()
                .isPresent();
            if (!isExists) {
                continue;
            }
            final Integer year =
              reportAttemptList.stream().map(ReportAttemptDTO::getYear).distinct().findFirst()
                .get();
            final Integer month =
              reportAttemptList.stream().map(ReportAttemptDTO::getMonth).distinct().findFirst()
                .get();
            // 開通次數
            final int permittedCount =
              reportAttemptList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .mapToInt(ReportAttemptDTO::getPermittedCount)
                .sum();
            // 授權次數
            final int realTriesCount =
              reportAttemptList.stream()
                .filter(txStatistics -> txStatistics.getIssuerBankId().equals(issuerBankId))
                .mapToInt(ReportAttemptDTO::getRealTriesCount)
                .sum();
            // 使用彈性授權的比例
            double percentage = (double) realTriesCount / permittedCount;
            if (Double.isNaN(percentage)) {
                percentage = 0.0;
            } else {
                DecimalFormat decimalFormat = new DecimalFormat("#.###");
                percentage = Double.parseDouble(decimalFormat.format(percentage)) * 100;
            }
            ReportAttemptDTO reportAttempt = new ReportAttemptDTO();
            reportAttempt.setIssuerBankId(issuerBankId);
            reportAttempt.setYear(year);
            reportAttempt.setMonth(month);
            reportAttempt.setPermittedCount(permittedCount);
            reportAttempt.setRealTriesCount(realTriesCount);
            reportAttempt.setPercentage(percentage);
            monthResultList.add(reportAttempt);
        }
        return monthResultList;
    }

    /**
     * 匯出人工彈性授權統計報表 - excel
     */
    public void exportAttemptAuthReportXls(ReportQueryDTO queryDto) throws IOException {
        String fileName = ExcelBuildUtils.getFileNameFormat(MessageConstants.get(MessageConstants.EXCEL_ATTEMPT));
        try {
            List<AttemptAuthorizeDTO> dataList = findAttemptAuthReportByCreateMillis(queryDto);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();

            final List<String> header = Arrays.asList(
                MessageConstants.get(MessageConstants.EXCEL_BANK_NAME),
                MessageConstants.get(MessageConstants.EXCEL_YEAR),
                MessageConstants.get(MessageConstants.EXCEL_MONTH),
                MessageConstants.get(MessageConstants.EXCEL_DAY),
                MessageConstants.get(MessageConstants.EXCEL_ATTEMPT_ATTEMPT_TIMES),
                MessageConstants.get(MessageConstants.EXCEL_ATTEMPT_AUTHORIZATION_TIMES));
            ExcelBuildUtils.createHeader(workbook, sheet, header);

            // Create data body
            for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
                Row row = sheet.createRow(rowNum);
                AttemptAuthorizeDTO dto = dataList.get(rowNum - 1);
                row.createCell(0).setCellValue(dto.getIssuerBankName());
                row.createCell(1).setCellValue(dto.getYear());
                row.createCell(2).setCellValue(dto.getMonth());
                String dayText =
                  dto.getDayOfMonth() == null ? "" : String.valueOf(dto.getDayOfMonth());
                row.createCell(3).setCellValue(dayText);

                String authCountText = String
                  .format("%d (%s)", dto.getRealTriesCount(), dto.getStatistics());
                row.createCell(4).setCellValue(dto.getPermittedCount());
                row.createCell(5).setCellValue(authCountText);
            }
            ExcelBuildUtils.resizeColums(sheet, header.size());
            ExcelBuildUtils.doExport(httpServletResponse, fileName, workbook);
        } catch (IOException e) {
            log.error("[exportAttemptAuthReportXls] export file failed, request params={}",
                StringUtils.normalizeSpace(queryDto.toString()));
            throw e;
        }
    }

    /**
     * 查詢失敗原因統計報表
     */
    public List<StatisticsErrorReasonDTO> findErrorReasonReport(ReportQueryDTO queryDto)
      throws IllegalArgumentException {
        try {
            if (queryDto.isMonthlyReport()) { // 查詢月報表
                if (EnvironmentConstants.ORG_ISSUER_BANK_ID == queryDto.getIssuerBankId()) {
                    return reportMonthErrorReasonDao.findByYearAndMonth(
                            queryDto.getYear().intValue(), queryDto.getMonth().intValue()).stream()
                            .map(StatisticsErrorReasonDTO::valueOf).collect(Collectors.toList());
                } else {
                    return reportMonthErrorReasonDao.findByIssuerBankIdAndYearAndMonth(
                            queryDto.getIssuerBankId().longValue(), queryDto.getYear().intValue(), 
                            queryDto.getMonth().intValue()).stream()
                            .map(StatisticsErrorReasonDTO::valueOf).collect(Collectors.toList());
                }
            } else { // 查詢日報表
                if (EnvironmentConstants.ORG_ISSUER_BANK_ID == queryDto.getIssuerBankId()) {
                    return reportDailyErrorReasonDao.findByYearAndMonthAndDay(
                            queryDto.getYear().intValue(), queryDto.getMonth().intValue(), 
                            queryDto.getDay().intValue()).stream()
                            .map(StatisticsErrorReasonDTO::valueOf).collect(Collectors.toList());
                } else {
                    return reportDailyErrorReasonDao.findByIssuerBankIdAndYearAndMonthAndDay(
                            queryDto.getIssuerBankId().longValue(), queryDto.getYear().intValue(), 
                            queryDto.getMonth().intValue(), queryDto.getDay().intValue()).stream()
                            .map(StatisticsErrorReasonDTO::valueOf).collect(Collectors.toList());
                }
            }
        } catch (Exception e) {
            throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    /**
     * 匯出失敗原因統計報表 - excel
     */
    public void exportErrorReasonReportXls(ReportQueryDTO queryDto) throws IOException {
        String fileName = ExcelBuildUtils.getFileNameFormat("error_reason");
        try {
            List<StatisticsErrorReasonDTO> dataList = findErrorReasonReport(queryDto);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            List<String> header = Arrays.asList("銀行名稱", "年", "月", "日", "原因一", "原因二", "原因三");
            ExcelBuildUtils.createHeader(workbook, sheet, header);

            // Create data body
            for (int rowNum = 1; rowNum <= dataList.size(); rowNum++) {
                Row row = sheet.createRow(rowNum);
                StatisticsErrorReasonDTO dto = dataList.get(rowNum - 1);
                row.createCell(0).setCellValue(dto.getIssuerBankName());
                row.createCell(1).setCellValue(dto.getYear());
                row.createCell(2).setCellValue(dto.getMonth());
                String dayText = dto.getDay() == null ? "" : String.valueOf(dto.getDay());
                row.createCell(3).setCellValue(dayText);

                String reason1 = String
                  .format("%s (%s)", dto.getReason1(), dto.getReasonPercentage1());
                String reason2 = String
                  .format("%s (%s)", dto.getReason2(), dto.getReasonPercentage2());
                String reason3 = String
                  .format("%s (%s)", dto.getReason3(), dto.getReasonPercentage3());
                row.createCell(4).setCellValue(reason1);
                row.createCell(5).setCellValue(reason2);
                row.createCell(6).setCellValue(reason3);
            }
            ExcelBuildUtils.resizeColums(sheet, header.size());
            ExcelBuildUtils.doExport(httpServletResponse, fileName, workbook);
        } catch (Exception e) {
            log.error("[exportErrorReasonReportXls] export fail failed, request params={}",
                StringUtils.normalizeSpace(queryDto.toString()));
            throw e;
        }
    }

    /**
     * 查詢瀏覽器異常紀錄清單
     */
    public List<BrowserErrorLogResultDTO> getBrowserErrorLogList(ReportQueryDTO queryDto)
      throws IllegalArgumentException {
        try {
            if (queryDto.isMonthlyReport()) { // 查詢月報表
                return browserErrorLogDAO.findByIssuerBankIdAndYearAndMonth(
                        queryDto.getIssuerBankId().longValue(), queryDto.getYear().intValue(), 
                        queryDto.getMonth().intValue()).stream()
                        .map(BrowserErrorLogResultDTO::valueOf).collect(Collectors.toList());
            } else { // 查詢日報表
                return browserErrorLogDAO.findByIssuerBankIdAndYearAndMonthAndDay(
                        queryDto.getIssuerBankId().longValue(), queryDto.getYear().intValue(), 
                        queryDto.getMonth().intValue(), queryDto.getDay().intValue()).stream()
                        .map(BrowserErrorLogResultDTO::valueOf).collect(Collectors.toList());
            }
        } catch (DateTimeParseException e) {
            log.error("[getBrowserErrorLogList] parse query date string error, request params={}",
              StringUtils.normalizeSpace(queryDto.toString()));
            throw e;
        }
    }

    /**
     * 匯出瀏覽器異常紀錄CSV
     */
    public void exportBrowserErrorLogXls(ReportQueryDTO queryDto) throws IOException {

        List<BrowserErrorLogResultDTO> browserErrorLogResultDtoList = getBrowserErrorLogList(
          queryDto);
        String fileName = ExcelBuildUtils.getFileNameFormat("browser_error_log");

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();
            final List<String> header =
              Arrays.asList(
                "銀行ID",
                "瀏覽器類型",
                "年",
                "月",
                "日",
                "Client端發起第一次驗證(Challenge)請求逾時",
                "使用者驗證(challenge)操作逾時",
                "CReq傳入無效的參數",
                "驗證(Challenge)時系統異常");

            ExcelBuildUtils.createHeader(workbook, sheet, header);

            // Create data body
            for (int rowNum = 1; rowNum <= browserErrorLogResultDtoList.size(); rowNum++) {
                Row row = sheet.createRow(rowNum);
                BrowserErrorLogResultDTO dto = browserErrorLogResultDtoList.get(rowNum - 1);
                row.createCell(0).setCellValue(dto.getIssuerBankId());
                row.createCell(1).setCellValue(dto.getBrowserType());
                row.createCell(2).setCellValue(dto.getYear());
                row.createCell(3).setCellValue(dto.getMonth());
                String dayText =
                  dto.getDay() == null || dto.getDay() == 0 ? "" : String.valueOf(dto.getDay());
                row.createCell(4).setCellValue(dayText);
                row.createCell(5).setCellValue(dto.getClientFirstChallengeRequestTimeoutCount());
                row.createCell(6).setCellValue(dto.getChallengeOperationTimeoutCount());
                row.createCell(7).setCellValue(dto.getCreqInvalidArgsCount());
                row.createCell(8).setCellValue(dto.getChallengeSystemAbnormalCount());
            }
            ExcelBuildUtils.resizeColums(sheet, header.size());
            ExcelBuildUtils.doExport(httpServletResponse, fileName, workbook);
        } catch (Exception e) {
            log.error("[exportBrowserErrorLogXls] export file failed, request params={}",
                StringUtils.normalizeSpace(queryDto.toString()));
            throw e;
        }
    }

    public List<String> getAllJobNameList() throws SchedulerException {
        List<String> jobGroupNames = scheduler.getJobGroupNames();
        List<String> resultList = new ArrayList<>();
        if (jobGroupNames == null | scheduler.getJobGroupNames().isEmpty()) {
            return resultList;
        }
        for (String groupName : jobGroupNames) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                resultList.add(jobKey.getName());
            }
        }
        return resultList;
    }
}
