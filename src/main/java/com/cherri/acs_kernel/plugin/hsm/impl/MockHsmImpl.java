package com.cherri.acs_kernel.plugin.hsm.impl;

import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMCalculateAvInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMCalculateCvvInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMDecryptInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMEncryptInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMGenerateKeyInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMImportKeyInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMInitializeInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMSignInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMCalculateAvResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMCalculateCvvResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMDecryptResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMEncryptResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMGenerateKeyResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMImportKeyResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMInitializeResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMSignResultDTO;
import com.cherri.acs_kernel.plugin.enumerator.HSMSignMechanism;
import com.cherri.acs_kernel.plugin.hsm.impl.config.CloudHsmPluginConfig;
import com.cherri.acs_kernel.plugin.hsm.impl.dto.RsaSignatureAlgorithm;
import com.cherri.acs_kernel.plugin.hsm.impl.exception.CloudHsmException;
import com.cherri.acs_kernel.plugin.hsm.impl.model.dao.MockHsmKeyDAO;
import com.cherri.acs_kernel.plugin.hsm.impl.model.domain.KeyType;
import com.cherri.acs_kernel.plugin.hsm.impl.model.domain.MockHsmKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.xml.bind.DatatypeConverter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * When 3DS Authentication occurred, the ACS will send the issuer properties (defined in
 * 'issuerPropertyDefinitionList') to this plugin.
 *
 * <p>Step1. Define the issuerPropertyDefinitionList and all property definitions will show in ACS
 * Portal let user to configure.
 *
 * <p>Step2. Implement the plugin functions
 *
 * <p>Step3. Test this plugin by unit test
 */
@Log4j2
@Component
@ConditionalOnProperty(prefix = "hsm", name = "enable", havingValue = "false")
public class MockHsmImpl extends HSMImplBase {

    private final CloudHsmPluginConfig cloudHsmPluginConfig;
    private final MockHsmKeyDAO mockHsmKeyDAO;
    private static final SecureRandom random = new SecureRandom();
    private static final byte[] ZERO_IV = DatatypeConverter.parseHexBinary("0000000000000000");

    public MockHsmImpl(CloudHsmPluginConfig cloudHsmPluginConfig, MockHsmKeyDAO mockHsmKeyDAO) {
        log.warn("[MockHsm] Not connect to HSM because hsm.enable=false");
        this.cloudHsmPluginConfig = cloudHsmPluginConfig;
        this.mockHsmKeyDAO = mockHsmKeyDAO;
    }

    @Override
    public HSMInitializeResultDTO initialize(HSMInitializeInvokeDTO hsmInitializeDTO) {
        log.info("[HSMImpl] v0.0.2");
        log.info("[HSMImpl] init start");
        Security.addProvider(new BouncyCastleProvider());

        return HSMInitializeResultDTO.newInstanceOfSuccess();
    }

    @SneakyThrows
    @Override
    public HSMGenerateKeyResultDTO generateKey(HSMGenerateKeyInvokeDTO hsmGenerateKeyInvokeDTO) {
        log.info("[HSMImpl] generateKey start");
        log.info("[HSMImpl] hsmGenerateKeyInvokeDTO=" + hsmGenerateKeyInvokeDTO);

        // Do generate key here...
        try {
            switch (hsmGenerateKeyInvokeDTO.getHsmMechanism()) {
                case CKM_AES_KEY_GEN:
                    // Generate AES Key
                    KeyGenerator aesKeyGen = KeyGenerator.getInstance("AES");
                    aesKeyGen.init(256); // for example
                    SecretKey secretKey = aesKeyGen.generateKey();

                    // save key in DB
                    MockHsmKey aesKey =
                            new MockHsmKey(
                                    hsmGenerateKeyInvokeDTO.getKeyLabel(),
                                    KeyType.AES,
                                    secretKey.getEncoded());
                    mockHsmKeyDAO.saveKey(aesKey);

                    log.info("[generateKey] generate aesKey success");
                    return HSMGenerateKeyResultDTO.newInstanceOfSuccessWithData(
                            aesKey.getId(), null);

                case CKM_RSA_PKCS_KEY_PAIR_GEN:
                    // Generate RSA Key Pair
                    KeyPairGenerator rsaKeyGen = KeyPairGenerator.getInstance("RSA");
                    rsaKeyGen.initialize(2048);
                    KeyPair rsaKeyPair = rsaKeyGen.generateKeyPair();

                    RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) rsaKeyPair.getPrivate();
                    RSAPublicKey rsaPublicKey = (RSAPublicKey) rsaKeyPair.getPublic();

                    // save key in DB
                    MockHsmKey rsaKey =
                            new MockHsmKey(
                                    hsmGenerateKeyInvokeDTO.getKeyLabel(),
                                    KeyType.RSA,
                                    rsaPrivateKey.getEncoded());
                    mockHsmKeyDAO.saveKey(rsaKey);

                    log.info("[generateKey] generate rsaKeyPair success");
                    return HSMGenerateKeyResultDTO.newInstanceOfSuccessWithData(
                            rsaKey.getId(), rsaPublicKey);

                default:
                    return HSMGenerateKeyResultDTO.newInstanceOfExceptionHappened(
                            new CloudHsmException(
                                    "The HsmMechanism is not support. HsmMechanism="
                                            + hsmGenerateKeyInvokeDTO
                                                    .getHsmMechanism()
                                                    .toString()));
            }
        } catch (Exception e) {
            log.error("[generateKey] error", e);
            return HSMGenerateKeyResultDTO.newInstanceOfExceptionHappened(e);
        }
    }

    @SneakyThrows
    @Override
    public HSMImportKeyResultDTO importKey(HSMImportKeyInvokeDTO hsmImportKeyInvokeDTO) {
        log.info("[HSMImpl] importKey start");
        log.info("[HSMImpl] hsmImportKeyInvokeDTO=" + hsmImportKeyInvokeDTO);
        // Do import key here...

        try {
            switch (hsmImportKeyInvokeDTO.getHsmMechanism()) {
                case CKM_DES:
                    // Import DES Key
                    // save key in DB
                    MockHsmKey desKey =
                            new MockHsmKey(
                                    hsmImportKeyInvokeDTO.getKeyLabel(),
                                    KeyType.DES,
                                    DatatypeConverter.parseHexBinary(
                                            hsmImportKeyInvokeDTO.getDesKeyHex()));
                    mockHsmKeyDAO.saveKey(desKey);
                    log.info("[importKey] import DES3 key success");
                    return HSMImportKeyResultDTO.newInstanceOfSuccessWithData(desKey.getId());

                case CKM_DES3:
                    // Import DES Key
                    // save key in DB
                    MockHsmKey des3Key =
                        new MockHsmKey(
                            hsmImportKeyInvokeDTO.getKeyLabel(),
                            KeyType.DES3,
                            DatatypeConverter.parseHexBinary(
                                hsmImportKeyInvokeDTO.getDesKeyHex()));
                    mockHsmKeyDAO.saveKey(des3Key);
                    log.info("[importKey] import DES3 key success");
                    return HSMImportKeyResultDTO.newInstanceOfSuccessWithData(des3Key.getId());

                case CKM_HMAC:
                    MockHsmKey hmacKey =
                        new MockHsmKey(
                            hsmImportKeyInvokeDTO.getKeyLabel(),
                            KeyType.HMAC,
                            DatatypeConverter.parseHexBinary(
                                hsmImportKeyInvokeDTO.getDesKeyHex()));
                    mockHsmKeyDAO.saveKey(hmacKey);
                    log.info("[importKey] import HMAC key success");
                    return HSMImportKeyResultDTO.newInstanceOfSuccessWithData(hmacKey.getId());

                default:
                    return HSMImportKeyResultDTO.newInstanceOfExceptionHappened(
                            new CloudHsmException(
                                    "The HsmMechanism is not support. HsmMechanism="
                                            + hsmImportKeyInvokeDTO.getHsmMechanism().toString()));
            }
        } catch (Exception e) {
            log.error("[importKey] error", e);
            return HSMImportKeyResultDTO.newInstanceOfExceptionHappened(e);
        }
    }

    @SneakyThrows
    @Override
    public HSMEncryptResultDTO encrypt(HSMEncryptInvokeDTO hsmEncryptInvokeDTO) {
        log.info("[HSMImpl] encryption start");

        MockHsmKey key = mockHsmKeyDAO.findKeyById(hsmEncryptInvokeDTO.getKeyId());

        // Do encryption here...
        try {
            switch (hsmEncryptInvokeDTO.getHsmMechanism()) {
                case CKM_AES_CBC:
                    // AES CBC encrypt
                    Cipher aesCbcCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    SecretKey aesKey = key.toAesKey();
                    byte[] iv = new byte[16];
                    random.nextBytes(iv);
                    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                    aesCbcCipher.init(Cipher.ENCRYPT_MODE, aesKey, ivParameterSpec);

                    byte[] ciphertext = aesCbcCipher.doFinal(hsmEncryptInvokeDTO.getPlainText());
                    byte[] ivAndCiphertext = new byte[iv.length + ciphertext.length];
                    System.arraycopy(iv, 0, ivAndCiphertext, 0, iv.length);
                    System.arraycopy(ciphertext, 0, ivAndCiphertext, iv.length, ciphertext.length);

                    return HSMEncryptResultDTO.newInstanceOfSuccessWithData(ivAndCiphertext);

                case CKM_DES_ECB:
                    Cipher desEcbCipher = Cipher.getInstance("DES/ECB/NoPadding");
                    SecretKey desKey = key.toDesKey();
                    desEcbCipher.init(Cipher.ENCRYPT_MODE, desKey);

                    // DES ECB encrypt
                    return HSMEncryptResultDTO.newInstanceOfSuccessWithData(
                        desEcbCipher.doFinal(hsmEncryptInvokeDTO.getPlainText()));

                case CKM_DES3_CBC:
                    Cipher des3EcbCipher = Cipher.getInstance("DESede/CBC/NoPadding");
                    SecretKey des3Key = key.toDes3Key();
                    des3EcbCipher.init(Cipher.ENCRYPT_MODE, des3Key, new IvParameterSpec(ZERO_IV));

                    // DES ECB encrypt
                    return HSMEncryptResultDTO.newInstanceOfSuccessWithData(
                        des3EcbCipher.doFinal(hsmEncryptInvokeDTO.getPlainText()));

                case CKM_SHA256_HMAC:
                    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                    SecretKey hmacKey = key.toHmacKey();
                    sha256_HMAC.init(hmacKey);
                    return HSMEncryptResultDTO.newInstanceOfSuccessWithData(
                        sha256_HMAC.doFinal(hsmEncryptInvokeDTO.getPlainText()));

                default:
                    return HSMEncryptResultDTO.newInstanceOfExceptionHappened(
                            new CloudHsmException(
                                    "The HsmMechanism is not support. HsmMechanism="
                                            + hsmEncryptInvokeDTO.getHsmMechanism().toString()));
            }
        } catch (Exception e) {
            log.error("[encrypt] error", e);
            return HSMEncryptResultDTO.newInstanceOfExceptionHappened(e);
        }
    }

    @SneakyThrows
    @Override
    public HSMDecryptResultDTO decrypt(HSMDecryptInvokeDTO hsmDecryptInvokeDTO) {
        log.info("[HSMImpl] decryption start");

        MockHsmKey key = mockHsmKeyDAO.findKeyById(hsmDecryptInvokeDTO.getKeyId());

        // Do decryption here...
        try {
            switch (hsmDecryptInvokeDTO.getHsmMechanism()) {
                case CKM_AES_CBC:
                    // AES CBC decrypt
                    Cipher aesCbcCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    SecretKey aesKey = key.toAesKey();
                    byte[] iv = Arrays.copyOfRange(hsmDecryptInvokeDTO.getCipherText(), 0, 16);
                    byte[] ciphertext =
                            Arrays.copyOfRange(
                                    hsmDecryptInvokeDTO.getCipherText(),
                                    16,
                                    hsmDecryptInvokeDTO.getCipherText().length);

                    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                    aesCbcCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);
                    byte[] plaintext = aesCbcCipher.doFinal(ciphertext);

                    return HSMDecryptResultDTO.newInstanceOfSuccessWithData(plaintext);

                case CKM_DES_ECB:
                    // DES ECB decrypt
                    Cipher desEcbCipher = Cipher.getInstance("DES/ECB/NoPadding");
                    SecretKey desKey = key.toDesKey();
                    desEcbCipher.init(Cipher.DECRYPT_MODE, desKey);
                    return HSMDecryptResultDTO.newInstanceOfSuccessWithData(
                            desEcbCipher.doFinal(hsmDecryptInvokeDTO.getCipherText()));

                case CKM_DES3_CBC:
                    // DES ECB decrypt
                    Cipher des3EcbCipher = Cipher.getInstance("DESede/CBC/NoPadding");
                    SecretKey des3Key = key.toDes3Key();
                    des3EcbCipher.init(Cipher.DECRYPT_MODE, des3Key, new IvParameterSpec(ZERO_IV));
                    return HSMDecryptResultDTO.newInstanceOfSuccessWithData(
                        des3EcbCipher.doFinal(hsmDecryptInvokeDTO.getCipherText()));

                case CKM_RSA_PKCS_OAEP_WITH_SHA256_AND_MGF1_PADDING:
                    // decryptWithRsaEcbOaepWithSHA256AndMGF1Padding
                    Cipher rsaEcbOaepCipher =
                            Cipher.getInstance("RSA/ECB/OAEPWithSHA256AndMGF1Padding");
                    OAEPParameterSpec oaepParameterSpec =
                            new OAEPParameterSpec(
                                    "SHA-256",
                                    "MGF1",
                                    MGF1ParameterSpec.SHA256,
                                    PSource.PSpecified.DEFAULT);
                    rsaEcbOaepCipher.init(
                            Cipher.DECRYPT_MODE, key.toRsaPrivateKey(), oaepParameterSpec);

                    return HSMDecryptResultDTO.newInstanceOfSuccessWithData(
                            rsaEcbOaepCipher.doFinal(hsmDecryptInvokeDTO.getCipherText()));

                default:
                    return HSMDecryptResultDTO.newInstanceOfExceptionHappened(
                            new CloudHsmException(
                                    "The HsmMechanism is not support. HsmMechanism="
                                            + hsmDecryptInvokeDTO.getHsmMechanism().toString()));
            }
        } catch (Exception e) {
            log.error("[decrypt] error", e);
            return HSMDecryptResultDTO.newInstanceOfExceptionHappened(e);
        }
    }

    @SneakyThrows
    @Override
    public HSMSignResultDTO sign(HSMSignInvokeDTO hsmSignInvokeDTO) {
        log.info("[HSMImpl] signing start");
        log.info("[HSMImpl] hsmSignInvokeDTO=" + hsmSignInvokeDTO);
        // Do signing here...

        MockHsmKey key = mockHsmKeyDAO.findKeyById(hsmSignInvokeDTO.getKeyId());

        try {
            switch (hsmSignInvokeDTO.getHsmMechanism()) {
                case CKM_SHA1_RSA_PKCS:
                case CKM_SHA256_RSA_PKCS:
                    RsaSignatureAlgorithm rsaSignatureAlgorithm;
                    if (hsmSignInvokeDTO.getHsmMechanism() == HSMSignMechanism.CKM_SHA1_RSA_PKCS) {
                        rsaSignatureAlgorithm = RsaSignatureAlgorithm.SHA1;
                    } else {
                        rsaSignatureAlgorithm = RsaSignatureAlgorithm.SHA256;
                    }
                    Signature sig =
                            Signature.getInstance(rsaSignatureAlgorithm.getJceAlgorithmName());
                    sig.initSign(key.toRsaPrivateKey());
                    sig.update(hsmSignInvokeDTO.getMessage());

                    return HSMSignResultDTO.newInstanceOfSuccessWithData(sig.sign());

                case CKM_SHA256_RSA_PKCS_PSS:
                    Signature rsaPssSign = Signature.getInstance("SHA256withRSA/PSS", "BC");
                    PSSParameterSpec spec1 =
                        new PSSParameterSpec(
                            "SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 32, 1);
                    rsaPssSign.setParameter(spec1);
                    rsaPssSign.initSign(key.toRsaPrivateKey());
                    rsaPssSign.update(hsmSignInvokeDTO.getMessage());

                    return HSMSignResultDTO.newInstanceOfSuccessWithData(rsaPssSign.sign());

                default:
                    return HSMSignResultDTO.newInstanceOfExceptionHappened(
                            new CloudHsmException(
                                    "The HsmMechanism is not support. HsmMechanism="
                                            + hsmSignInvokeDTO.getHsmMechanism().toString()));
            }
        } catch (Exception e) {
            log.error("[sign] error", e);
            return HSMSignResultDTO.newInstanceOfExceptionHappened(e);
        }
    }

    @Override
    public HSMCalculateCvvResultDTO calculateCvv(
            HSMCalculateCvvInvokeDTO hsmCalculateCvvInvokeDTO) {
        return HSMCalculateCvvResultDTO.newInstanceOfExceptionHappened(
                new CloudHsmException("calculateCvv is not supported"));
    }

    @Override
    public HSMCalculateAvResultDTO calculateAv(HSMCalculateAvInvokeDTO hsmCalculateAvInvokeDTO) {
        return HSMCalculateAvResultDTO.newInstanceOfExceptionHappened(
            new CloudHsmException("calculateCvv is not supported"));
    }
}
