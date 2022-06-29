package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.controller.request.ClassicRbaReportReqDto;
import com.cherri.acs_portal.dto.ApiPageResponse;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.rba.ClassicRbaReportDto;
import com.cherri.acs_portal.dto.report.*;
import com.cherri.acs_portal.facade.ClassicRbaFacade;
import com.cherri.acs_portal.facade.ReportFacade;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/auth/report")
public class ReportController extends ContextProvider {

  private final ReportFacade facade;
  private final ClassicRbaFacade classicRbaFacade;

  @Autowired
  public ReportController(ReportFacade facade, ClassicRbaFacade classicRbaFacade) {
    this.facade = facade;
    this.classicRbaFacade = classicRbaFacade;
  }

  /** 商店異常交易統計報表 */
  @PostMapping("/abnormal-transaction")
  @PreAuthorize(
      "hasRole('ROLE_RT_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ABNORMAL_TRANSACTION_LIST)
  public ApiPageResponse abnormalTransactionList(
      @Valid @RequestBody AbnormalTransactionQueryDTO queryDto) {
    Optional<PagingResultDTO<AbnormalTransactionResultDTO>> result =
        facade.findAbnormalTransactionReport(queryDto);
    return result.map(ApiPageResponse::new).orElse(new ApiPageResponse<>(new PagingResultDTO<>()));
  }

  /** 商店異常交易統計報表 - 匯出報表 */
  @PostMapping("/abnormal-transaction/export/xls")
  @PreAuthorize(
      "hasRole('ROLE_RT_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ABNORMAL_TRANSACTION_EXPORT)
  public void exportAbnormalTransactionReport(
      @Valid @RequestBody AbnormalTransactionQueryDTO queryDto) throws IOException {
    facade.downloadAbnormalTransaction(queryDto);
  }

  /** 人工彈性授權統計報表 */
  @PostMapping("/statistic-report/attempt-auth")
  @PreAuthorize(
      "hasRole('ROLE_RT_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.STATISTIC_REPORT_LIST)
  public ApiResponse statisticAttemptAuthList(@Valid @RequestBody ReportQueryDTO queryDto) {
    List<AttemptAuthorizeDTO> result = facade.findAttemptAuthReport(queryDto);
    return new ApiResponse<>(result);
  }

  /** 人工彈性授權統計報表 - 匯出報表 */
  @PostMapping("/statistic-report/attempt-auth/export/xls")
  @PreAuthorize(
      "hasRole('ROLE_RT_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.STATISTIC_REPORT_LIST)
  public void exportAttemptAuthReport(@Valid @RequestBody ReportQueryDTO queryDto)
      throws IOException {
    facade.exportAttemptAuthReportForXls(queryDto);
  }

  /** 各交易狀態統計數值報表 */
  @PostMapping("/statistic-report/transaction-status")
  @PreAuthorize(
      "hasRole('ROLE_RT_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.STATISTIC_REPORT_LIST)
  public ApiResponse transactionStatusList(@Valid @RequestBody ReportQueryDTO queryDto) {
    List<SimpleStatisticsTransactionStatusDTO> result = facade.findTransactionStatusReport(queryDto);
    return new ApiResponse<>(result);
  }

  /** 各交易狀態統計數值報表-detail 各銀行依 CardBrand統計 */
  @PostMapping("/statistic-report/transaction-status/detail")
  @Secured("ROLE_RT_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.STATISTIC_REPORT_LIST)
  public ApiResponse transactionStatusDetailList(@RequestBody ReportQueryDTO queryDto) {
    List<StatisticsTransactionStatusDetailDTO> result =
        facade.findTransactionStatusDetailReport(queryDto);
    return new ApiResponse<>(result);
  }

  /** 各交易狀態統計數值報表 - 匯出報表 */
  @PostMapping("/statistic-report/transaction-status/export/xls")
  @PreAuthorize(
      "hasRole('ROLE_RT_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.STATISTIC_REPORT_LIST)
  public void exportTransactionStatusDetailReport(@Valid @RequestBody ReportQueryDTO queryDto)
      throws IOException {
    facade.exportTransactionStatusDetailReportForXls(queryDto);
  }

  /** 失敗原因統計報表 */
  @PostMapping("/statistic-report/error-reason")
  @PreAuthorize(
      "hasRole('ROLE_RT_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.STATISTIC_REPORT_LIST)
  public ApiResponse statisticErrorReasonList(@Valid @RequestBody ReportQueryDTO queryDto) {
    List<StatisticsErrorReasonDTO> result = facade.findErrorReasonReport(queryDto);
    return new ApiResponse<>(result);
  }

  /** 失敗原因統計報表 - 匯出報表 */
  @PostMapping("/statistic-report/error-reason/export/xls")
  @PreAuthorize(
      "hasRole('ROLE_RT_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.STATISTIC_REPORT_LIST)
  public void exportStatisticErrorReasonReport(@Valid @RequestBody ReportQueryDTO queryDto)
      throws IOException {
    facade.exportErrorReasonReportForXls(queryDto);
  }

  /** 瀏覽器異常紀錄 */
  @Secured("ROLE_RT_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.BROWSER_ERROR_LOG)
  @PostMapping(value = "/browser-error-log")
  public ApiResponse getBrowserErrorLog(@Valid @RequestBody ReportQueryDTO queryDto) {
    List<BrowserErrorLogResultDTO> browserErrorLogResultDTOList =
        facade.getBrowserErrorLogList(queryDto);
    return new ApiResponse<>(browserErrorLogResultDTOList);
  }

  /** 匯出瀏覽器異常紀錄 - 匯出報表 */
  @PostMapping("/browser-error-log/export/xls")
  @PreAuthorize(
      "hasRole('ROLE_RT_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.ABNORMAL_TRANSACTION_EXPORT)
  public void exportBrowserErrorLogXls(@Valid @RequestBody ReportQueryDTO queryDto)
      throws IOException {
    facade.exportBrowserErrorLogXls(queryDto);
  }

  /** 取得目前的job名稱列表，此api功能不會被portal-UI使用 */
  @GetMapping("/job/list")
  @Secured("ROLE_ACCESS_MULTI_BANK") // 這裡的權限控管就不再多新增一個權限，擁有ACCESS_MULTI_BANK就可使用此API
  public ApiResponse jobNameList() {
    return new ApiResponse<>(facade.getAllJobNameList());
  }

  /** 手動觸發job，此api功能不會被portal-UI使用 */
  @GetMapping("/job/trigger/{jobName}")
  @Secured("ROLE_ACCESS_MULTI_BANK")
  public ApiResponse manualTriggerJobByJobName(
          @PathVariable String jobName,
          @RequestParam String dateText // Format: YYYY-MM-DD
  ) {
    facade.triggerJobByJobName(jobName, dateText);
    return ApiResponse.SUCCESS_API_RESPONSE;
  }

  @PostMapping("/classic-rba")
  @Secured("ROLE_CLASSIC_RBA_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.CLASSIC_RBA_LIST)
  public ApiResponse classicRbaReport(@Valid @RequestBody ClassicRbaReportReqDto queryDto) {
    return classicRbaFacade.getClassicRbaReport(queryDto)
            .map(ApiResponse::valueOf)
            .orElse(
                    ApiResponse.valueOf(
                            ClassicRbaReportDto.builder()
                            .classicRbaStatusList(Collections.emptyList()).build()));
  }

  @PostMapping("/classic-rba/xls")
  @Secured("ROLE_CLASSIC_RBA_QUERY")
  @AuditLogHandler(methodName = AuditLogMethodNameEnum.CLASSIC_RBA_LIST)
  public void exportClassicRbaReport(@Valid @RequestBody ClassicRbaReportReqDto queryDto) {
     classicRbaFacade.exportClassicRbaReport(queryDto);
  }
}
