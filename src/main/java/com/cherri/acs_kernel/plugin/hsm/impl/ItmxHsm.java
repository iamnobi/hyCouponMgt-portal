package com.cherri.acs_kernel.plugin.hsm.impl;

import com.cherri.acs_kernel.plugin.dto.hsm.invoke.*;
import com.cherri.acs_kernel.plugin.dto.hsm.result.*;
import com.cherri.acs_kernel.plugin.hsm.impl.dto.GenRsaKeyPairResultDTO;
import com.cherri.acs_kernel.plugin.hsm.impl.exception.CloudHsmException;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import sun.security.pkcs11.wrapper.*;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
@Service
@ConditionalOnProperty(prefix = "hsm", name = "enable", havingValue = "true")
public class ItmxHsm extends HSMImplBase {
  interface HsmCommand<T> {
    T run(long session);
  }

  private static final SecureRandom random = new SecureRandom();
  private PKCS11 pkcs11 = null;
  private Long loginSession = null;
  private final Map<String, Long> keyHandleMap = new HashMap<>();
  private long hsmSlotId;
  private final String slotDescription;
  private final String hsmPin;
  private final String hsmLibPath;

  public ItmxHsm(
      @Value("${hsm.slot-description}") String slotDescription,
      @Value("${hsm.pin}") String hsmPin,
      @Value("${hsm.pkcs11-lib-path}") String hsmLibPath) {
    this.slotDescription = slotDescription;
    this.hsmPin = hsmPin;
    this.hsmLibPath = hsmLibPath;

    this.initializePkcs11();
    this.login();
  }

  public byte[] rsaSha256Sign(String privateKeyId, byte[] plaintext) {
    // get key handle
    long privateKeyHandle = this.getKeyHandleByKeyId(privateKeyId);

    // SHA256withRSA mechanism
    CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_SHA256_RSA_PKCS);

    // Sign
    return runHsmCommand(
        session -> {
          try {
            pkcs11.C_SignInit(session, mechanism, privateKeyHandle);
          } catch (PKCS11Exception e) {
            throw new RuntimeException("[rsaOaepSign] C_SignInit error", e);
          }

          try {
            byte[] signature = pkcs11.C_Sign(session, plaintext);
            return signature;
          } catch (PKCS11Exception e) {
            throw new RuntimeException("[rsaOaepSign] C_Sign error", e);
          }
        });
  }

  public byte[] rsaOaepDecrypt(String privateKeyId, byte[] ciphertext) {
    // get key handle
    long privateKeyHandle = this.getKeyHandleByKeyId(privateKeyId);

    // OAEP param
    CK_RSA_PKCS_OAEP_PARAMS ckRsaPkcsOaepParams = new CK_RSA_PKCS_OAEP_PARAMS();
    ckRsaPkcsOaepParams.hashAlg = PKCS11Constants.CKM_SHA256;
    ckRsaPkcsOaepParams.mgf = 0x00000002L; // CKG_MGF1_SHA256
    ckRsaPkcsOaepParams.source = 0L;
    ckRsaPkcsOaepParams.pSourceData = new byte[0];
    // OAEP mechanism
    CK_MECHANISM rsaOaepMechanism = new CK_MECHANISM();
    rsaOaepMechanism.mechanism = PKCS11Constants.CKM_RSA_PKCS_OAEP;
    rsaOaepMechanism.pParameter = ckRsaPkcsOaepParams;

    // Decrypt
    return runHsmCommand(
        session -> {
          try {
            pkcs11.C_DecryptInit(session, rsaOaepMechanism, privateKeyHandle);
          } catch (PKCS11Exception e) {
            throw new RuntimeException("[rsaOaepDecrypt] C_DecryptInit error", e);
          }

          try {
            byte[] buffer = new byte[256];
            int len =
                pkcs11.C_Decrypt(
                    session, ciphertext, 0, ciphertext.length, buffer, 0, buffer.length);
            byte[] decryptedData = Arrays.copyOf(buffer, len);
            return decryptedData;
          } catch (PKCS11Exception e) {
            throw new RuntimeException("[rsaOaepDecrypt] C_Decrypt error", e);
          }
        });
  }

  public GenRsaKeyPairResultDTO generateRsa2048KeyPair(String keyLabel) {

    String privateKeyId = UUID.randomUUID().toString();

    // create RSA key pair
    CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS_KEY_PAIR_GEN);

    byte[] publicExponentBytes = {0x01, 0x00, 0x01}; // 2^16 + 1
    CK_ATTRIBUTE[] publicKeyAttributes = {
      new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PUBLIC_KEY),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_RSA),
      // new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, false), -> 不能填
      // new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),    -> 不能填
      new CK_ATTRIBUTE(PKCS11Constants.CKA_PUBLIC_EXPONENT, publicExponentBytes),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_MODULUS_BITS, 2048),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, keyLabel + ".public"),
    };
    CK_ATTRIBUTE[] privateKeyAttributes = {
      new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PRIVATE_KEY),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_RSA),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, false),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, keyLabel),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_ID, privateKeyId),
    };
    return runHsmCommand(
        session -> {
          // objectHandles[0] -> public key
          // objectHandles[1] -> private key
          long[] objectHandles;
          try {
            objectHandles =
                pkcs11.C_GenerateKeyPair(
                    session, mechanism, publicKeyAttributes, privateKeyAttributes);
          } catch (PKCS11Exception e) {
            throw new RuntimeException("[generateRsa2048KeyPair] C_GenerateKeyPair error", e);
          }

          long publicKeyHandle = objectHandles[0];
          long privateKeyHandle = objectHandles[1];

          // get public key
          CK_ATTRIBUTE[] queryPublicKeyAttributes =
              new CK_ATTRIBUTE[] {
                new CK_ATTRIBUTE(PKCS11Constants.CKA_PUBLIC_EXPONENT),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_MODULUS),
              };

          try {
            pkcs11.C_GetAttributeValue(session, publicKeyHandle, queryPublicKeyAttributes);
          } catch (PKCS11Exception e) {
            throw new RuntimeException("[generateRsa2048KeyPair] C_GetAttributeValue error", e);
          }
          CK_ATTRIBUTE ckaPublicExponent = queryPublicKeyAttributes[0];
          CK_ATTRIBUTE ckaModulus = queryPublicKeyAttributes[1];

          // to RSAPublicKey object
          RSAPublicKey rsaPublicKey;
          try {
            rsaPublicKey =
                getRsaPublicKey(ckaModulus.getBigInteger(), ckaPublicExponent.getBigInteger());
          } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("[generateRsa2048KeyPair] getRsaPublicKey error", e);
          }

          return new GenRsaKeyPairResultDTO(privateKeyId, rsaPublicKey);
        });
  }

  public byte[] aesCbcDecrypt(String keyId, byte[] ivAndCiphertext) {
    long keyHandle = this.getKeyHandleByKeyId(keyId);

    // split iv and ciphertext
    byte[] iv = Arrays.copyOfRange(ivAndCiphertext, 0, 16);
    byte[] ciphertext = Arrays.copyOfRange(ivAndCiphertext, 16, ivAndCiphertext.length);

    // CKM_AES_CBC_PAD
    CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, iv);

    return runHsmCommand(
        session -> {
          try {
            pkcs11.C_DecryptInit(session, mechanism, keyHandle);
          } catch (PKCS11Exception e) {
            throw new RuntimeException("[aesCbcDecrypt] C_DecryptInit error", e);
          }

          try {
            byte[] buffer = new byte[ciphertext.length];
            int len =
                pkcs11.C_Decrypt(
                    session, ciphertext, 0, ciphertext.length, buffer, 0, buffer.length);
            byte[] decryptedData = Arrays.copyOf(buffer, len);
            return decryptedData;
          } catch (PKCS11Exception e) {
            throw new RuntimeException("[aesCbcDecrypt] C_Decrypt error", e);
          }
        });
  }

  /**
   * AES CBC encrypt (AES_CBC_PAD)
   *
   * @param keyId
   * @param plaintext
   * @return iv + ciphertext
   */
  public byte[] aesCbcEncrypt(String keyId, byte[] plaintext) {
    long keyHandle = this.getKeyHandleByKeyId(keyId);

    // random iv
    byte[] iv = genRandomBytes(16);

    // CKM_AES_CBC_PAD
    CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, iv);

    byte[] ciphertext =
        runHsmCommand(
            session -> {
              // C_EncryptInit
              try {
                pkcs11.C_EncryptInit(session, mechanism, keyHandle);
              } catch (PKCS11Exception e) {
                throw new RuntimeException("[aesCbcEncrypt] C_EncryptInit error", e);
              }

              // C_Encrypt
              byte[] buffer = new byte[plaintext.length + 32];
              try {
                int len =
                    pkcs11.C_Encrypt(
                        session, plaintext, 0, plaintext.length, buffer, 0, buffer.length);
                byte[] encryptedData = Arrays.copyOf(buffer, len);
                return encryptedData;

              } catch (PKCS11Exception e) {
                throw new RuntimeException("[aesCbcEncrypt] C_Encrypt error", e);
              }
            });

    byte[] ivAndCiphertext = new byte[iv.length + ciphertext.length];
    System.arraycopy(iv, 0, ivAndCiphertext, 0, iv.length);
    System.arraycopy(ciphertext, 0, ivAndCiphertext, iv.length, ciphertext.length);
    return ivAndCiphertext;
  }

  /**
   * CKM_AES_KEY_GEN
   *
   * @param keyLabel
   * @return keyId (CKA_ID)
   */
  public String generateAes256Key(String keyLabel) {
    String keyId = UUID.randomUUID().toString();

    // create AES key
    CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_GEN);
    CK_ATTRIBUTE[] attributes = {
      new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_SECRET_KEY),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_AES),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE_LEN, 32), // AES_256
      new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_DERIVE, true),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, false),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, keyLabel),
      new CK_ATTRIBUTE(PKCS11Constants.CKA_ID, keyId),
    };
    log.info("[generateAes256Key] start C_GenerateKey. keyId={}", keyId);
    runHsmCommand(
        session -> {
          try {
            return pkcs11.C_GenerateKey(session, mechanism, attributes);
          } catch (PKCS11Exception e) {
            throw new RuntimeException("[generateAes256Key] C_GenerateKey error", e);
          }
        });
    log.info("[generateAes256Key] C_GenerateKey success. keyId={}", keyId);
    return keyId;
  }

  /**
   * 用 keyId 查詢 keyHandle 並 「快取」
   *
   * <p>NOTE: 發現每次重啟之後 keyId 有可能會變，所以每次 application 啟動要重新取得 keyHandle， 也因此不跟 CloudHsm 一樣直接儲存
   * keyHandle 在 DB 而是
   *
   * @param keyId CKA_ID
   * @return keyHandle
   */
  private long getKeyHandleByKeyId(String keyId) {
    // get keyHandle in keyHandleMap
    if (keyHandleMap.get(keyId) != null) {
      return keyHandleMap.get(keyId);
    }

    long keyHandle =
        runHsmCommand(
            session -> {
              CK_ATTRIBUTE[] attributes = {
                new CK_ATTRIBUTE(PKCS11Constants.CKA_ID, keyId),
              };
              try {
                pkcs11.C_FindObjectsInit(session, attributes);
                long[] objectHandles = pkcs11.C_FindObjects(session, 1);
                pkcs11.C_FindObjectsFinal(session);
                return objectHandles[0];
              } catch (Exception e) {
                throw new RuntimeException("[findKeyHandleByKeyId] failed", e);
              }
            });

    keyHandleMap.put(keyId, keyHandle);

    return keyHandle;
  }

  private long findSlotId(String slotDescription) {
    Long slotId = null;
    // find slot id
    try {
      log.info("[findSlotId] C_GetSlotList");
      long[] slotIdList = pkcs11.C_GetSlotList(true);
      log.info("[findSlotId] C_GetSlotList={}", slotIdList);

      for (long l : slotIdList) {
        log.info("[findSlotId] C_GetSlotInfo({})", l);
        CK_SLOT_INFO ck_slot_info = pkcs11.C_GetSlotInfo(l);
        log.info("[findSlotId] ck_slot_info={}", ck_slot_info);

        if (ck_slot_info.slotDescription == null) {
          continue;
        }

        if (slotDescription.equals(new String(ck_slot_info.slotDescription).trim())) {
          slotId = l;
          break;
        }
      }
    } catch (Exception e) {
      log.error("[findSlotId] find slot id error={}", e.getMessage(), e);
    }

    // not found
    if (slotId == null) {
      throw new RuntimeException("[findSlotId] cannot find slot id of " + slotDescription);
    }

    log.info("[findSlotId] find slot id success: {}", slotId);
    return slotId;
  }

  private void login() {
    // Open Session
    try {
      loginSession =
          pkcs11.C_OpenSession(hsmSlotId, PKCS11Constants.CKF_SERIAL_SESSION, null, null);
    } catch (PKCS11Exception e) {
      throw new RuntimeException("[login] C_OpenSession error", e);
    }

    // 登入
    try {
      pkcs11.C_Login(loginSession, PKCS11Constants.CKU_USER, hsmPin.toCharArray());
    } catch (PKCS11Exception e) {
      throw new RuntimeException("[login] C_Login error", e);
    }
  }

  private <T> T runHsmCommand(HsmCommand<T> hsmCommand) {
    // Open Session
    long sessionHandle;
    try {
      sessionHandle =
          pkcs11.C_OpenSession(
              hsmSlotId,
              PKCS11Constants.CKF_SERIAL_SESSION | PKCS11Constants.CKF_RW_SESSION,
              null,
              null);
    } catch (PKCS11Exception e) {
      throw new RuntimeException("[runHsmCommand] C_OpenSession error", e);
    }

    // 登入一次就可以

    // Run command
    try {
      return hsmCommand.run(sessionHandle);

    } finally {
      // close session
      try {
        pkcs11.C_CloseSession(sessionHandle);
      } catch (PKCS11Exception e) {
        log.warn("[runHsmCommand] C_CloseSession error", e);
      }
    }
  }

  private void initializePkcs11() {
    CK_C_INITIALIZE_ARGS ckCInitializeArgs = new CK_C_INITIALIZE_ARGS();
    ckCInitializeArgs.CreateMutex = null;
    ckCInitializeArgs.DestroyMutex = null;
    ckCInitializeArgs.LockMutex = null;
    ckCInitializeArgs.UnlockMutex = null;
    // Load Pkcs11 動態連結庫 (.so)
    pkcs11 = null;
    try {
      log.info("pkcs11LibPath = {}", hsmLibPath);
      pkcs11 = PKCS11.getInstance(hsmLibPath, "C_GetFunctionList", ckCInitializeArgs, false);
      // Find Slot Id
      hsmSlotId = this.findSlotId(slotDescription);
      // Finalize pkcs11
      Runtime.getRuntime()
          .addShutdownHook(
              new Thread() {
                public void run() {
                  try {
                    log.info("pkcs11.C_Logout");
                    pkcs11.C_Logout(loginSession);
                  } catch (PKCS11Exception e) {
                    log.error("pkcs11.C_Logout error", e);
                  }

                  try {
                    log.info("pkcs11.C_Finalize");
                    pkcs11.C_Finalize(null);
                  } catch (PKCS11Exception e) {
                    log.error("pkcs11.C_Finalize error", e);
                  }
                }
              });
    } catch (IOException | PKCS11Exception e) {
      throw new RuntimeException("[initializePkcs11] Unable to load pkcs11 lib", e);
    }
  }

  private static byte[] genRandomBytes(int len) {
    byte[] ret = new byte[len];
    random.nextBytes(ret);
    return ret;
  }

  private RSAPublicKey getRsaPublicKey(BigInteger modulus, BigInteger publicExponent)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponent);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return (RSAPublicKey) keyFactory.generatePublic(keySpec);
  }

  // HSMImplBase implementation
  @Override
  public HSMInitializeResultDTO initialize(HSMInitializeInvokeDTO hsmInitializeDTO) {
    return null;
  }

  @Override
  public HSMGenerateKeyResultDTO generateKey(HSMGenerateKeyInvokeDTO hsmGenerateKeyInvokeDTO) {
    log.info("[HSMImpl] generateKey start");
    log.info("[HSMImpl] hsmGenerateKeyInvokeDTO=" + StringUtils.normalizeSpace(hsmGenerateKeyInvokeDTO.toString()));

    // Do generate key here...
    try {
      switch (hsmGenerateKeyInvokeDTO.getHsmMechanism()) {
        case CKM_AES_KEY_GEN:
          // Generate AES Key
          String keyId = this.generateAes256Key(hsmGenerateKeyInvokeDTO.getKeyLabel());

          log.info("[generateKey] generate aesKey success");
          return HSMGenerateKeyResultDTO.newInstanceOfSuccessWithData(keyId, null);

        case CKM_RSA_PKCS_KEY_PAIR_GEN:
          // Generate RSA Key Pair
          GenRsaKeyPairResultDTO genRsaKeyPairResultDTO =
              this.generateRsa2048KeyPair(hsmGenerateKeyInvokeDTO.getKeyLabel());

          log.info("[generateKey] generate rsaKeyPair success");
          return HSMGenerateKeyResultDTO.newInstanceOfSuccessWithData(
              genRsaKeyPairResultDTO.getPrivateKeyId(), genRsaKeyPairResultDTO.getPublicKey());

        default:
          return HSMGenerateKeyResultDTO.newInstanceOfExceptionHappened(
              new CloudHsmException(
                  "The HsmMechanism is not support. HsmMechanism="
                      + hsmGenerateKeyInvokeDTO.getHsmMechanism().toString()));
      }
    } catch (Exception e) {
      log.error("[generateKey] error", e);
      return HSMGenerateKeyResultDTO.newInstanceOfExceptionHappened(e);
    }
  }

  @Override
  public HSMEncryptResultDTO encrypt(HSMEncryptInvokeDTO hsmEncryptInvokeDTO) {
    log.info("[HSMImpl] encryption start");

    // Do encryption here...
    try {
      switch (hsmEncryptInvokeDTO.getHsmMechanism()) {
        case CKM_AES_CBC:
          // AES CBC encrypt
          return HSMEncryptResultDTO.newInstanceOfSuccessWithData(
              this.aesCbcEncrypt(
                  hsmEncryptInvokeDTO.getKeyId(), hsmEncryptInvokeDTO.getPlainText()));

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

  @Override
  public HSMDecryptResultDTO decrypt(HSMDecryptInvokeDTO hsmDecryptInvokeDTO) {
    log.info("[HSMImpl] decryption start");

    // Do decryption here...
    try {
      switch (hsmDecryptInvokeDTO.getHsmMechanism()) {
        case CKM_AES_CBC:
          // AES CBC decrypt

          return HSMDecryptResultDTO.newInstanceOfSuccessWithData(
              this.aesCbcDecrypt(
                  hsmDecryptInvokeDTO.getKeyId(), hsmDecryptInvokeDTO.getCipherText()));

        case CKM_RSA_PKCS_OAEP_WITH_SHA256_AND_MGF1_PADDING:
          // decryptWithRsaEcbOaepWithSHA256AndMGF1Padding

          return HSMDecryptResultDTO.newInstanceOfSuccessWithData(
              this.rsaOaepDecrypt(
                  hsmDecryptInvokeDTO.getKeyId(), hsmDecryptInvokeDTO.getCipherText()));

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

  @Override
  public HSMImportKeyResultDTO importKey(HSMImportKeyInvokeDTO hsmImportKeyInvokeDTO) {
    throw new OceanException(ResultStatus.SERVER_ERROR, "importKey not implemented");
  }

  @Override
  public HSMSignResultDTO sign(HSMSignInvokeDTO hsmSignInvokeDTO) {
    log.info("[HSMImpl] signing start");
    log.info("[HSMImpl] hsmSignInvokeDTO=" + StringUtils.normalizeSpace(hsmSignInvokeDTO.toString()));

    // Do signing here...
    try {
      switch (hsmSignInvokeDTO.getHsmMechanism()) {
        case CKM_SHA256_RSA_PKCS:
          // SHA256withRSA Signature

          return HSMSignResultDTO.newInstanceOfSuccessWithData(
              this.rsaSha256Sign(hsmSignInvokeDTO.getKeyId(), hsmSignInvokeDTO.getMessage())
          );

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
  public HSMCalculateCvvResultDTO calculateCvv(HSMCalculateCvvInvokeDTO hsmCalculateCvvInvokeDTO) {
    throw new OceanException(ResultStatus.SERVER_ERROR, "calculateCvv not implemented");
  }

  @Override
  public HSMCalculateAvResultDTO calculateAv(HSMCalculateAvInvokeDTO hsmCalculateAvInvokeDTO) {
    throw new OceanException(ResultStatus.SERVER_ERROR, "calculateAv not implemented");
  }
}
