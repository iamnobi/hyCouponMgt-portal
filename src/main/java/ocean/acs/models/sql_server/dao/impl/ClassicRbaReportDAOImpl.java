package ocean.acs.models.sql_server.dao.impl;

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
import ocean.acs.models.sql_server.entity.ClassicRbaReport;


@Log4j2
@Repository
@AllArgsConstructor
public class ClassicRbaReportDAOImpl implements ClassicRbaReportDAO {

    private static final String BY_DAY_CONDITION =
            " year = :year and month = :month and day_of_month = :dayOfMonth ";
    private static final String GROUP_BY_DAY = " group by :year, :month, :dayOfMonth ";

    private static final String BY_MONTH_CONDITION = " year = :year and month = :month ";
    private static final String GROUP_BY_MONTH = "group by :year, :month ";

    private static final String sql = "select sum(bin_range_f) as bin_range_f, \n"
                                    + " sum(bin_range_c) as bin_range_c, \n"
                                    + " sum(bin_range_r) as bin_range_r, \n"
                                    + " sum(block_code_f) as block_code_f, \n"
                                    + " sum(block_code_c) as block_code_c, \n"
                                    + " sum(block_code_r) as block_code_r, \n"
                                    + " sum(purchase_amount_f) as purchase_amount_f, \n"
                                    + " sum(purchase_amount_c) as purchase_amount_c, \n"
                                    + " sum(purchase_amount_r) as purchase_amount_r, \n"
                                    + " sum(cardholder_data_f) as cardholder_data_f, \n"
                                    + " sum(cardholder_data_c) as cardholder_data_c, \n"
                                    + " sum(cardholder_data_r) as cardholder_data_r, \n"
                                    + " sum(cumulative_amount_f) as cumulative_amount_f, \n"
                                    + " sum(cumulative_amount_c) as cumulative_amount_c, \n"
                                    + " sum(cumulative_amount_r) as cumulative_amount_r, \n"
                                    + " sum(cumulative_transaction_f) as cumulative_transaction_f, \n"
                                    + " sum(cumulative_transaction_c) as cumulative_transaction_c, \n"
                                    + " sum(cumulative_transaction_r) as cumulative_transaction_r, \n"
                                    + " sum(location_consistency_f) as location_consistency_f, \n"
                                    + " sum(location_consistency_c) as location_consistency_c, \n"
                                    + " sum(location_consistency_r) as location_consistency_r, \n"
                                    + " sum(browser_language_f) as browser_language_f, \n"
                                    + " sum(browser_language_c) as browser_language_c, \n"
                                    + " sum(browser_language_r) as browser_language_r, \n"
                                    + " sum(vpn_f) as vpn_f, \n"
                                    + " sum(vpn_c) as vpn_c, \n"
                                    + " sum(vpn_r) as vpn_r, \n"
                                    + " sum(proxy_f) as proxy_f, \n"
                                    + " sum(proxy_c) as proxy_c, \n"
                                    + " sum(proxy_r) as proxy_r, \n"
                                    + " sum(private_browsing_f) as private_browsing_f, \n"
                                    + " sum(private_browsing_c) as private_browsing_c, \n"
                                    + " sum(private_browsing_r) as private_browsing_r, \n"
                                    + " sum(device_variation_f) as device_variation_f, \n"
                                    + " sum(device_variation_c) as device_variation_c, \n"
                                    + " sum(device_variation_r) as device_variation_r, \n"
                                    + " sum(mcc_f) as mcc_f, \n"
                                    + " sum(mcc_c) as mcc_c, \n"
                                    + " sum(mcc_r) as mcc_r, \n"
                                    + " sum(recurring_payment_f) as recurring_payment_f, \n"
                                    + " sum(recurring_payment_c) as recurring_payment_c, \n"
                                    + " sum(recurring_payment_r) as recurring_payment_r, \n"
                                    + " sum(black_list_card_f) as black_list_card_f, \n"
                                    + " sum(black_list_card_c) as black_list_card_c, \n"
                                    + " sum(black_list_card_r) as black_list_card_r, \n"
                                    + " sum(black_list_ip_f) as black_list_ip_f, \n"
                                    + " sum(black_list_ip_c) as black_list_ip_c, \n"
                                    + " sum(black_list_ip_r) as black_list_ip_r, \n"
                                    + " sum(black_list_merchant_url_f) as black_list_merchant_url_f, \n"
                                    + " sum(black_list_merchant_url_c) as black_list_merchant_url_c, \n"
                                    + " sum(black_list_merchant_url_r) as black_list_merchant_url_r, \n"
                                    + " sum(black_list_device_id_f) as black_list_device_id_f, \n"
                                    + " sum(black_list_device_id_c) as black_list_device_id_c, \n"
                                    + " sum(black_list_device_id_r) as black_list_device_id_r, \n"
                                    + " sum(white_list_f) as white_list_f, \n"
                                    + " sum(white_list_c) as white_list_c, \n"
                                    + " sum(white_list_r) as white_list_r, \n"
                                    + " sum(attempt_f) as attempt_f, \n"
                                    + " sum(attempt_c) as attempt_c, \n"
                                    + " sum(attempt_r) as attempt_r \n"
                                    + " from classic_rba_report"
                                    + " where %s and issuer_bank_id is not null and delete_flag = 0 \n"
                                    + " %s ";

    private final ocean.acs.models.sql_server.repository.ClassicRbaReportRepository repo;
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
            return ClassicRbaReport.builder().binRangeFrictionless(rs.getLong("bin_range_f"))
                    .binRangeChallenge(rs.getLong("bin_range_c"))
                    .binRangeReject(rs.getLong("bin_range_r"))
                    .blockCodeFrictionless(rs.getLong("block_code_f"))
                    .blockCodeChallenge(rs.getLong("block_code_c"))
                    .blockCodeReject(rs.getLong("block_code_r"))
                    .purchaseAmountFrictionless(rs.getLong("purchase_amount_f"))
                    .purchaseAmountChallenge(rs.getLong("purchase_amount_c"))
                    .purchaseAmountReject(rs.getLong("purchase_amount_r"))
                    .cardholderDataFrictionless(rs.getLong("cardholder_data_f"))
                    .cardholderDataChallenge(rs.getLong("cardholder_data_c"))
                    .cardholderDataReject(rs.getLong("cardholder_data_r"))
                    .cumulativeAmountFrictionless(rs.getLong("cumulative_amount_f"))
                    .cumulativeAmountChallenge(rs.getLong("cumulative_amount_c"))
                    .cumulativeAmountReject(rs.getLong("cumulative_amount_r"))
                    .cumulativeTransactionFrictionless(rs.getLong("cumulative_transaction_f"))
                    .cumulativeTransactionChallenge(rs.getLong("cumulative_transaction_c"))
                    .cumulativeTransactionReject(rs.getLong("cumulative_transaction_r"))
                    .locationConsistencyFrictionless(rs.getLong("location_consistency_f"))
                    .locationConsistencyChallenge(rs.getLong("location_consistency_c"))
                    .locationConsistencyReject(rs.getLong("location_consistency_r"))
                    .browserLanguageFrictionless(rs.getLong("browser_language_f"))
                    .browserLanguageChallenge(rs.getLong("browser_language_c"))
                    .browserLanguageReject(rs.getLong("browser_language_r"))
                    .vpnFrictionless(rs.getLong("vpn_f")).vpnChallenge(rs.getLong("vpn_c"))
                    .vpnReject(rs.getLong("vpn_r")).proxyFrictionless(rs.getLong("proxy_f"))
                    .proxyChallenge(rs.getLong("proxy_c")).proxyReject(rs.getLong("proxy_r"))
                    .privateBrowsingFrictionless(rs.getLong("private_browsing_f"))
                    .privateBrowsingChallenge(rs.getLong("private_browsing_c"))
                    .privateBrowsingReject(rs.getLong("private_browsing_r"))
                    .deviceVariationFrictionless(rs.getLong("device_variation_f"))
                    .deviceVariationChallenge(rs.getLong("device_variation_c"))
                    .deviceVariationReject(rs.getLong("device_variation_r"))
                    .mccFrictionless(rs.getLong("mcc_f")).mccChallenge(rs.getLong("mcc_c"))
                    .mccReject(rs.getLong("mcc_r"))
                    .recurringPaymentFrictionless(rs.getLong("recurring_payment_f"))
                    .recurringPaymentChallenge(rs.getLong("recurring_payment_c"))
                    .recurringPaymentReject(rs.getLong("recurring_payment_r"))
                    .blackListCardFrictionless(rs.getLong("black_list_card_f"))
                    .blackListCardChallenge(rs.getLong("black_list_card_c"))
                    .blackListCardReject(rs.getLong("black_list_card_r"))
                    .blackListIpFrictionless(rs.getLong("black_list_ip_f"))
                    .blackListIpChallenge(rs.getLong("black_list_ip_c"))
                    .blackListIpReject(rs.getLong("black_list_ip_r"))
                    .blackListMerchantUrlFrictionless(rs.getLong("black_list_merchant_url_f"))
                    .blackListMerchantUrlChallenge(rs.getLong("black_list_merchant_url_c"))
                    .blackListMerchantUrlReject(rs.getLong("black_list_merchant_url_r"))
                    .blackListDeviceIdFrictionless(rs.getLong("black_list_device_id_f"))
                    .blackListDeviceIdChallenge(rs.getLong("black_list_device_id_c"))
                    .blackListDeviceIdReject(rs.getLong("black_list_device_id_r"))
                    .whiteListFrictionless(rs.getLong("white_list_f"))
                    .whiteListChallenge(rs.getLong("white_list_c"))
                    .whiteListReject(rs.getLong("white_list_r"))
                    .attemptFrictionless(rs.getLong("attempt_f"))
                    .attemptChallenge(rs.getLong("attempt_c"))
                    .attemptReject(rs.getLong("attempt_r")).build();
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
