package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.controller.request.PluginIssuerRuntimeUpdateReqDTO;
import com.cherri.acs_portal.controller.response.PluginIssuerRuntimeResDTO;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.facade.PluginFacade;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@Validated
@RequestMapping("/auth/plugin")
public class PluginController extends ContextProvider {

    private PluginFacade facade;

    @Autowired
    public PluginController(PluginFacade facade) {
        this.facade = facade;
    }

    /** 取得銀行所有套件設定列表 */
    @GetMapping(value = "/issuer/runtimeprop/get/{issuerBankId}")
    @PreAuthorize(
      "hasRole('ROLE_PLUGIN_ISSUER_PROPERTY_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PLUGIN_ISSUER_RUNTIME_PROPERTY_LIST)
    public ApiResponse getAllPluginIssuerRuntimeProperty(@PathVariable Long issuerBankId) {
        List<PluginIssuerRuntimeResDTO> resDTOList =
          facade.getAllPluginIssuerRuntimeProperty(issuerBankId);
        return new ApiResponse<>(resDTOList);
    }

    @PostMapping("/issuer/runtimeprop/update")
    @PreAuthorize(
      "hasRole('ROLE_PLUGIN_ISSUER_PROPERTY_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#runtimeUpdateReqDTO.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PLUGIN_ISSUER_RUNTIME_PROPERTY_MODIFY)
    public ApiResponse updateAllPluginIssuerRuntimeProperty(
      @Valid @RequestBody PluginIssuerRuntimeUpdateReqDTO runtimeUpdateReqDTO) {
        facade.updateAllPluginIssuerRuntimeProperty(
          runtimeUpdateReqDTO.getIssuerBankId(),
          getUserAccount(),
          runtimeUpdateReqDTO.getPropertyCollectionList());
        return new ApiResponse<>();
    }
}
