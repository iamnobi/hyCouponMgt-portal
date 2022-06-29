package com.cherri.acs_portal.manager.impl;

import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.controller.request.TxSummaryReqDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.cardholder.HolderQueryDTO;
import com.cherri.acs_portal.dto.common.IdsQueryDTO;
import com.cherri.acs_portal.dto.hsm.DecryptResultDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogExportDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogHeaderDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogHeaderQueryDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogHeaderQueryExportDTO;
import com.cherri.acs_portal.dto.transactionLog.TxLogSummaryDTO;
import com.cherri.acs_portal.manager.AcsIntegratorManager;
import com.cherri.acs_portal.manager.TransactionLogManager;
import com.cherri.acs_portal.util.AcsPortalUtil;
import com.cherri.acs_portal.util.DateUtil;
import com.cherri.acs_portal.util.HmacUtils;
import com.cherri.acs_portal.util.PageQuerySqlUtils;
import com.google.common.collect.ImmutableMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.DeviceChannel;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.NoSuchDataException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class TransactionLogManagerImpl implements TransactionLogManager {

    private final NamedParameterJdbcTemplate npJdbcTemplate;
    private final AcsIntegratorManager acsIntegratorManager;
    private final HsmPlugin hsmPlugin;

    private static final String SELECT_LOGS_COLUMNS =
      " tx.id, tx.acs_trans_id, tx.acs_integrator_trans_id, tx.device_id, tx.create_millis, tx.issuer_bank_id, pi.card_number, pi.card_number_en, auth.purchase_amount, auth.purchase_exponent,auth.purchase_currency, auth.trans_status ares, rs.trans_status rres,"
        + "ecm.msg as error_description, tx.challenge_error_reason_code, bank.name as issuer_name, auth.acquirer_b_i_n";

    private static final String SELECT_SUMMARY_COLUMNS =
      " pi.card_number_en, tx.device_channel, tx.ip, tx.create_millis, tx.ares_result_reason_code, ecm.msg as challenge_reason, \n"
        + "(select msg from error_code_mapping where code = tx.challenge_error_reason_code) as otp_result, \n"
        + " auth.cardholder_name, auth.mp_subscriber mobile_phone, \n"
        + " auth.bill_addr_line_1, auth.bill_addr_line_2, auth.bill_addr_line_3, \n"
        + " auth.create_millis, auth.merchant_name, auth.merchant_country_code, auth.purchase_currency, auth.browser_user_agent,\n"
        + " auth.trans_status ares, rs.trans_status rres, auth.purchase_date, tx.challenge_otp_channel \n";

    private static final String SELECT_REPORT_COLUMNS =
      SELECT_LOGS_COLUMNS + ", " + SELECT_SUMMARY_COLUMNS;

    private static final String LOGS_BASE_SQL =
      "select %s\n"
        + "from kernel_transaction_log tx\n"
        + "left join pan_info pi on tx.pan_info_id = pi.id\n"
        + "left join authentication_log auth on tx.authentication_log_id = auth.id\n"
        + "left join result_log rs on tx.result_log_id = rs.id\n"
        + "left join error_code_mapping ecm on tx.ares_result_reason_code = ecm.code\n"
        + "left join issuer_bank bank on tx.issuer_bank_id = bank.id\n";

    @Autowired
    public TransactionLogManagerImpl(
        NamedParameterJdbcTemplate npJdbcTemplate, AcsIntegratorManager acsIntegratorManager,
        HsmPlugin hsmPlugin) {
        this.npJdbcTemplate = npJdbcTemplate;
        this.acsIntegratorManager = acsIntegratorManager;
        this.hsmPlugin = hsmPlugin;
    }

    /** 取得交易紀錄 */
    @Override
    public PagingResultDTO<TxLogHeaderDTO> getLogs(TxLogHeaderQueryDTO queryDto, boolean canSeePan) {

        Map<String, Object> params = new HashMap<>();
        String conditionSql = createConditionSql(queryDto, params);
        String countSql = String.format(LOGS_BASE_SQL, " count(tx.id) total") + conditionSql;
        String querySql = String.format(LOGS_BASE_SQL, SELECT_LOGS_COLUMNS) + conditionSql
          + " order by tx.create_millis desc";
        querySql = paging(querySql, params, queryDto.getStartRowNumber(), queryDto.getLimit());

        try {
            Long total = npJdbcTemplate.queryForObject(countSql, params, Long.class);
            List<TxLogHeaderDTO> data = npJdbcTemplate
              .query(querySql, params, new TxLogHeaderRowMapper());

            if(canSeePan) {
                log.debug("[getLogs] decrypt pan");
                decryptPanInTxLogHeaderDTO(data);
            }

            return new PagingResultDTO<>(total, queryDto, data);
        } catch (EmptyResultDataAccessException e) {
            return new PagingResultDTO<>(0L, 0, 0, Collections.emptyList());
        } catch (Exception e) {
            log.error("[getLogs] unknown exception, queryDto={}, querySql={}", queryDto, querySql);
            throw e;
        }
    }

    /** 建立查詢條件SQL (WHERE...) */
    private String createConditionSql(
      TxLogHeaderQueryDTO queryDto, Map<String, Object> params) {

        List<String> conditionList = new ArrayList<>();

        byStartTime(queryDto, conditionList, params);
        byEndTime(queryDto, conditionList, params);
        byIssuerBankId(queryDto, conditionList, params);
        byCardNumber(queryDto, conditionList, params);
        byCardBrand(queryDto, conditionList, params);
        byIdentityNumber(queryDto, conditionList, params);
        byAres(queryDto, conditionList, params);
        byRres(queryDto, conditionList, params);
        byThreeDSServerTransID(queryDto, conditionList, params);
        byAcsTransID(queryDto, conditionList, params);
        byDsTransID(queryDto, conditionList, params);
        bySdkTransID(queryDto, conditionList, params);
        byThreeDSOperatorId(queryDto, conditionList, params); /** 3DS Operator ID 00003 add Criteria 3ds operID in transaction inquiry */
        // 移除只做了3DS Method 的交易
        byNotThreeDSMethod(conditionList, params);

        String conditionSql = "";
        if (params.size() > 0) {
            conditionSql = conditionList.stream().collect(Collectors.joining(" and ", "where", ""));
        }
        return conditionSql;
    }

    private void byStartTime(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
      Map<String, Object> params) {
        if (queryDto.getStartMillis() != null) {
            conditionList.add(" tx.create_millis >= :startTime ");
            params.put("startTime", queryDto.getStartMillis());
        }
    }

    private void byEndTime(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
      Map<String, Object> params) {
        if (queryDto.getEndMillis() != null) {
            conditionList.add(" tx.create_millis <= :endTime ");
            params.put("endTime", queryDto.getEndMillis());
        }
    }

    private void byIssuerBankId(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
      Map<String, Object> params) {
        if (queryDto.getIssuerBankId() != null && queryDto.getIssuerBankId() != 0) {
            conditionList.add(" tx.issuer_bank_id = :issuerBankId ");
            params.put("issuerBankId", queryDto.getIssuerBankId());
        }
    }

    /** 3DS Operator ID 00003 add Criteria 3ds operID in transaction inquiry */
    private void byThreeDSOperatorId(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
                                Map<String, Object> params) {
        if (queryDto.getThreeDSOperatorId() != null && !queryDto.getThreeDSOperatorId().equalsIgnoreCase("0")) {
            conditionList.add(" tx.three_d_s_server_operator_id = :threeDSOperatorId ");
            params.put("threeDSOperatorId", queryDto.getThreeDSOperatorId());
            log.info("3DS Oper ID : {}", StringUtils.normalizeSpace(queryDto.getThreeDSOperatorId()));
        }else{
            log.info("3DS Oper ID is empty");
        }
    }


    private void byCardNumber(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
      Map<String, Object> params) {
        if (StringUtils.isNotBlank(queryDto.getPan())) {
            String cardNumber = queryDto.getPan().trim();
            String cardNumberHashKey = HmacUtils
              .encrypt(cardNumber, EnvironmentConstants.hmacHashKey);
            conditionList.add(" pi.card_number_hash = :pan ");
            params.put("pan", cardNumberHashKey);
        }
    }

    private void byCardBrand(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
      Map<String, Object> params) {
        if (StringUtils.isNotEmpty(queryDto.getCardType())) {
            String cardBrand = queryDto.getCardType().trim();
            conditionList.add(" tx.card_brand = :cardBrand ");
            params.put("cardBrand", cardBrand);
        }
    }

    private void byIdentityNumber(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
      Map<String, Object> params) {
        if (StringUtils.isNotBlank(queryDto.getIdentityNumber())) {
            HolderQueryDTO holderQuery = new HolderQueryDTO();
            holderQuery.setIdentityNumber(queryDto.getIdentityNumber());
            holderQuery.setIssuerBankId(queryDto.getIssuerBankId());

            Set<String> cardList = acsIntegratorManager.getCardList(holderQuery);
            if (cardList == null || cardList.isEmpty()) {
                String impossibleCardNumber = "XXX!";
                conditionList.add(" pi.card_number = :cardNumber ");
                params.put("cardNumber", impossibleCardNumber);
            } else {
                List<String> hashCardNumbers =
                  cardList.stream()
                    .map(
                      cardNumber -> HmacUtils.encrypt(cardNumber, EnvironmentConstants.hmacHashKey))
                    .collect(Collectors.toList());

                conditionList.add(" pi.card_number_hash in (:pans) ");
                params.put("pans", hashCardNumbers);
            }
        }
    }

    private void byAres(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
      Map<String, Object> params) {
        if (StringUtils.isNotBlank(queryDto.getAres())) {
            conditionList.add(" auth.trans_status = :ares ");
            params.put("ares", queryDto.getAres());
        }
    }

    private void byRres(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
      Map<String, Object> params) {
        if (StringUtils.isNotBlank(queryDto.getRres())) {
            conditionList.add(" rs.trans_status = :rres ");
            params.put("rres", queryDto.getRres());
        }
    }

    private void byThreeDSServerTransID(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
      Map<String, Object> params) {
        if (StringUtils.isNotBlank(queryDto.getThreeDSServerTransID())) {
            conditionList.add(" tx.three_d_s_server_trans_id = :threeDSServerTransID ");
            params.put("threeDSServerTransID", queryDto.getThreeDSServerTransID());
        }
    }

    private void byAcsTransID(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
      Map<String, Object> params) {
        if (StringUtils.isNotBlank(queryDto.getAcsTransID())) {
            conditionList.add(" tx.acs_Trans_id = :acsTransID ");
            params.put("acsTransID", queryDto.getAcsTransID());
        }
    }

    private void byDsTransID(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
      Map<String, Object> params) {
        if (StringUtils.isNotBlank(queryDto.getDsTransID())) {
            conditionList.add(" tx.ds_Trans_id = :dsTransID ");
            params.put("dsTransID", queryDto.getDsTransID());
        }
    }

    private void bySdkTransID(TxLogHeaderQueryDTO queryDto, List<String> conditionList,
      Map<String, Object> params) {
        if (StringUtils.isNotBlank(queryDto.getSdkTransID())) {
            conditionList.add(" tx.sdk_trans_id = :sdkTransID ");
            params.put("sdkTransID", queryDto.getSdkTransID());
        }
    }

    private void byNotThreeDSMethod(List<String> conditionList, Map<String, Object> params) {
        conditionList.add(" tx.sys_updater is not null ");
        conditionList.add(" tx.sys_updater != :threeDSMethod ");
        params.put("threeDSMethod", MessageType.ThreeDSMethod.name());
    }

    private String paging(String querySql, Map<String, Object> params, long startRowNumber,
      long limit) {
        querySql = PageQuerySqlUtils.get(querySql);
        params.put(PageQuerySqlUtils.PARAM_START_ROW_NUMBER, startRowNumber);
        params.put(PageQuerySqlUtils.PARAM_LIMIT, limit);
        return querySql;
    }


    class TxLogHeaderRowMapper extends BeanPropertyRowMapper<TxLogHeaderDTO> {

        @Override
        public TxLogHeaderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TxLogHeaderDTO dto = new TxLogHeaderDTO();
            dto.setId(rs.getLong("id"));
            dto.setAcsTransId(rs.getString("acs_trans_id"));
            dto.setAcsIntegratorTransId(rs.getString("acs_integrator_trans_id"));
            dto.setDeviceId(rs.getString("device_id") == null ? "" : rs.getString("device_id"));
            dto.setPan(rs.getString("card_number"));
            dto.setEnCardNumber(rs.getString("card_number_en"));
            dto.setAmount(
              Double.toString(
                applyPurchaseExponent(
                  rs.getInt("purchase_exponent"), rs.getDouble("purchase_amount"))));
            dto.setCurrency(
              AcsPortalUtil.currencyCodeAndNameFormatString(rs.getString("purchase_currency")));
            dto.setTransTime(rs.getLong("create_millis"));
            dto.setAres(rs.getString("ares"));
            dto.setRres(rs.getString("rres"));
            dto.setFailureReason(rs.getString("error_description"));
            dto.setChallengeErrorReasonCode(rs.getString("challenge_error_reason_code"));
            dto.setIssuerName(rs.getString("issuer_name"));
            dto.setIssuerBankId(rs.getLong("issuer_bank_id"));
            dto.setAcqBin(rs.getString("acquirer_b_i_n"));
            return dto;
        }
    }

    private Double applyPurchaseExponent(Integer purchaseExponent, Double purchaseAmount) {
        // NPA
        if (purchaseExponent == null || purchaseAmount == null) {
            return 0.0;
        }
        // PA
        if (purchaseExponent > 0) {
            return purchaseAmount / Math.pow(10, purchaseExponent);
        }
        return purchaseAmount;
    }

    /** 取得交易紀錄摘要 */
    @Override
    public Optional<TxLogSummaryDTO> getSummary(TxSummaryReqDTO queryDto) {
        String querySQL = String.format(LOGS_BASE_SQL, SELECT_SUMMARY_COLUMNS);
        querySQL += " where tx.id = :id";
        Map<String, Object> params = ImmutableMap.of("id", queryDto.getTransactionLogId());

        try {
            TxLogSummaryDTO data =
              npJdbcTemplate.queryForObject(querySQL, params, new TxLogSummaryRowMapper());
            return Optional.ofNullable(data);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchDataException("Transaction log " + MessageConstants.get(MessageConstants.NOT_FOUND));
        } catch (Exception e) {
            log.error("[getSummary] unknown exception, queryDto={}, querySQL={}", queryDto,
              querySQL);
            throw e;
        }
    }

    public static class TxLogSummaryRowMapper extends BeanPropertyRowMapper<TxLogSummaryDTO> {

        @Override
        public TxLogSummaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TxLogSummaryDTO dto = new TxLogSummaryDTO();

            DeviceChannel deviceChannel = DeviceChannel.codeOf(rs.getString("device_channel"));
            String deviceChannelName = deviceChannel == null ? "" : deviceChannel.name();
            dto.setDeviceChannel(deviceChannelName);

            dto.setIp(rs.getString("ip"));
            dto.setCardholderName(rs.getString("cardholder_name"));
            dto.setMobilePhone(rs.getString("mobile_phone"));
            dto.setBillingAddress(getBillAddrCombineStr(rs));
            dto.setShoppingTime(DateUtil.utcToTwDateTimeMillis(rs.getString("purchase_date")));
            dto.setMerchantName(rs.getString("merchant_name"));
            dto.setMerchantCountryCode(
              AcsPortalUtil.countryCodeAndNameFormatString(rs.getString("merchant_country_code")));
            dto.setCurrencyCode(
              AcsPortalUtil.currencyCodeAndNameFormatString(rs.getString("purchase_currency")));
            dto.setAres(rs.getString("ares"));
            dto.setRres(rs.getString("rres"));
            dto.setUserAgent(rs.getString("browser_user_agent"));
            dto.setChallengeReason(rs.getString("challenge_reason"));
            dto.setEnCardNumber(rs.getString("card_number_en"));
            dto.setCreateMillis(rs.getLong("create_millis"));
            String otpResult = rs.getString("otp_result");
            String aresResultReasonCode = rs.getString("ares_result_reason_code");
            if (StringUtils.isBlank(otpResult)) {
                boolean isAResC = TransStatus.Challenge.getCode().equals(dto.getAres());
                boolean isRResY = TransStatus.Authentication.getCode().equals(dto.getRres());
                boolean isRResN = TransStatus.NotAuthenticated.getCode().equals(dto.getRres());
                if (isAResC && isRResY) {
                    otpResult = "Challenge Success";
                } else if (isAResC && isRResN) {
                    otpResult = "Challenge Fail";
                }
            }
            if (aresResultReasonCode != null) {
                switch (aresResultReasonCode) {
                    case "1911":
                        dto.setRbaDescription("Pass");
                        break;
                    case "1913":
                        dto.setRbaDescription("(See Detail)");
                }
            }
            dto.setOtpResult(otpResult);
            dto.setChallengeOtpChannel(StringUtils.isBlank(rs.getString("challenge_otp_channel"))
              ? ""
              : rs.getString("challenge_otp_channel")
            );
            return dto;
        }

        private String getBillAddrCombineStr(ResultSet rs) throws SQLException {
            String billAddrLine1 = rs.getString("bill_addr_line_1");
            String billAddrLine2 = rs.getString("bill_addr_line_2");
            String billAddrLine3 = rs.getString("bill_addr_line_3");

            return Arrays.asList(billAddrLine1, billAddrLine2, billAddrLine3).stream()
              .filter(StringUtils::isNotEmpty)
              .collect(Collectors.joining("; "));
        }
    }

    @Override
    public List<TxLogExportDTO> reportQuery(TxLogHeaderQueryExportDTO queryDto, boolean canSeePan) {
        Map<String, Object> params = new HashMap<>();
        String conditionSql = createConditionSql(queryDto, params);
        String querySql = String.format(LOGS_BASE_SQL, SELECT_REPORT_COLUMNS)
          + conditionSql
          + " order by tx.create_millis desc";

        try {
            List<TxLogExportDTO> txLogExportDTOList = npJdbcTemplate
                .query(querySql, params, new TxLogExportRowMapper(queryDto.getTimeZone()));

            if(canSeePan) {
                log.debug("[getLogs] decrypt pan");
                decryptPanInTxLogExportDTO(txLogExportDTOList);
            }

            return txLogExportDTOList;
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("[reportQuery] unknown exception, queryDto={}, querySql={}", queryDto,
              querySql);
            throw e;
        }
    }

    class TxLogExportRowMapper extends BeanPropertyRowMapper<TxLogExportDTO> {
        private final ZoneId zoneId;
        TxLogExportRowMapper(String timeZone) {
            this.zoneId = ZoneId.of(timeZone);
        }

        @Override
        public TxLogExportDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TxLogHeaderDTO headerDto = new TxLogHeaderRowMapper().mapRow(rs, rowNum);
            TxLogSummaryDTO summaryDto = new TxLogSummaryRowMapper().mapRow(rs, rowNum);
            TxLogExportDTO exportDto = new TxLogExportDTO();

            if (headerDto != null) {
                exportDto.setKernelTransactionLogId(headerDto.getId());
                exportDto.setPan(headerDto.getPan());
                exportDto.setAmount(headerDto.getAmount());
                exportDto.setAres(headerDto.getAres());
                exportDto.setRres(headerDto.getRres());
                exportDto.setTransTime(DateUtil.millisToDateTimeStr(headerDto.getTransTime(), zoneId));
                exportDto.setIssuerName(headerDto.getIssuerName());
                exportDto.setIssuerBankId(headerDto.getIssuerBankId());
                exportDto.setEncCardNumber(headerDto.getEnCardNumber());
                exportDto.setId(headerDto.getId());
                exportDto.setAcqBIn((headerDto.getAcqBin()));
            }
            if (summaryDto != null) {
                BeanUtils.copyProperties(summaryDto, exportDto);
                exportDto.setErrorReason(summaryDto.getOtpResult());
            }
            exportDto.setShoppingTime(DateUtil.utcToTwDateTimeStr(rs.getString("purchase_date"), zoneId));
            return exportDto;
        }
    }

    @Override
    public List<TxLogExportDTO> reportQueryByIds(IdsQueryDTO queryDto, boolean canSeePan) {
        List<Long> ids = queryDto.getIds();
        Map<String, Object> params = new HashMap<>();
        String conditionSql = "";
        if (ids != null && !ids.isEmpty()) {
            conditionSql = " where tx.id in (:ids) ";
            params.put("ids", ids);
        }
        String querySql = String.format(LOGS_BASE_SQL, SELECT_REPORT_COLUMNS);
        querySql += conditionSql + " order by tx.create_millis desc";

        try {
            List<TxLogExportDTO> data =
              npJdbcTemplate.query(querySql, params, new TxLogExportRowMapper(queryDto.getTimeZone()));
            if(canSeePan) {
                log.debug("[reportQueryByIds] decrypt pan");
                decryptPanInTxLogExportDTO(data);
            }
            return data;
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("[reportQueryByIds] unknown exception, ids={}",
                StringUtils.normalizeSpace(Arrays.toString(ids.toArray())));
            throw e;
        }
    }

    private void decryptPanInTxLogExportDTO(List<TxLogExportDTO> txLogExportDTOList) {
        txLogExportDTOList.forEach(txLogExportDTO -> {
            if (StringUtils.isNotEmpty(txLogExportDTO.getEncCardNumber())) {
                DecryptResultDTO decryptResultDTO = null;
                try {
                    decryptResultDTO = hsmPlugin
                        .decryptWithIssuerBankId(txLogExportDTO.getEncCardNumber(),
                            txLogExportDTO.getIssuerBankId());
                    txLogExportDTO.setPan(decryptResultDTO.getString());
                } catch (Exception e) {
                    log.warn("[decryptPanInTxLogExportDTO] decrypt pan failed. txLogId={}", txLogExportDTO.getId());
                } finally {
                    if (decryptResultDTO != null) {
                        decryptResultDTO.clearPlainText();
                    }
                }
            }
        });
    }
    private void decryptPanInTxLogHeaderDTO(List<TxLogHeaderDTO> txLogHeaderDTOList) {
        txLogHeaderDTOList.forEach(txLogExportDTO -> {
            if (StringUtils.isNotEmpty(txLogExportDTO.getEnCardNumber())) {
                DecryptResultDTO decryptResultDTO = null;
                try {
                    decryptResultDTO = hsmPlugin
                        .decryptWithIssuerBankId(txLogExportDTO.getEnCardNumber(),
                            txLogExportDTO.getIssuerBankId());
                    txLogExportDTO.setPan(decryptResultDTO.getString());
                } catch (Exception e) {
                    log.warn("[decryptPanInTxLogHeaderDTO] decrypt pan failed. txLogId={}", txLogExportDTO.getId());
                } finally {
                    if (decryptResultDTO != null) {
                        decryptResultDTO.clearPlainText();
                    }
                }
            }
        });
    }

}
