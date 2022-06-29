package com.cherri.acs_kernel.plugin.hsm.impl.dto;

import javax.crypto.Cipher;

public enum CipherMode {
    ENCRYPT(Cipher.ENCRYPT_MODE),
    DECRYPT(Cipher.DECRYPT_MODE),
    WRAP(Cipher.WRAP_MODE),
    UNWRAP(Cipher.UNWRAP_MODE);

    private final int code;

    CipherMode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
