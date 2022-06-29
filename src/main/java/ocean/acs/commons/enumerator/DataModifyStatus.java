package ocean.acs.commons.enumerator;

public enum DataModifyStatus {

    DRAFT("Draft"), PUBLISHED("Published"), UNKNOWN("Unknown");

    private String symbol;

    DataModifyStatus(String symbol) {
        this.symbol = symbol;
    }

    public String getTypeSymbol() {
        return symbol;
    }

    public static DataModifyStatus getBySymbol(String typeSymbol) {
        for (DataModifyStatus dataType : DataModifyStatus.values()) {
            if (typeSymbol.equalsIgnoreCase(dataType.getTypeSymbol())) {
                return dataType;
            }
        }

        return DataModifyStatus.UNKNOWN;
    }
}
