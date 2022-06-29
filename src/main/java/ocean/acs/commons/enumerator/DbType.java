package ocean.acs.commons.enumerator;

public enum DbType {
    
    ORACLE, MYSQL, SQLSERVER, UNKNOWN;

    public static DbType getByName(String dbTypeName) {
        if (null == dbTypeName) {
            return UNKNOWN;
        }
        dbTypeName = dbTypeName.toLowerCase().trim();
        if (dbTypeName.contains("oracle")) {
            return ORACLE;
        }
        if (dbTypeName.contains("mysql")) {
            return MYSQL;
        }
        if (dbTypeName.contains("sqlserver")) {
            return SQLSERVER;
        }
        return UNKNOWN;
    }
}
