package com.cherri.acs_kernel.plugin.hsm.impl.model.domain;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.UUID;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@Data
@NoArgsConstructor
public class MockHsmKey {
    private String id;
    private String keyLabel;
    private KeyType keyType;
    private byte[] key;

    public MockHsmKey(String keyLabel, KeyType keyType, byte[] key) {
        this.id = UUID.randomUUID().toString();
        this.keyLabel = keyLabel;
        this.keyType = keyType;
        this.key = key;
    }

    public SecretKey toAesKey() {
        return new SecretKeySpec(getKey(), "AES");
    }

    @SneakyThrows
    public RSAPrivateKey toRsaPrivateKey() {
        PKCS8EncodedKeySpec spec =
            new PKCS8EncodedKeySpec(getKey());

        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    @SneakyThrows
    public SecretKey toDesKey() {
        DESKeySpec dks = new DESKeySpec(getKey());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        return skf.generateSecret(dks);
    }

    @SneakyThrows
    public SecretKey toDes3Key() {
        DESedeKeySpec dks = new DESedeKeySpec(getKey());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");
        return skf.generateSecret(dks);
    }

    @SneakyThrows
    public SecretKey toHmacKey() {
        return new SecretKeySpec(getKey(), "HmacSHA256");
    }
}
