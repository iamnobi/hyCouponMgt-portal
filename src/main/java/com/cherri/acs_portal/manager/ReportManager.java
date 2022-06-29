package com.cherri.acs_portal.manager;

import com.cherri.acs_portal.dto.report.AbnormalTransactionDTO;
import com.cherri.acs_portal.dto.report.AbnormalTransactionMonthlyDTO;
import com.cherri.acs_portal.dto.report.BankTransStatusDetailDTO;
import com.cherri.acs_portal.dto.report.StatisticsTransactionStatusDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogErrorReasonDTO;

import java.util.List;

public interface ReportManager {

  /**
   *
   * @param transStatusList 如果 null 則查全部
   * @param startMillis
   * @param endMillis
   * @return
   */
  List<AbnormalTransactionDTO> statisticsAbnormalTransactionRate(
      List<String> transStatusList,
      long startMillis,
      long endMillis);

  List<AbnormalTransactionMonthlyDTO> statisticsAbnormalTransactionRateMonthly(
      int year,
      int month);

  List<StatisticsTransactionStatusDTO> statisticsTransactionStatus(
      long startMillis, long endMillis);

  List<BankTransStatusDetailDTO> statisticsTransStatusDetail(long startMillis, long endMillis);

  List<TxLogErrorReasonDTO> statisticsErrorReason(
      Long issuerBankId, long startMillis, long endMillis);
}
