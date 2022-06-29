package ocean.acs.commons.enumerator;

public enum MessageCategory {
    PA("01"),
    NPA("02");

    private String code;

    MessageCategory(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static boolean isPA(String code) {
        return MessageCategory.PA.getCode().equals(code);
    }

    public static boolean isNPA(String code) {
        return MessageCategory.NPA.getCode().equals(code);
    }

    public static MessageCategory codeOf(String code) {
        MessageCategory[] messageCategoryArray = values();
        for (MessageCategory messageCategory : messageCategoryArray) {
            if (String.valueOf(messageCategory.code).equals(code)) {
                return messageCategory;
            }
        }
        return null;
    }
}
