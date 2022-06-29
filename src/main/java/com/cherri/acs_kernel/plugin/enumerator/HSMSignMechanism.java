package com.cherri.acs_kernel.plugin.enumerator;

public enum HSMSignMechanism {
  // For generating CSR
  CKM_SHA256_RSA_PKCS,
  CKM_SHA1_RSA_PKCS, // Xml Signing

  // For JWS
  CKM_SHA256_RSA_PKCS_PSS,
}
