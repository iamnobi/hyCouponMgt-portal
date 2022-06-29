package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.report.AbnormalTransactionQueryDTO;
import com.cherri.acs_portal.dto.report.AbnormalTransactionResultDTO;
import com.cherri.acs_portal.dto.report.AttemptAuthorizeDTO;
import com.cherri.acs_portal.dto.report.BrowserErrorLogResultDTO;
import com.cherri.acs_portal.dto.report.QueryTimeRange;
import com.cherri.acs_portal.dto.report.ReportQueryDTO;
import com.cherri.acs_portal.dto.report.SimpleStatisticsTransactionStatusDTO;
import com.cherri.acs_portal.dto.report.StatisticsErrorReasonDTO;
import com.cherri.acs_portal.dto.report.StatisticsTransactionStatusDTO;
import com.cherri.acs_portal.dto.report.StatisticsTransactionStatusDetailDTO;
import com.cherri.acs_portal.service.AbnormalTransactionJobService;
import com.cherri.acs_portal.service.BrowserAbnormalJobService;
import com.cherri.acs_portal.service.ReportService;
import com.cherri.acs_portal.service.StatisticsReportJobService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ReportFacade {

    private final ReportService reportService;
    private final StatisticsReportJobService statisticsReportJobService;
    private final BrowserAbnormalJobService browserAbnormalJobService;
    private final AbnormalTransactionJobService abnormalTransactionJobService;

    @Autowired
    public ReportFacade(
      ReportService reportService,
      StatisticsReportJobService statisticsReportJobService,
      BrowserAbnormalJobService browserAbnormalJobService,
      AbnormalTransactionJobService abnormalTransactionJobService) {
        this.reportService = reportService;
        this.statisticsReportJobService = statisticsReportJobService;
        this.browserAbnormalJobService = browserAbnormalJobService;
        this.abnormalTransactionJobService = abnormalTransactionJobService;
    }

    public Optional<PagingResultDTO<AbnormalTransactionResultDTO>> findAbnormalTransactionReport(
      AbnormalTransactionQueryDTO queryDto) {
        return reportService.findAbnormalTransactionReport(queryDto);
    }

    public void downloadAbnormalTransaction(AbnormalTransactionQueryDTO queryDto)
      throws IOException {
        try {
            reportService.exportAbnormalTransaction(queryDto);
        } catch (IOException e) {
            log.error("[downloadAbnormalTransaction] execute failed.");
            throw e;
        }
    }

    public List<AttemptAuthorizeDTO> findAttemptAuthReport(ReportQueryDTO queryDto) {
        return reportService.findAttemptAuthReportByCreateMillis(queryDto);
    }

    public void exportAttemptAuthReportForXls(ReportQueryDTO queryDto) throws IOException {
        try {
            reportService.exportAttemptAuthReportXls(queryDto);
        } catch (IOException e) {
            log.error("[exportAttemptAuthReportForXls] execute failed.");
            throw e;
        }
    }

    public List<StatisticsErrorReasonDTO> findErrorReasonReport(ReportQueryDTO queryDto) {
        return reportService.findErrorReasonReport(queryDto);
    }

    public void exportErrorReasonReportForXls(ReportQueryDTO queryDto) throws IOException {
        try {
            reportService.exportErrorReasonReportXls(queryDto);
        } catch (IOException e) {
            log.error("[exportErrorReasonReportForXls] execute failed.");
            throw e;
        }
    }

    public List<SimpleStatisticsTransactionStatusDTO> findTransactionStatusReport(
      ReportQueryDTO queryDto) {
        List<SimpleStatisticsTransactionStatusDTO> resultList =
          reportService.findTransactionStatusReport(queryDto);
        return resultList;
    }

    public List<StatisticsTransactionStatusDetailDTO> findTransactionStatusDetailReport(
      ReportQueryDTO queryDto) {
        List<StatisticsTransactionStatusDetailDTO> resultList =
          reportService.findTransactionStatusDetailReportByDataMillis(queryDto);
        return resultList;
    }

    public void exportTransactionStatusDetailReportForXls(ReportQueryDTO queryDto)
      throws IOException {
        try {
            reportService.exportTransactionStatusReportXls(queryDto);
        } catch (IOException e) {
            log.error("[exportTransactionStatusDetailReportForXls] execute failed.");
            throw e;
        }
    }

    public List<BrowserErrorLogResultDTO> getBrowserErrorLogList(ReportQueryDTO queryDto) {
        return reportService.getBrowserErrorLogList(queryDto);
    }

    public void exportBrowserErrorLogXls(ReportQueryDTO queryDto) throws IOException {
        try {
            reportService.exportBrowserErrorLogXls(queryDto);
        } catch (IOException e) {
            log.error("[exportBrowserErrorLogXls] execute failed.");
            throw e;
        }
    }

    public List<String> getAllJobNameList() throws OceanException {
        try {
            return reportService.getAllJobNameList();
        } catch (Exception e) {
            log.error("[getAllJobNameList] Get all job name error", e);
            throw new OceanException(ResultStatus.SERVER_ERROR, "Get all job name error.");
        }
    }

    public void triggerJobByJobName(String jobName, String dateText) throws OceanException {
        QueryTimeRange queryTimeRange;
        if (null == dateText) {
            queryTimeRange = statisticsReportJobService.getTodayQueryTimeRange();
        } else {
            queryTimeRange = statisticsReportJobService.convertToTimeRange(dateText);
        }
        final String operator = "manualTriggerJob";
        boolean isExecuteSuccess = false;
        log.debug(
          "Job is running, jobName={}, startDate={}, endDate={}",
          StringUtils.normalizeSpace(jobName),
          queryTimeRange.getStartZonedDateTime(),
          queryTimeRange.getEndZonedDateTime());
        switch (jobName) {
            case "Job_Error_Reason_Daily":
                isExecuteSuccess =
                  statisticsReportJobService.statisticsDailyErrorReason(queryTimeRange, operator);
                break;
            case "Job_Error_Reason_Month":
                isExecuteSuccess =
                  statisticsReportJobService.statisticsMonthErrorReason(queryTimeRange, operator);
                break;
            case "Job_Transaction_Status_Detail":
                isExecuteSuccess =
                  statisticsReportJobService.statisticsTransactionStatusDetail(
                    queryTimeRange, operator);
                break;
            case "Job_System_Health":
                throw new OceanException(
                  ResultStatus.UNKNOWN, "Trigger job:" + jobName + " is not supported.");
            case "Job_Attempt_Authorize":
                isExecuteSuccess =
                  statisticsReportJobService.statisticsAttemptAuth(queryTimeRange, operator);
                break;
            case "Job_Browser_Error_Log":
                isExecuteSuccess = browserAbnormalJobService.staticBrowserErrorLog(operator);
                break;
            case "Job_Transaction_Status":
                isExecuteSuccess =
                  statisticsReportJobService.statisticsTransactionStatus(queryTimeRange, operator);
                break;
            case "Job_Abnormal_Transaction":
                isExecuteSuccess =
                  abnormalTransactionJobService.statisticsAbnormalTransaction(
                    queryTimeRange, operator);
                abnormalTransactionJobService.statisticsAbnormalTransactionMonthly(
                    queryTimeRange.getStartZonedDateTime().getYear(),
                    queryTimeRange.getStartZonedDateTime().getMonthValue(), operator);
                break;
            default:
                break;
        }
        log.debug("[triggerJobByJobName] Job done, jobName={}, result={}",
            StringUtils.normalizeSpace(jobName),
            isExecuteSuccess
        );
        if (!isExecuteSuccess) {
            throw new OceanException(ResultStatus.SERVER_ERROR,
              "Trigger job:" + jobName + " error.");
        }
    }
}
