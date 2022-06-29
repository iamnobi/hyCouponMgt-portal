package com.cherri.acs_portal.service;

import com.cherri.acs_kernel.plugin.AuthenticationInterface;
import com.cherri.acs_kernel.plugin.dto.authentication.invoke.AuthenticateInvokeDTO;
import com.cherri.acs_kernel.plugin.dto.authentication.result.AuthenticateResultDTO;
import com.cherri.acs_portal.config.security.CustomizedUserPrincipal;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.SessionAttributes;
import com.cherri.acs_portal.manager.LdapManager;
import com.cherri.acs_portal.util.HmacUtils;
import com.cherri.acs_portal.util.RecaptchaUtils;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditLogAction;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.AuditLogDAO;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.dao.UserAccountDAO;
import ocean.acs.models.dao.UserGroupDAO;
import ocean.acs.models.data_object.entity.AuditLogDO;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.entity.UserGroupDO;
import ocean.acs.models.data_object.portal.UserAccountDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Acs Portal User Detail Service impl
 *
 * @author Alan Chen
 */
@Log4j2
@Service
public class AcsPortalUserDetailsServiceImpl implements UserDetailsService {

    private static final String LOGIN_EVERYONE_ROLE = "LOGIN_EVERYONE";

    @Value("${account.operation.root.enabled:false}")
    private boolean rootAccountEnabled;
    private final HttpServletRequest request;
    private final UserAccountDAO userAccountDAO;
    private final UserGroupDAO userGroupDAO;
    private final IssuerBankDAO issuerBankDAO;
    private final AuditLogDAO auditLogDAO;
    private final MimaPolicyService mimaPolicyService;
    private final AuthenticationInterface authenticationInterface;

    @Autowired
    private RecaptchaUtils recaptchaUtils;

    @Autowired
    public AcsPortalUserDetailsServiceImpl(
      HttpServletRequest request, UserAccountDAO userAccountDAO,
      UserGroupDAO userGroupDAO, IssuerBankDAO issuerBankDAO,
      AuditLogDAO auditLogDAO, MimaPolicyService mimaPolicyService,
      AuthenticationInterface authenticationInterface) {
        this.request = request;
        this.userAccountDAO = userAccountDAO;
        this.userGroupDAO = userGroupDAO;
        this.issuerBankDAO = issuerBankDAO;
        this.auditLogDAO = auditLogDAO;
        this.mimaPolicyService = mimaPolicyService;
        this.authenticationInterface = authenticationInterface;
    }

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        String bankCode = request.getParameter("bankCode");
        String password = request.getParameter("password");
        String recaptchaToken = request.getParameter("recaptchaToken");
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password) || StringUtils.isEmpty(recaptchaToken)) {
            throw new BadCredentialsException("LOGIN_FAILED");
        }

        boolean isVerified = recaptchaUtils.isTokenValid(recaptchaToken);

        if (!isVerified) {
            throw new BadCredentialsException("LOGIN_FAILED_BAD_RECAPTCHA_TOKEN");
        }

        log.debug("[loadUserByUsername] account={}", account);
        AuditLogDO auditLog = AuditLogDO.initLoginAuditLogInstance(request,
          EnvironmentConstants.ORG_ISSUER_BANK_ID, account);

        /* Get User Account */
        UserAccountDO userAccount = getUserAccount(bankCode, account, auditLog);

        /* Verify Mima */
        isVerified = isVerified && verifyUserPwd(userAccount, account, password);
        log.debug("[loadUserByUsername] Verify [{}] password, isVerified: {}", account, isVerified);

        /* Verify Login Principle */
        /* 帳戶鎖定 */
        if (mimaPolicyService.isAccountLocked(userAccount)) {
            saveErrorAuditLog(auditLog, "" + HttpStatus.UNAUTHORIZED.value(),
                AuditLogAction.N.name());
            throw new BadCredentialsException("LOGIN_FAILED_LOCKED");
        }

        /* 驗證失敗，失敗次數加一 */
        if (!isVerified) {
            userAccount.setTryFailCount(userAccount.getTryFailCount() + 1);
            userAccountDAO.save(userAccount);
            saveErrorAuditLog(auditLog, "" + HttpStatus.UNAUTHORIZED.value(),
                AuditLogAction.N.name());
            throw new BadCredentialsException("LOGIN_FAILED_BAD_CREDENTIAL");
        }

        /* Reset TryFailCount after login successfully. */
        userAccount.setTryFailCount(0);
        userAccount.setForgetMimaCount(0);
        userAccount.setLastLoginMillis(System.currentTimeMillis());
        userAccountDAO.save(userAccount);

        /* Check Group */
        Set<String> userOwnGroups = queryUserGroupAll(userAccount.getId(), auditLog);

        /* Set attributes to request's session. */
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttributes.ISSUER_BANK_ID, userAccount.getIssuerBankId());
        session.setAttribute(SessionAttributes.ACCOUNT, account);
        session.setAttribute(SessionAttributes.PERMISSION, userOwnGroups);
        session.setAttribute(SessionAttributes.SYSTEM_ADMIN, isSystemAccount(userAccount.getId()));
        session.setAttribute(SessionAttributes.IS_MFA_VERIFIED, false);
        session.setAttribute(SessionAttributes.CAN_SEE_PAN, userOwnGroups.contains("CAN_SEE_PAN_QUERY"));

        //FIXME 此功能是否有用處
        /* Set default permission for every login user, "LOGIN_EVERYONE" */
        userOwnGroups.add(LOGIN_EVERYONE_ROLE);

        try {
            auditLogDAO.save(auditLog);
        } catch (DatabaseException e) {
            log.error("[loadUserByUsername] Audit log save error", e);
        }
        return new CustomizedUserPrincipal(
          account, password, userAccount.getIssuerBankId(), userOwnGroups);
    }


    /**
     * 取得使用者帳號
     *
     * @param bankCode Bank code
     * @param account  Account
     * @param auditLog Audit Log
     * @return UserAccountDO User account object
     * @throws UsernameNotFoundException USER_ACCOUNT_NOT_FOUND 使用者未發現
     */
    private UserAccountDO getUserAccount(String bankCode, String account, AuditLogDO auditLog) {
        Optional<UserAccountDO> userAccountOpt;
        Long issuerBankId;

        if (EnvironmentConstants.IS_MULTI_ISSUER) {
            /* [多銀行] 組織管理員、組織一般使用者、會員銀行管理員、會員銀行一般使用者 */
            log.info("[getUserAccount] Multiple-Bank account login...");
            issuerBankId = getIssuerBankId(bankCode);
            userAccountOpt = userAccountDAO
              .findByIssuerBankIdAndAccountAndDeleteFlagFalse(issuerBankId, account);
            auditLog.setIssuerBankId(issuerBankId);
            if (userAccountOpt.isPresent() && isNotSystemAccount(userAccountOpt.get().getId())) {
                return userAccountOpt.get();
            }
            /* [多銀行]系統管理員帳號 */
            if (rootAccountEnabled && EnvironmentConstants.ORG_ISSUER_BANK_ID == issuerBankId) {
                log.info("[getUserAccount] Root account login...");
                issuerBankId = EnvironmentConstants.ORG_ISSUER_BANK_ID;
                userAccountOpt = userAccountDAO
                  .findByIssuerBankIdAndAccountAndDeleteFlagFalse(issuerBankId, account);
                auditLog.setIssuerBankId(issuerBankId);
                if (userAccountOpt.isPresent() && isSystemAccount(userAccountOpt.get().getId())) {
                    return userAccountOpt.get();
                }
            }
        } else {
            log.info("[getUserAccount] Single-Bank account login...");
            issuerBankId = EnvironmentConstants.MONO_ISSUER_BANK_ID;
            userAccountOpt = userAccountDAO
              .findByIssuerBankIdAndAccountAndDeleteFlagFalse(issuerBankId, account);
            auditLog.setIssuerBankId(issuerBankId);
            if (userAccountOpt.isPresent()) {
                /* [單銀行] 銀行管理員、一般使用者 */
                if (isNotSystemAccount(userAccountOpt.get().getId())) {
                    return userAccountOpt.get();
                }
                /* [單銀行] 系統管理員帳號 */
                if (rootAccountEnabled && isSystemAccount(userAccountOpt.get().getId())) {
                    log.info("[getUserAccount] Root account login...");
                    return userAccountOpt.get();
                }
            }
        }

        /* User account not found */
        log.warn("[getUserAccount] Account not found. account: {}", account);
        saveErrorAuditLog(auditLog, "" + HttpStatus.UNAUTHORIZED.value(), AuditLogAction.N.name());
        throw new UsernameNotFoundException("USER_ACCOUNT_NOT_FOUND");
    }

    private boolean isSystemAccount(Long id) {
        return id != null && id < 0;
    }

    private boolean isNotSystemAccount(Long id) {
        return id != null && id > 0;
    }

    /** 是否為會員銀行登入 */
    private Long getIssuerBankId(String bankCode) {
        return StringUtils.isNotBlank(bankCode)
          ? findIssuerBankId(bankCode)
          : EnvironmentConstants.ORG_ISSUER_BANK_ID;
    }

    /**
     * 透過BankCode查詢IssuerBankId
     *
     * @param bankCode 銀行代碼
     * @return issuerBankId 銀行代碼ID
     * @throws UsernameNotFoundException ISSUER_CODE_NOT_FOUND 銀行代碼未發現
     */
    private Long findIssuerBankId(String bankCode) {
        try {
            Optional<IssuerBankDO> issuerBankOptional = issuerBankDAO.findByCode(bankCode);
            if (issuerBankOptional.isPresent()) {
                return issuerBankOptional.get().getId();
            }
        } catch (DatabaseException e) {
            log.error("", e);
            log.warn("[findIssuerBankId] Bank code not found. bankCode={}", bankCode);
        }
        throw new UsernameNotFoundException("ISSUER_CODE_NOT_FOUND");
    }

    /**
     * 驗證使用者密碼是否正確
     *
     * <p>
     * LDAP authentication
     * 1. Authentication plugin use {@link AuthenticationInterface#authenticate(AuthenticateInvokeDTO)}
     * 2. Mock LDAP test use {@link LdapManager#ldapAuth(String, String)}
     * </p>
     * <p>
     * System account system
     * 1. Encrypted input password equals database encrypted password
     * </p>
     */
    private boolean verifyUserPwd(UserAccountDO userAccount, String account, String password) {
        String encryptedInputPwd = HmacUtils.encrypt(password, EnvironmentConstants.hmacHashKey);

        /* Is Root Account */
        if (isSystemAccount(userAccount.getId())) {
            return encryptedInputPwd.equalsIgnoreCase(userAccount.getEncryptedPassword());
        }

        /* LDAP Mode */
        if (EnvironmentConstants.IS_EXTERNAL_AUTHENTICATION_SERVICE) {
            log.info("[verifyUserPwd] Account verify by external authentication service.");
            AuthenticateResultDTO authenticateResult = authenticationInterface.authenticate(
              new AuthenticateInvokeDTO(account, password, null, null));

            /* Exception happened */
            if (authenticateResult.isExceptionHappened()) {
                log.error("[verifyUserPwd] Authenticate plugin exception message: {}",
                  authenticateResult.getException().getMessage());
            }
            return authenticateResult.isAuthenticated();
        }

        /* Normal Mode */
        log.info("[verifyUserPwd] Account verify by built in account management.");
        return encryptedInputPwd.equalsIgnoreCase(userAccount.getEncryptedPassword());
    }

    /**
     * 查詢使用者權限
     *
     * @param userAccountId User account id
     * @param auditLog      Audit log
     * @return Permission List
     * @throws BadCredentialsException USER_GROUP_NOT_FOUND 群組清單為空
     * @throws BadCredentialsException LOGIN_FAILED_BAD_CREDENTIAL 未知錯誤
     */
    private Set<String> queryUserGroupAll(Long userAccountId, AuditLogDO auditLog) {
        List<UserGroupDO> userGroupList = userGroupDAO.findUserGroup(userAccountId);

        /* 群組清單為空 */
        if (Collections.isEmpty(userGroupList)) {
            log.error("[queryUserGroupAll] User's group not configured in the system");
            saveErrorAuditLog(auditLog, "" + HttpStatus.UNAUTHORIZED.value(),
              AuditLogAction.N.name());
            throw new BadCredentialsException("USER_GROUP_NOT_FOUND");
        }

        try {
            /* 群組清單只有一個 */
            if (userGroupList.size() == 1) {
                return userGroupList.get(0).getPermissionSymbol();
            }

            /* 擁有多個群組時整合權限 */
            Set<String> userOwnGroups = new HashSet<>();
            for (UserGroupDO up : userGroupList) {
                userOwnGroups.addAll(up.getPermissionSymbol());
            }
            return userOwnGroups;

        } catch (IllegalAccessException e) {
            saveErrorAuditLog(auditLog, "" + HttpStatus.UNAUTHORIZED.value(),
              AuditLogAction.N.name());
            log.error("[loadUserByUsername] reflection error", e);
            throw new BadCredentialsException("LOGIN_FAILED_BAD_CREDENTIAL");
        }
    }

    private void saveErrorAuditLog(AuditLogDO auditLog, String errorCode, String action) {
        auditLog.setErrorResult(errorCode, action);
        try {
            auditLogDAO.save(auditLog);
        } catch (DatabaseException e) {
            log.error("[saveErrorAuditLog]", e);
        }
    }
}
