package ocean.acs.commons.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import ocean.acs.commons.exception.HmacException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

public class HmacUtils {

    public static final String CIPHER_ENCRYPT_TYPE_HMAC = "HmacSHA256";

    public static String encrypt(String value, byte[] encryptKey) throws HmacException {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        try {
            Mac sha256_HMAC = Mac.getInstance(CIPHER_ENCRYPT_TYPE_HMAC);
            sha256_HMAC.init(new SecretKeySpec(encryptKey, CIPHER_ENCRYPT_TYPE_HMAC));
            return Hex.encodeHexString(sha256_HMAC.doFinal(value.getBytes()));
        } catch (Exception e) {
            throw new HmacException(e.getMessage(), e);
        }
    }

    public static String createHMAC256Key() throws HmacException {
        try {
            KeyGenerator keyGenerator = null;
            keyGenerator = KeyGenerator.getInstance(CIPHER_ENCRYPT_TYPE_HMAC);
            keyGenerator.init(256);
            return Hex.encodeHexString(keyGenerator.generateKey().getEncoded());
        } catch (Exception e) {
            throw new HmacException(e.getMessage(), e);
        }
    }

}
