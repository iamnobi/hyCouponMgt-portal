package com.cherri.acs_portal.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HmacUtils {

    private static final Logger log = LoggerFactory.getLogger(HmacUtils.class);

    public static final String CIPHER_ENCRYPT_TYPE_HMAC = "HmacSHA256";

    public static SecretKeySpec HMAC_SECRET_KEY;

    /**
     * @return EncodeHexString
     * @throws NoSuchAlgorithmException
     */
    public static String encrypt(String value, byte[] encryptKey) {
        try {
            Mac sha256_HMAC = Mac.getInstance(CIPHER_ENCRYPT_TYPE_HMAC);
            if (HMAC_SECRET_KEY == null) {
                HMAC_SECRET_KEY = new SecretKeySpec(encryptKey, CIPHER_ENCRYPT_TYPE_HMAC);
            }
            sha256_HMAC.init(HMAC_SECRET_KEY);
            return Hex.encodeHexString(sha256_HMAC.doFinal(value.getBytes()));
        } catch (Exception e) {
            log.error("[encrypt] unknown exception", e);
        }
        return "";
    }

    public static String createHMAC256Key() {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(CIPHER_ENCRYPT_TYPE_HMAC);
        } catch (NoSuchAlgorithmException e) {
            log.error("[createHMAC256Key] algorithm={}", CIPHER_ENCRYPT_TYPE_HMAC, e);
        }
        keyGenerator.init(256);
        return Hex.encodeHexString(keyGenerator.generateKey().getEncoded());
    }

    @SneakyThrows
    public static byte[] getSalt(int length) {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[length];
        sr.nextBytes(salt);
        return salt;
    }
}
