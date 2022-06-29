package com.cherri.acs_kernel.plugin.enumerator;

public enum HSMEncryptDecryptMechanism {
  // For encryption/decryption
  CKM_AES_CBC,
  CKM_AES_GCM,
  CKM_DES_ECB,
  CKM_RSA_PKCS_OAEP_WITH_SHA256_AND_MGF1_PADDING,
  CKM_DES3_CBC,
  CKM_SHA256_HMAC
}
