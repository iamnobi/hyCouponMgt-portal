package com.cherri.acs_kernel.plugin.hsm.impl.dto;

public enum AesKeySize {
    AES256(256);

    private final int keySizeInBits;

    AesKeySize(int keySizeInBits) {
        this.keySizeInBits = keySizeInBits;
    }

    public int getKeySizeInBits() {
        return keySizeInBits;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name(), keySizeInBits);
    }
}
