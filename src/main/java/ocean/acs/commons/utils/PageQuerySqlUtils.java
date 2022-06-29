package ocean.acs.commons.utils;

import ocean.acs.commons.constant.PortalEnvironmentConstants;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;

public class PageQuerySqlUtils {

    public static final String PARAM_START_ROW_NUMBER = "startRowNumber";
    public static final String PARAM_LIMIT = "limit";
    public static final String PLACEHOLDER_START_ROW_NUMBER = ":" + PARAM_START_ROW_NUMBER;
    public static final String PLACEHOLDER_LIMIT = ":" + PARAM_LIMIT;

    public static String getPaginationSQLForQuery(String query, int page, int pageSize) {
        String pagingSql = PageQuerySqlUtils.get(query);

        return pagingSql
            .replace(
                PageQuerySqlUtils.PLACEHOLDER_START_ROW_NUMBER,
                String.valueOf(PageQuerySqlUtils.getStartRowNumber(page, pageSize)))
            .replace(
                PageQuerySqlUtils.PLACEHOLDER_LIMIT,
                String.valueOf(PageQuerySqlUtils.getLimit(page, pageSize)));
    }

    public static String get(String querySql) {
        String sql = null;

        if (PortalEnvironmentConstants.DATABASE_TYPE.equals("oracle")) {
            sql = String.format(
                    "select tt.*\n" + 
                    "from ( select rownum as rowno, t.*\n" + 
                    "       from (%s) t\n" + 
                    "       where rownum <= " + PLACEHOLDER_LIMIT + "\n" + 
                    "     ) tt\n" + 
                    "where  tt.rowno >= " + PLACEHOLDER_START_ROW_NUMBER, querySql);
        } else if (PortalEnvironmentConstants.DATABASE_TYPE.equals("sql_server")) {
            sql =
                String.format(
                    "%s offset %s rows fetch next %s rows only",
                    querySql, PLACEHOLDER_START_ROW_NUMBER, PLACEHOLDER_LIMIT);
        } else {
            throw new OceanException(
                ResultStatus.DB_READ_ERROR,
                "unsupported database type: " + PortalEnvironmentConstants.DATABASE_TYPE);
        }
        return sql;
    }

    public static long getStartRowNumber(int startPageNumber, int pageSize) {
        int prePage = (startPageNumber - 1);
        int nextRowNumber = (prePage * pageSize) + 1;

        if (PortalEnvironmentConstants.DATABASE_TYPE.equals("sql_server")) {
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
