package com.cherri.acs_portal.service;

import com.cherri.acs_portal.AcsPortalApplication;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.constant.SessionAttributes;
import com.cherri.acs_portal.constant.SystemConstants;
import com.cherri.acs_portal.controller.request.BankIdPageQueryDTO;
import com.cherri.acs_portal.controller.request.LoginForgetMimaRequest;
import com.cherri.acs_portal.controller.request.UserPageQueryDTO;
import com.cherri.acs_portal.dto.ChangeMimaRequestDto;
import com.cherri.acs_portal.dto.MailDto;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.bank.IssuerBankAdminDto;
import com.cherri.acs_portal.dto.bank.IssuerBankAdminListDto;
import com.cherri.acs_portal.dto.mima.MimaPolicyDto;
import com.cherri.acs_portal.dto.usermanagement.AccountLockReason;
import com.cherri.acs_portal.dto.usermanagement.UserAccountDTO;
import com.cherri.acs_portal.util.HmacUtils;
import com.google.common.io.BaseEncoding;
import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.AccountGroupDAO;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.dao.MimaRecordDAO;
import ocean.acs.models.dao.UserAccountDAO;
import ocean.acs.models.dao.UserGroupDAO;
import ocean.acs.models.dao.UserMimaResetTokenDAO;
import ocean.acs.models.data_object.entity.AccountGroupDO;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.entity.MimaRecordDO;
import ocean.acs.models.data_object.entity.UserGroupDO;
import ocean.acs.models.data_object.entity.UserMimaResetTokenDO;
import ocean.acs.models.data_object.portal.IssuerBankAdminDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.UserAccountDO;
import ocean.acs.models.data_object.portal.UserPageQueryDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Log4j2
@Service
public class UserManagementService {

    // 1 day
    private final long ACCOUNT_ACTIVE_MAIL_EXPIRE_MILLIS = 1000 * 60 * 60 * 24;
    // 15 minutes
    private final long FORGET_MIMA_MAIL_EXPIRE_MILLIS = 1000 * 60 * 15;

    @Value("${password.letter.from}")
    private String passwordLetterFrom;
    @Value("${password.letter.subject}")
    private String passwordLetterSubject;

    private final UserAccountDAO userAccountDao;
    private final UserGroupDAO userGroupDao;
    private final AccountGroupDAO accountGroupDao;
    private final IssuerBankDAO issuerBankDAO;
    private final UserMimaResetTokenDAO userMimaResetTokenDAO;
    private final MimaRecordDAO mimaRecordDao;
    private final MailService mailService;
    private final MimaPolicyService mimaPolicyService;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public UserManagementService(UserAccountDAO userAccountDao, UserGroupDAO userGroupDao,
        AccountGroupDAO accountGroupDao, IssuerBankDAO issuerBankDAO,
        UserMimaResetTokenDAO userMimaResetTokenDAO,
        MimaRecordDAO mimaRecordDao, MailService mailService,
        MimaPolicyService mimaPolicyService,
        SpringTemplateEngine templateEngine) {
        this.userAccountDao = userAccountDao;
        this.userGroupDao = userGroupDao;
        this.accountGroupDao = accountGroupDao;
        this.issuerBankDAO = issuerBankDAO;
        this.userMimaResetTokenDAO = userMimaResetTokenDAO;
        this.mimaRecordDao = mimaRecordDao;
        this.mailService = mailService;
        this.mimaPolicyService = mimaPolicyService;
        this.templateEngine = templateEngine;
    }

    /** 查詢單一使用者 */
    public Optional<UserAccountDTO> findUserById(long userId) {
        return userAccountDao.findByIdAndDeleteFlagFalse(userId).map(UserAccountDTO::valueOf)
            .map(userAccountDTO -> {
                // find groups
                userAccountDTO.setUserGroupDOList(
                    userGroupDao.findByUserAccountIdAndNotDelete(userAccountDTO.getId()));
                return userAccountDTO;
            });
    }

    /** 查詢單一使用者 */
    public Optional<UserAccountDTO> findUserByIssuerBankIdAndAccount(long issuerBankId, String account) {
        return userAccountDao.findByIssuerBankIdAndAccountAndDeleteFlagFalse(issuerBankId, account)
          .map(UserAccountDTO::valueOf);
    }

    /** 查詢單一使用者（含已刪除） */
    public Optional<UserAccountDTO> findOneUserIncludeDeleted(long id)
      throws OceanException {
        try {
            return userAccountDao.findById(id).map(UserAccountDTO::valueOf);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus());
        }
    }

    /** 查詢使用者列表* */
    public PagingResultDTO<UserAccountDTO> findUserPage(UserPageQueryDTO queryDto)
      throws DatabaseException {

        PagingResultDO<UserAccountDO> queryResult = userAccountDao.findUser(
          UserPageQueryDO.builder()
            .issuerBankId(queryDto.getIssuerBankId())
            .account(queryDto.getAccount())
            .page(queryDto.getPage())
            .pageSize(queryDto.getPageSize())
            .build(), null);
        if (queryResult == null || queryResult.getData().isEmpty()) {
            return PagingResultDTO.empty();
        }

        // 查詢該使用者所屬群組
        List<UserAccountDTO> data = queryResult.getData().stream().map(userAccountEntity -> {
            List<UserGroupDO> userGroupList = userGroupDao
              .findByUserAccountIdAndNotDelete(userAccountEntity.getId());
            String allGroupName = userGroupList.stream().map(UserGroupDO::getName)
              .collect(Collectors.joining(","));
            UserAccountDTO userAcctDto = UserAccountDTO.valueOf(userAccountEntity);
            userAcctDto.setGroup(allGroupName);
            return userAcctDto;
        }).collect(Collectors.toList());

        PagingResultDTO<UserAccountDTO> pageResult = new PagingResultDTO<>(data);
        pageResult.setTotal(queryResult.getTotal());
        pageResult.setTotalPages(queryResult.getTotalPages());
        pageResult.setCurrentPage(queryResult.getCurrentPage());
        return pageResult;
    }

    /** 新增使用者 */
    public UserAccountDTO createUser(UserAccountDTO createDto) throws OceanException {
        verifyCreateUser(createDto);
        try {
            UserAccountDO entity = new UserAccountDO();
            entity.setIssuerBankId(createDto.getIssuerBankId());
            entity.setAccount(createDto.getAccount());
            entity.setUsername(createDto.getName());
            entity.setPhone(createDto.getPhone());
            entity.setEmail(createDto.getEmail());
            entity.setDepartment(createDto.getDepartment());
            entity.setExt(createDto.getExt());
            entity.setAuditStatus(createDto.getAuditStatus().getSymbol());
            entity.setCreator(createDto.getCreator());
            entity.setCreateMillis(System.currentTimeMillis());
            entity.setLastLoginMillis(System.currentTimeMillis());
            entity.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());

            log.info("[createUser] save user");
            entity = userAccountDao.saveOrUpdate(entity);

            if (entity.getEmail() != null && !EnvironmentConstants.IS_EXTERNAL_AUTHENTICATION_SERVICE) {
                // Generate User Mima Reset Token
                log.info("[createUser] generate token");
                UserMimaResetTokenDO userMimaResetTokenDO = this.generateUserMimaResetToken(
                    entity.getId(), createDto.getCreator(), ACCOUNT_ACTIVE_MAIL_EXPIRE_MILLIS);

                log.info("[createUser] send mail");
                // NOTE: INVITATION email 因為不確定收到的人是使用什麼語言，所以以一律傳送英文信解決
                sendMail(userMimaResetTokenDO.getToken(), entity, "INVITATION", Locale.ENGLISH);
            }
            createDto.setId(entity.getId());
            return createDto;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus());
        }
    }

    private void sendMail(String token, UserAccountDO entity, String templateName, Locale locale) {
        MailDto mailDto = new MailDto();
        mailDto.setFrom(passwordLetterFrom);
        mailDto.setTo(entity.getEmail());
        mailDto.setSubject(passwordLetterSubject);
        mailDto.setSentDate(new Date());

        Map<String, Object> paramMap = new HashMap<>(3);
        paramMap.put("name", entity.getUsername());
        paramMap.put("account", entity.getAccount());
        paramMap.put("resetPasswordUrl", String.format("%s/ds-portal/#/settingPassword/%s/%s",
            EnvironmentConstants.ACS_URL, entity.getIssuerBankId(), token));
        paramMap.put("email", entity.getEmail());

        String template = getMailTemplateString(templateName, paramMap, locale);
        mailDto.setText(template);
        mailService.multipleSendHtmlMail(Collections.singletonList(mailDto));
    }

    /**
     * Get template html string
     *
     * @param templateName Template html name
     * @param paramMap     Template html parameter
     * @return template string
     */
    private String getMailTemplateString(String templateName, Map<String, Object> paramMap, Locale locale) {
        Context ctx = new Context(locale, paramMap);
        return templateEngine.process(templateName, ctx);
    }


    public void verifyCreateUser(UserAccountDTO createDto) {
        if (!isExistIssuerBankId(createDto.getIssuerBankId())) {
            throw new OceanException(ResultStatus.NO_SUCH_DATA);
        }
        if (isDuplicateAccount(
          createDto.getIssuerBankId(), createDto.getAccount())) {
            String message = String
              .format("account already exists, account=%s", createDto.getAccount());
            log.warn("[verifyCreateUser] " + StringUtils.normalizeSpace(message));
            throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT, message);
        }
    }

    public boolean isExistIssuerBankId(long issuerBankId) {
        return issuerBankDAO.findById(issuerBankId).isPresent();
    }

    /** 是否已存在相同帳號 */
    public boolean isDuplicateAccount(Long issuerBankId, String account) throws OceanException {
        return userAccountDao
          .existsByIssuerBankIdAndAccountAndDeleteFlagIsFalse(issuerBankId, account);
    }

    public void unlockUser(long id) {
        try {
            UserAccountDO userAccountDO = userAccountDao.findById(id)
                .orElseThrow(() -> new OceanException(ResultStatus.NO_SUCH_DATA));
            userAccountDO.setTryFailCount(0);
            userAccountDO.setForgetMimaCount(0);
            userAccountDO.setSendOtpCount(0);
            userAccountDO.setVerifyOtpCount(0);
            userAccountDO.setLastLoginMillis(System.currentTimeMillis());
            userAccountDao.saveOrUpdate(userAccountDO);

        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus());
        }
    }

    /** 更新使用者 */
    public UserAccountDTO updateUser(UserAccountDTO updateDto) throws OceanException {
        try {
            UserAccountDO entity;
            if (updateDto.getId() != null) {
                // find by id
                entity = userAccountDao.findByIdAndDeleteFlagFalse(updateDto.getId()
                ).orElseThrow(() -> getNoSuchDataException(updateDto.getId()));
            } else {
                // find by issuerBankId and account
                entity = userAccountDao.findByIssuerBankIdAndAccountAndDeleteFlagFalse(
                    updateDto.getIssuerBankId(), updateDto.getAccount()
                ).orElseThrow(() -> new OceanException(ResultStatus.NO_SUCH_DATA));
                updateDto.setId(entity.getId());
            }

            if (null != updateDto.getDepartment()) {
                entity.setDepartment(updateDto.getDepartment());
            }
            if (null != updateDto.getName()) {
                entity.setUsername(updateDto.getName());
            }
            if (null != updateDto.getPhone()) {
                entity.setPhone(updateDto.getPhone());
            }
            if (null != updateDto.getExt()) {
                entity.setExt(updateDto.getExt());
            }
            // NOTE: email cannot be updated
            // if (null != updateDto.getEmail()) {
            //    entity.setEmail(updateDto.getEmail());
            // }
            if (null != updateDto.getAuditStatus()) {
                entity.setAuditStatus(updateDto.getAuditStatus().getSymbol());
            }
            if (null != updateDto.getTryFailCount()) {
                entity.setTryFailCount(updateDto.getTryFailCount());
            }
            entity.setUpdater(updateDto.getUpdater());
            entity.setUpdateMillis(System.currentTimeMillis());
            userAccountDao.saveOrUpdate(entity);
            return updateDto;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus());
        }
    }

    /** 刪除使用者 */
    @Transactional
    public boolean deleteUser(DeleteDataDTO deleteDto) throws OceanException {
        try {
            UserAccountDO entity = userAccountDao.findByIdAndDeleteFlagFalse(deleteDto.getId()
            ).orElseThrow(() -> getNoSuchDataException(deleteDto.getId()));
            entity.setDeleter(deleteDto.getUser());
            entity.setDeleteMillis(System.currentTimeMillis());
            entity.setDeleteFlag(true);
            userAccountDao.saveOrUpdate(entity);
            accountGroupDao.deleteByAccountId(entity.getId());
            return true;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus());
        }
    }

    public NoSuchDataException getNoSuchDataException(Long id) {
        return new NoSuchDataException("id=" + id + " not found.");
    }

    /** 查詢被鎖定的使用者帳號 */
    public PagingResultDTO<UserAccountDTO> findLockUserList(BankIdPageQueryDTO query)
      throws OceanException {
        MimaPolicyDto mimaPolicyDto = mimaPolicyService.query(query.getIssuerBankId());
        UserPageQueryDO pageQuery = new UserPageQueryDO();
        pageQuery.setIssuerBankId(query.getIssuerBankId());
        pageQuery.setPage(query.getPage());
        pageQuery.setPageSize(query.getPageSize());
        pageQuery.setForgetMimaCount(SystemConstants.FORGET_MIMA_COUNT);
        pageQuery.setSendOtpCount(SystemConstants.SEND_OTP_COUNT);
        pageQuery.setVerifyOtpCount(SystemConstants.VERIFY_OTP_COUNT);
        pageQuery.setAccountMaxIdleDay(mimaPolicyDto.getAccountMaxIdleDay());
        try {
            PagingResultDO<UserAccountDO> queryResult = userAccountDao.findUser(
                pageQuery, mimaPolicyDto.getLoginRetryCount());
            if (queryResult.isEmpty()) {
                return PagingResultDTO.empty();
            }
            List<UserAccountDTO> data =
                queryResult.getData().stream()
                    .map(
                        userAccountDO -> {
                            UserAccountDTO dto = UserAccountDTO.valueOf(userAccountDO);
                            AccountLockReason accountLockReason =
                                mimaPolicyService.getAccountLockReason(mimaPolicyDto, userAccountDO);
                            dto.setAccountLockReason(MessageConstants.get(accountLockReason.getI18n()));
                            return dto;
                        })
                    .collect(Collectors.toList());
            PagingResultDTO<UserAccountDTO> pagingResult = new PagingResultDTO<>(data);
            pagingResult.setCurrentPage(queryResult.getCurrentPage());
            pagingResult.setTotal(queryResult.getTotal());
            pagingResult.setTotalPages(queryResult.getTotalPages());
            return pagingResult;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /**
     * 驗證舊密碼是否正確
     *
     * @param issuerBankId 銀行代碼
     * @param account      使用者帳號
     * @param mima         舊密碼
     * @return 是否正確
     */
    public boolean oldMimaIsValid(long issuerBankId, String account, String mima) {
        Optional<UserAccountDO> userAccountOptional =
          userAccountDao.findByIssuerBankIdAndAccountAndDeleteFlagFalse(issuerBankId, account);
        if (userAccountOptional.isPresent()) {
            UserAccountDO userAccount = userAccountOptional.get();
            if (userAccount.getEncryptedPassword() == null)
                userAccount.setEncryptedPassword("");
            return userAccount.getEncryptedPassword().equals(encryptWithHmacHash(mima));
        }
        return false;
    }

    /**
     * 更新密碼
     *
     * @param dto 變更密碼需求物件
     * @return 是否更新成功
     * @throws OceanException {@link ResultStatus} USER_NOT_FOUND
     * @throws OceanException {@link ResultStatus} DB_SAVE_ERROR
     */
    public void updateMima(Long issuerBankId, String account, String mima) {
        try {
            // 1. update UserAccount
            Optional<UserAccountDO> userAccountOptional = userAccountDao
              .findByIssuerBankIdAndAccountAndDeleteFlagFalse(issuerBankId, account);
            UserAccountDO userAccount = userAccountOptional.orElseThrow(()
              -> new OceanException(ResultStatus.NO_SUCH_DATA));

            userAccount.setEncryptedPassword(encryptWithHmacHash(mima));
            userAccountDao.saveOrUpdate(userAccount);

            // 2. update MimaRecord
            mimaRecordDao.saveRecord(MimaRecordDO.builder()
                .account(userAccount.getAccount())
                .issuerBankId(userAccount.getIssuerBankId())
                .encryptedMima(HmacUtils.encrypt(mima, EnvironmentConstants.hmacHashKey))
                .createTime(System.currentTimeMillis())
                .build());
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus());
        }
    }


    /** 查詢會員銀行管理員 by id */
    public Optional<IssuerBankAdminDto> getIssuerBankAdminById(Long id) {
        Optional<UserAccountDO> result = userAccountDao.findByIdAndDeleteFlagFalse(id);
        return result.map(IssuerBankAdminDto::valueOf);
    }

    /**
     * 查詢會員銀行管理員列表 by issuerBankId<br> 擁有系統預設銀行管理權限使用者帳號
     */
    public List<IssuerBankAdminListDto> getIssuerBankAdminList(Long issuerBankId) {
        List<UserAccountDO> bankAdminList = userAccountDao
          .findBankAdminListByIssuerBankId(issuerBankId);
        return bankAdminList.stream().map(IssuerBankAdminListDto::valueOf)
          .collect(Collectors.toList());
    }

    /** 修改銀行管理員 */
    public IssuerBankAdminDto updateIssuerBankAdmin(IssuerBankAdminDto issuerBankAdminDto) {
        Optional<UserAccountDO> userAccountDOOptional = userAccountDao
          .findByIdAndDeleteFlagFalse(issuerBankAdminDto.getId());
        if (!userAccountDOOptional.isPresent()) {
            throw new OceanException(ResultStatus.NO_SUCH_DATA, "User account not found!!");
        }
        UserAccountDO userAccountDO = userAccountDOOptional.get();
        userAccountDO.setAccount(issuerBankAdminDto.getAccount());
        userAccountDO.setUsername(issuerBankAdminDto.getUserName());
        // NOTE: email cannot be updated
        // userAccountDO.setEmail(issuerBankAdminDto.getEmail());
        userAccountDO.setPhone(issuerBankAdminDto.getPhone());
        userAccountDO.setDepartment(issuerBankAdminDto.getDepartment());
        userAccountDO.setExt(issuerBankAdminDto.getExt());
        userAccountDO.setIssuerBankId(issuerBankAdminDto.getBankId());

        userAccountDO.setAuditStatus(issuerBankAdminDto.getAuditStatus().getSymbol());
        userAccountDO.setUpdater(issuerBankAdminDto.getUser());
        userAccountDO.setUpdateMillis(System.currentTimeMillis());
        try {
            userAccountDO = userAccountDao.saveOrUpdate(userAccountDO);
            issuerBankAdminDto.setId(userAccountDO.getId());
            return issuerBankAdminDto;
        } catch (DatabaseException e) {
            log.error("[updateIssuerBankAdmin] Update error.", e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR);
        }
    }

    /** 更新銀行管理員的審核狀態 */
    public IssuerBankAdminDto updateIssuerBankAdminAuditStatus(
      IssuerBankAdminDto issuerBankAdminDto) {
        IssuerBankAdminDO issuerBankAdminDO = IssuerBankAdminDO.builder()
          .userName(issuerBankAdminDto.getUserName())
          .user(issuerBankAdminDto.getUser())
          .bankId(issuerBankAdminDto.getBankId())
          .auditStatus(issuerBankAdminDto.getAuditStatus())
          .account(issuerBankAdminDto.getAccount())
          .department(issuerBankAdminDto.getDepartment())
          .email(issuerBankAdminDto.getEmail())
          .ext(issuerBankAdminDto.getExt())
          .fileContent(issuerBankAdminDto.getFileContent())
          .fileName(issuerBankAdminDto.getFileName())
          .id(issuerBankAdminDto.getId())
          .issuerBankId(issuerBankAdminDto.getIssuerBankId())
          .phone(issuerBankAdminDto.getPhone())
          .version(issuerBankAdminDto.getVersion())
          .build();
        userAccountDao.update(issuerBankAdminDO);
        userAccountDao.updateAuditStatusById(issuerBankAdminDO);
        return issuerBankAdminDto;
    }

    /** 刪除銀行管理員 */
    public DeleteDataDTO deleteIssuerBankAdmin(DeleteDataDTO deleteDataDTO) {
        Optional<UserAccountDO> userAccountDOOptional = userAccountDao
          .findByIdAndDeleteFlagFalse(deleteDataDTO.getId());
        if (!userAccountDOOptional.isPresent()) {
            throw new OceanException(ResultStatus.NO_SUCH_DATA, "User account not found!");
        }
        UserAccountDO userAccountDO = userAccountDOOptional.get();
        userAccountDO.setAuditStatus(deleteDataDTO.getAuditStatus().getSymbol());
        userAccountDO.setDeleteFlag(true);
        userAccountDO.setDeleter(deleteDataDTO.getUser());
        userAccountDO.setDeleteMillis(System.currentTimeMillis());
        userAccountDO = userAccountDao.delete(userAccountDO);
        deleteDataDTO.setAuditStatus(AuditStatus.getStatusBySymbol(userAccountDO.getAuditStatus()));
        return deleteDataDTO;
    }

    /**
     * 新增銀行管理員<br> 新增銀行管理員帳號同時綁定系統預設的銀行帳號群組（透過設定ACCOUNT_GROUP）
     */
    public IssuerBankAdminDto createIssuerBankAdmin(IssuerBankAdminDto issuerBankAdminDto) {
        UserAccountDO userAccountDO = new UserAccountDO();
        userAccountDO.setAccount(issuerBankAdminDto.getAccount());
        userAccountDO.setUsername(issuerBankAdminDto.getUserName());
        userAccountDO.setEmail(issuerBankAdminDto.getEmail());
        userAccountDO.setPhone(issuerBankAdminDto.getPhone());
        userAccountDO.setDepartment(issuerBankAdminDto.getDepartment());
        userAccountDO.setExt(issuerBankAdminDto.getExt());
        userAccountDO.setAuditStatus(issuerBankAdminDto.getAuditStatus().getSymbol());
        userAccountDO.setIssuerBankId(issuerBankAdminDto.getBankId());
        userAccountDO.setCreator(issuerBankAdminDto.getUser());
        userAccountDO.setCreateMillis(System.currentTimeMillis());
        userAccountDO.setLastLoginMillis(System.currentTimeMillis());
        /* add user */
        log.info("[createIssuerBankAdmin] save user");
        userAccountDO = userAccountDao.save(userAccountDO);
        addSystemAdminDefaultGroup(userAccountDO);

        /* Send Mail */
        if (StringUtils.isNotBlank(userAccountDO.getEmail())
          && !EnvironmentConstants.IS_EXTERNAL_AUTHENTICATION_SERVICE) {
            // Generate User Mima Reset Token
            log.info("[createIssuerBankAdmin] generate token");
            UserMimaResetTokenDO userMimaResetTokenDO = this.generateUserMimaResetToken(
                userAccountDO.getId(), userAccountDO.getCreator(), ACCOUNT_ACTIVE_MAIL_EXPIRE_MILLIS);

            log.info("[createIssuerBankAdmin] send mail");
            // NOTE: INVITATION email 因為不確定收到的人是使用什麼語言，所以以一律傳送英文信解決
            sendMail(userMimaResetTokenDO.getToken(), userAccountDO, "INVITATION", Locale.ENGLISH);
        }
        issuerBankAdminDto.setId(userAccountDO.getId());
        return issuerBankAdminDto;
    }

    /** 新增AccountGroup */
    public UserAccountDO addSystemAdminDefaultGroup(UserAccountDO userAccount) {
        /* Find Bank */
        userGroupDao.findSysDefaultBankAdminByIssuerBankId(userAccount.getIssuerBankId())
          /* Add account group */
          .map(userGroup -> accountGroupDao.add(AccountGroupDO.builder()
            .accountId(userAccount.getId())
            .groupId(userGroup.getId())
            .auditStatus(AuditStatus.PUBLISHED)
            .creator(userAccount.getCreator())
            .createMillis(userAccount.getCreateMillis())
            .build())
          ).orElseGet(() -> {
            log.error("[addAccountGroup] error, UserGroup not found, issuerBankId={}",
              userAccount.getIssuerBankId());
            throw new OceanException(ResultStatus.NO_SUCH_DATA, "UserGroup not found");
        });
        return userAccount;
    }

    public boolean isAccountExistedInOtherPeople(Long userAccountId, String account)
      throws OceanException {
        return userAccountDao.existsByAccountNotSelfAndAvailable(userAccountId, account);
    }

    /**
     * Hmac Hash 加密
     *
     * @param str 文字
     * @return 加密後文字
     */
    private String encryptWithHmacHash(String str) {
        if (str == null || str.isEmpty())
            return "";
        return HmacUtils.encrypt(str, EnvironmentConstants.hmacHashKey);
    }

    private UserMimaResetTokenDO generateUserMimaResetToken(Long userAccountId, String creator,
        Long tokenExpireMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        // 1. delete all user's tokens
        userMimaResetTokenDAO
            .deleteAliveUserMimaResetToken(userAccountId, creator,
                currentTimeMillis);
        // 2. gen token
        String token = BaseEncoding.base64Url().encode(HmacUtils.getSalt(32));
        // 3. save token
        UserMimaResetTokenDO userMimaResetToken = new UserMimaResetTokenDO(
            token,
            userAccountId,
            currentTimeMillis + tokenExpireMillis,
            creator,
            currentTimeMillis,
            false,
            null,
            null
        );
        return userMimaResetTokenDAO.save(userMimaResetToken);
    }

    public Optional<UserAccountDO> findUserByMimaResetToken(String token)
        throws DatabaseException {
        UserMimaResetTokenDO userMimaResetTokenDO = userMimaResetTokenDAO.findByToken(token).orElse(null);
        if (userMimaResetTokenDO == null) {
            log.warn("[findUserByMimaResetToken] cannot find UserMimaResetToken by token={}", StringUtils.normalizeSpace(token));
            return Optional.empty();
        }
        if (userMimaResetTokenDO.isExpired()) {
            log.warn("[findUserByMimaResetToken] expired token={}", StringUtils.normalizeSpace(token));
            return Optional.empty();
        }

        Optional<UserAccountDO> userAccountDOOptional = userAccountDao.findById(userMimaResetTokenDO.getUserAccountId());

        if (!userAccountDOOptional.isPresent()) {
            log.warn("[findUserByMimaResetToken] cannot find UserAcccount by token={}", StringUtils.normalizeSpace(token));
        }

        return userAccountDOOptional;
    }

    public void deleteMimaResetToken(String token, String deleter) {
        UserMimaResetTokenDO userMimaResetTokenDO = userMimaResetTokenDAO.findByToken(token).orElse(null);
        if (userMimaResetTokenDO == null) {
            log.warn("[deleteMimaResetToken] token is already deleted");
            return;
        }
        userMimaResetTokenDO.setDeleteFlag(true);
        userMimaResetTokenDO.setDeleter(deleter);
        userMimaResetTokenDO.setDeleteMillis(System.currentTimeMillis());
        userMimaResetTokenDAO.save(userMimaResetTokenDO);
    }

    public void forgetMima(LoginForgetMimaRequest loginForgetMimaRequest) {
        // find issuer bank id
        Long issuerBankId;
        if (StringUtils.isEmpty(loginForgetMimaRequest.getBankCode())) {
            // empty bank code -> ORG
            issuerBankId = -1L;
        } else {
            // bank
            try {
                IssuerBankDO issuerBankDO = issuerBankDAO
                    .findByCode(loginForgetMimaRequest.getBankCode())
                    .orElse(null);
                if (issuerBankDO == null) {
                    log.warn("[forgetMima] bank code does not exists.");
                    return;
                }
                issuerBankId = issuerBankDO.getId();
            } catch (DatabaseException e) {
                throw new OceanException(e.getResultStatus(), e.getMessage(), e);
            }
        }

        UserAccountDO userAccountDO = userAccountDao
            .findByIssuerBankIdAndAccountAndDeleteFlagFalse(
                issuerBankId, loginForgetMimaRequest.getAccount())
            .orElse(null);
        if (userAccountDO == null) {
            log.warn("[forgetMima] account does not exists.");
            return;
        }

        boolean accountLocked = mimaPolicyService.isAccountLocked(userAccountDO);
        if (accountLocked) {
            throw new OceanException(ResultStatus.ACCOUNT_LOCKED);
        }

        // increase forget mima count
        userAccountDao.increaseForgetMimaCount(
            userAccountDO.getId(), userAccountDO.getAccount(), System.currentTimeMillis());

        // generate mima reset token
        UserMimaResetTokenDO userMimaResetTokenDO = this.generateUserMimaResetToken(
            userAccountDO.getId(),
            loginForgetMimaRequest.getAccount(),
            FORGET_MIMA_MAIL_EXPIRE_MILLIS);

        // send mail
        this.sendMail(
            userMimaResetTokenDO.getToken(),
            userAccountDO,
            "RESET_MIMA",
            LocaleContextHolder.getLocale());
    }

}
