package com.cherri.acs_portal.manager.impl;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.manager.ClassicRbaReportManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.ClassicRbaReportDO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class ClassicRbaReportManagerImpl implements ClassicRbaReportManager {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String QUERY_CLASSIC_RBA_REPORT_SQL = "select\n" +
      "    ISSUER_BANK_ID,\n" +
      "    \n" +
      "    0 as BIN_RANGE_F,\n" +
      "    s.BIN_RANGE_C,\n" +
      "    s.BIN_RANGE_R,\n" +
      "    \n" +
      "    0 as BLOCK_CODE_F,\n" +
      "    s.BLOCK_CODE_C,\n" +
      "    BLOCK_CODE_R,\n" +
      "    \n" + // RBA Start
      "    s.PURCHASE_AMOUNT_F,\n" +
      "    s.PURCHASE_AMOUNT_C,\n" +
      "    0 as PURCHASE_AMOUNT_R,\n" +
      "    \n" +
      "    s.CARDHOLDER_DATA_F,\n" +
      "    s.CARDHOLDER_DATA_C,\n" +
      "    0 as CARDHOLDER_DATA_R,\n" +
      "    \n" +
      "    s.CUMULATIVE_AMOUNT_F,\n" +
      "    s.CUMULATIVE_AMOUNT_C,\n" +
      "    0 as CUMULATIVE_AMOUNT_R,\n" +
      "    \n" +
      "    s.CUMULATIVE_TRANSACTION_F,\n" +
      "    s.CUMULATIVE_TRANSACTION_C,\n" +
      "    0 as CUMULATIVE_TRANSACTION_R,\n" +
      "    \n" +
      "    s.LOCATION_CONSISTENCY_F,\n" +
      "    s.LOCATION_CONSISTENCY_C,\n" +
      "    0 as LOCATION_CONSISTENCY_R,\n" +
      "    \n" +
      "    s.BROWSER_LANGUAGE_F,\n" +
      "    s.BROWSER_LANGUAGE_C,\n" +
      "    0 as BROWSER_LANGUAGE_R,\n" +
      "    \n" +
      "    s.VPN_F,\n" +
      "    s.VPN_C,\n" +
      "    0 as VPN_R,\n" +
      "    \n" +
      "    s.PROXY_F,\n" +
      "    s.PROXY_C,\n" +
      "    0 as PROXY_R,\n" +
      "    \n" +
      "    s.PRIVATE_BROWSING_F,\n" +
      "    s.PRIVATE_BROWSING_C,\n" +
      "    0 as PRIVATE_BROWSING_R,\n" +
      "    \n" +
      "    s.DEVICE_VARIATION_F,\n" +
      "    s.DEVICE_VARIATION_C,\n" +
      "    0 as DEVICE_VARIATION_R,\n" +
      "    \n" +
      "    s.MCC_F,\n" +
      "    s.MCC_C,\n" +
      "    0 as MCC_R,\n" +
      "    \n" +
      "    s.RECURRING_PAYMENT_F,\n" +
      "    s.RECURRING_PAYMENT_C,\n" +
      "    0 as RECURRING_PAYMENT_R,\n" +
      "    \n" + // RBA End
      "    0 as BLACK_LIST_CARD_F,\n" +
      "    s.BLACK_LIST_CARD_C,\n" +
      "    s.BLACK_LIST_CARD_R,\n" +
      "    \n" +
      "    0 as BLACK_LIST_IP_F,\n" +
      "    s.BLACK_LIST_IP_C,\n" +
      "    s.BLACK_LIST_IP_R,\n" +
      "    \n" +
      "    0 as BLACK_LIST_MERCHANT_URL_F,\n" +
      "    s.BLACK_LIST_MERCHANT_URL_C,\n" +
      "    s.BLACK_LIST_MERCHANT_URL_R,\n" +
      "    \n" +
      "    0 as BLACK_LIST_DEVICE_ID_F,\n" +
      "    s.BLACK_LIST_DEVICE_ID_C,\n" +
      "    s.BLACK_LIST_DEVICE_ID_R,\n" +
      "    \n" +
      "    \n" +
      "    s.WHITE_LIST_F,\n" +
      "    s.WHITE_LIST_C,\n" +
      "    0 as WHITE_LIST_R,\n" +
      "    \n" +
      "    s.ATTEMPT_F,\n" +
      "    s.ATTEMPT_C,\n" +
      "    0 as ATTEMPT_R\n" +
      "from (\n" +
      "    select \n" +
      "        ISSUER_BANK_ID, \n" +
      "        sum(case when ARES_RESULT_REASON_CODE != 1101 then 1 else 0 end) BIN_RANGE_C,\n" +
      "        sum(case when ARES_RESULT_REASON_CODE = 1101 then 1 else 0 end) BIN_RANGE_R,\n" +

      "        sum(case when \n" +
      "                       ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) BLOCK_CODE_C,\n" +
      "        sum(case when ARES_RESULT_REASON_CODE = 1201 then 1 else 0 end) BLOCK_CODE_R,\n" +

      "        \n" + // RBA Start
      "        sum(case when RBA_PURCHASE_AMOUNT_RES = 1 then 1 else 0 end) PURCHASE_AMOUNT_F,\n" +
      "        sum(case when \n" +
      "                       RBA_PURCHASE_AMOUNT_RES != 1 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) PURCHASE_AMOUNT_C,\n" +
      "        \n" +
      "        sum(case when RBA_CARDHOLDER_DATA_RES = 1 then 1 else 0 end) CARDHOLDER_DATA_F,\n" +
      "        sum(case when \n" +
      "                       RBA_CARDHOLDER_DATA_RES != 1 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) CARDHOLDER_DATA_C,\n" +
      "        \n" +
      "        sum(case when RBA_CUMULATIVE_AMOUNT_RES = 1 then 1 else 0 end) CUMULATIVE_AMOUNT_F,\n"
      +
      "        sum(case when \n" +
      "                       RBA_CUMULATIVE_AMOUNT_RES != 1 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) CUMULATIVE_AMOUNT_C,\n" +
      "        \n" +
      "        sum(case when RBA_CUMULATIVE_TX_RES = 1 then 1 else 0 end) CUMULATIVE_TRANSACTION_F,\n"
      +
      "        sum(case when \n" +
      "                       RBA_CUMULATIVE_TX_RES != 1 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) CUMULATIVE_TRANSACTION_C,\n" +
      "        \n" +
      "        sum(case when RBA_LOCATION_CONSISTENCY_RES = 1 then 1 else 0 end) LOCATION_CONSISTENCY_F,\n"
      +
      "        sum(case when \n" +
      "                       RBA_LOCATION_CONSISTENCY_RES != 1 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) LOCATION_CONSISTENCY_C,\n" +
      "        \n" +
      "        sum(case when RBA_BROWSER_LANGUAGE_RES = 1 then 1 else 0 end) BROWSER_LANGUAGE_F,\n"
      +
      "        sum(case when \n" +
      "                       RBA_BROWSER_LANGUAGE_RES != 1 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) BROWSER_LANGUAGE_C,\n" +
      "        \n" +
      "        sum(case when RBA_VPN_RES = 1 then 1 else 0 end) VPN_F,\n" +
      "        sum(case when \n" +
      "                       RBA_VPN_RES != 1 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) VPN_C,\n" +
      "        \n" +
      "        sum(case when RBA_PROXY_RES = 1 then 1 else 0 end) PROXY_F,\n" +
      "        sum(case when \n" +
      "                       RBA_PROXY_RES != 1 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) PROXY_C,\n" +
      "        \n" +
      "        sum(case when RBA_PRIVATE_BROWSING_RES = 1 then 1 else 0 end) PRIVATE_BROWSING_F,\n"
      +
      "        sum(case when \n" +
      "                       RBA_PRIVATE_BROWSING_RES != 1 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) PRIVATE_BROWSING_C,\n" +
      "        \n" +
      "        sum(case when RBA_DEVICE_VARIATION_RES = 1 then 1 else 0 end) DEVICE_VARIATION_F,\n"
      +
      "        sum(case when \n" +
      "                       RBA_DEVICE_VARIATION_RES != 1 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) DEVICE_VARIATION_C,\n" +
      "        \n" +
      "        sum(case when RBA_MCC_RES = 1 then 1 else 0 end) MCC_F,\n" +
      "        sum(case when \n" +
      "                       RBA_MCC_RES != 1 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) MCC_C,\n" +
      "        \n" +
      "        sum(case when RBA_RECURRING_PAYMENT_RES = 1 then 1 else 0 end) RECURRING_PAYMENT_F,\n"
      +
      "        sum(case when \n" +
      "                       RBA_RECURRING_PAYMENT_RES != 1 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                 then 1 else 0 end) RECURRING_PAYMENT_C,\n" +
      "        \n" + // RBA End

      "        sum(case when \n" +
      "                       ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1301 \n" +
      "                 then 1 else 0 end) BLACK_LIST_CARD_C,\n" +
      "        sum(case when ARES_RESULT_REASON_CODE = 1301 then 1 else 0 end) BLACK_LIST_CARD_R,\n"
      +
      "        \n" +
      "        sum(case when \n" +
      "                       ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1301 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1501 \n" +
      "                 then 1 else 0 end) BLACK_LIST_IP_C,\n" +
      "        sum(case when ARES_RESULT_REASON_CODE = 1501 then 1 else 0 end) BLACK_LIST_IP_R,\n" +
      "        \n" +
      "        sum(case when \n" +
      "                       ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1301 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1501 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1601 \n" +
      "                 then 1 else 0 end) BLACK_LIST_MERCHANT_URL_C,\n" +
      "        sum(case when ARES_RESULT_REASON_CODE = 1601 then 1 else 0 end) BLACK_LIST_MERCHANT_URL_R,\n"
      +
      "        \n" +
      "        sum(case when \n" +
      "                       ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1301 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1501 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1601 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1401 \n" +
      "                 then 1 else 0 end) BLACK_LIST_DEVICE_ID_C,\n" +
      "        sum(case when ARES_RESULT_REASON_CODE = 1401 then 1 else 0 end) BLACK_LIST_DEVICE_ID_R,\n"
      +
      "        \n" +
      "        sum(case when ARES_RESULT_REASON_CODE = 1900 then 1 else 0 end) WHITE_LIST_F,\n" +
      "        sum(case when \n" +
      "                       ARES_RESULT_REASON_CODE != 1900 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1301 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1501 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1601 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1401 \n" +
      "                 then 1 else 0 end) WHITE_LIST_C,\n" +
      "        \n" +
      "        sum(case when ARES_RESULT_REASON_CODE = 1910 then 1 else 0 end) ATTEMPT_F,\n" +
      "        sum(case when \n" +
      "                       ARES_RESULT_REASON_CODE != 1910 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1900 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1101 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1201 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1301 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1501 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1601 \n" +
      "                       and ARES_RESULT_REASON_CODE != 1401 \n" +
      "                 then 1 else 0 end) ATTEMPT_C\n" +
      "        \n" +
      "    from KERNEL_TRANSACTION_LOG\n" +
      "    where CREATE_MILLIS between :yesterdayMidnightMillis and :todayMidnightMillis \n" +
      "    and ISSUER_BANK_ID is not null\n" +
      "    group by ISSUER_BANK_ID\n" +
      ") s";

    @Override
    public List<ClassicRbaReportDO> queryClassicRbaReportDailyData() throws DatabaseException {

        long todayMidnightMillis = getTodayMidnightMillis();
        long yesterdayMidnightMillis = getYesterdayMidnightMillis();
        SqlParameterSource params = new MapSqlParameterSource()
          .addValue("todayMidnightMillis", todayMidnightMillis)
          .addValue("yesterdayMidnightMillis", yesterdayMidnightMillis);

        try {
            return namedParameterJdbcTemplate.query(QUERY_CLASSIC_RBA_REPORT_SQL,
              params, new ClassicRbaReportMapper());
        } catch (Exception e) {
            String message = "[queryClassicRbaReportDailyData] query classic rba daily report fail.";
            log.error(message, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    private static long getTodayMidnightMillis() {
        return toTwMillis(ZonedDateTime.now());
    }

    private static long getYesterdayMidnightMillis() {
        return toTwMillis(ZonedDateTime.now().minusDays(1L));
    }

    private static long toTwMillis(ZonedDateTime zonedDateTime) {
        return LocalDateTime.of(
          zonedDateTime.getYear(),
          zonedDateTime.getMonthValue(),
          zonedDateTime.getDayOfMonth(), 0, 0, 0)
          .atZone(EnvironmentConstants.ACS_TIMEZONE_ID).toInstant()
          .toEpochMilli();
    }

    public static class ClassicRbaReportMapper implements RowMapper<ClassicRbaReportDO> {

        @Override
        public ClassicRbaReportDO mapRow(ResultSet rs, int rowNum) throws SQLException {

            ZonedDateTime zdt = ZonedDateTime.now(EnvironmentConstants.ACS_TIMEZONE_ID).minusDays(1L);

            return ClassicRbaReportDO.builder()
              .year(zdt.getYear())
              .month(zdt.getMonthValue())
              .dayOfMonth(zdt.getDayOfMonth())
              .issuerBankId(rs.getLong("ISSUER_BANK_ID"))
              .binRangeFrictionless(rs.getLong("BIN_RANGE_F"))
              .binRangeChallenge(rs.getLong("BIN_RANGE_C"))
              .binRangeReject(rs.getLong("BIN_RANGE_R"))
              .blockCodeFrictionless(rs.getLong("BLOCK_CODE_F"))
              .blockCodeChallenge(rs.getLong("BLOCK_CODE_C"))
              .blockCodeReject(rs.getLong("BLOCK_CODE_F"))
              .purchaseAmountFrictionless(rs.getLong("PURCHASE_AMOUNT_F"))
              .purchaseAmountChallenge(rs.getLong("PURCHASE_AMOUNT_C"))
              .purchaseAmountReject(rs.getLong("PURCHASE_AMOUNT_R"))
              .cardholderDataFrictionless(rs.getLong("CARDHOLDER_DATA_F"))
              .cardholderDataChallenge(rs.getLong("CARDHOLDER_DATA_C"))
              .cardholderDataReject(rs.getLong("CARDHOLDER_DATA_R"))
              .cumulativeAmountFrictionless(rs.getLong("CUMULATIVE_AMOUNT_F"))
              .cumulativeAmountChallenge(rs.getLong("CUMULATIVE_AMOUNT_C"))
              .cumulativeAmountReject(rs.getLong("CUMULATIVE_AMOUNT_R"))
              .cumulativeTransactionFrictionless(rs.getLong("CUMULATIVE_TRANSACTION_F"))
              .cumulativeTransactionChallenge(rs.getLong("CUMULATIVE_TRANSACTION_C"))
              .cumulativeTransactionReject(rs.getLong("CUMULATIVE_TRANSACTION_R"))
              .locationConsistencyFrictionless(rs.getLong("LOCATION_CONSISTENCY_F"))
              .locationConsistencyChallenge(rs.getLong("LOCATION_CONSISTENCY_C"))
              .locationConsistencyReject(rs.getLong("LOCATION_CONSISTENCY_R"))
              .browserLanguageFrictionless(rs.getLong("BROWSER_LANGUAGE_F"))
              .browserLanguageChallenge(rs.getLong("BROWSER_LANGUAGE_C"))
              .browserLanguageReject(rs.getLong("BROWSER_LANGUAGE_R"))
              .vpnFrictionless(rs.getLong("VPN_F"))
              .vpnChallenge(rs.getLong("VPN_C"))
              .vpnReject(rs.getLong("VPN_R"))
              .proxyFrictionless(rs.getLong("PROXY_F"))
              .proxyChallenge(rs.getLong("PROXY_C"))
              .proxyReject(rs.getLong("PROXY_R"))
              .privateBrowsingFrictionless(rs.getLong("PRIVATE_BROWSING_F"))
              .privateBrowsingChallenge(rs.getLong("PRIVATE_BROWSING_C"))
              .privateBrowsingReject(rs.getLong("PRIVATE_BROWSING_R"))
              .deviceVariationFrictionless(rs.getLong("DEVICE_VARIATION_F"))
              .deviceVariationChallenge(rs.getLong("DEVICE_VARIATION_C"))
              .deviceVariationReject(rs.getLong("DEVICE_VARIATION_R"))
              .mccFrictionless(rs.getLong("MCC_F"))
              .mccChallenge(rs.getLong("MCC_C"))
              .mccReject(rs.getLong("MCC_R"))
              .recurringPaymentFrictionless(rs.getLong("RECURRING_PAYMENT_F"))
              .recurringPaymentChallenge(rs.getLong("RECURRING_PAYMENT_C"))
              .recurringPaymentReject(rs.getLong("RECURRING_PAYMENT_R"))
              .blackListCardFrictionless(rs.getLong("BLACK_LIST_CARD_F"))
              .blackListCardChallenge(rs.getLong("BLACK_LIST_CARD_C"))
              .blackListCardReject(rs.getLong("BLACK_LIST_CARD_R"))
              .blackListIpFrictionless(rs.getLong("BLACK_LIST_IP_F"))
              .blackListIpChallenge(rs.getLong("BLACK_LIST_IP_C"))
              .blackListIpReject(rs.getLong("BLACK_LIST_IP_R"))
              .blackListMerchantUrlFrictionless(rs.getLong("BLACK_LIST_MERCHANT_URL_F"))
              .blackListMerchantUrlChallenge(rs.getLong("BLACK_LIST_MERCHANT_URL_C"))
              .blackListMerchantUrlReject(rs.getLong("BLACK_LIST_MERCHANT_URL_R"))
              .blackListDeviceIdFrictionless(rs.getLong("BLACK_LIST_DEVICE_ID_F"))
              .blackListDeviceIdChallenge(rs.getLong("BLACK_LIST_DEVICE_ID_C"))
              .blackListDeviceIdReject(rs.getLong("BLACK_LIST_DEVICE_ID_R"))
              .whiteListFrictionless(rs.getLong("WHITE_LIST_F"))
              .whiteListChallenge(rs.getLong("WHITE_LIST_C"))
              .whiteListReject(rs.getLong("WHITE_LIST_R"))
              .attemptFrictionless(rs.getLong("ATTEMPT_F"))
              .attemptChallenge(rs.getLong("ATTEMPT_C"))
              .attemptReject(rs.getLong("ATTEMPT_R"))
              .build();
        }
    }


}
