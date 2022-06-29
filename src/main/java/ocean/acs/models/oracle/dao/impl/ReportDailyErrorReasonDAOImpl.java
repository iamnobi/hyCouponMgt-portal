package ocean.acs.models.oracle.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.ReportDailyErrorReasonDAO;
import ocean.acs.models.data_object.entity.ReportDailyErrorReasonDO;
import ocean.acs.models.data_object.portal.ComplexStatisticsErrorReasonDO;
import ocean.acs.models.oracle.entity.ReportDailyErrorReason;

@Repository
@AllArgsConstructor
public class ReportDailyErrorReasonDAOImpl implements ReportDailyErrorReasonDAO {

    private static final String STATISTICS_ERROR_REASON_SQL = "select bank.name issuerBankName,"
            + " reason.year, reason.month, reason.day_of_month,\n"
            + " (select msg from error_code_mapping where code=reason.reason_code_1) reason1,\n"
            + " reason.reason_percentage_1 reasonPercentage1,\n"
            + " (select msg from error_code_mapping where code=reason.reason_code_2) reason2,\n"
            + " reason.reason_percentage_2 reasonPercentage2,\n"
            + " (select msg from error_code_mapping where code=reason.reason_code_3) reason3,\n"
            + " reason.reason_percentage_3 reasonPercentage3\n"
            + "from Report_Daily_Error_Reason reason\n"
            + "join issuer_bank bank on reason.issuer_bank_id = bank.id\n"
            + "where %s year = :year and month = :month and day_of_month = :day\n"
            + "order by bank.code asc";

    private final ocean.acs.models.oracle.repository.ReportDailyErrorReasonRepository repo;
    private final NamedParameterJdbcTemplate npJdbcTemplate;

    @Override
    public void saveAll(List<ReportDailyErrorReasonDO> reportDailyErrorReasonDoList) {
        List<ReportDailyErrorReason> reportDailyErrorReasonList = reportDailyErrorReasonDoList
                .stream().map(ReportDailyErrorReason::valueOf).collect(Collectors.toList());
        repo.saveAll(reportDailyErrorReasonList);
    }

    @Override
    public Optional<ReportDailyErrorReasonDO> findByIssuerBankIdAndYearAndMonthAndDay(
            Long issuerBankId, int year, int month, int day) {
        return repo.findByIssuerBankIdAndYearAndMonthAndDayOfMonth(issuerBankId, year, month, day)
                .map(ReportDailyErrorReasonDO::valueOf);
    }

    @Override
    public List<ComplexStatisticsErrorReasonDO> findByDataMillisBetween(long startMillis,
            long endMillis) {
        Map<String, Object> params = new HashMap<>();
        params.put("startMillis", startMillis);
        params.put("endMillis", endMillis);
        final String bankQuerySql = "";
        return doQuery(String.format(STATISTICS_ERROR_REASON_SQL, bankQuerySql), params);
    }

    @Override
    public List<ComplexStatisticsErrorReasonDO> findByIssuerBankIdAndDataMillisBetween(
            Long issuerBankId, long startMillis, long endMillis) {
        Map<String, Object> params = new HashMap<>();
        params.put("issuerBankId", issuerBankId);
        params.put("startMillis", startMillis);
        params.put("endMillis", endMillis);
        final String bankQuerySql = " reason.issuer_bank_id = :issuerBankId and ";
        return doQuery(String.format(STATISTICS_ERROR_REASON_SQL, bankQuerySql), params);
    }

    @Override
    public List<ComplexStatisticsErrorReasonDO> findByYearAndMonthAndDay(int year, int month,
            int day) {
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        params.put("day", day);

        final String bankQuerySql = "";
        return doQuery(String.format(STATISTICS_ERROR_REASON_SQL, bankQuerySql), params);
    }

    @Override
    public List<ComplexStatisticsErrorReasonDO> findByIssuerBankIdAndYearAndMonthAndDay(
            long issuerBankId, int year, int month, int day) {
        Map<String, Object> params = new HashMap<>();
        params.put("issuerBankId", issuerBankId);
        params.put("year", year);
        params.put("month", month);
        params.put("day", day);

        final String bankQuerySql = " reason.issuer_bank_id = :issuerBankId and ";
        return doQuery(String.format(STATISTICS_ERROR_REASON_SQL, bankQuerySql), params);
    }

    private List<ComplexStatisticsErrorReasonDO> doQuery(String sql, Map<String, Object> params) {
        return npJdbcTemplate.query(sql, params,
                (rs, rowNum) -> ComplexStatisticsErrorReasonDO.newInstance(
                        rs.getString("issuerBankName"), rs.getInt("year"), rs.getInt("month"),
                        rs.getInt("day_of_month"),
                        rs.getString("reason1") == null ? "無" : rs.getString("reason1"),
                        rs.getDouble("reasonPercentage1"),
                        rs.getString("reason2") == null ? "無" : rs.getString("reason2"),
                        rs.getDouble("reasonPercentage2"),
                        rs.getString("reason3") == null ? "無" : rs.getString("reason3"),
                        rs.getDouble("reasonPercentage3")));
    }
}
