package com.cherri.acs_portal.util;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;

@Log4j2
public class PageQuerySqlUtils {

    public static final String PARAM_START_ROW_NUMBER = "startRowNumber";
    public static final String PARAM_LIMIT = "limit";
    public static final String PLACEHOLDER_START_ROW_NUMBER = ":" + PARAM_START_ROW_NUMBER;
    public static final String PLACEHOLDER_LIMIT = ":" + PARAM_LIMIT;

    public static String getPaginationSQLForQuery(String query, int page, int pageSize) {
        String pagingSql = ocean.acs.commons.utils.PageQuerySqlUtils.get(query);

        return pagingSql
          .replace(
            ocean.acs.commons.utils.PageQuerySqlUtils.PLACEHOLDER_START_ROW_NUMBER,
            String.valueOf(
              ocean.acs.commons.utils.PageQuerySqlUtils.getStartRowNumber(page, pageSize)))
          .replace(
            ocean.acs.commons.utils.PageQuerySqlUtils.PLACEHOLDER_LIMIT,
            String.valueOf(ocean.acs.commons.utils.PageQuerySqlUtils.getLimit(page, pageSize)));
    }

    public static String get(String querySql) {
        String sql = null;

        if (EnvironmentConstants.DATABASE_TYPE.equals("oracle")) {
            sql =
              String.format(
                "select tt.*\n"
                  + "from ( select rownum as rowno, t.*\n"
                  + "       from (%s) t\n"
                  + "       where rownum <= " + PLACEHOLDER_LIMIT + "\n"
                  + "     ) tt\n"
                  + "where  tt.rowno >= " + PLACEHOLDER_START_ROW_NUMBER,
                querySql);
        } else if (EnvironmentConstants.DATABASE_TYPE.equals("sql_server")) {
            sql =
              String.format(
                "%s offset %s rows fetch next %s rows only",
                querySql, PLACEHOLDER_START_ROW_NUMBER, PLACEHOLDER_LIMIT);
        } else {
            throw new OceanException(
              ResultStatus.DB_READ_ERROR,
              "unsupported database type: " + EnvironmentConstants.DATABASE_TYPE);
        }
        return sql;
    }

    public static long getStartRowNumber(int startPageNumber, int pageSize) {
        int prePage = (startPageNumber - 1);
        int nextRowNumber = (prePage * pageSize) + 1;

        if (EnvironmentConstants.DATABASE_TYPE.equals("sql_server")) {
            // sql server 從 0 開始，所以要減 1
            nextRowNumber = nextRowNumber - 1;
        }

        return nextRowNumber;
    }

    public static long getLimit(int startPageNumber, int pageSize) {
        startPageNumber = startPageNumber < 1 ? 1 : startPageNumber;
        long maxRowNumber = startPageNumber * pageSize;
        return maxRowNumber;
    }

    public static int getTotalPage(long total, int pageSize) {
        return (int) Math.ceil((double) total / pageSize);
    }
}
