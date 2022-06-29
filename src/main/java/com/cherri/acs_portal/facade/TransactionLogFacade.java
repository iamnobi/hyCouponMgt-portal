package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.controller.request.TxSummaryReqDTO;
import com.cherri.acs_portal.controller.response.SmsRecordDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.common.IdsQueryDTO;
import com.cherri.acs_portal.dto.transactionLog.TransactionLogDetailDto;
import com.cherri.acs_portal.dto.transactionLog.TxLogHeaderDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogHeaderQueryDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogHeaderQueryExportDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogSummaryDTO;
import com.cherri.acs_portal.service.TransactionLogService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionLogFacade {

    private final TransactionLogService txLogService;
    private final HttpServletResponse response;

    @Autowired
    public TransactionLogFacade(
      TransactionLogService txLogService,
      HttpServletResponse response) {
        this.response = response;
        this.txLogService = txLogService;
    }

    /**
     * 取得交易紀錄
     */
    public PagingResultDTO<TxLogHeaderDTO> getLogs(TxLogHeaderQueryDTO queryDto, boolean canSeePan) {
        return txLogService.getLogs(queryDto, canSeePan);
    }

    /**
     * 取得交易紀錄摘要
     */
    public Optional<TxLogSummaryDTO> getSummary(TxSummaryReqDTO queryDto) {
        return txLogService.getSummary(queryDto);
    }

    public Optional<TransactionLogDetailDto> getTransactionLogDetail(Long transactionLogId) {
        return txLogService.getTransactionLogDetail(transactionLogId);
    }

    /**
     * 匯出交易紀錄 - 全部匯出
     */
    public void exportReportByQuery(TxLogHeaderQueryExportDTO queryDto, boolean canSeePan) throws IOException {
        txLogService.exportXlsByQuery(queryDto, response, canSeePan);
    }

    /**
     * 匯出交易紀錄 - 勾選
     */
    public void exportReportByIds(IdsQueryDTO queryDto, boolean canSeePan) throws IOException {
        txLogService.exportXlsByIds(queryDto, response, canSeePan);
    }

    /**
     * 取得簡訊紀錄
     */
    public List<SmsRecordDTO> getSmsRecords(String acsTransId) {
        return txLogService.getSmsRecords(acsTransId);
    }

}
