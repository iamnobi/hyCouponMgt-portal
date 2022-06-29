package ocean.acs.commons.utils;

public class MaskUtils {

    private static final String MASK_SYMBOL = "*";

    public static String phoneNumberMask(String phoneNumber) {
        if (phoneNumber == null) {
            return "";
        }
        if (phoneNumber.length() < 5) {
            return phoneNumber;
        }
        char[] phoneCharAry = phoneNumber.toCharArray();
        int maskStart = 0;
        int maskEnd = (phoneNumber.length() - 1) - 4;
        String maskStr = doMaskValue(phoneCharAry, maskStart, maskEnd);
        return maskStr;
    }

    private static String doMaskValue(char[] valueCharAry, int maskStart, int maskEnd) {
        StringBuilder maskStr = new StringBuilder();
        for (int i = 0; i < valueCharAry.length; i++) {
            if (i >= maskStart && i <= maskEnd) {
                maskStr.append(MASK_SYMBOL);
            } else {
                maskStr.append(valueCharAry[i]);
            }
        }
        return maskStr.toString();
    }

    public static String acctNumberMask(String acctNumber) {
        if (acctNumber == null) {
            return "";
        }
        if (acctNumber.length() < 10) {
            return acctNumber;
        }
        char[] acctNumberCharAry = acctNumber.toCharArray();
        int maskStart = 6;
        int maskEnd = 4;
        maskEnd = (acctNumber.length() - 1) - maskEnd;
        String maskStr = doMaskValue(acctNumberCharAry, maskStart, maskEnd);
        return maskStr;
    }

    public static String addressMask(String address) {
        if (address == null) {
            return "";
        }
        char[] acctNumberCharAry = address.toCharArray();
        int maskStart = 3;
        int maskEnd = 3;
        maskEnd = (address.length() - 1) - maskEnd;
        String maskStr = doMaskValue(acctNumberCharAry, maskStart, maskEnd);
        return maskStr;
    }

    public static String identityNumberMask(String identityNumber) {
        if (identityNumber == null) {
            return "";
        }
        if (identityNumber.length() < 5) {
            return identityNumber;
        }
        char[] identityNumberCharAry = identityNumber.toCharArray();
        int maskStart = 5;
        int maskEnd = (identityNumber.length() - 1);
        String maskStr = doMaskValue(identityNumberCharAry, maskStart, maskEnd);
        return maskStr;
    }

    public static String cardholderNameMask(String cardholderName) {
        if (cardholderName == null) {
            return "";
        }
        if (cardholderName.length() < 2) {
            return cardholderName;
        }
        char[] cardholderNameCharAry = cardholderName.toCharArray();
        int maskStart = 1;
        int maskEnd = cardholderName.length() - 1;
        String maskStr = doMaskValue(cardholderNameCharAry, maskStart, maskEnd);
        return maskStr;
    }

    public static String emailMask(String email) {
        if (email == null) {
            return "";
        }
        if (!email.contains("@")) {
            return email;
        }
        char[] cardholderNameCharAry = email.toCharArray();
        int maskStart = 0;
        int maskEnd = email.indexOf("@") - 1;
        String maskStr = doMaskValue(cardholderNameCharAry, maskStart, maskEnd);
        return maskStr;
    }

    public static String birthdayMask(String birthday) {
        if (birthday == null) {
            return "";
        }
        if (birthday.length() < 8) {
            return birthday;
        }
        char[] cardholderNameCharAry = birthday.toCharArray();
        int maskStart = 3;
        int maskEnd = (birthday.length() - 1) - 1;
        String maskStr = doMaskValue(cardholderNameCharAry, maskStart, maskEnd);
        return maskStr;
    }

}
