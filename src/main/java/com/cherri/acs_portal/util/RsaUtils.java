package com.cherri.acs_portal.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class RsaUtils {

    public static final String RSA_PADDING = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    public static String encrypt(byte[] value, PublicKey encryptKey)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(RSA_PADDING);
        OAEPParameterSpec oaepParameterSpec =
          new OAEPParameterSpec(
            "SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.ENCRYPT_MODE, encryptKey, oaepParameterSpec);
        byte[] encryptBytes = cipher.doFinal(value);
        return Base64.getEncoder().encodeToString(encryptBytes);
    }
}
