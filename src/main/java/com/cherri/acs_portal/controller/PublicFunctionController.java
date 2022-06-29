package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.controller.request.LoginForgetMimaRequest;
import com.cherri.acs_portal.controller.request.LoginResetMimaRequest;
import com.cherri.acs_portal.controller.response.MimaPolicyQueryResponse;
import com.cherri.acs_portal.controller.response.UiConfigDTO;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.facade.AccountManagementFacade;
import com.cherri.acs_portal.facade.MimaPolicyFacade;
import com.cherri.acs_portal.facade.UiConfigFacade;
import javax.validation.Valid;
import ocean.acs.commons.exception.DatabaseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicFunctionController extends ContextProvider {

    private final UiConfigFacade uiConfigFacade;
    private final MimaPolicyFacade mimaPolicyFacade;
    private final AccountManagementFacade accountManagementFacade;

    public PublicFunctionController(UiConfigFacade uiConfigFacade,
        MimaPolicyFacade mimaPolicyFacade,
        AccountManagementFacade accountManagementFacade) {
        this.uiConfigFacade = uiConfigFacade;
        this.mimaPolicyFacade = mimaPolicyFacade;
        this.accountManagementFacade = accountManagementFacade;
    }

    @GetMapping("/ui-configuration")
    public UiConfigDTO getUiConfig() {
        return uiConfigFacade.getUiConfig();
    }

    /** Portal UI 查詢密碼原則設定 */
    @GetMapping("/policy/mima/ui/desc/{issuerBankId}")
    public ApiResponse<MimaPolicyQueryResponse> queryDesc(
        @PathVariable("issuerBankId") Long issuerBankId) {
        return mimaPolicyFacade.query(issuerBankId);
    }

    @PostMapping("/mima/change/link")
    public ApiResponse resetMima(@RequestBody @Valid LoginResetMimaRequest request) throws DatabaseException {
        return accountManagementFacade.changeMimaByLink(request);
    }

    @PostMapping("/mima/forget")
    public ApiResponse forgetMima(@RequestBody @Valid LoginForgetMimaRequest loginForgetMimaRequest) {
        accountManagementFacade.forgetMima(loginForgetMimaRequest);
        return ApiResponse.SUCCESS_API_RESPONSE;
    }

}
