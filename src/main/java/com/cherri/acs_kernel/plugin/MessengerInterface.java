package com.cherri.acs_kernel.plugin;

import com.cherri.acs_kernel.plugin.dto.messenger.invoke.InitializeInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.messenger.invoke.SendAuthInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.messenger.result.InitializeResultDTO;
import com.cherri.acs_kernel.plugin.dto.messenger.result.SendAuthResultDTO;

public interface MessengerInterface extends IPlugin {
    InitializeResultDTO initialize(InitializeInvokeDTO initializeInvokeDTO);

    SendAuthResultDTO sendAuthentication(SendAuthInvokeDTO generateInvokeDTO);
}
