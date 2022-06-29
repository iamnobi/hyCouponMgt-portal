package ocean.acs.commons.enumerator;

import ocean.acs.commons.utils.StringCustomizedUtils;

public enum AuditActionType {
    
    ADD("A"), DELETE("D"), UPDATE("U"), UNKNOWN("UN");

    private String symbol;

    AuditActionType(String symbol) {
        this.symbol = symbol;
    }

    public String getTypeSymbol() {
        return symbol;
    }

    public static AuditActionType getBySymbol(String typeSymbol) {
        if (StringCustomizedUtils.isEmpty(typeSymbol))
            return AuditActionType.UNKNOWN;

        for (AuditActionType dataType : AuditActionType.values()) {
            if (typeSymbol.equalsIgnoreCase(dataType.getTypeSymbol())) {
                return dataType;
            }
        }

        return AuditActionType.UNKNOWN;
    }
}
