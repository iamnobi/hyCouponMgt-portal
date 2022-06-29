package ocean.acs.commons.okhttp.utils;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;

/**
 * JweUtil
 *
 * @author Alan Chen
 */
@Log4j2
public class JweUtil {

    public static String decryptJwe(byte[] secretKeyBytes, JWEObject jweObject)
      throws JOSEException {
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, 0, secretKeyBytes.length, "AES");
        byte[] key = secretKey.getEncoded();
        EncryptionMethod jweMethod = jweObject.getHeader().getEncryptionMethod();
        if (jweMethod == EncryptionMethod.A128GCM) {
            key = Arrays.copyOfRange(key, 0, 16);
        }
        jweObject.decrypt(new DirectDecrypter(key));
        return jweObject.getPayload().toString();
    }

    public static String encryptJwe(
      EncryptionMethod encryptionMethod, byte[] secretKeyBytes, String message, String keyID)
      throws JOSEException, OceanException {
        if (encryptionMethod.equals(EncryptionMethod.A128GCM)) {
            return encryptJWEA128GCM(secretKeyBytes, message, keyID);
        } else if (encryptionMethod.equals(EncryptionMethod.A128CBC_HS256)) {
            return encryptJWEA128CBC_HS256(secretKeyBytes, message, keyID);
        }
        log.debug("[encryptJwe] EncryptionMethod not supported, encryptionMethod={}",
          encryptionMethod);
        throw new OceanException(ResultStatus.SERVER_ERROR, "EncryptionMethod not supported");
    }

    private static String encryptJWEA128GCM(byte[] secretKeyBytes, String message, String keyID)
      throws JOSEException {
        JWEHeader header =
          new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128GCM).keyID(keyID).build();
        JWEObject jweObject = new JWEObject(header, new Payload(message));
        byte[] leftKey = Arrays.copyOfRange(secretKeyBytes, 0, 16);

        jweObject.encrypt(new DirectEncrypter(leftKey));
        return jweObject.serialize();
    }

    private static String encryptJWEA128CBC_HS256(byte[] secretKeyBytes, String message,
      String keyID)
      throws JOSEException {
        JWEHeader header =
          new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
            .keyID(keyID)
            .build();
        JWEObject jweObject = new JWEObject(header, new Payload(message));

        jweObject.encrypt(new DirectEncrypter(secretKeyBytes));
        return jweObject.serialize();
    }
}
