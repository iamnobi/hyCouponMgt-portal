package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.controller.request.TxSummaryReqDTO;
import com.cherri.acs_portal.controller.response.SmsRecordDTO;
import com.cherri.acs_portal.dto.ApiPageResponse;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.common.IdsQueryDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogHeaderDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogHeaderQueryDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogHeaderQueryExportDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogSummaryDTO;
import com.cherri.acs_portal.facade.TransactionLogFacade;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/auth/transaction")
public class TransactionLogController extends ContextProvider {

    private static final int UUID_LENGTH = 36;

    private TransactionLogFacade facade;

    @Autowired
    public TransactionLogController(TransactionLogFacade facade) {
        this.facade = facade;
    }

    /**
     * 查詢交易紀錄
     */
    @PostMapping("/record")
    @PreAuthorize(
      "hasRole('ROLE_TX_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.TRANSACTION_LOG_LIST)
    public ApiPageResponse logs(@Valid @RequestBody TxLogHeaderQueryDTO queryDto) {
        PagingResultDTO<TxLogHeaderDTO> logResult = facade.getLogs(queryDto, canSeePan());
        return new ApiPageResponse<>(logResult);
    }

    /**
     * 查詢交易紀錄摘要
     */
    @PostMapping("/record/summary")
    @PreAuthorize(
      "hasRole('ROLE_TX_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.TRANSACTION_LOG_SUMMARY_GET)
    public ApiResponse<TxLogSummaryDTO> summary(@RequestBody TxSummaryReqDTO queryDto) {
        if (queryDto == null) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "Incorrect request arguments");
        }
        return new ApiResponse<>(facade.getSummary(queryDto).orElse(new TxLogSummaryDTO()));
    }

    /**
     * 查詢交易紀錄詳細交易資料
     */
    @GetMapping("/record/detail/{transactionLogId}")
    @Secured("ROLE_TX_QUERY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.TRANSACTION_LOG_DETAIL_GET)
    public ApiResponse detail(@PathVariable Long transactionLogId) {
        return facade
          .getTransactionLogDetail(transactionLogId)
          .map(ApiResponse::valueOf)
          .orElse(
            new ApiResponse(ResultStatus.SERVER_ERROR, "Failed in query transaction-log-detail."));
    }

    /**
     * 簡訊紀錄查詢<br> 以交易紀錄的acsIntegratorTransId (KERNEL_TRANSACTION_LOG.ACS_INTEGRATOR_TRANS_ID) <br>
     * 至ACS Integrator的integrator_transaction_log.acs_kernel_trans_id查integrator_transaction_log.id，
     * <br>
     * 再以integrator_transaction_log.id去查otp_generated_log.fisc_tbmsg_id
     * 再以fisc_tbmsg_id至FISC的TBMSG_LOG查簡訊紀錄
     */
    @GetMapping("/record/sms/{acsTransId}")
    @Secured("ROLE_TX_QUERY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.TRANSACTION_LOG_SMS_GET)
    public ApiResponse sms(@PathVariable String acsTransId) {
        if (StringUtils.isEmpty(acsTransId) || acsTransId.length() < UUID_LENGTH) {
            return new ApiResponse(
              ResultStatus.ILLEGAL_ARGUMENT,
                MessageConstants.get(MessageConstants.NOT_SUPPORTED) + ", acsTransId:" + acsTransId);
        }

        List<SmsRecordDTO> result = facade.getSmsRecords(acsTransId);
        return ApiResponse.valueOf(result);
    }

    /**
     * 匯出交易紀錄 - 全部匯出
     */
    @PostMapping("/record/query/export/xls")
    @PreAuthorize(
      "hasRole('ROLE_TX_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.TRANSACTION_LOG_EXPORT)
    public void exportReportByQuery(@Valid @RequestBody TxLogHeaderQueryExportDTO queryDto)
      throws IOException {

        facade.exportReportByQuery(queryDto, canSeePan());
    }

    /**
     * 匯出交易紀錄 - 勾選
     */
    @PostMapping("/record/export/xls")
    @PreAuthorize(
      "hasRole('ROLE_TX_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#queryDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.TRANSACTION_LOG_EXPORT)
    public void exportReportByIds(@Valid @RequestBody IdsQueryDTO queryDto) throws IOException {
        facade.exportReportByIds(queryDto, canSeePan());
    }

}
