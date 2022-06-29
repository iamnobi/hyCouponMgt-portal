package com.cherri.acs_portal.controller;

import com.cherri.acs_portal.aspect.AuditLogHandler;
import com.cherri.acs_portal.aspect.AuditLogMethodNameEnum;
import com.cherri.acs_portal.config.security.CustomizedUserPrincipal;
import com.cherri.acs_portal.controller.request.CreateBankGroupReqDto;
import com.cherri.acs_portal.controller.request.CreateOrDeleteGroupMemberReqDto;
import com.cherri.acs_portal.controller.request.DeleteBankGroupReqDto;
import com.cherri.acs_portal.controller.request.UpdateBankGroupReqDto;
import com.cherri.acs_portal.controller.request.UpdateGroupPermissionReqDto;
import com.cherri.acs_portal.controller.response.BankGroupDto;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.controller.response.GroupMemberDto;
import com.cherri.acs_portal.controller.response.ModuleSettingPermissionResponse;
import com.cherri.acs_portal.controller.response.PermissionDto;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.PageQueryDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.usermanagement.UserGroupDto;
import com.cherri.acs_portal.facade.PermissionFacade;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.ThreeDsVersion;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/permission")
public class PermissionController extends ContextProvider {

    @Autowired
    private PermissionFacade permissionFacade;

    /**
     * 取得登入帳號的權限
     */
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<PermissionDto> getUserPermissions() {
        PermissionDto permissionDto = permissionFacade.getUserPermissions(getIssuerBankId(), getUserAccount());
        return new ApiResponse<PermissionDto>(permissionDto);
    }

    /**
     * 查詢群組
     */
    @PreAuthorize("hasRole('ROLE_USER_GROUP_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PERMISSION_GET)
    @GetMapping(value = "/group/bank/{issuerBankId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<List<BankGroupDto>> getBankGroups(
      @PathVariable("issuerBankId") Long issuerBankId) {
        List<BankGroupDto> bankGroups = permissionFacade.getBankGroups(issuerBankId);
        return new ApiResponse<List<BankGroupDto>>(bankGroups);
    }

    /**
     * 新增群組
     */
    @PreAuthorize("hasRole('ROLE_USER_GROUP_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#request.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PERMISSION_ROLE_CREATE)
    @PostMapping(value = "/group/bank/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<DataEditResultDTO> createBankGroup(
      @Valid @RequestBody CreateBankGroupReqDto request) {
        UserGroupDto userGroupDto = UserGroupDto.valueOf(request);
        userGroupDto.setCreator(super.getUserAccount());
        DataEditResultDTO dataEditResultDto = permissionFacade.createBankGroup(userGroupDto);
        return new ApiResponse<DataEditResultDTO>(dataEditResultDto);
    }

    /**
     * 修改群組
     */
    @PreAuthorize("hasRole('ROLE_USER_GROUP_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#request.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PERMISSION_ROLE_UPDATE)
    @PostMapping(value = "/group/bank/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<DataEditResultDTO> updateBankGroup(
      @Valid @RequestBody UpdateBankGroupReqDto request) {
        validateIsSystemDefaultUserGroupId(request.getGroupId());

        UserGroupDto userGroupDto = UserGroupDto.valueOf(request);
        userGroupDto.setUpdater(super.getUserAccount());
        DataEditResultDTO dataEditResultDto = permissionFacade.updateBankGroup(userGroupDto);
        return new ApiResponse<DataEditResultDTO>(dataEditResultDto);
    }

    /**
     * 檢查userGroupId是否是系統預設群組權限
     */
    private void validateIsSystemDefaultUserGroupId(Long userGroupId) {
        long defaultSysOrgUserGroupId = -1;
        if (userGroupId == defaultSysOrgUserGroupId) {
            throw new OceanException("default SYSTEM ORG user group editing is not allowed");
        }
    }

    /**
     * 刪除群組前，檢查群組中只有一個群組的成員列表
     */
    @PreAuthorize("hasRole('ROLE_USER_GROUP_QUERY') && @permissionService.checkAccessMultipleBank(authentication.principal,#request.issuerBankId)")
    @PostMapping(value = "/group/bank/delete/check", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<List<String>> deleteBankGroupCheck(@Valid @RequestBody DeleteBankGroupReqDto request) {
        return new ApiResponse(permissionFacade.deleteBankGroupCheck(request));
    }

    /**
     * 刪除群組
     */
    @PreAuthorize("hasRole('ROLE_USER_GROUP_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#request.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PERMISSION_ROLE_DELETE)
    @PostMapping(value = "/group/bank/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<?> deleteBankGroup(@Valid @RequestBody DeleteBankGroupReqDto request) {
        validateIsSystemDefaultUserGroupId(request.getGroupId());

        DeleteDataDTO deleteDataDto = new DeleteDataDTO(request.getGroupId(),
          request.getIssuerBankId(),
          AuditStatus.PUBLISHED, super.getUserAccount(), ThreeDsVersion.TWO.getCode());
        deleteDataDto.setUser(super.getUserAccount());
        permissionFacade.deleteBankGroup(deleteDataDto);
        return ApiResponse.SUCCESS_API_RESPONSE;
    }

    /**
     * 查詢群組權限
     */
    @Secured("ROLE_USER_GROUP_QUERY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PERMISSION_GET)
    @GetMapping(value = "/group/{groupId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<PermissionDto> getGroupPermissions(@PathVariable("groupId") Long groupId) {
        PermissionDto permissionDto = permissionFacade.getGroupPermissions(groupId);
        return new ApiResponse<>(permissionDto);
    }

    /**
     * 修改群組權限
     */
    @PreAuthorize("hasRole('ROLE_USER_GROUP_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#request.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PERMISSION_ROLE_PERMISSION_UPDATE)
    @PostMapping(value = "/group/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<DataEditResultDTO> updateGroupPermission(
      @Valid @RequestBody UpdateGroupPermissionReqDto request) {
        request.checkUpdatingPermissionRule();
        UserGroupDto userGroupDto = UserGroupDto.valueOf(request);
        userGroupDto.setUpdater(super.getUserAccount());
        DataEditResultDTO dataEditResultDto = permissionFacade.updateGroupPermission(userGroupDto);
        return new ApiResponse<DataEditResultDTO>(dataEditResultDto);
    }

    /**
     * 查詢成員
     */
    @Secured("ROLE_USER_GROUP_QUERY")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PERMISSION_ROLE_LIST)
    @PostMapping(value = "/group/member/{groupId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse getGroupMembers(@PathVariable("groupId") Long groupId,
      @RequestBody PageQueryDTO pageQueryDto) {
        PagingResultDTO<GroupMemberDto> groupMemberPages = permissionFacade.getGroupMembers(groupId,
          pageQueryDto);
        return new ApiResponse<>(groupMemberPages);
    }

    /**
     * 新增成員
     */
    @PreAuthorize("hasRole('ROLE_USER_GROUP_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#request.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PERMISSION_ROLE_UPDATE)
    @PostMapping(value = "/group/member/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<DataEditResultDTO> createGroupMember(
      @Valid @RequestBody CreateOrDeleteGroupMemberReqDto request) {
        request.setCreatorOrDeleter(super.getUserAccount());
        DataEditResultDTO dataEditResultDto = permissionFacade.createGroupMember(request);
        return new ApiResponse<DataEditResultDTO>(dataEditResultDto);
    }

    /**
     * 刪除成員
     */
    @PreAuthorize("hasRole('ROLE_USER_GROUP_MODIFY') && @permissionService.checkAccessMultipleBank(authentication.principal,#request.issuerBankId)")
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PERMISSION_ROLE_DELETE)
    @PostMapping(value = "/group/member/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<?> deleteGroupMember(
      @Valid @RequestBody CreateOrDeleteGroupMemberReqDto request) {
        request.setCreatorOrDeleter(super.getUserAccount());
        permissionFacade.deleteGroupMember(request);
        return ApiResponse.SUCCESS_API_RESPONSE;
    }

    /** 功能模組啟用開關 */
    @AuditLogHandler(methodName = AuditLogMethodNameEnum.PERMISSION_MODULE_SETTING_LIST)
    @GetMapping(value = "/module/setting/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResponse<Set<ModuleSettingPermissionResponse>> getModuleSettingPermission() {
        Set<ModuleSettingPermissionResponse> responseData =
          permissionFacade.getModuleSettingPermission();
        return new ApiResponse<>(responseData);
    }
}
