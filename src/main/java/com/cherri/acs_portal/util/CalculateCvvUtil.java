package com.cherri.acs_portal.util;

import com.cherri.acs_portal.dto.acs_integrator.CalculateCvvDTO;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Log4j2
public class CalculateCvvUtil {

    public interface DesProvider {

        byte[] desEncrypt(String keyId, byte[] data) throws Exception;

        byte[] desDecrypt(String keyId, byte[] data) throws Exception;
    }

    public static String calculateCvvWithVersion2(
      CalculateCvvDTO calculateCvvDTO, DesProvider desProvider) throws Exception {
        String keyA = calculateCvvDTO.getKeyA();
        String keyB = calculateCvvDTO.getKeyB();
        String step1 =
          calculateCvvDTO.getCardNumber()
            + calculateCvvDTO.getAtnLastFour()
            + calculateCvvDTO.getThreeDSecurityResult();

        String step2 = StringUtils.rightPad(step1, 32, "0");

        byte[] step3_frontBlock = DatatypeConverter.parseHexBinary(step2.substring(0, 16));
        byte[] step3_endBlock = DatatypeConverter.parseHexBinary(step2.substring(16));

        byte[] step4 = desProvider.desEncrypt(keyA, step3_frontBlock);

        byte[] step5_xor = xor(step3_endBlock, step4);

        byte[] step5 = desProvider.desEncrypt(keyA, step5_xor);

        byte[] step6 = desProvider.desDecrypt(keyB, step5);

        byte[] step7 = desProvider.desEncrypt(keyA, step6);

        String step7Hex = DatatypeConverter.printHexBinary(step7);

        log.info("[CalculateCavv] step7={}", StringUtils.normalizeSpace(step7Hex));

        Pattern notNumberPattern = Pattern.compile("[^0-9]");
        String step8 = notNumberPattern.matcher(step7Hex).replaceAll("");

        Pattern notAlphaPattern = Pattern.compile("[^ABCDEF]");
        String step9_1 = notAlphaPattern.matcher(step7Hex).replaceAll("");

        StringBuilder stringBuilder = new StringBuilder();
        for (char ch : step9_1.toCharArray()) {
            stringBuilder.append((Integer.parseInt(String.valueOf(ch), 16) - 10));
        }

        String step9_2 = stringBuilder.toString();

        String step10 = step8 + step9_2;
        log.info("[CalculateCavv] step10={}", StringUtils.normalizeSpace(step10));

        String cvvOutput = step10.substring(0, 3);
        return cvvOutput;
    }

    /** Return XOR of two byte array of different or same size. */
    private static byte[] xor(byte[] data1, byte[] data2) {
        // make data2 the largest...
        if (data1.length > data2.length) {
            byte[] tmp = data2;
            data2 = data1;
            data1 = tmp;
        }

        for (int i = 0; i < data1.length; i++) {
            data2[i] ^= data1[i];
        }
        return data2;
    }
}
