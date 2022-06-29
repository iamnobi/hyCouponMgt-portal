package com.cherri.acs_kernel.plugin;

import com.cherri.acs_kernel.plugin.dto.authentication.invoke.AuthenticateInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.authentication.invoke.InitializeInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.authentication.result.AuthenticateResultDTO;
import com.cherri.acs_kernel.plugin.dto.authentication.result.InitializeResultDTO;

public interface AuthenticationInterface extends IPlugin {
    InitializeResultDTO initialize(InitializeInvokeDTO initializeInvokeDTO);

    AuthenticateResultDTO authenticate(AuthenticateInvokeDTO generateInvokeDTO);
}
