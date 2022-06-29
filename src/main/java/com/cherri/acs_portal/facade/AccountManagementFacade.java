package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.SessionAttributes;
import com.cherri.acs_portal.controller.request.BankIdPageQueryDTO;
import com.cherri.acs_portal.controller.request.ChangeMimaRequest;
import com.cherri.acs_portal.controller.request.LoginForgetMimaRequest;
import com.cherri.acs_portal.controller.request.LoginResetMimaRequest;
import com.cherri.acs_portal.controller.request.UserPageQueryDTO;
import com.cherri.acs_portal.controller.response.BankGroupDto;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.controller.response.UserAccountGroupResponse;
import com.cherri.acs_portal.controller.response.UserAccountResponse;
import com.cherri.acs_portal.controller.validator.impl.ChangeMimaValidator;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.usermanagement.AuditLogDto;
import com.cherri.acs_portal.dto.usermanagement.AuditLogListRequest;
import com.cherri.acs_portal.dto.usermanagement.UserAccountDTO;
import com.cherri.acs_portal.service.AuditLogService;
import com.cherri.acs_portal.service.AuditService;
import com.cherri.acs_portal.service.MimaPolicyService;
import com.cherri.acs_portal.service.PermissionService;
import com.cherri.acs_portal.service.UserManagementService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditActionType;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.ForceLogoutException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.data_object.portal.UserAccountDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AccountManagementFacade {

    private final HttpSession httpSession;
    private final AuditService auditService;
    private final UserManagementService userManagementService;
    private final AuditLogService auditLogService;
    private final MimaPolicyService mimaPolicyService;
    private final PermissionService permissionService;
    private final ChangeMimaValidator changeMimaValidator;

    @Autowired
    public AccountManagementFacade(HttpSession httpSession,
        AuditService auditService,
        UserManagementService userManagementService,
        AuditLogService auditLogService,
        MimaPolicyService mimaPolicyService,
        PermissionService permissionService,
        ChangeMimaValidator changeMimaValidator) {
        this.httpSession = httpSession;
        this.auditService = auditService;
        this.userManagementService = userManagementService;
        this.auditLogService = auditLogService;
        this.mimaPolicyService = mimaPolicyService;
        this.permissionService = permissionService;
        this.changeMimaValidator = changeMimaValidator;
    }

    public PagingResultDTO<UserAccountGroupResponse> findUserList(UserPageQueryDTO query)
      throws OceanException {
        try {
            PagingResultDTO<UserAccountDTO> queryResult = userManagementService.findUserPage(query);
            List<UserAccountGroupResponse> data = queryResult.getData().stream()
              .map(UserAccountGroupResponse::valueOf)
              .collect(Collectors.toList());
            return new PagingResultDTO<>(queryResult, data);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public UserAccountResponse findUser(Long userId) {
        return userManagementService.findUserById(userId).map(UserAccountResponse::valueOf)
          .orElseThrow(() -> new IllegalArgumentException("invalid argument : userId"));
    }

    public UserAccountResponse findUser(Long issuerBankId, String account) {
        return userManagementService
            .findUserByIssuerBankIdAndAccount(issuerBankId, account)
            .map(UserAccountResponse::valueOf)
            .orElseThrow(() -> new OceanException(ResultStatus.NO_SUCH_DATA));
    }

    @Transactional
    public DataEditResultDTO createUser(UserAccountDTO createDto, List<Long> groupIdList)
      throws OceanException {
        userManagementService.verifyCreateUser(createDto);
        createDto.setAuditStatus(AuditStatus.PUBLISHED);
        UserAccountDTO result = userManagementService.createUser(createDto);
        permissionService.updateUserGroupList(result.getId(), groupIdList, createDto.getCreator());
        return new DataEditResultDTO(result);
    }

    @Transactional
    public DataEditResultDTO updateUser(UserAccountDTO updateDto, List<Long> groupIdList)
      throws OceanException {
        updateDto.setAuditStatus(AuditStatus.PUBLISHED);
        UserAccountDTO result = userManagementService.updateUser(updateDto);
        if (groupIdList != null) {
            permissionService.updateUserGroupList(result.getId(), groupIdList, updateDto.getUpdater());
        }
        return new DataEditResultDTO(result);
    }

    public void deleteUser(DeleteDataDTO deleteDto) throws OceanException {
        try {
            if (auditService.isAuditingOnDemand(AuditFunctionType.USER_GROUP)) {
                auditService
                  .requestAudit(AuditFunctionType.USER_ACCOUNT, AuditActionType.DELETE, deleteDto);
            } else {
                deleteDto.setAuditStatus(AuditStatus.PUBLISHED);
                userManagementService.deleteUser(deleteDto);
            }
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public PagingResultDTO<UserAccountDTO> findLockUserList(BankIdPageQueryDTO query) {
        return userManagementService.findLockUserList(query);
    }

    public void unlockUser(Long id) {
        userManagementService.unlockUser(id);
    }

    public PagingResultDTO<AuditLogDto> listAuditLogByPage(AuditLogListRequest request) {
        try {
            return auditLogService.listAuditLogByPage(request);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public void downloadAuditLog(AuditLogListRequest request) throws IOException {
        try {
            auditLogService.downloadAuditLog(request);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public void downloadAuditLogByIdList(List<Long> idList) throws IOException {
        try {
            auditLogService.downloadAuditLogByIdList(idList);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public ApiResponse<Boolean> changeMimaByLink(LoginResetMimaRequest request) throws DatabaseException  {
        // 1. find by token
        UserAccountDO userAccountDO = userManagementService
            .findUserByMimaResetToken(request.getToken()).orElse(null);
        if (userAccountDO == null) {
            return new ApiResponse<>(ResultStatus.EXPIRED_PASSWORD_RESET_LINK);
        }

        // 2. check mima policy
        boolean isPasswordValid = mimaPolicyService.verifyMimaByIssuerBankId(
            userAccountDO.getIssuerBankId(), userAccountDO.getAccount(), request.getMima());
        if (!isPasswordValid) {
            return new ApiResponse<>(ResultStatus.DIFFERENT_WITH_MIMA_POLICY);
        }

        // 3. update
        userManagementService.updateMima(userAccountDO.getIssuerBankId(), userAccountDO.getAccount(), request.getMima());

        // 4. delete token
        userManagementService.deleteMimaResetToken(request.getToken(), userAccountDO.getAccount());

        return new ApiResponse<>(true);

    }

    /**
     * 變更密碼
     *
     * @param request ChangeMimaRequest
     * @return 是否成功
     */
    public ApiResponse<Boolean> changeMima(ChangeMimaRequest request, HttpServletResponse httpServletResponse) {
        try {
            if (!changeMimaValidator.isValid(request)) {
                return new ApiResponse<>(ResultStatus.ILLEGAL_ARGUMENT);
            }
            boolean oldMimaIsValid = userManagementService.oldMimaIsValid(
                request.getIssuerBankId(), request.getAccount(), request.getOldMima());
            if (!oldMimaIsValid) {
                // ============================================================
                // 同個 session 中，若變更密碼時舊密碼輸入錯誤超過次數，會強制登出
                // 以避免有心人士透過此功能 try 別人密碼
                // ============================================================
                Integer changeMimaFailedTimes = (Integer) httpSession.getAttribute(SessionAttributes.CHANGE_MIMA_FAILED_TIMES);
                if (changeMimaFailedTimes == null) {
                    changeMimaFailedTimes = 0;
                }
                changeMimaFailedTimes += 1;
                httpSession.setAttribute(SessionAttributes.CHANGE_MIMA_FAILED_TIMES, changeMimaFailedTimes);

                if (changeMimaFailedTimes == EnvironmentConstants.CHANGE_MIMA_MAX_FAILED_TIMES) {
                    log.warn("[changeMima] Logout user duo to failed to provide old password {} times.",
                        EnvironmentConstants.CHANGE_MIMA_MAX_FAILED_TIMES);
                    // logout user
                    throw new ForceLogoutException(ResultStatus.ILLEGAL_PASSWORD);
                }

                return new ApiResponse<>(ResultStatus.ILLEGAL_PASSWORD);
            }

            // check mima policy
            boolean isPasswordValid = mimaPolicyService.verifyMimaByIssuerBankId(
                request.getIssuerBankId(), request.getAccount(), request.getNewMima());
            if (!isPasswordValid) {
                return new ApiResponse<>(ResultStatus.DIFFERENT_WITH_MIMA_POLICY);
            }

            // update
            userManagementService.updateMima(request.getIssuerBankId(), request.getAccount(), request.getNewMima());

            return new ApiResponse<>(true);

        } catch (OceanException oe) {
            return new ApiResponse<>(oe.getResultStatus());
        } catch (ForceLogoutException e) {
            throw e;
        } catch (Exception e) {
            log.error("[changeMima] error", e);
            return new ApiResponse<>(ResultStatus.SERVER_ERROR);
        }
    }

    public void forgetMima(LoginForgetMimaRequest loginForgetMimaRequest) {
        userManagementService.forgetMima(loginForgetMimaRequest);
    }

    public List<BankGroupDto> getBankGroups(Long issuerBankId) {
        return permissionService.getBankGroups(issuerBankId);
    }
}
