package com.cherri.acs_portal.util;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Log4j2
public class AcsPortalUtil {

    private static final Pattern PATTERN_LEGAL_CARD_NUMBER = Pattern.compile("^(\\d{13,19})$");

    public static boolean isFiscUser(Long issuerBankId) {
        return EnvironmentConstants.ORG_ISSUER_BANK_ID == issuerBankId;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 取得國家名稱，若無法辨識則回傳:N/A
     *
     * @param countryCodeText (Ex.901)
     * @return 901 (TWD)
     */
    public static String getCountryName(String countryCodeText) {
        if (StringUtils.isBlank(countryCodeText)) {
            return "N/A";
        }
        String countryName;
        try {
            CountryCode countryCode = CountryCode.getByCode(Integer.parseInt(countryCodeText));
            countryName = countryCode == null ? "N/A" : countryCode.name();
        } catch (NumberFormatException e) {
            log.error("[getCountryName] Cannot format countryCodeText to int, countryCodeText={}",
              countryCodeText, e);
            countryName = "N/A";
        }
        return countryName;
    }

    /**
     * 幣別代碼與幣別名稱格式化，若無法轉換則回傳空字串
     *
     * @param codeStr (Ex.901)
     * @return 901 (TWD)
     */
    public static String currencyCodeAndNameFormatString(String codeStr) {
        if (StringUtils.isBlank(codeStr)) {
            return "";
        }
        CurrencyCode currencyCode = currencyCodeStrToCurrencyCode(codeStr);
        String currencyName = currencyCode == null ? "N/A" : currencyCode.name();
        return String.format("%s (%s)", codeStr, currencyName);
    }

    /**
     * 幣別代碼字串轉幣別列舉{@link CurrencyCode}
     *
     * @param codeStr 幣別代碼字串
     * @return {@link CurrencyCode}
     */
    public static CurrencyCode currencyCodeStrToCurrencyCode(String codeStr) {
        if (StringUtils.isNumeric(codeStr)) {
            return CurrencyCode.getByCode(Integer.parseInt(codeStr));
        }
        return null;
    }

    /**
     * 國家代碼與國家名稱格式化，若無法轉換則回傳:{countryCodeText} (N/A)
     *
     * @param countryCodeText (Ex.158)
     * @return 158 (TW)
     */
    public static String countryCodeAndNameFormatString(String countryCodeText) {
        if (StringUtils.isBlank(countryCodeText)) {
            return countryCodeText;
        }
        String countryName;
        try {
            CountryCode countryCode = CountryCode.getByCode(Integer.parseInt(countryCodeText));
            countryName = countryCode == null ? "N/A" : countryCode.name();
        } catch (NumberFormatException e) {
            log.error(
              "[countryCodeAndNameFormatString] Cannot format countryCodeText to int, countryCodeText={}",
              countryCodeText, e);
            countryName = "N/A";
        }
        return String.format("%s (%s)", countryCodeText, countryName);
    }

    /**
     * 合法的卡號格式，ex:<br> 1. 長度:13~19<br> 2. 半形數字
     *
     * @param cardNumber 卡號
     * @return true:合法，false:不合法
     */
    public static boolean isValidCardNumber(String cardNumber) {
        if (StringUtils.isBlank(cardNumber)) {
            return false;
        }
        return PATTERN_LEGAL_CARD_NUMBER.matcher(cardNumber).matches();
    }

    /**
     * 卡號右補0至長度19碼
     *
     * @param cardNumber
     * @return
     */
    public static BigInteger cardNumberRightPadZero(String cardNumber) {
        return new BigInteger(
          StringCustomizedUtils.appendZeroSuffix(
            cardNumber, StringCustomizedUtils.CARD_NUMBER_MAX_LENGTH, 0));
    }

    public static String decompress(String base64String) throws IOException, DataFormatException {
        byte[] data = Base64.getMimeDecoder().decode(base64String.getBytes(StandardCharsets.UTF_8));

        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        return new String(output, StandardCharsets.UTF_8);
    }
}
