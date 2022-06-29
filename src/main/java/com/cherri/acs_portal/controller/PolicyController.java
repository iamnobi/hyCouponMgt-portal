package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.controller.request.MimaPolicyUpdateRequest;
import com.cherri.acs_portal.controller.response.MimaPolicyQueryResponse;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.facade.MimaPolicyFacade;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 原則管理API
 *
 * @author Alan Chen
 */
@RestController
@RequestMapping("/auth/policy")
public class PolicyController {

    private final MimaPolicyFacade mimaPolicyFacade;

    @Autowired
    public PolicyController(MimaPolicyFacade mimaPolicyFacade) {
        this.mimaPolicyFacade = mimaPolicyFacade;
    }

    /** 取得基本欄位檢核條件設定 */
    @GetMapping("/mima/ui/rule")
    public ApiResponse<Map<String, Object>> getColumnBaseRule() {
        return mimaPolicyFacade.getColumnBaseRule();
    }

    /** 查詢密碼原則設定 */
    @GetMapping("/mima/{issuerBankId}")
    @Secured("ROLE_MIMA_POLICY_QUERY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.MIMA_POLICY_QUERY)
    public ApiResponse<MimaPolicyQueryResponse> query(
      @PathVariable("issuerBankId") Long issuerBankId) {
        return mimaPolicyFacade.query(issuerBankId);
    }

    /** 更新密碼原則設定 */
    @PostMapping("/mima/{issuerBankId}")
    @Secured("ROLE_MIMA_POLICY_MODIFY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.MIMA_POLICY_MODIFY)
    public ApiResponse<Boolean> update(
      @PathVariable("issuerBankId") Long issuerBankId,
      @RequestBody MimaPolicyUpdateRequest request) {
        return mimaPolicyFacade.update(request, issuerBankId);
    }
}
