package ocean.acs.models.sql_server.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.models.dao.ReportMonthErrorReasonDAO;
import ocean.acs.models.data_object.entity.ReportMonthErrorReasonDO;
import ocean.acs.models.data_object.portal.ComplexStatisticsErrorReasonDO;
import ocean.acs.models.sql_server.entity.ReportMonthErrorReason;

@Repository
@AllArgsConstructor
public class ReportMonthErrorReasonDAOImpl implements ReportMonthErrorReasonDAO {

    private static final String STATISTICS_ERROR_REASON_SQL = "select bank.name issuerBankName, \n"
            + " reason.year, reason.month,\n"
            + " (select msg from error_code_mapping where code=reason.reason_code_1) reason1,\n"
            + " reason.reason_percentage_1 reasonPercentage1,\n"
            + " (select msg from error_code_mapping where code=reason.reason_code_2) reason2,\n"
            + " reason.reason_percentage_2 reasonPercentage2,\n"
            + " (select msg from error_code_mapping where code=reason.reason_code_3) reason3,\n"
            + " reason.reason_percentage_3 reasonPercentage3\n"
            + "from report_month_error_reason reason\n"
            + "join issuer_bank bank on reason.issuer_bank_id = bank.id\n"
            + "where %s year = :year and month = :month\n"
            + "order by bank.code asc";

    private final ocean.acs.models.sql_server.repository.ReportMonthErrorReasonRepository repo;
    private final NamedParameterJdbcTemplate npJdbcTemplate;

    @Override
    public void saveAll(List<ReportMonthErrorReasonDO> reportDailyErrorReasonDoList) {
        List<ReportMonthErrorReason> reportDailyErrorReasonList = reportDailyErrorReasonDoList
                .stream().map(ReportMonthErrorReason::valueOf).collect(Collectors.toList());
        repo.saveAll(reportDailyErrorReasonList);
    }

    @Override
    public List<ComplexStatisticsErrorReasonDO> findByDataMillisBetween(long startMillis,
            long endMillis) {
        Map<String, Object> params = new HashMap<>();
        params.put("startMillis", startMillis);
        params.put("endMillis", endMillis);
        final String bankQuerySql = " reason.issuer_bank_id > 0 and "; // 不撈出id <= 0以下的系統預設資料
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
    public Optional<ReportMonthErrorReasonDO> findByIssuerBankIdAndYearAndMonth(Long issuerBankId,
            int year, int month) {
        return repo.findByIssuerBankIdAndYearAndMonth(issuerBankId, year, month)
                .map(ReportMonthErrorReasonDO::valueOf);
    }
    
    @Override
    public List<ComplexStatisticsErrorReasonDO> findByYearAndMonth(int year, int month) {
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("month", month);
        
        final String bankQuerySql = " reason.issuer_bank_id > 0 and "; // 不撈出id <= 0以下的系統預設資料
        return doQuery(String.format(STATISTICS_ERROR_REASON_SQL, bankQuerySql), params);
    }

    @Override
    public List<ComplexStatisticsErrorReasonDO> findByIssuerBankIdAndYearAndMonth(long issuerBankId,
            int year, int month) {
        Map<String, Object> params = new HashMap<>();
        params.put("issuerBankId", issuerBankId);
        params.put("year", year);
        params.put("month", month);
        
        final String bankQuerySql = " reason.issuer_bank_id = :issuerBankId and ";
        return doQuery(String.format(STATISTICS_ERROR_REASON_SQL, bankQuerySql), params);
    }
    
    private List<ComplexStatisticsErrorReasonDO> doQuery(String sql, Map<String, Object> params) {
        return npJdbcTemplate.query(sql, params,
                (rs, rowNum) -> ComplexStatisticsErrorReasonDO.newInstance(
                        rs.getString("issuerBankName"), rs.getInt("year"), rs.getInt("month"), null,
                        rs.getString("reason1") == null ? "無" : rs.getString("reason1"),
                        rs.getDouble("reasonPercentage1"),
                        rs.getString("reason2") == null ? "無" : rs.getString("reason2"),
                        rs.getDouble("reasonPercentage2"),
                        rs.getString("reason3") == null ? "無" : rs.getString("reason3"),
                        rs.getDouble("reasonPercentage3")));
    }
}
