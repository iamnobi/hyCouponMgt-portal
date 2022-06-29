package com.cherri.acs_kernel.plugin.hsm.impl.dto;

public enum RsaSignatureAlgorithm {
    SHA1("SHA1withRSA"),
    SHA256("SHA256WithRSA");

    private final String jceAlgorithmName;

    RsaSignatureAlgorithm(String jceAlgorithmName) {
        this.jceAlgorithmName = jceAlgorithmName;
    }

    public String getJceAlgorithmName() {
        return jceAlgorithmName;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name(), jceAlgorithmName);
    }
}
