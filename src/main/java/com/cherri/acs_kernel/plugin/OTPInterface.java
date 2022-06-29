package com.cherri.acs_kernel.plugin;

import com.cherri.acs_kernel.plugin.dto.otp.invoke.GenerateInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.otp.invoke.InitializeInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.otp.invoke.VerifyInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.otp.result.GenerateResultDTO;
import com.cherri.acs_kernel.plugin.dto.otp.result.InitializeResultDTO;
import com.cherri.acs_kernel.plugin.dto.otp.result.VerifyResultDTO;

public interface OTPInterface extends IPlugin {
    InitializeResultDTO initialize(InitializeInvokeDTO initializeInvokeDTO);

    GenerateResultDTO generate(GenerateInvokeDTO generateInvokeDTO);

    VerifyResultDTO verify(VerifyInvokeDTO verifyResultDTO);
}
