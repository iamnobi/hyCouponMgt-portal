package ocean.acs.models.oracle.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.commons.enumerator.BrowserErrorCodeEnum;
import ocean.acs.models.dao.BrowserErrorLogDAO;
import ocean.acs.models.data_object.portal.BrowserErrorLogResultDO;

@Repository
@AllArgsConstructor
public class BrowserErrorLogDAOImpl implements BrowserErrorLogDAO {

    private static final String DAY_REPORT_COLUMNS =
            " ISSUER_BANK_ID, BROWSER_TYPE, YEAR, MONTH, DAY_OF_MONTH, \n"
                    + "    sum(A) CLIENT_FIRST_CREQ_TIMEOUT, \n"
                    + "    sum(B) CHALLENGE_OPERATION_TIMEOUT, \n"
                    + "    sum(C) CREQ_INVALID_ARGS, \n"
                    + "    sum(D) CHALLENGE_SYSTEM_ABNORMAL \n";

    private static final String DAY_REPORT_GROUP_BY =
            " group by ISSUER_BANK_ID, BROWSER_TYPE, YEAR, MONTH, DAY_OF_MONTH ";

    private static final String MONTH_REPORT_COLUMNS =
            " ISSUER_BANK_ID, BROWSER_TYPE, YEAR, MONTH, null as DAY_OF_MONTH, \n"
                    + "    sum(A) CLIENT_FIRST_CREQ_TIMEOUT, \n"
                    + "    sum(B) CHALLENGE_OPERATION_TIMEOUT, \n"
                    + "    sum(C) CREQ_INVALID_ARGS, \n"
                    + "    sum(D) CHALLENGE_SYSTEM_ABNORMAL \n";

    private static final String MONTH_REPORT_GROUP_BY =
            " group by ISSUER_BANK_ID, BROWSER_TYPE, YEAR, MONTH ";

    private static final String QUERY_BROWSER_ERROR_LOG_SQL =
            "select %s from ( \n" + "    select * from BROWSER_ERROR_LOG \n" + "    pivot (\n"
                    + "        sum(ERROR_CODE_DAY_COUNT) \n" + "        for ERROR_CODE \n"
                    + "        in (4000 A, 4001 B, 4002 C, 4003 D) \n" + "        ) \n"
                    + "    where ISSUER_BANK_ID = :issuerBankId \n"
                    + "    and DATA_MILLIS between :startMillis and :endMillis \n" + ") " + " %s";

    private static final String BROWSER_ERROR_LOG_QUERY_MONTH = 
            "select ISSUER_BANK_ID, BROWSER_TYPE, YEAR, MONTH, null as DAY_OF_MONTH, \n" + 
            "    sum(A) CLIENT_FIRST_CREQ_TIMEOUT, \n" + 
            "    sum(B) CHALLENGE_OPERATION_TIMEOUT, \n" + 
            "    sum(C) CREQ_INVALID_ARGS, \n" + 
            "    sum(D) CHALLENGE_SYSTEM_ABNORMAL \n" + 
            "from ( \n" + 
            "    select * from BROWSER_ERROR_LOG \n" + 
            "    pivot (\n" + 
            "        sum(ERROR_CODE_DAY_COUNT) \n" + 
            "        for ERROR_CODE \n" + 
            "        in (4000 A, 4001 B, 4002 C, 4003 D) \n" + 
            "        ) \n" + 
            "    where ISSUER_BANK_ID = :issuerBankId \n" + 
            "    and YEAR = :year and MONTH = :month\n" + 
            ") group by ISSUER_BANK_ID, BROWSER_TYPE, YEAR, MONTH";

    private static final String BROWSER_ERROR_LOG_QUERY_DAY = 
            "select ISSUER_BANK_ID, BROWSER_TYPE, YEAR, MONTH, DAY_OF_MONTH, \n" + 
            "    sum(A) CLIENT_FIRST_CREQ_TIMEOUT, \n" + 
            "    sum(B) CHALLENGE_OPERATION_TIMEOUT, \n" + 
            "    sum(C) CREQ_INVALID_ARGS, \n" + 
            "    sum(D) CHALLENGE_SYSTEM_ABNORMAL \n" + 
            "from ( \n" + 
            "    select * from BROWSER_ERROR_LOG \n" + 
            "    pivot (\n" + 
            "        sum(ERROR_CODE_DAY_COUNT) \n" + 
            "        for ERROR_CODE \n" + 
            "        in (4000 A, 4001 B, 4002 C, 4003 D) \n" + 
            "        ) \n" + 
            "    where ISSUER_BANK_ID = :issuerBankId \n" + 
            "    and YEAR = :year and MONTH = :month and DAY_OF_MONTH = :day \n" + 
            ") group by ISSUER_BANK_ID, BROWSER_TYPE, YEAR, MONTH, DAY_OF_MONTH";

    private final ocean.acs.models.oracle.repository.BrowserErrorLogRepository repo;
    private final NamedParameterJdbcTemplate npJdbcTemplate;

    @Override
    public boolean generateDailyBrowserErrorLog(String operator, List<Integer> errorCodes) {
        repo.generateDailyBrowserErrorLog(operator, errorCodes);
        return true;
    }

    @Override
    public List<BrowserErrorLogResultDO> findByIssuerBankIdAndYearAndMonth(Long issuerBankId,
            long startMillis, long endMillis) {
        Map<String, Object> params = new HashMap<>();
        params.put("issuerBankId", issuerBankId);
        params.put("startMillis", startMillis);
        params.put("endMillis", endMillis);

        String queryByMonthSql = String.format(QUERY_BROWSER_ERROR_LOG_SQL, MONTH_REPORT_COLUMNS,
                MONTH_REPORT_GROUP_BY);

        return npJdbcTemplate.query(queryByMonthSql, params,
                new BrowserErrorLogResultDORowMapper());
    }

    @Override
    public List<BrowserErrorLogResultDO> findByIssuerBankIdAndYearAndMonthAndDay(Long issuerBankId,
            long startMillis, long endMillis) {

        Map<String, Object> params = new HashMap<>();
        params.put("issuerBankId", issuerBankId);
        params.put("startMillis", startMillis);
        params.put("endMillis", endMillis);

        String queryByDaySql =
                String.format(QUERY_BROWSER_ERROR_LOG_SQL, DAY_REPORT_COLUMNS, DAY_REPORT_GROUP_BY);

        return npJdbcTemplate.query(queryByDaySql, params, new BrowserErrorLogResultDORowMapper());
    }

    private static class BrowserErrorLogResultDORowMapper
            implements RowMapper<BrowserErrorLogResultDO> {
        @Override
        public BrowserErrorLogResultDO mapRow(ResultSet rs, int rowNum) throws SQLException {
            BrowserErrorLogResultDO dto = new BrowserErrorLogResultDO();
            dto.setIssuerBankId(rs.getLong("ISSUER_BANK_ID"));
            dto.setBrowserType(rs.getString("BROWSER_TYPE"));
            dto.setYear(rs.getInt("YEAR"));
            dto.setMonth(rs.getInt("MONTH"));
            dto.setDay(rs.getInt("DAY_OF_MONTH"));
            dto.setClientFirstChallengeRequestTimeoutCount(
                    rs.getInt(BrowserErrorCodeEnum.CLIENT_FIRST_CREQ_TIMEOUT.name()));
            dto.setChallengeOperationTimeoutCount(
                    rs.getInt(BrowserErrorCodeEnum.CHALLENGE_OPERATION_TIMEOUT.name()));
            dto.setCreqInvalidArgsCount(rs.getInt(BrowserErrorCodeEnum.CREQ_INVALID_ARGS.name()));
            dto.setChallengeSystemAbnormalCount(
                    rs.getInt(BrowserErrorCodeEnum.CHALLENGE_SYSTEM_ABNORMAL.name()));
            return dto;
        }
    }

    @Override
    public List<BrowserErrorLogResultDO> findByIssuerBankIdAndYearAndMonth(long issuerBankId,
            int year, int month) {
        Map<String, Object> params = new HashMap<>();
        params.put("issuerBankId", issuerBankId);
        params.put("year", year);
        params.put("month", month);

        return npJdbcTemplate.query(BROWSER_ERROR_LOG_QUERY_MONTH, params, 
                new BrowserErrorLogResultDORowMapper());
    }

    @Override
    public List<BrowserErrorLogResultDO> findByIssuerBankIdAndYearAndMonthAndDay(long issuerBankId,
            int year, int month, int day) {
        Map<String, Object> params = new HashMap<>();
        params.put("issuerBankId", issuerBankId);
        params.put("year", year);
        params.put("month", month);
        params.put("day", day);

        return npJdbcTemplate.query(BROWSER_ERROR_LOG_QUERY_DAY, params, 
                new BrowserErrorLogResultDORowMapper());
    }
}
