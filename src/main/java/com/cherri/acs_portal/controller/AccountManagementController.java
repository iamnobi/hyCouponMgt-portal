package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.controller.request.BankIdPageQueryDTO;
import com.cherri.acs_portal.controller.request.ChangeMimaRequest;
import com.cherri.acs_portal.controller.request.CheckIdleDayRequest;
import com.cherri.acs_portal.controller.request.CheckMimaIsExpiredRequest;
import com.cherri.acs_portal.controller.request.UnlockUserOperationReqDTO;
import com.cherri.acs_portal.controller.request.UserOperationReqDTO;
import com.cherri.acs_portal.controller.request.UserPageQueryDTO;
import com.cherri.acs_portal.controller.request.UserProfileUpdateRequest;
import com.cherri.acs_portal.controller.response.BankGroupDto;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.controller.response.UserAccountGroupResponse;
import com.cherri.acs_portal.controller.response.UserAccountResponse;
import com.cherri.acs_portal.dto.ApiPageResponse;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.usermanagement.AuditLogListRequest;
import com.cherri.acs_portal.dto.usermanagement.UserAccountDTO;
import com.cherri.acs_portal.facade.AccountManagementFacade;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/auth/account-management")
public class AccountManagementController extends ContextProvider {

    @Autowired
    private AccountManagementFacade accountManagementFacade;

    @PostMapping("/user/list")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.USER_LIST)
    @PreAuthorize("hasRole('ROLE_USER_GROUP_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#query.issuerBankId)")
    public ApiPageResponse getUserList(@RequestBody UserPageQueryDTO query) {
        PagingResultDTO<UserAccountGroupResponse> result = accountManagementFacade
          .findUserList(query);
        return new ApiPageResponse<>(result);
    }

    @GetMapping("/user/group/list")
    @Secured("ROLE_USER_GROUP_QUERY")
    public ApiResponse<List<BankGroupDto>> getGroupList() {
        List<BankGroupDto> bankGroupDtoList = accountManagementFacade.getBankGroups(getIssuerBankId());
        return new ApiResponse<>(bankGroupDtoList);
    }

    @GetMapping(value = "/user/{issuerBankId}/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.USER_LIST)
    @PreAuthorize("hasRole('ROLE_USER_GROUP_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    public ApiResponse<UserAccountResponse> getUser(@PathVariable("issuerBankId") Long issuerBankId,
      @PathVariable("userId") Long userId) {
        UserAccountResponse result = accountManagementFacade.findUser(userId);
        return new ApiResponse<>(result);
    }

    @PostMapping("/user/create")
    @PreAuthorize("hasRole('ROLE_USER_GROUP_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#createDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.USER_CREATE)
    public ApiResponse<DataEditResultDTO> createUser(@RequestBody UserOperationReqDTO createDto) {
        if (createDto.getGroupIdList() == null || createDto.getGroupIdList().isEmpty()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "groupIdList cannot be empty");
        }
        UserAccountDTO serviceDto = new UserAccountDTO(createDto);
        serviceDto.setCreator(getUserAccount());
        return new ApiResponse<>(accountManagementFacade.createUser(serviceDto, createDto.getGroupIdList()));
    }

    @PostMapping("/user/update")
    @PreAuthorize("hasRole('ROLE_USER_GROUP_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#updateDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.USER_UPDATE)
    public ApiResponse updateUser(@RequestBody UserOperationReqDTO updateDto) {
        if (updateDto.getGroupIdList() == null || updateDto.getGroupIdList().isEmpty()) {
            throw new OceanException(ResultStatus.ILLEGAL_ARGUMENT, "groupIdList cannot be empty");
        }
        UserAccountDTO serviceDto = new UserAccountDTO(updateDto);
        serviceDto.setUpdater(getUserAccount());
        return new ApiResponse<>(accountManagementFacade.updateUser(serviceDto, updateDto.getGroupIdList()));
    }

    /**
     * user 自己 update 自己的 profile
     */
    @PostMapping("/user/profile/update")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.USER_PROFILE_UPDATE)
    public ApiResponse updateUserProfile(@Valid @RequestBody UserProfileUpdateRequest request) {
        UserAccountDTO dto = new UserAccountDTO();
        dto.setDepartment(request.getDepartment());
        dto.setName(request.getName());
        dto.setPhone(request.getPhone());
        dto.setExt(request.getExt());

        dto.setIssuerBankId(getIssuerBankId());
        dto.setAccount(getUserAccount());
        dto.setUpdater(getUserAccount());
        return new ApiResponse<>(accountManagementFacade.updateUser(dto, null));
    }
    /**
     * user 查詢自己的 profile
     */
    @GetMapping("/user/profile")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.USER_PROFILE_GET)
    public ApiResponse getUserProfile() {
        return new ApiResponse(accountManagementFacade.findUser(getIssuerBankId(), getUserAccount()));
    }

    /**
     * 刪除使用者
     */
    @PostMapping("/user/delete")
    @PreAuthorize("hasRole('ROLE_USER_GROUP_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#deleteDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.USER_DELETE)
    public ApiResponse deleteUser(@RequestBody DeleteDataDTO deleteDto) {
        deleteDto.setUser(getUserAccount());
        accountManagementFacade.deleteUser(deleteDto);
        return ApiResponse.SUCCESS_API_RESPONSE;
    }

    @PostMapping("/lock/user/list")
    @PreAuthorize("hasRole('ROLE_UNLOCK_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#query.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ACCOUNT_LOCK_USER_LIST)
    public ApiPageResponse getLockUserList(@RequestBody BankIdPageQueryDTO query) {
        query.setIssuerBankId(query.getIssuerBankId(), getIssuerBankId());
        PagingResultDTO<UserAccountDTO> result = accountManagementFacade.findLockUserList(query);
        return new ApiPageResponse<>(result);
    }

    @PostMapping("/unlock/user")
    @PreAuthorize("hasRole('ROLE_UNLOCK_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#reqDto.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.ACCOUNT_UNLOCK_USER)
    public ApiResponse unlockUserById(@RequestBody UnlockUserOperationReqDTO reqDto) {
        accountManagementFacade.unlockUser(reqDto.getId());
        return ApiResponse.SUCCESS_API_RESPONSE;
    }

    /**
     * 操作記錄
     */
    @PostMapping("/audit-log/list")
    @PreAuthorize("hasRole('ROLE_AUDIT_LOG_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#auditLogListRequest.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.AUDIT_LOG_LIST)
    public ApiPageResponse list(@RequestBody AuditLogListRequest auditLogListRequest) {
        if (auditLogListRequest.getIssuerBankId() == null) {
            auditLogListRequest.setIssuerBankId(getIssuerBankId());
        }
        return new ApiPageResponse<>(
          accountManagementFacade.listAuditLogByPage(auditLogListRequest));
    }

    /**
     * 執行動作列表
     */
    @GetMapping("/audit-log/method-name")
    @Secured({"ROLE_AUDIT_LOG_QUERY"})
    public ApiResponse auditLogMethodNameList() {
        return ApiResponse.valueOf(Arrays.asList(AuditLogMethodNameEnum.values()));
    }

    /** 變更密碼 */
    @PostMapping("/user/mima")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.MIMA_MODIFY)
    public ApiResponse<Boolean> changeMima(@RequestBody ChangeMimaRequest changeMimaRequest,
        HttpServletResponse httpServletResponse) {
        return accountManagementFacade.changeMima(changeMimaRequest, httpServletResponse);
    }
}
