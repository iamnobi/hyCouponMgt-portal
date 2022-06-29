package ocean.acs.commons.utils;

import java.math.BigInteger;

public class PortalUtil {

    /**
     * 卡號右補0至長度19碼
     * 
     * @param cardNumber
     * @return
     */
    public static BigInteger cardNumberRightPadZero(String cardNumber) {
        return new BigInteger(StringCustomizedUtils.appendZeroSuffix(cardNumber,
                StringCustomizedUtils.CARD_NUMBER_MAX_LENGTH, 0));
    }

}
