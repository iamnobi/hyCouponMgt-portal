package com.cherri.acs_portal.util;

import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.model.enumerator.CsrSignatureAlgorithm;
import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.List;
import javax.annotation.Nonnull;
import javax.security.auth.x500.X500Principal;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.ParsingException;
import sun.security.util.DerOutputStream;
import sun.security.x509.AlgorithmId;

@Log4j2
public class CertificateUtils {

    private static final String KEY_STORE_ALGORITHM = "PKCS12";
    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * @return KeyPair contains: PrivateKey and PublicKey
     */
    public static KeyPair genRSAKey() {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            gen.initialize(KEY_SIZE);
            return gen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("[genRSAKey] algorithm={}", KEY_ALGORITHM, e);
            throw new OceanException(e.getMessage());
        }
    }

    public static String genCsr(
      PrivateKey privateKey, PublicKey publicKey, String distinguishedName) {
        try {
            X500Principal subject = new X500Principal(distinguishedName);
            ContentSigner signGen = new JcaContentSignerBuilder("SHA256WithRSA").build(privateKey);
            PKCS10CertificationRequestBuilder builder =
              new JcaPKCS10CertificationRequestBuilder(subject, publicKey);
            PKCS10CertificationRequest csr = builder.build(signGen);
            return writeToString(csr, "CSR");
        } catch (OperatorCreationException e) {
            log.error("[genCsr] {}", writeToString(privateKey, "Private Key"), e);
            throw new OceanException(
              ResultStatus.MESSAGE_RECEIVED_INVALID, MessageConstants.get(MessageConstants.PRIVATE_KEY_INVALID));
        }
    }

    public static String calculateRsaModulusSha256Hex(RSAPublicKey rsaPublicKey) {
        byte[] modulus = rsaPublicKey.getModulus().toByteArray();
        return DigestUtils.sha256Hex(modulus);
    }

    public static <T> String writeToString(T key, String srcFileName) {
        try (StringWriter writer = new StringWriter()) {
            try (JcaPEMWriter pem = new JcaPEMWriter(writer)) {
                pem.writeObject(key);
                pem.flush();
            }
            return writer.toString();
        } catch (IOException e) {
            log.error("[writeToString] write key error", e);
            throw new OceanException(
              ResultStatus.MESSAGE_RECEIVED_INVALID,
              String.format("The %s %s", srcFileName, MessageConstants.get(MessageConstants.IO_WRITE_ERROR)));
        }
    }

    public static byte[] mergeToP12(X509Certificate cert, String privateKeyText, String password) {
        PrivateKey privateKey = readPrivatekey(privateKeyText);
        try {
            KeyStore pkcs12 = getPKCS12KeyStore();
            pkcs12.setKeyEntry(
              "cherriPortalPrivateKey",
              privateKey,
              password.toCharArray(),
              new X509Certificate[]{cert});
            try (ByteArrayOutputStream p12 = new ByteArrayOutputStream()) {
                pkcs12.store(p12, password.toCharArray());
                return p12.toByteArray();
            }
        } catch (KeyStoreException | NoSuchAlgorithmException e) {
            log.error("[mergeToP12] store keyStore error", e);
            throw new OceanException(e.getMessage());
        } catch (IOException e) {
            log.error("[mergeToP12] store keyStore error", e);
            throw new OceanException(
              ResultStatus.MESSAGE_RECEIVED_INVALID,
              "The p12 file " + MessageConstants.get(MessageConstants.IO_WRITE_ERROR));
        } catch (CertificateException e) {
            log.error("[mergeToP12] store keyStore error", e);
            throw new OceanException(ResultStatus.MESSAGE_RECEIVED_INVALID, e.getMessage());
        }
    }

    public static X509Certificate readCert(InputStream certInput) {
        try {
            return (X509Certificate) new CertificateFactory().engineGenerateCertificate(certInput);
        } catch (CertificateException e) {
            log.error("[readCert] generate certificate error", e);
            throw new OceanException(ResultStatus.MESSAGE_RECEIVED_INVALID, e.getMessage());
        }
    }

    public static X509Certificate readP12(ByteArrayInputStream certInput, String password) {
        try {
            KeyStore p12 = getPKCS12KeyStore(certInput, password.toCharArray());
            Enumeration e = p12.aliases();
            String alias = (String) e.nextElement();
            return (X509Certificate) p12.getCertificate(alias);
        } catch (KeyStoreException e) {
            log.error("[readP12] get X509 certificates error", e);
            throw new OceanException(e.getMessage());
        }
    }

    private static PrivateKey readPrivatekey(String unencryptedPrivateKey) {
        byte[] encoded = base64Decode(unencryptedPrivateKey, "PRIVATE");
        PKCS8EncodedKeySpec kspec = new PKCS8EncodedKeySpec(encoded);
        try {
            return getKeyFactory().generatePrivate(kspec);
        } catch (InvalidKeySpecException e) {
            log.error("[readPrivatekey] generate private key error", e);
            throw new OceanException(
              ResultStatus.MESSAGE_RECEIVED_INVALID, MessageConstants.get(MessageConstants.KEY_SPECIFICATIONS_INVALID));
        }
    }

    public static PublicKey readPublicKey(String unencryptedPublicKey) {
        byte[] encoded = base64Decode(unencryptedPublicKey, "PUBLIC");
        X509EncodedKeySpec xspec = new X509EncodedKeySpec(encoded);
        try {
            return getKeyFactory().generatePublic(xspec);
        } catch (InvalidKeySpecException e) {
            log.error("[readPublicKey] generate public key error", e);
            throw new OceanException(
              ResultStatus.MESSAGE_RECEIVED_INVALID, MessageConstants.get(MessageConstants.KEY_SPECIFICATIONS_INVALID));
        }
    }

    private static byte[] base64Decode(String keyText, String type) {
        if (StringUtils.isEmpty(keyText)) {
            throw new OceanException(
              ResultStatus.COLUMN_NOT_EMPTY, type + " KEY " + MessageConstants.get(MessageConstants.COLUMN_NOT_EMPTY));
        }
        if ("PRIVATE".equals(type)) {
            type = String.format("RSA %s", type);
        }

        keyText =
          keyText
            .replace("-----BEGIN " + type + " KEY-----", "")
            .replace("-----END " + type + " KEY-----", "");

        return Base64.decode(keyText);
    }

    private static KeyFactory getKeyFactory() {
        try {
            return KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            log.error("[getKeyFactory] algorithm={}", KEY_ALGORITHM, e);
            throw new OceanException(e.getMessage());
        }
    }

    public static String genRandomPassword() {
        final char[] allAllowed = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789").toCharArray();
        int length = 10;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(allAllowed[random.nextInt(allAllowed.length)]);
        }

        return password.toString();
    }

    private static KeyStore getPKCS12KeyStore() {
        return getPKCS12KeyStore(null, null);
    }

    private static KeyStore getPKCS12KeyStore(InputStream certInput, char[] password) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_ALGORITHM);
            keyStore.load(certInput, password);
            return keyStore;
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            log.error("[getPKCS12KeyStore] load keyStore error", e);
            throw new OceanException(e.getMessage());
        } catch (IOException e) {
            log.error("[getPKCS12KeyStore] load keyStore error", e);
            String errMsg = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
            if (errMsg.contains("keystore password was incorrect")) {
                throw new OceanException(ResultStatus.INVALID_FORMAT,
                    MessageConstants.get(MessageConstants.MIMA_INCORRECT));
            } else if (errMsg.contains("toderinputstream rejects tag type")) {
                throw new OceanException(
                  ResultStatus.NOT_SUPPORTED, "File " + MessageConstants.get(MessageConstants.NOT_SUPPORTED));
            }
            throw new OceanException(MessageConstants.get(MessageConstants.IO_LOAD_ERROR));
        } catch (CertificateException e) {
            log.error("[getPKCS12KeyStore] load keyStore error", e);
            throw new OceanException(ResultStatus.MESSAGE_RECEIVED_INVALID, e.getMessage());
        }
    }

    public static List<X509Certificate> readPkcs7CertificateChain(
      @Nonnull InputStream certificateChain) {
        try {
            return (List<X509Certificate>) new CertificateFactory().engineGenerateCertificates(certificateChain);
        } catch (CertificateException e) {
            log.error("[readPkcs7CertificateChain] generate certificate error", e);
            throw new OceanException(ResultStatus.MESSAGE_RECEIVED_INVALID, e.getMessage());
        }
    }

    private static boolean isCaCertificate(X509Certificate certificate) {
        boolean isCaCertificate;
        boolean[] keyUsage = certificate.getKeyUsage();
        try {
            isCaCertificate = keyUsage[5];
        } catch (Exception e) {
            // 憑證可能會沒有 keyUsage 的情況，會回傳 null
            isCaCertificate = false;
        }
        return isCaCertificate;
    }

    private static boolean isSelfSigned(X509Certificate certificate) {
        X500Principal subjectDn = certificate.getSubjectX500Principal();
        X500Principal issuerDn = certificate.getIssuerX500Principal();
        return subjectDn.equals(issuerDn);
    }

    private static boolean isRootCaCertificate(X509Certificate certificate) {
        return isCaCertificate(certificate) && isSelfSigned(certificate);
    }

    public static byte[] createCertificationRequestInfo(X500Name x500Name, PublicKey publicKey) {
        try {
            CertificationRequestInfo csrInfo = new CertificationRequestInfo(x500Name,
                SubjectPublicKeyInfo.getInstance(publicKey.getEncoded()), new DERSet());
            return csrInfo.getEncoded();

        } catch (IOException e) {
            log.error("[createCertificationRequestInfo] error", e);
            throw new OceanException(ResultStatus.SERVER_ERROR,
              "Server error while create CertificationRequestInfo");
        }
    }

    public static String encodeCsr(byte[] certReqInfo, byte[] csrSignature,
      CsrSignatureAlgorithm csrSignatureAlgorithm) {
        try {
            // create PKCS#10 Certificate Signing Request (CSR)
            byte[] csrDEREncoded = createCertificationRequestValue(
              certReqInfo, csrSignatureAlgorithm.getCsrSignatureAlgorithmName(), csrSignature);

            // Parse byte array to string.
            String csrPEMEncoded = createPEMFormatCsr(csrDEREncoded);

            return csrPEMEncoded;

        } catch (NoSuchAlgorithmException e) {
            log.error("[encodeCsr] invalid csr algorithm = {}",
              csrSignatureAlgorithm.getCsrSignatureAlgorithmName(), e);
            throw new OceanException(
              ResultStatus.SERVER_ERROR,
              "[encodeCsr] invalid csr algorithm = " + csrSignatureAlgorithm
                .getCsrSignatureAlgorithmName());
        } catch (Exception e) {
            log.error("[encodeCsr] error", e);
            throw new OceanException(ResultStatus.SERVER_ERROR,
              ResultStatus.SERVER_ERROR.toString());
        }
    }

    private static byte[] createCertificationRequestValue(
      byte[] certReqInfo, String signAlgo, byte[] signature)
      throws IOException, NoSuchAlgorithmException {
        final DerOutputStream der1 = new DerOutputStream();
        der1.write(certReqInfo);

        // add signature algorithm identifier, and a digital signature on the certification request
        // information
        AlgorithmId.get(signAlgo).encode(der1);
        der1.putBitString(signature);

        // final DER encoded output
        final DerOutputStream der2 = new DerOutputStream();
        der2.write((byte) 48, der1);
        return der2.toByteArray();
    }

    private static String createPEMFormatCsr(byte[] data) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(out);
        ps.println("-----BEGIN NEW CERTIFICATE REQUEST-----");
        ps.println(java.util.Base64.getMimeEncoder().encodeToString(data));
        ps.println("-----END NEW CERTIFICATE REQUEST-----");
        return out.toString();
    }

    private static String createPEMFormatCsr2(byte[] data) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(out);
        ps.println("-----BEGIN CERTIFICATE REQUEST-----");
        ps.println(BaseEncoding.base64().withSeparator("\n", 64).encode(data));
        ps.println("-----END CERTIFICATE REQUEST-----");
        return out.toString();
    }
}
