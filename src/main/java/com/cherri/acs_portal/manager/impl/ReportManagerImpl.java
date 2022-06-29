package com.cherri.acs_portal.manager.impl;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.report.AbnormalTransactionDTO;
import com.cherri.acs_portal.dto.report.AbnormalTransactionMonthlyDTO;
import com.cherri.acs_portal.dto.report.BankTransStatusDetailDTO;
import com.cherri.acs_portal.dto.report.BankTransStatusRecordDTO;
import com.cherri.acs_portal.dto.report.CardBrandTransStatusDTO;
import com.cherri.acs_portal.dto.report.StatisticsTransactionStatusDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogErrorReasonDTO;
import com.cherri.acs_portal.manager.ReportManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ReportManagerImpl implements ReportManager {

    private final NamedParameterJdbcTemplate npJdbcTemplate;
    private final IssuerBankDAO bankDao;

    private static final String STATISTICS_ABNORMAL_TRANSACTION_SQL =
      "select ((molecule.molecule_transStatusSum / denominator.denominator_transStatusSum) * 100) rate, \n"
        +
        "         molecule.issuer_bank_id,\n" +
        "         molecule.acquirer_merchant_id\n" +
        "         from ( select sum(trans_status) molecule_transStatusSum, \n" +
        "                issuer_bank_id,\n" +
        "                acquirer_merchant_id \n" +
        "                from ( select count(auth.trans_status) trans_status, \n" +
        "                       tx.issuer_bank_id,\n" +
        "                       auth.acquirer_merchant_id \n" +
        "                       from kernel_transaction_log tx \n" +
        "                       join authentication_log auth on tx.authentication_log_id = auth.id \n"
        +
        "                       where 1 = 1  \n" +
        "                       and auth.trans_status in (:moleculeTransStatusList) \n" +
        "                       and tx.create_millis between :startMillis and :endMillis \n" +
        "                       group by tx.issuer_bank_id, auth.acquirer_merchant_id \n" +
        "                       union all \n" +
        "                       select count(rs.trans_status) trans_status, \n" +
        "                       tx.issuer_bank_id,\n" +
        "                       auth.acquirer_merchant_id \n" +
        "                       from kernel_transaction_log tx \n" +
        "                       join authentication_log auth on tx.authentication_log_id = auth.id \n"
        +
        "                       join result_log rs on tx.result_log_id = rs.id \n" +
        "                       where 1 = 1 \n" +
        "                       and rs.trans_status in (:moleculeTransStatusList) \n" +
        "                       and tx.create_millis between :startMillis and :endMillis \n" +
        "                       group by tx.issuer_bank_id, auth.acquirer_merchant_id\n" +
        "                    ) m \n" +
        "                group by issuer_bank_id, acquirer_merchant_id \n" +
        "            ) molecule \n" +
        "            join \n" +
        "            ( select denominator_transStatusSum, \n" +
        "              issuer_bank_id,\n" +
        "              acquirer_merchant_id \n" +
        "              from ( select sum(trans_status) denominator_transStatusSum,\n" +
        "                     issuer_bank_id,\n" +
        "                     acquirer_merchant_id \n" +
        "                     from ( select count(auth.trans_status) trans_status, \n" +
        "                            tx.issuer_bank_id,\n" +
        "                            auth.acquirer_merchant_id \n" +
        "                            from kernel_transaction_log tx \n" +
        "                            join authentication_log auth on tx.authentication_log_id = auth.id \n"
        +
        "                            where 1 = 1 \n" +
        "                            and auth.trans_status in (:denominatorTransStatusList) \n" +
        "                            and tx.create_millis between :startMillis and :endMillis \n" +
        "                            group by tx.issuer_bank_id, auth.acquirer_merchant_id \n" +
        "                            union all \n" +
        "                            select count(rs.trans_status) transStatusSum, \n" +
        "                            tx.issuer_bank_id,\n" +
        "                            auth.acquirer_merchant_id \n" +
        "                            from kernel_transaction_log tx \n" +
        "                            join authentication_log auth on tx.authentication_log_id = auth.id \n"
        +
        "                            join result_log rs on tx.result_log_id = rs.id \n" +
        "                            where 1 = 1\n" +
        "                            and rs.trans_status in (:denominatorTransStatusList) \n" +
        "                            and tx.create_millis between :startMillis and :endMillis \n" +
        "                            group by tx.issuer_bank_id, auth.acquirer_merchant_id \n" +
        "                     ) m group by issuer_bank_id, acquirer_merchant_id \n" +
        "              )\n" +
        "            ) denominator \n" +
        "            on molecule.acquirer_merchant_id = denominator.acquirer_merchant_id\n" +
        "            and molecule.issuer_bank_id = denominator.issuer_bank_id";

    private static final String STATISTICS_TRANSACTION_STATUS_SQL =
      "select id issuerBankId, name issuerBankName,\n"
        // 總交易量
        + "   (select count(a.id) from authentication_log a \n"
        + "       join kernel_transaction_log t on t.authentication_log_id = a.id  \n"
        + "       where t.issuer_bank_id = bank.id and a.create_millis between :startMillis and :endMillis\n"
        + "    ) total,\n"
        // 動態密碼筆數
        + "    (select count(clgCode.id) from challenge_log clg \n"
        + "        join challenge_code_log clgCode on clg.id = clgcode.challenge_log_id \n"
        + "        join kernel_transaction_log tx on clg.kernel_transaction_log_id = tx.id \n"
        + "        where tx.issuer_bank_id = bank.id and tx.create_millis between :startMillis and :endMillis) otpCount,\n"
        // 尚未經過身份驗證:A, 系統未支援此transStatus
        + "    0 a,\n"
        // 身份驗證失敗:N
        + "   (select count(a.id) from authentication_log a \n"
        + "       join kernel_transaction_log t on t.authentication_log_id = a.id  \n"
        + "       where t.issuer_bank_id = bank.id and a.create_millis between :startMillis and :endMillis\n"
        + "    and trans_status = 'N') n,\n"
        // 身份驗證成功:Y
        + "   (select count(a.id) from authentication_log a \n"
        + "       join kernel_transaction_log t on t.authentication_log_id = a.id  \n"
        + "       where t.issuer_bank_id = bank.id and a.create_millis between :startMillis and :endMillis\n"
        + "    and trans_status = 'Y') y,\n"
        // Challenge驗證失敗:C -> N
        + "   (select count(a.id) from authentication_log a \n"
        + "       join kernel_transaction_log t on t.authentication_log_id = a.id  \n"
        + "       join result_log r on r.id = t.result_log_id\n"
        + "       where t.issuer_bank_id = bank.id and a.create_millis between :startMillis and :endMillis\n"
        + "     and r.trans_status = 'N') cn,\n"
        // Challenge驗證成功:C -> Y
        + "   (select count(a.id) from authentication_log a \n"
        + "       join kernel_transaction_log t on t.authentication_log_id = a.id  \n"
        + "       join result_log r on r.id = t.result_log_id\n"
        + "       where t.issuer_bank_id = bank.id and a.create_millis between :startMillis and :endMillis\n"
        + "     and r.trans_status = 'Y') cy,\n"
        // 系統異常:U
        + "   (\n"
        + "     (select count(a.id) from authentication_log a \n"
        + "        join kernel_transaction_log t on t.authentication_log_id = a.id  \n"
        + "        where t.issuer_bank_id = bank.id and a.create_millis between :startMillis and :endMillis\n"
        + "         and trans_status = 'U')\n" // 系統有回U
        + "     + (select count(a.id) from authentication_log a \n"
        + "        join kernel_transaction_log t on t.authentication_log_id = a.id  \n"
        + "        where t.issuer_bank_id = bank.id and a.create_millis between :startMillis and :endMillis\n"
        + "         and trans_status IS NULL)\n" // 系統該回應U卻沒有回
        + "     + (select count(a.id) from authentication_log a \n"
        + "        join kernel_transaction_log t on t.authentication_log_id = a.id  \n"
        + "        where t.issuer_bank_id = bank.id and a.create_millis between :startMillis and :endMillis\n"
        + "         and trans_status = 'C' and t.result_log_id IS NULL)\n" // 有做完challenge但系統沒有發RReq
        + "    ) u,\n"
        // 交易拒絕:R
        + "   (select count(a.id) from authentication_log a \n"
        + "       join kernel_transaction_log t on t.authentication_log_id = a.id  \n"
        + "       where t.issuer_bank_id = bank.id and a.create_millis between :startMillis and :endMillis\n"
        + "    and trans_status = 'R') r\n"
        + " from issuer_bank bank "
        + " where bank.id > 0 and bank.delete_flag = 0";

    private static final String ARES_TRANS_STATUS_STATISTICS_YNC_SQL =
      "select t.issuer_bank_id, t.card_brand, a.trans_status, count(a.id) count from authentication_log a\n"
        + "join issuer_bank b on a.issuer_bank_id = b.id and b.delete_flag=0 \n"
        + "join kernel_transaction_log t on t.authentication_log_id = a.id \n"
        + "where a.create_millis between :startMillis and :endMillis and (a.trans_status in ('Y', 'N') or (a.trans_status = 'C' and t.result_log_id is not null))\n"
        + "group by t.issuer_bank_id, t.card_brand, a.trans_status \n"
        + "order by t.issuer_bank_id, t.card_brand";

    private static final String ARES_TRANS_STATUS_STATISTICS_U_SQL =
      "select t.issuer_bank_id, t.card_brand, 'U' trans_status, count(a.id) count from authentication_log a \n"
        + "join issuer_bank b on a.issuer_bank_id = b.id and b.delete_flag=0 \n"
        + "join kernel_transaction_log t on t.authentication_log_id = a.id \n"
        + "where a.create_millis between :startMillis and :endMillis and (a.trans_status = 'U' or  trans_status IS NULL or (trans_status = 'C' and t.result_log_id IS NULL))\n"
        + "group by t.issuer_bank_id, t.card_brand \n"
        + "order by t.issuer_bank_id, t.card_brand";

    private static final String RREQ_TRANS_STATUS_STATISTICS_YN_SQL =
      "select t.issuer_bank_id, t.card_brand, r.trans_status, count(a.id) count from authentication_log a \n"
        + "join issuer_bank b on a.issuer_bank_id = b.id and b.delete_flag=0 \n"
        + "join kernel_transaction_log t on t.authentication_log_id = a.id \n"
        + "join Result_Log r on r.id = t.result_log_id\n"
        + "where a.create_millis between :startMillis and :endMillis and r.trans_status in ('Y', 'N')\n"
        + "group by t.issuer_bank_id, t.card_brand, r.trans_status \n"
        + "order by t.issuer_bank_id, card_brand";

    private static final String STATISTICS_ERROR_REASON_SQL =
      "select issuer_bank_id, ERROR_REASON_CODE, count(ERROR_REASON_CODE) ERROR_REASON_COUNT from (\n"
        + "    select issuer_bank_id, ARES_RESULT_REASON_CODE ERROR_REASON_CODE\n"
        + "    from kernel_transaction_log txLog\n"
        + "    where txLog.issuer_bank_id =:issuerBankId  and ares_result_reason_code is not null and ares_result_reason_code in (:aResErrorReasonCodeList)\n"
        + "          and txLog.create_millis between :startTimeMillis and :endTimeMillis   \n"
        + ") areq\n"
        + "group by issuer_bank_id, ERROR_REASON_CODE\n"
        + "union all \n"
        + "select issuer_bank_id, ERROR_REASON_CODE, count(ERROR_REASON_CODE) ERROR_REASON_COUNT from (\n"
        + "    select txLog.issuer_bank_id,\n"
        + "           CHALLENGE_ERROR_REASON_CODE ERROR_REASON_CODE\n"
        + "    from kernel_transaction_log txLog\n"
        + "    where txLog.issuer_bank_id =:issuerBankId and challenge_error_reason_code is not null\n"
        + "          and txLog.create_millis between :startTimeMillis and :endTimeMillis\n"
        + ") creq\n"
        + "group by issuer_bank_id, ERROR_REASON_CODE\n"
        + "union all\n"
        + "select issuer_bank_id, ERROR_REASON_CODE, count(ERROR_REASON_CODE) ERROR_REASON_COUNT from (\n"
        + "    select txLog.issuer_bank_id,\n"
        + "           RESULT_ERROR_REASON_CODE ERROR_REASON_CODE\n"
        + "    from kernel_transaction_log txLog\n"
        + "    where txLog.issuer_bank_id =:issuerBankId and result_error_reason_code is not null \n"
        + "          and txLog.create_millis between :startTimeMillis and :endTimeMillis\n"
        + ") rreq\n"
        + "group by issuer_bank_id, ERROR_REASON_CODE\n"
        + "order by ERROR_REASON_COUNT desc";

    @Autowired
    public ReportManagerImpl(NamedParameterJdbcTemplate npJdbcTemplate, IssuerBankDAO bankDao) {
        this.npJdbcTemplate = npJdbcTemplate;
        this.bankDao = bankDao;
    }

    @Override
    public List<AbnormalTransactionDTO> statisticsAbnormalTransactionRate(
        List<String> transStatusList,
        long startMillis,
        long endMillis) {
        String sql =
            "select tx.ISSUER_BANK_ID, auth.ACQUIRER_MERCHANT_ID, count(1) count\n"
                + "from KERNEL_TRANSACTION_LOG tx\n"
                + "         inner join AUTHENTICATION_LOG auth on tx.AUTHENTICATION_LOG_ID = auth.ID\n"
                + "         left join RESULT_LOG result on tx.RESULT_LOG_ID = result.ID\n"
                + "where tx.CREATE_MILLIS between :startMillis and :endMillis\n"
                + "  and auth.ACQUIRER_MERCHANT_ID is not null\n"
                //   and (auth.TRANS_STATUS in (:transStatusList) or result.TRANS_STATUS in (:transStatusList))
                + "  %s\n"
                + "group by tx.ISSUER_BANK_ID, auth.ACQUIRER_MERCHANT_ID";
        Map<String, Object> params = new HashMap<>();
        params.put("startMillis", startMillis);
        params.put("endMillis", endMillis);

        if (transStatusList != null) {
            sql = String.format(sql,
                "and (auth.TRANS_STATUS in (:transStatusList) or result.TRANS_STATUS in (:transStatusList))");
            params.put("transStatusList", transStatusList);
        } else {
            sql = String.format(sql, "");
        }

        try {
            return npJdbcTemplate.query(
                sql,
                params,
                (rs, rowNum) -> {
                    Long issuerBankId = rs.getLong("issuer_bank_id");
                    String merchantId = rs.getString("acquirer_merchant_id");
                    Long count = rs.getLong("count");

                    AbnormalTransactionDTO.Key key = new AbnormalTransactionDTO.Key(issuerBankId,
                        merchantId);
                    return new AbnormalTransactionDTO(key, null, count);
                });
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            log.error(
                "[statisticsAbnormalTransactionRate] unknown exception, transStatusList={}, startMillis={}, endMillis={}",
                transStatusList,
                startMillis,
                endMillis, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<AbnormalTransactionMonthlyDTO> statisticsAbnormalTransactionRateMonthly(int year,
        int month) {

        Map<String, String> merchantNameMap = findMerchantNameForMonthlyReport(year,
            month);

        String sql =
            "select ISSUER_BANK_ID,\n"
            + "       MERCHANT_ID,\n"
            + "       sum(U_COUNT)     U_COUNT,\n"
            + "       sum(N_COUNT)     N_COUNT,\n"
            + "       sum(TOTAL_COUNT) TOTAL_COUNT\n"
            + "from ABNORMAL_TRANSACTION\n"
            + "where YEAR = :year\n"
            + "  and MONTH = :month\n"
            + "group by ISSUER_BANK_ID, MERCHANT_ID";
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        List<AbnormalTransactionMonthlyDTO> abnormalTransactionMonthlyDTOList = npJdbcTemplate
            .query(
                sql,
                params,
                (rs, rowNum) -> {
                    return new AbnormalTransactionMonthlyDTO(
                        rs.getLong("ISSUER_BANK_ID"),
                        rs.getString("MERCHANT_ID"),
                        merchantNameMap.get(rs.getString("MERCHANT_ID")),
                        rs.getLong("U_COUNT"),
                        rs.getLong("N_COUNT"),
                        rs.getLong("TOTAL_COUNT"));
                });
        return abnormalTransactionMonthlyDTOList;
    }

    private Map<String, String> findMerchantNameForMonthlyReport(int year, int month) {
        String sql =
            "select distinct(MERCHANT_ID), MERCHANT_NAME\n"
                + "from ABNORMAL_TRANSACTION\n"
                + "where YEAR = :year\n"
                + "  and MONTH = :month";
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        List<Map<String, String>> resultList = npJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Map<String, String> result = new HashMap<>();
            result.put("MERCHANT_ID", rs.getString("MERCHANT_ID"));
            result.put("MERCHANT_NAME", rs.getString("MERCHANT_NAME"));
            return result;
        });
        return resultList.stream()
            .collect(Collectors
                .toMap(r -> r.get("MERCHANT_ID"), r -> r.get("MERCHANT_NAME"), (s, s2) -> s));
    }

    private void printLog(String executor, String sql, Map<String, Object> params) {
        if (log.isDebugEnabled()) {
            log.debug("[{}] execute sql:{},\nsql params={}", executor, sql, params);
            if (params.containsKey("startMillis")) {
                Date date = new Date((Long) params.get("startMillis"));
                log.debug("[{}] startDateTime={}", executor, date);
            }
            if (params.containsKey("endMillis")) {
                Date date = new Date((Long) params.get("endMillis"));
                log.debug("[{}] endDateTime={}", executor, date);
            }
        }
    }

    @Override
    public List<StatisticsTransactionStatusDTO> statisticsTransactionStatus(
      long startMillis, long endMillis) {
        Map<String, Object> params = new HashMap<>();
        params.put("startMillis", startMillis);
        params.put("endMillis", endMillis);

        String querySql = STATISTICS_TRANSACTION_STATUS_SQL;

        printLog("statisticsTransactionStatus", querySql, params);

        try {
            List<StatisticsTransactionStatusDTO> resultList = new ArrayList<>();
            npJdbcTemplate.query(
              querySql,
              params,
              rs -> {
                  StatisticsTransactionStatusDTO dto =
                    new StatisticsTransactionStatusDTO(
                      rs.getLong("issuerBankId"),
                      rs.getString("issuerBankName"),
                      rs.getString("total"),
                      rs.getString("otpCount"),
                      rs.getString("n"),
                      rs.getString("a"),
                      rs.getString("y"),
                      rs.getString("cy"),
                      rs.getString("cn"),
                      rs.getString("r"),
                      rs.getString("u"));
                  resultList.add(dto);
              });
            return resultList;
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            log.error(
              "[statisticsTransactionStatus] unknown exception, startMillis={}, endMillis={}",
              startMillis,
              endMillis, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<BankTransStatusDetailDTO> statisticsTransStatusDetail(
      long startMillis, long endMillis) {

        Map<String, Object> params = new HashMap<>();
        params.put("startMillis", startMillis);
        params.put("endMillis", endMillis);

        try {
            // 取得所有銀行
            Map<Long, String> bankMap = getAvailableBank();
            if (bankMap.isEmpty()) {
                return Collections.emptyList();
            }

            // 統計ARes.transStatus:Y,N,C,R
            printLog(
              "statisticsTransStatusDetailByCardBrand", ARES_TRANS_STATUS_STATISTICS_YNC_SQL,
              params);
            List<BankTransStatusRecordDTO> aResTransStatusYNCRList =
              doStatisticsTransStatusDetail(ARES_TRANS_STATUS_STATISTICS_YNC_SQL, params);
            if (aResTransStatusYNCRList.isEmpty()) {
                return Collections.emptyList();
            }

            // 統計ARes.transStatus:U
            printLog(
              "statisticsTransStatusDetailByCardBrand", ARES_TRANS_STATUS_STATISTICS_U_SQL, params);
            List<BankTransStatusRecordDTO> aResTransStatusUList =
              doStatisticsTransStatusDetail(ARES_TRANS_STATUS_STATISTICS_U_SQL, params);

            // 統計RReq.transStatus:Y,N
            printLog(
              "statisticsTransStatusDetailByCardBrand", RREQ_TRANS_STATUS_STATISTICS_YN_SQL,
              params);
            List<BankTransStatusRecordDTO> rreqTransStatusYNList =
              doStatisticsTransStatusDetail(RREQ_TRANS_STATUS_STATISTICS_YN_SQL, params);

            List<BankTransStatusDetailDTO> result = new ArrayList<>();
            // 逐銀行組資料
            bankMap.forEach(
              (bankId, bankName) -> {
                  // Filter by bankId
                  List<BankTransStatusRecordDTO> bankAresYNCR =
                    aResTransStatusYNCRList.stream()
                      .filter(record -> bankId.equals(record.getIssuerBankId()))
                      .collect(Collectors.toList());
                  List<BankTransStatusRecordDTO> bankAresU =
                    aResTransStatusUList.stream()
                      .filter(record -> bankId.equals(record.getIssuerBankId()))
                      .collect(Collectors.toList());
                  List<BankTransStatusRecordDTO> bankRreqYN =
                    rreqTransStatusYNList.stream()
                      .filter(record -> bankId.equals(record.getIssuerBankId()))
                      .collect(Collectors.toList());

                  List<CardBrandTransStatusDTO> cardBrandTransStatusList = new ArrayList<>();
                  // 逐CardBrand組資料
                  for (String cardBrand : EnvironmentConstants.ACS_SUPPORTED_CARD_BRAND_LIST) {
                      // Add to cardBrandTransStatusList
                      CardBrandTransStatusDTO cardBrandTransStatus =
                        createCardBrandTransStatusData(cardBrand, bankAresYNCR, bankAresU,
                          bankRreqYN);
                      cardBrandTransStatusList.add(cardBrandTransStatus);
                  }

                  result
                    .add(BankTransStatusDetailDTO.newInstance(bankName, cardBrandTransStatusList));
              });

            return result;

        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();

        } catch (Exception e) {
            log.error(
              "[statisticsTransactionStatusRate] unknown exception, startMillis={}, endMillis={}",
              startMillis,
              endMillis, e);
            throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
    }

    private List<BankTransStatusRecordDTO> doStatisticsTransStatusDetail(
      String querySql, Map<String, Object> params) {
        return npJdbcTemplate.query(
          querySql,
          params,
          (rs, rowNum) ->
            BankTransStatusRecordDTO.builder()
              .issuerBankId(rs.getLong("issuer_bank_id"))
              .cardBrand(rs.getString("card_brand"))
              .transStatus(rs.getString("trans_status"))
              .count(rs.getLong("count"))
              .build());
    }

    private CardBrandTransStatusDTO createCardBrandTransStatusData(
      String cardBrand,
      List<BankTransStatusRecordDTO> bankAresYNCR,
      List<BankTransStatusRecordDTO> bankAresU,
      List<BankTransStatusRecordDTO> bankRreqYN) {
        // ARes
        List<BankTransStatusRecordDTO> aResYNCRByCardBrandList =
          filterByCardBrand(bankAresYNCR, cardBrand);
        List<BankTransStatusRecordDTO> aResUByCardBrandList =
          filterByCardBrand(bankAresU, cardBrand);

        long aResY = filterByTransStatus(aResYNCRByCardBrandList, TransStatus.Authentication);
        long aResN =
          filterByTransStatus(aResYNCRByCardBrandList, TransStatus.NotAuthenticated);
        long aResC = filterByTransStatus(aResYNCRByCardBrandList, TransStatus.Challenge);
        Long aResU = filterByTransStatus(aResUByCardBrandList, TransStatus.CouldNotVerify);
        long aResR;
        if (EnvironmentConstants.IS_MULTI_ISSUER) {
            aResR = 0L;
        } else {
            aResR = filterByTransStatus(aResYNCRByCardBrandList, TransStatus.Rejected);
        }

        CardBrandTransStatusDTO.TransStatus aresTransStatusDto =
          CardBrandTransStatusDTO.TransStatus.newInstanceForARes(
            aResY, aResN, aResC, aResR, aResU);
        // RReq
        List<BankTransStatusRecordDTO> rReqTransStatusYNByCardBrandList =
          filterByCardBrand(bankRreqYN, cardBrand);
        Long rReqY =
          filterByTransStatus(rReqTransStatusYNByCardBrandList, TransStatus.Authentication);
        Long rReqN =
          filterByTransStatus(
            rReqTransStatusYNByCardBrandList, TransStatus.NotAuthenticated);
        CardBrandTransStatusDTO.TransStatus rreqTransStatusDto =
          CardBrandTransStatusDTO.TransStatus.newInstanceForRReq(rReqY, rReqN);

        return CardBrandTransStatusDTO.newInstance(
          cardBrand, aresTransStatusDto, rreqTransStatusDto);
    }

    /** Key = issuerBankId, value = issuerBankName */
    private Map<Long, String> getAvailableBank() throws DatabaseException {
        List<IssuerBankDO> bankList = bankDao.findAll();
        if (bankList.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, String> bankMap = new HashMap<>();
        bankList.stream()
          .filter(bank -> bank.getId() > 0) // 過濾系統預設的銀行ID
          .forEach(bank -> bankMap.put(bank.getId(), bank.getName()));
        return bankMap;
    }

    private List<BankTransStatusRecordDTO> filterByCardBrand(
      List<BankTransStatusRecordDTO> transStatusByCardBrandList, String cardBrand) {
        return transStatusByCardBrandList.stream()
          .filter(bankTransStatusRecord -> cardBrand.equals(bankTransStatusRecord.getCardBrand()))
          .collect(Collectors.toList());
    }

    private Long filterByTransStatus(
      List<BankTransStatusRecordDTO> bankTransStatusRecordList, TransStatus transStatus) {
        return bankTransStatusRecordList.stream()
          .filter(cardBrandTransStatusDto -> transStatus.getCode()
            .equals(cardBrandTransStatusDto.getTransStatus()))
          .map(BankTransStatusRecordDTO::getCount)
          .findFirst()
          .orElse(0L);
    }

    @Override
    public List<TxLogErrorReasonDTO> statisticsErrorReason(
      Long issuerBankId, long startTimeMillis, long endTimeMillis) {
        Map<String, Object> params = new HashMap<>();
        params.put("issuerBankId", issuerBankId);
        params.put("startTimeMillis", startTimeMillis);
        params.put("endTimeMillis", endTimeMillis);
        params.put("aResErrorReasonCodeList", ResultStatus.getAResErrorReasonCodeList());

        List<TxLogErrorReasonDTO> resultList =
          npJdbcTemplate.query(
            STATISTICS_ERROR_REASON_SQL,
            params,
            (rs, rowNum) ->
              TxLogErrorReasonDTO.newInstance(
                rs.getLong("ISSUER_BANK_ID"),
                rs.getInt("ERROR_REASON_CODE"),
                rs.getInt("ERROR_REASON_COUNT")));
        return resultList;
    }
}
