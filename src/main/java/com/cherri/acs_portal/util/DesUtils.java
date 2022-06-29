package com.cherri.acs_portal.util;

import java.security.InvalidKeyException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import lombok.SneakyThrows;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/** Port from JS version implemented in NCCC (see DesUtils.js) */
public class DesUtils {

    private static final String KCV_ENCRYPT_DATA_HEX = "0000000000000000";

    public static void main(String[] args) throws Exception {
        //        String c1 = "860BE5F82F26944504B0315B57C7296D";
        //        String c2 = "FBD9C4FD343E8FB02AA1869BDF4A3B32";
        //        String c3 = "45A81098D0FD79ADADF24AE9BCBCEF62";
        //        String key = combineKeyComponents(c1, c2, c3);
        //        System.out.println(String.format("%s %s", key, calculateKCV(key)));
        //        System.out.println(String.format("%s %s", c1, calculateKCV(c1)));
        //        System.out.println(String.format("%s %s", c2, calculateKCV(c2)));
        //        System.out.println(String.format("%s %s", c3, calculateKCV(c3)));

        String c1 = "7F2FF7EFC7649D6E";
        String c2 = "F9E4CF95307D8B0C";
        String key = combineKeyComponents(c1, c2, null);

    }

    public static String combineKeyComponents(
      String component1Hex, String component2Hex, String component3Hex) {
        if (component1Hex == null || component2Hex == null) {
            throw new NullPointerException("component1 or component2 is null");
        }
        if (component1Hex.length() != component2Hex.length()) {
            throw new IllegalArgumentException("component length is different");
        }
        if (component3Hex != null && component3Hex.length() != component1Hex.length()) {
            throw new IllegalArgumentException("component length is different");
        }
        byte[] component1;
        byte[] component2;
        byte[] component3 = null;
        try {
            component1 = Hex.decodeHex(component1Hex);
            component2 = Hex.decodeHex(component2Hex);
            if (component3Hex != null) {
                component3 = Hex.decodeHex(component3Hex);
            }
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Invalid componentHex", e);
        }

        byte[] combineKey = new byte[component1.length];
        for (int i = 0; i < component1.length; i++) {
            if (component3 == null) {
                combineKey[i] = (byte) (component1[i] ^ component2[i]);
            } else {
                combineKey[i] = (byte) (component1[i] ^ component2[i] ^ component3[i]);
            }
        }
        String combineKeyHex = Hex.encodeHexString(combineKey);
        return convertKeyParityBitsForceOdd(combineKeyHex);
    }

    public static String convertKeyParityBitsForceOdd(String keyHex) {
        StringBuilder resultKey = new StringBuilder();
        for (int i = 0; i < keyHex.length(); i += 2) {
            String hex = keyHex.substring(i, i + 2);
            String bin = hex2bin(hex);
            resultKey.append(forceOdd(bin));
        }
        return resultKey.toString();
    }

    public static String hex2bin(String hex) {
        String a = "00000000" + Integer.toBinaryString(Integer.parseInt(hex, 16));
        return a.substring(a.length() - 8);
    }

    public static String bin2hex(String bin) {
        return Integer.toHexString(Integer.parseInt(bin, 2)).toUpperCase();
    }

    public static String forceOdd(String bin) {
        char[] chars = bin.toCharArray();
        int countOf1Bits = 0;
        for (int i = 0; i < chars.length - 1; i++) {
            if ('1' == chars[i]) {
                countOf1Bits += 1;
            }
        }
        String parity = countOf1Bits % 2 == 0 ? "1" : "0";
        String binaryWithParity = bin.substring(0, bin.length() - 1) + parity;
        String hex = bin2hex(binaryWithParity);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex;
    }
}
