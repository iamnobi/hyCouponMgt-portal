package ocean.acs.commons.enumerator;

import ocean.acs.commons.utils.StringCustomizedUtils;

public enum AuditStatus {
    
    AUDITING("A"), PUBLISHED("P"), REJECTED("R"), WITHDRAW("W"), UNKNOWN("U");

    private String symbol;

    AuditStatus(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static AuditStatus getStatusBySymbol(String symbol) {
        if (StringCustomizedUtils.isEmpty(symbol))
            return AuditStatus.UNKNOWN;

        AuditStatus[] statusList = values();
        for (AuditStatus auditStatus : statusList) {
            if (auditStatus.getSymbol().equalsIgnoreCase(symbol)) {
                return auditStatus;
            }
        }
        return AuditStatus.UNKNOWN;
    }

    @Override
    public String toString() {
        return this.symbol;
    }
}
