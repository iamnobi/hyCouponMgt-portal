package com.cherri.acs_portal.model.enumerator;

import com.cherri.acs_kernel.plugin.enumerator.HSMSignMechanism;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CsrSignatureAlgorithm {
    SHA1("SHA1withRSA", HSMSignMechanism.CKM_SHA1_RSA_PKCS),
    SHA256("SHA256WithRSA", HSMSignMechanism.CKM_SHA256_RSA_PKCS);

    private final String csrSignatureAlgorithmName;
    private final HSMSignMechanism hsmSignMechanism;
}
