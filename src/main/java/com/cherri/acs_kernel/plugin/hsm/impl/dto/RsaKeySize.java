package com.cherri.acs_kernel.plugin.hsm.impl.dto;

public enum RsaKeySize {
    RSA2048(2048);

    private final int keySizeInBits;

    RsaKeySize(int keySizeInBits) {
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
