package com.cherri.acs_portal.manager;

import com.cherri.acs_portal.controller.request.TxSummaryReqDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.common.IdsQueryDTO;
import com.cherri.acs_portal.dto.transactionLog.*;

import java.util.List;
import java.util.Optional;

public interface TransactionLogManager {

  /** 取得交易紀錄 */
  PagingResultDTO<TxLogHeaderDTO> getLogs(TxLogHeaderQueryDTO queryDto, boolean canSeePan);

  /** 取得交易紀錄摘要 */
  Optional<TxLogSummaryDTO> getSummary(TxSummaryReqDTO queryDto);

  /** 查詢匯出的交易紀錄報表 - 全部匯出 */
  List<TxLogExportDTO> reportQuery(TxLogHeaderQueryExportDTO queryDto, boolean canSeePan);

  /** 查詢匯出的交易紀錄報表 - 勾選 */
  List<TxLogExportDTO> reportQueryByIds(IdsQueryDTO queryDto, boolean canSeePan);

}
