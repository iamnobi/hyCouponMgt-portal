package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.controller.request.CreateOrDeleteGroupMemberReqDto;
import com.cherri.acs_portal.controller.request.DeleteBankGroupReqDto;
import com.cherri.acs_portal.controller.response.BankGroupDto;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.controller.response.GroupMemberDto;
import com.cherri.acs_portal.controller.response.ModuleSettingPermissionResponse;
import com.cherri.acs_portal.controller.response.PermissionDto;
import com.cherri.acs_portal.dto.PageQueryDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.usermanagement.AccountGroupDto;
import com.cherri.acs_portal.dto.usermanagement.DeleteGroupMemberDto;
import com.cherri.acs_portal.dto.usermanagement.UserGroupDto;
import com.cherri.acs_portal.service.AuditService;
import com.cherri.acs_portal.service.PermissionService;
import java.util.List;
import java.util.Set;
import ocean.acs.commons.enumerator.AuditActionType;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionFacade {

    private final AuditService auditService;
    private final PermissionService permissionService;

    @Autowired
    public PermissionFacade(PermissionService permissionService,
      AuditService auditService) {
        this.permissionService = permissionService;
        this.auditService = auditService;
    }

    public PermissionDto getUserPermissions(Long issuerBankId, String account) {
        return permissionService.getUserPermissions(issuerBankId, account);
    }

    public List<BankGroupDto> getBankGroups(Long issuerBankId) {
        return permissionService.getBankGroups(issuerBankId);
    }

    public DataEditResultDTO createBankGroup(UserGroupDto userGroupDto) {
        try {
            if (permissionService
              .isUserGroupNameExisted(userGroupDto.getIssuerBankId(), userGroupDto.getName())) {
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT,
                  "user group name is existed");
            }

            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.USER_GROUP);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.USER_GROUP, AuditActionType.ADD, userGroupDto);
            } else {
                userGroupDto.setAuditStatus(AuditStatus.PUBLISHED);
                UserGroupDto result = permissionService.createBankGroup(userGroupDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public DataEditResultDTO updateBankGroup(UserGroupDto userGroupDto) {
        try {
            if (permissionService
              .isUserGroupNameExisted(userGroupDto.getIssuerBankId(), userGroupDto.getName())) {
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT,
                  "user group name is existed");
            }

            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.USER_GROUP);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.USER_GROUP, AuditActionType.UPDATE, userGroupDto);
            } else {
                userGroupDto.setAuditStatus(AuditStatus.PUBLISHED);
                UserGroupDto result = permissionService.updateBankGroup(userGroupDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public List<String> deleteBankGroupCheck(DeleteBankGroupReqDto request) {
        return permissionService.deleteBankGroupCheck(request);
    }

    public void deleteBankGroup(DeleteDataDTO deleteDataDto) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.USER_GROUP);
            if (isDemandAuditing) {
                auditService
                  .requestAudit(AuditFunctionType.USER_GROUP, AuditActionType.DELETE,
                    deleteDataDto);
            } else {
                deleteDataDto.setAuditStatus(AuditStatus.PUBLISHED);
                permissionService.deleteBankGroup(deleteDataDto);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public PermissionDto getGroupPermissions(Long groupId) {
        return permissionService.getGroupPermissions(groupId);
    }

    public DataEditResultDTO updateGroupPermission(UserGroupDto userGroupDto) {
        try {
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.USER_GROUP);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.PERMISSION, AuditActionType.UPDATE, userGroupDto);
            } else {
                userGroupDto.setAuditStatus(AuditStatus.PUBLISHED);
                UserGroupDto result = permissionService.updateGroupPermission(userGroupDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public PagingResultDTO<GroupMemberDto> getGroupMembers(Long groupId,
      PageQueryDTO pageQueryDTO) {
        return permissionService.getGroupMembers(groupId, pageQueryDTO);
    }

    public DataEditResultDTO createGroupMember(
      CreateOrDeleteGroupMemberReqDto createGroupMemberReqDto) {
        try {
            AccountGroupDto accountGroupDto = permissionService
              .checkAccountGroupBeforeCreate(createGroupMemberReqDto);
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.USER_GROUP);
            if (isDemandAuditing) {
                return auditService
                  .requestAudit(AuditFunctionType.ACCOUNT_GROUP, AuditActionType.ADD,
                    accountGroupDto);
            } else {
                accountGroupDto.setAuditStatus(AuditStatus.PUBLISHED);
                AccountGroupDto result = permissionService.createGroupMember(accountGroupDto);
                return new DataEditResultDTO(result);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public void deleteGroupMember(CreateOrDeleteGroupMemberReqDto deleteGroupMemberReqDto) {
        try {
            DeleteGroupMemberDto deleteDto = permissionService
              .checkAccountGroupBeforeDelete(deleteGroupMemberReqDto);
            boolean isDemandAuditing = auditService
              .isAuditingOnDemand(AuditFunctionType.USER_GROUP);
            if (isDemandAuditing) {
                auditService
                  .requestAudit(AuditFunctionType.ACCOUNT_GROUP, AuditActionType.DELETE, deleteDto);
            } else {
                deleteDto.setAuditStatus(AuditStatus.PUBLISHED);
                permissionService.deleteGroupMember(deleteDto);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public Set<ModuleSettingPermissionResponse> getModuleSettingPermission() {
        return permissionService.getModuleSettingPermission();
    }

}
