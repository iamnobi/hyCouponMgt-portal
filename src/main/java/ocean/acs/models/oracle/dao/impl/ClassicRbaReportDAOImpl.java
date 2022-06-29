package ocean.acs.models.oracle.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.ClassicRbaReportDAO;
import ocean.acs.models.data_object.entity.ClassicRbaReportDO;
import ocean.acs.models.oracle.entity.ClassicRbaReport;


@Log4j2
@Repository
@AllArgsConstructor
public class ClassicRbaReportDAOImpl implements ClassicRbaReportDAO {

    private static final String BY_DAY_CONDITION =
            " YEAR = :year and MONTH = :month and DAY_OF_MONTH = :dayOfMonth ";
    private static final String GROUP_BY_DAY = " group by :year, :month, :dayOfMonth ";

    private static final String BY_MONTH_CONDITION = " YEAR = :year and MONTH = :month ";
    private static final String GROUP_BY_MONTH = "group by :year, :month ";

    private static final String sql = "select " + " sum(BIN_RANGE_F) as BIN_RANGE_F, \n"
            + " sum(BIN_RANGE_C) as BIN_RANGE_C, \n" + " sum(BIN_RANGE_R) as BIN_RANGE_R, \n"
            + " sum(BLOCK_CODE_F) as BLOCK_CODE_F, \n" + " sum(BLOCK_CODE_C) as BLOCK_CODE_C, \n"
            + " sum(BLOCK_CODE_R) as BLOCK_CODE_R, \n"
            + " sum(PURCHASE_AMOUNT_F) as PURCHASE_AMOUNT_F, \n"
            + " sum(PURCHASE_AMOUNT_C) as PURCHASE_AMOUNT_C, \n"
            + " sum(PURCHASE_AMOUNT_R) as PURCHASE_AMOUNT_R, \n"
            + " sum(CARDHOLDER_DATA_F) as CARDHOLDER_DATA_F, \n"
            + " sum(CARDHOLDER_DATA_C) as CARDHOLDER_DATA_C, \n"
            + " sum(CARDHOLDER_DATA_R) as CARDHOLDER_DATA_R, \n"
            + " sum(CUMULATIVE_AMOUNT_F) as CUMULATIVE_AMOUNT_F, \n"
            + " sum(CUMULATIVE_AMOUNT_C) as CUMULATIVE_AMOUNT_C, \n"
            + " sum(CUMULATIVE_AMOUNT_R) as CUMULATIVE_AMOUNT_R, \n"
            + " sum(CUMULATIVE_TRANSACTION_F) as CUMULATIVE_TRANSACTION_F, \n"
            + " sum(CUMULATIVE_TRANSACTION_C) as CUMULATIVE_TRANSACTION_C, \n"
            + " sum(CUMULATIVE_TRANSACTION_R) as CUMULATIVE_TRANSACTION_R, \n"
            + " sum(LOCATION_CONSISTENCY_F) as LOCATION_CONSISTENCY_F, \n"
            + " sum(LOCATION_CONSISTENCY_C) as LOCATION_CONSISTENCY_C, \n"
            + " sum(LOCATION_CONSISTENCY_R) as LOCATION_CONSISTENCY_R, \n"
            + " sum(BROWSER_LANGUAGE_F) as BROWSER_LANGUAGE_F, \n"
            + " sum(BROWSER_LANGUAGE_C) as BROWSER_LANGUAGE_C, \n"
            + " sum(BROWSER_LANGUAGE_R) as BROWSER_LANGUAGE_R, \n" + " sum(VPN_F) as VPN_F, \n"
            + " sum(VPN_C) as VPN_C, \n" + " sum(VPN_R) as VPN_R, \n"
            + " sum(PROXY_F) as PROXY_F, \n" + " sum(PROXY_C) as PROXY_C, \n"
            + " sum(PROXY_R) as PROXY_R, \n" + " sum(PRIVATE_BROWSING_F) as PRIVATE_BROWSING_F, \n"
            + " sum(PRIVATE_BROWSING_C) as PRIVATE_BROWSING_C, \n"
            + " sum(PRIVATE_BROWSING_R) as PRIVATE_BROWSING_R, \n"
            + " sum(DEVICE_VARIATION_F) as DEVICE_VARIATION_F, \n"
            + " sum(DEVICE_VARIATION_C) as DEVICE_VARIATION_C, \n"
            + " sum(DEVICE_VARIATION_R) as DEVICE_VARIATION_R, \n" + " sum(MCC_F) as MCC_F, \n"
            + " sum(MCC_C) as MCC_C, \n" + " sum(MCC_R) as MCC_R, \n"
            + " sum(RECURRING_PAYMENT_F) as RECURRING_PAYMENT_F, \n"
            + " sum(RECURRING_PAYMENT_C) as RECURRING_PAYMENT_C, \n"
            + " sum(RECURRING_PAYMENT_R) as RECURRING_PAYMENT_R, \n"
            + " sum(BLACK_LIST_CARD_F) as BLACK_LIST_CARD_F, \n"
            + " sum(BLACK_LIST_CARD_C) as BLACK_LIST_CARD_C, \n"
            + " sum(BLACK_LIST_CARD_R) as BLACK_LIST_CARD_R, \n"
            + " sum(BLACK_LIST_IP_F) as BLACK_LIST_IP_F, \n"
            + " sum(BLACK_LIST_IP_C) as BLACK_LIST_IP_C, \n"
            + " sum(BLACK_LIST_IP_R) as BLACK_LIST_IP_R, \n"
            + " sum(BLACK_LIST_MERCHANT_URL_F) as BLACK_LIST_MERCHANT_URL_F, \n"
            + " sum(BLACK_LIST_MERCHANT_URL_C) as BLACK_LIST_MERCHANT_URL_C, \n"
            + " sum(BLACK_LIST_MERCHANT_URL_R) as BLACK_LIST_MERCHANT_URL_R, \n"
            + " sum(BLACK_LIST_DEVICE_ID_F) as BLACK_LIST_DEVICE_ID_F, \n"
            + " sum(BLACK_LIST_DEVICE_ID_C) as BLACK_LIST_DEVICE_ID_C, \n"
            + " sum(BLACK_LIST_DEVICE_ID_R) as BLACK_LIST_DEVICE_ID_R, \n"
            + " sum(WHITE_LIST_F) as WHITE_LIST_F, \n" + " sum(WHITE_LIST_C) as WHITE_LIST_C, \n"
            + " sum(WHITE_LIST_R) as WHITE_LIST_R, \n" + " sum(ATTEMPT_F) as ATTEMPT_F, \n"
            + " sum(ATTEMPT_C) as ATTEMPT_C, \n" + " sum(ATTEMPT_R) as ATTEMPT_R \n"
            + " from CLASSIC_RBA_REPORT " + " where %s \n" + " and ISSUER_BANK_ID is not null \n"
            + " and DELETE_FLAG = 0 \n" + " %s ";

    private final ocean.acs.models.oracle.repository.ClassicRbaReportRepository repo;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<ClassicRbaReportDO> findByGroupByDayOfMonth(int year, int month, int dayOfMonth)
            throws DatabaseException {
        try {

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("year", year);
            params.addValue("month", month);
            params.addValue("dayOfMonth", dayOfMonth);

            String dayReportSql = String.format(sql, BY_DAY_CONDITION, GROUP_BY_DAY);

            List<ClassicRbaReport> classicRbaReportList = namedParameterJdbcTemplate
                    .query(dayReportSql, params, new ClassicRbaReportMapper());

            if (CollectionUtils.isNotEmpty(classicRbaReportList)) {
                return Optional.ofNullable(classicRbaReportList.get(0))
                        .map(ClassicRbaReportDO::valueOf);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("[findByDay] error message={}", e.getMessage());
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<ClassicRbaReportDO> findByGroupByMonth(int year, int month)
            throws DatabaseException {
        try {

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("year", year);
            params.addValue("month", month);

            String monthReportSql = String.format(sql, BY_MONTH_CONDITION, GROUP_BY_MONTH);

            List<ClassicRbaReport> classicRbaReportList = namedParameterJdbcTemplate
                    .query(monthReportSql, params, new ClassicRbaReportMapper());

            if (CollectionUtils.isNotEmpty(classicRbaReportList)) {
                return Optional.ofNullable(classicRbaReportList.get(0))
                        .map(ClassicRbaReportDO::valueOf);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("[findByDay] error message={}", e.getMessage());
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    private static class ClassicRbaReportMapper implements RowMapper<ClassicRbaReport> {

        @Override
        public ClassicRbaReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            return ClassicRbaReport.builder().binRangeFrictionless(rs.getLong("BIN_RANGE_F"))
                    .binRangeChallenge(rs.getLong("BIN_RANGE_C"))
                    .binRangeReject(rs.getLong("BIN_RANGE_R"))
                    .blockCodeFrictionless(rs.getLong("BLOCK_CODE_F"))
                    .blockCodeChallenge(rs.getLong("BLOCK_CODE_C"))
                    .blockCodeReject(rs.getLong("BLOCK_CODE_R"))
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
                    .vpnFrictionless(rs.getLong("VPN_F")).vpnChallenge(rs.getLong("VPN_C"))
                    .vpnReject(rs.getLong("VPN_R")).proxyFrictionless(rs.getLong("PROXY_F"))
                    .proxyChallenge(rs.getLong("PROXY_C")).proxyReject(rs.getLong("PROXY_R"))
                    .privateBrowsingFrictionless(rs.getLong("PRIVATE_BROWSING_F"))
                    .privateBrowsingChallenge(rs.getLong("PRIVATE_BROWSING_C"))
                    .privateBrowsingReject(rs.getLong("PRIVATE_BROWSING_R"))
                    .deviceVariationFrictionless(rs.getLong("DEVICE_VARIATION_F"))
                    .deviceVariationChallenge(rs.getLong("DEVICE_VARIATION_C"))
                    .deviceVariationReject(rs.getLong("DEVICE_VARIATION_R"))
                    .mccFrictionless(rs.getLong("MCC_F")).mccChallenge(rs.getLong("MCC_C"))
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
                    .attemptReject(rs.getLong("ATTEMPT_R")).build();
        }
    }

    @Override
    public void deleteYesterdayReport() throws DatabaseException {
        LocalDate localDate = LocalDate.now(ZoneId.of("Asia/Taipei")).minusDays(1L);
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int dayOfMonth = localDate.getDayOfMonth();

        try {
            List<ClassicRbaReport> classicRbaReportList =
                    repo.findByYearAndMonthAndDayOfMonthAndDeleteFlagFalse(year, month, dayOfMonth);
            if (CollectionUtils.isEmpty(classicRbaReportList)) {
                return;
            }
            classicRbaReportList = classicRbaReportList.stream().peek(e -> e.setDeleteFlag(true))
                    .collect(Collectors.toList());
            repo.saveAll(classicRbaReportList);
        } catch (Exception e) {
            log.error("[deleteYesterdayReport] error message={}", e.getMessage());
            throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }

    }

    @Override
    public boolean batchSave(List<ClassicRbaReportDO> classicRbaReportDoList)
            throws DatabaseException {
        try {
            List<ClassicRbaReport> classicRbaRepoList = classicRbaReportDoList.stream()
                    .map(ClassicRbaReport::valueOf).collect(Collectors.toList());
            repo.saveAll(classicRbaRepoList);
            return true;
        } catch (Exception e) {
            log.error("[batchSave] error message={}", e.getMessage());
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

}
