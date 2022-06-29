package com.cherri.acs_kernel.plugin;

import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMCalculateAvInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMCalculateCvvInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMDecryptInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMEncryptInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMGenerateKeyInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMImportKeyInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMInitializeInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.invoke.HSMSignInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMCalculateAvResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMCalculateCvvResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMDecryptResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMEncryptResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMGenerateKeyResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMImportKeyResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMInitializeResultDTO;
import com.cherri.acs_kernel.plugin.dto.hsm.result.HSMSignResultDTO;

public interface HSMInterface extends IPlugin {

    HSMInitializeResultDTO initialize(HSMInitializeInvokeDTO hsmInitializeDTO);

    HSMGenerateKeyResultDTO generateKey(HSMGenerateKeyInvokeDTO hsmGenerateKeyInvokeDTO);

    HSMImportKeyResultDTO importKey(HSMImportKeyInvokeDTO hsmImportKeyInvokeDTO);

    HSMEncryptResultDTO encrypt(HSMEncryptInvokeDTO hsmEncryptInvokeDTO);

    HSMDecryptResultDTO decrypt(HSMDecryptInvokeDTO hsmDecryptInvokeDTO);

    HSMSignResultDTO sign(HSMSignInvokeDTO hsmSignInvokeDTO);

    HSMCalculateCvvResultDTO calculateCvv(HSMCalculateCvvInvokeDTO hsmCalculateCvvInvokeDTO);
    HSMCalculateAvResultDTO calculateAv(HSMCalculateAvInvokeDTO hsmCalculateAvInvokeDTO);

}
