package com.cherri.acs_portal.manager.impl;

import com.cherri.acs_portal.config.FiscProperties;
import com.cherri.acs_portal.dto.housekeeping.BackupRowDto;
import com.cherri.acs_portal.dto.housekeeping.TabColumnDto;
import com.cherri.acs_portal.dto.housekeeping.TableColumnInfoDto;
import com.cherri.acs_portal.manager.HouseKeepingManager;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.BackupTableEnum;
import ocean.acs.commons.exception.OceanException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class HouseKeepingManagerImpl implements HouseKeepingManager {

    @Value("${spring.datasource.url}")
    private String acsDatabaseUrl;
    @Value("${spring.datasource.username}")
    private String acsDatabaseUsername;
    @Value("${spring.datasource.password}")
    private String acsDatabasePassword;

    @Value("${spring.datasource.connect-data-type}")
    private String acsDatabaseConnectDataType;

    @Autowired
    private FiscProperties fiscProperties;

    private final static String DB_LINK = "DBLINK";

    @Autowired
    @Qualifier("acsPortalNamedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate acsPortalNamedParameterJdbcTemplate;

    @Autowired
    @Qualifier("prod41NamedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate prod41NamedParameterJdbcTemplate;

    private static final String SELECT_USER_TAB_COLUMNS =
      "select TABLE_NAME, COLUMN_NAME, DATA_TYPE from USER_TAB_COLUMNS " +
        " where TABLE_NAME in ( :backupTableSet ) " +
        " order by TABLE_NAME";

    private static final String SELECT_DISTINCT_TABLE_NAMES =
      "select distinct(TABLE_NAME) from USER_TABLES";

    private static final String SELECT_FK_DEPENDENCY_LEVEL =
      "select REF_TABLE, max(\"LEVEL\") as \"LEVEL\" from (\n" +
        "    select c.*, LEVEL FROM (\n" +
        "        select\n" +
        "            a.TABLE_NAME \"REF_TABLE\",\n" +
        "            b.TABLE_NAME \"REF_BY\"\n" +
        "        from USER_CONSTRAINTS a, USER_CONSTRAINTS b \n" +
        "        where b.CONSTRAINT_TYPE = 'R'\n" +
        "        and a.CONSTRAINT_NAME = b.R_CONSTRAINT_NAME\n" +
        "        and a.TABLE_NAME in ( :backupTableSet )\n" +
        "    ) c\n" +
        "    CONNECT BY PRIOR REF_TABLE = REF_BY\n" +
        ") GROUP BY REF_TABLE";

    @Override
    public List<TabColumnDto> getTabColumnList(Set<String> backupTableSet) {
        log.debug("[getTabColumnList] execute SQL={}", SELECT_USER_TAB_COLUMNS);

        try {
            return acsPortalNamedParameterJdbcTemplate.query(
              SELECT_USER_TAB_COLUMNS,
              new MapSqlParameterSource("backupTableSet", backupTableSet),
              (rs, rowNum) -> TabColumnDto.builder()
                .tableName(rs.getString("TABLE_NAME"))
                .columnName(rs.getString("COLUMN_NAME"))
                .dataType(rs.getString("DATA_TYPE"))
                .build());
        } catch (Exception e) {
            String message = String.format(
              "[getTableColumnList] unknown exception, get backup table info fail, table=%s",
              String.join(",", backupTableSet));
            log.error(message, e);
            throw new OceanException(message, e);
        }

    }

    @Override
    public Map<BackupTableEnum, Integer> getBackupTableForeignKeyDependencyLevel(
      Set<String> backupTableSet) {
        log.debug("[getBackupTableForeignKeyDependencyLevel] execute SQL={}",
          SELECT_FK_DEPENDENCY_LEVEL);

        try {
            return acsPortalNamedParameterJdbcTemplate.query(
              SELECT_FK_DEPENDENCY_LEVEL,
              new MapSqlParameterSource("backupTableSet", backupTableSet),
              rs -> {
                  Map<BackupTableEnum, Integer> map = new HashMap<>();
                  while (rs.next()) {
                      map.put(BackupTableEnum.valueOf(rs.getString("REF_TABLE")),
                        rs.getInt("LEVEL"));
                  }
                  return map;
              });
        } catch (Exception e) {
            String message = "[getBackupTableForeignKeyDependencyLevel] unknown exception, get foreign key dependency fail";
            log.error(message, e);
            throw new OceanException(message, e);
        }
    }

    @Override
    public List<BackupRowDto> getBackupRowDtoList(TableColumnInfoDto tableColumnInfoDto) {
        String insertStatementAlias = "INSERT_STATEMENT";
        String fileAlias = "FILE_BLOB";
        String tableName = tableColumnInfoDto.getTableName();
        List<String> columnNameList = tableColumnInfoDto.getColumnInfoDtoList().stream()
          .filter(e -> !isBlob(e))
          .map(TableColumnInfoDto.ColumnInfoDto::getColumnName)
          .collect(Collectors.toList());

        String sql =
          "select ID, ('insert into " + tableName + " ( " + String.join(", ", columnNameList)
            + " ) " +
            " values ( '" + composeInsertIntoValues(columnNameList) + " ' );') as "
            + insertStatementAlias +
            getBlobColumnSql(tableColumnInfoDto, fileAlias) +
            " from " + tableName + "" +
            " where (to_date('1970/01/01', 'YYYY/MM/DD') + (CREATE_MILLIS/1000/60/60/24)) " +
            " between trunc(SYSDATE - 1) and trunc(SYSDATE)";

        log.debug("[getBackupRowDtoList] execute SQL={}", sql);

        try {
            return acsPortalNamedParameterJdbcTemplate.query(
              sql, (rs, rowNum) -> {
                  BackupRowDto.BackupRowDtoBuilder builder = BackupRowDto.builder();
                  builder.tableName(tableName)
                    .id(rs.getString("ID"))
                    .insertStatement(rs.getString(insertStatementAlias));
                  if (tableColumnInfoDto.isHasBlob()) {
                      builder.file(rs.getBytes(fileAlias));
                  }
                  return builder.build();
              });
        } catch (Exception e) {
            String message = String.format(
              "[getInsertStatement] unknown exception, get insert statement fail, sql=%s",
              sql);
            log.error(message, e);
            throw new OceanException(message, e);
        }
    }

    private boolean isBlob(TableColumnInfoDto.ColumnInfoDto columnInfoDto) {
        return JDBCType.BLOB.getName().equalsIgnoreCase(columnInfoDto.getDataType());
    }

    private String getBlobColumnSql(TableColumnInfoDto tableColumnInfoDto, String alias) {
        isTableExceedOneBlobColumn(tableColumnInfoDto);
        return tableColumnInfoDto.getColumnInfoDtoList().stream()
          .filter(this::isBlob)
          .findAny()
          .map(e -> ", " + e.getColumnName() + " as " + alias)
          .orElse("");
    }

    private void isTableExceedOneBlobColumn(TableColumnInfoDto tableColumnInfoDto) {
        long blobColumnCount = tableColumnInfoDto.getColumnInfoDtoList().stream()
          .filter(this::isBlob).count();
        if (blobColumnCount > 1) {
            String message = String.format(
              "[isTableExceedOneBlobColumn] Cannot exceed one BLOB column, table=%s, BLOB column count=%s",
              tableColumnInfoDto.getTableName(), blobColumnCount);
            log.error(message);
            throw new OceanException(message);
        }
    }

    /**
     * input: [col1, col2]<br> output: || '''' || col1 || '''' || ', ' || '''' || col2 || '''' ||
     */
    private String composeInsertIntoValues(List<String> columnNameList) {
        StringBuilder columnValues = new StringBuilder();
        columnNameList.forEach(col -> {
            columnValues.append(" || '''' || ").append(col)
              .append(" || '''' || ', '"); // '''' escape quote 2 times
        });
        return columnValues.replace(
          columnValues.lastIndexOf("', '") - 1,
          columnValues.length(), "").toString();
    }

    @Override
    public List<String> getProd41TableNameList() {
        log.debug("[getProd41TableNameList] execute SQL={}", SELECT_DISTINCT_TABLE_NAMES);

        return prod41NamedParameterJdbcTemplate.query(
          SELECT_DISTINCT_TABLE_NAMES,
          (rs, rowNum) -> rs.getString("TABLE_NAME"));
    }


    @Override
    public boolean isDatabaseLinkExist() {
        String sql = "select DB_LINK from USER_DB_LINKS where DB_LINK = :dblink";
        MapSqlParameterSource params = new MapSqlParameterSource("dblink", DB_LINK);

        log.debug("[isDatabaseLinkExist] execute SQL={}", sql);

        return prod41NamedParameterJdbcTemplate.query(
          sql, params,
          (rs, rowNum) ->
            rs.getString("DB_LINK"))
          .stream().findAny().isPresent();
    }

    /**
     * Create database link from PROD41 to RAC(ACS)
     */
    @Override
    public void createDatabaseLink() {
        log.debug("Create Database Link, DB_LINK={}", DB_LINK);

        // ACS Database connection info
        String target = acsDatabaseUrl.substring(acsDatabaseUrl.lastIndexOf(":") + 1);
        String url = acsDatabaseUrl
          .substring(acsDatabaseUrl.indexOf("@") + 1, acsDatabaseUrl.lastIndexOf(":"));
        String host = url.split(":")[0];
        String port = url.split(":")[1];

        String connectData = getConnectData(target);
        String connectString = getConnectString(host, port, connectData);
        String sql = "CREATE DATABASE LINK " + DB_LINK
          + " CONNECT TO " + acsDatabaseUsername + "\n"
          + " IDENTIFIED BY " + acsDatabasePassword + "\n"
          + " USING " + connectString;

        log.debug("[createDatabaseLink] execute SQL={}", sql);

        prod41NamedParameterJdbcTemplate.execute(
          sql, new MapSqlParameterSource(), PreparedStatement::execute);
    }

    @Override
    public void createProd41BackupTables(Set<String> backupTableSet) {
        log.debug("[createProd41BackupTables] start to create tables={}", backupTableSet);
        backupTableSet.forEach(this::createTableByCopyingFromRemoteViaDblink);
    }

    private void createTableByCopyingFromRemoteViaDblink(String tableName) {
        String sql = "create table " + tableName + " as (select * from " + tableName + "@" + DB_LINK
          + " where 1=2)";

        log.debug("[createTableByCopyingFromRemoteViaDblink] execute SQL={}", sql);

        try {
            prod41NamedParameterJdbcTemplate.execute(sql, PreparedStatement::execute);
        } catch (DataException e) {
            log.warn("[createTableByCopyingFromRemoteViaDblink] PROD41.{} is already existing",
              tableName);
        }
    }

    private String getConnectData(String target) {
        if ("SID".equalsIgnoreCase(acsDatabaseConnectDataType)) {
            return "(CONNECT_DATA=(SID=" + target + ")";
        } else if ("SERVICE_NAME".equalsIgnoreCase(acsDatabaseConnectDataType)) {
            return "(CONNECT_DATA=(SERVICE_NAME=" + target + ")";
        }
        return "";
    }

    private String getConnectString(String host, String port, String connectData) {
        return "'(DESCRIPTION=\n" +
          "   (ADDRESS=(PROTOCOL=TCP)(HOST=" + host + ")(PORT=" + port + "))\n" +
          connectData + ")\n" +
          ")'";
    }

    @Override
    public void backupLogOverDaysToProd41(Set<String> backupTableSet) {
        backupTableSet.forEach(this::backupLogOverDaysToProd41);
    }

    private void backupLogOverDaysToProd41(String tableName) {
        int backupDays = fiscProperties.getHouseKeeping().getBackupDays();
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql = "insert into %s "
          + " select * from %s a"
          + " where (to_date('1970/01/01', 'YYYY/MM/DD') + (a.CREATE_MILLIS/1000/60/60/24)) "
          + " between trunc(SYSDATE - :backupDaysStart) and trunc(SYSDATE - :backupDaysEnd)";

        sql = String.format(sql, tableName, tableName + "@"
          + DB_LINK); // e.g insert into AUDITING select * from AUDITING@DBLINK a where ...

        log.debug("[backupLogOverDaysToProd41] execute SQL={}", sql);

        params.addValue("backupDaysStart", backupDays + 1);
        params.addValue("backupDaysEnd", backupDays);

        try {
            prod41NamedParameterJdbcTemplate.execute(sql, params, PreparedStatement::execute);
        } catch (Exception e) {
            String message =
              String.format(
                "[backupLogOverDaysToProd41] unknown exception, backup log over %d days fail, backup SQL=%s",
                backupDays, sql);
            log.error(message, e);
            throw new OceanException(message);
        }

    }

    @Override
    public void closeDatabaseLink() {
        String sql = "ALTER SESSION CLOSE DATABASE LINK " + DB_LINK;

        log.debug("[closeDatabaseLink] execute SQL={}", sql);

        try {
            prod41NamedParameterJdbcTemplate.execute(
              sql, new MapSqlParameterSource(), PreparedStatement::execute);
        } catch (Exception e) {
            String message = String
              .format("[closeDatabaseLink] unknown exception, close SQL=%s", sql);
            log.error(message, e);
            throw new OceanException(message);
        }
    }

    @Override
    public boolean isDatabaseLinkOpen() {
        String sql = "select DB_LINK from V$DBLINK where DB_LINK = :dblink";
        MapSqlParameterSource params = new MapSqlParameterSource("dblink", DB_LINK);

        log.debug("[isDatabaseLinkOpen] execute SQL={}", sql);

        return prod41NamedParameterJdbcTemplate.query(
          sql, params,
          (rs, rowNum) ->
            rs.getString("DB_LINK"))
          .stream().findAny().isPresent();
    }

    @Override
    public void deleteLogOverDaysFromAcs(List<String> backupTableList) {
        backupTableList.forEach(this::deleteLogOverDaysFromAcs);
    }

    private void deleteLogOverDaysFromAcs(String tableName) {
        int deleteOverDays = fiscProperties.getHouseKeeping().getBackupDays();
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql = "delete from %s "
          + " where (to_date('1970/01/01', 'YYYY/MM/DD') + (CREATE_MILLIS/1000/60/60/24)) "
          + " between trunc(SYSDATE - :deleteOverDaysStart) and trunc(SYSDATE - :deleteOverDaysEnd)";
        sql = String.format(sql, tableName);

        params.addValue("deleteOverDaysStart", deleteOverDays + 1);
        params.addValue("deleteOverDaysEnd", deleteOverDays);

        log.debug("[deleteLogOverDaysFromAcs] execute SQL={}", sql);

        try {
            acsPortalNamedParameterJdbcTemplate.execute(sql, params, PreparedStatement::execute);
        } catch (Exception e) {
            String message =
              String.format(
                "[deleteLogOverDaysFromAcs] unknown exception, delete log over %d days fail, delete SQL=%s",
                deleteOverDays, sql);
            log.error(message, e);
            throw new OceanException(message);
        }

    }

    @Override
    public void deleteLogOverDaysFromProd41(Set<String> backupTableSet) {
        backupTableSet.forEach(this::deleteLogOverDaysFromProd41);
    }

    private void deleteLogOverDaysFromProd41(String tableName) {
        int cleanDays = fiscProperties.getHouseKeeping().getCleanDays();
        MapSqlParameterSource params = new MapSqlParameterSource("cleanDays", cleanDays);
        String sql = "delete from %s "
          + " where (to_date('1970/01/01', 'YYYY/MM/DD') + (CREATE_MILLIS/1000/60/60/24)) < trunc(SYSDATE - :cleanDays)";
        sql = String.format(sql, tableName);

        log.debug("[deleteLogOverDaysFromProd41] execute SQL={}", sql);

        try {
            prod41NamedParameterJdbcTemplate.execute(sql, params, PreparedStatement::execute);
        } catch (Exception e) {
            String message =
              String.format(
                "[deleteLogOverDaysFromProd41] unknown exception, delete log over %d days fail, delete SQL=%s",
                cleanDays, sql);
            log.error(message, e);
            throw new OceanException(message);
        }

    }

}
