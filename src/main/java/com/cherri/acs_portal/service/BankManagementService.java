package com.cherri.acs_portal.service;

import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.controller.response.OtpSendingSettingResDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.bank.*;
import com.cherri.acs_portal.dto.system.CardBrandDTO;
import com.cherri.acs_portal.manager.BankHandlingFeeManager;
import com.cherri.acs_portal.util.FileUtils;
import com.cherri.acs_portal.util.StringCustomizedUtils;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.*;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.*;
import ocean.acs.models.data_object.entity.*;
import ocean.acs.models.data_object.portal.DeleteDataDO;
import ocean.acs.models.data_object.portal.PortalIssuerBankDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BankManagementService {

    /** 系統預設銀行管理者群組USER_GROUP.ID */
    private static final String SYSTEM_DEFAULT_BANK_ADMIN_USER_GROUP_NAME =
            "SYS_DEFAULT_BANK_ADMIN_USER_GROUP";

    private final BankHandlingFeeManager bankHandlingFeeManager;
    private final IssuerBankDAO issuerBankDao;
    private final IssuerTradingChannelDAO issuerTradingChannelDao;
    private final IssuerBrandLogoDAO issuerBrandLogoDao;
    private final IssuerLogoDAO issuerLogoDao;
    private final IssuerHandingFeeDAO issuerHandingFeeDao;
    private final SecretKeyDAO secretKeyDao;
    private final SmsTemplateDAO smsTemplateDao;
    private final UserGroupDAO userGroupDao;
    private final UserAccountDAO userAccountDao;
    private final ChallengeViewMessageDAO challengeViewMessageDao;
    private final ChallengeViewOtpSettingDAO challengeViewOtpSettingDao;
    private final BlackListAuthStatusDAO blackListAuthStatusDao;
    private final BatchImportDAO batchImportDao;
    private final OtpSendingSettingDAO otpSendingSettingDao;
    private final BinRangeDAO binRangeDao;
    private final OtpOperationLogDAO otpOperationLogDao;
    private final ThreeDSVerifiedDAO threeDSVerifiedDao;
    private final AuditingDAO auditingDao;
    private final WhiteListAttemptAuthorizeDAO whiteListAttemptAuthorizeDao;
    private final PanInfoDAO panInfoDao;
    private final PanOtpStatisticsDAO panOtpStatisticsDao;
    private final BlackListPanDAO blackListPanDao;
    private final WhiteListPanDAO whiteListPanDao;
    private final WhiteListAttemptSettingDAO whiteListAttemptSettingDao;
    private final BlackListIpGroupDAO blackListIpGroupDao;
    private final BlackListMerchantUrlDAO blackListMerchantUrlDao;
    private final LangDAO langDao;
    private final BlackListDeviceInfoDAO blackListDeviceInfoDao;
    private final IssuerRuntimePropertyDAO issuerRuntimePropertyDao;

    private final HttpServletResponse httpServletResponse;
    private final ClassicRbaCheckDAO classicRbaCheckDao;
    private final CardBrandConfigurationHelper cardBrandConfigurationHelper;
    private final HsmPlugin hsmPlugin;

    @Autowired
    public BankManagementService(
            BankHandlingFeeManager bankHandlingFeeManager,
            IssuerBankDAO bankDao,
            IssuerTradingChannelDAO issuerTradingChannelDao,
            IssuerBrandLogoDAO issuerBrandLogoDao,
            IssuerLogoDAO issuerLogoDao,
            IssuerHandingFeeDAO issuerHandingFeeDao,
            SecretKeyDAO secretKeyDao,
            SmsTemplateDAO smsTemplateDao,
            UserGroupDAO userGroupDao,
            UserAccountDAO userAccountDao,
            ChallengeViewMessageDAO challengeViewMessageDao,
            ChallengeViewOtpSettingDAO challengeViewOtpSettingDao,
            BlackListAuthStatusDAO blackListAuthStatusDao,
            BatchImportDAO batchImportDao,
            OtpSendingSettingDAO otpSendingSettingDao,
            LangDAO langDao,
            BlackListDeviceInfoDAO blackListDeviceInfoDao,
            IssuerRuntimePropertyDAO issuerRuntimePropertyDao,
            ClassicRbaCheckDAO classicRbaCheckDao,
            BinRangeDAO binRangeDao,
            OtpOperationLogDAO otpOperationLogDao,
            ThreeDSVerifiedDAO threeDSVerifiedDao,
            AuditingDAO auditingDao,
            WhiteListAttemptAuthorizeDAO whiteListAttemptAuthorizeDao,
            PanInfoDAO panInfoDao,
            PanOtpStatisticsDAO panOtpStatisticsDao,
            BlackListPanDAO blackListPanDao,
            WhiteListPanDAO whiteListPanDao,
            WhiteListAttemptSettingDAO whiteListAttemptSettingDao,
            BlackListIpGroupDAO blackListIpGroupDao,
            BlackListMerchantUrlDAO blackListMerchantUrlDao,
            HttpServletResponse httpServletResponse,
            CardBrandConfigurationHelper cardBrandConfigurationHelper,
            HsmPlugin hsmPlugin) {
        this.bankHandlingFeeManager = bankHandlingFeeManager;
        this.issuerBankDao = bankDao;
        this.issuerTradingChannelDao = issuerTradingChannelDao;
        this.issuerBrandLogoDao = issuerBrandLogoDao;
        this.issuerLogoDao = issuerLogoDao;
        this.issuerHandingFeeDao = issuerHandingFeeDao;
        this.secretKeyDao = secretKeyDao;
        this.smsTemplateDao = smsTemplateDao;
        this.userGroupDao = userGroupDao;
        this.userAccountDao = userAccountDao;
        this.challengeViewMessageDao = challengeViewMessageDao;
        this.challengeViewOtpSettingDao = challengeViewOtpSettingDao;
        this.blackListAuthStatusDao = blackListAuthStatusDao;
        this.batchImportDao = batchImportDao;
        this.otpSendingSettingDao = otpSendingSettingDao;
        this.langDao = langDao;
        this.blackListDeviceInfoDao = blackListDeviceInfoDao;
        this.issuerRuntimePropertyDao = issuerRuntimePropertyDao;
        this.binRangeDao = binRangeDao;
        this.otpOperationLogDao = otpOperationLogDao;
        this.threeDSVerifiedDao = threeDSVerifiedDao;
        this.auditingDao = auditingDao;
        this.whiteListAttemptAuthorizeDao = whiteListAttemptAuthorizeDao;
        this.panInfoDao = panInfoDao;
        this.panOtpStatisticsDao = panOtpStatisticsDao;
        this.blackListPanDao = blackListPanDao;
        this.whiteListPanDao = whiteListPanDao;
        this.whiteListAttemptSettingDao = whiteListAttemptSettingDao;
        this.blackListIpGroupDao = blackListIpGroupDao;
        this.blackListMerchantUrlDao = blackListMerchantUrlDao;
        this.httpServletResponse = httpServletResponse;
        this.classicRbaCheckDao = classicRbaCheckDao;
        this.cardBrandConfigurationHelper = cardBrandConfigurationHelper;
        this.hsmPlugin = hsmPlugin;
    }

    public List<IssuerBankDto> findAll() {
        try {
            return issuerBankDao.findAll().stream()
                    .map(IssuerBankDto::valueOf)
                    .collect(Collectors.toList());
        } catch (DatabaseException e) {
            log.error("[findAll] database error", e);
            throw new OceanException(ResultStatus.DB_READ_ERROR, "Read error");
        }
    }

    public List<IssuerBankDto> getIssuerBankList()
            throws OceanException {
        try {
            List<IssuerBankDO> resultPage = issuerBankDao.findAll();

            List<IssuerBankDto> content =
                    resultPage.stream()
                            .map(IssuerBankDto::valueOf)
                            .collect(Collectors.toList());

            return content;

        } catch (DatabaseException e) {
            log.error("[getIssuerBankList] database error", e);
            throw new OceanException(ResultStatus.DB_READ_ERROR, "Read error");
        }
    }

    public Optional<IssuerBankDto> getIssuerBankById(Long id) {
        Optional<IssuerBankDO> result = issuerBankDao.findById(id);
        return result.map(IssuerBankDto::valueOf);
    }

    /** 新增會員銀行 */
    @Transactional(rollbackFor = Exception.class)
    public IssuerBankDto createIssuerBank(IssuerBankDto issuerBankDto) {
        if (isBankCodeExisted(issuerBankDto.getCode())) {
            throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT, "bank code is existed.");
        }
        // create SymmetricKey
        String keyId =
                createIssuerHsmEncryptKey(issuerBankDto.getCode(), issuerBankDto.getIssuerBankId());
        issuerBankDto.setSymmetricKeyId(keyId);

        // create sensitive data key
        String sensitiveDataKeyId =
            createIssuerHsmSensitiveDataKey(issuerBankDto.getCode(), issuerBankDto.getIssuerBankId());
        issuerBankDto.setSensitiveDataKeyId(sensitiveDataKeyId);

        // create acsOperatorId
        //00003 allow control asc/3ds oper id by user
        //String newAcsOperatorId = createAcsOperatorId();
        String newAcsOperatorId = issuerBankDto.getAcsOperatorId();
        log.info("ACS Operator ID={}", StringUtils.normalizeSpace(newAcsOperatorId));
        ////
        PortalIssuerBankDO issuerBankDO =
                PortalIssuerBankDO.builder()
                        .name(issuerBankDto.getName())
                        .code(issuerBankDto.getCode())
                        .id(issuerBankDto.getId())
                        .issuerBankId(issuerBankDto.getIssuerBankId())
                        .symmetricKeyId(issuerBankDto.getSymmetricKeyId())
                        .sensitiveDataKeyId(issuerBankDto.getSensitiveDataKeyId())
                        .threeDSMethodUrl(issuerBankDto.getThreeDSMethodUrl())
                        .acsUrl(issuerBankDto.getAcsUrl())
                        .acsRefNumber(issuerBankDto.getAcsRefNumber())
                        .acsOperatorId(newAcsOperatorId)
                        .auditStatus(issuerBankDto.getAuditStatus())
                        .user(issuerBankDto.getUser())
                        .fileName(issuerBankDto.getFileName())
                        .fileContent(issuerBankDto.getFileContent())
                        .version(issuerBankDto.getVersion())
                        .build();

        // Create bank
        issuerBankDao
                .add(issuerBankDO)
                .map(
                        issuerBank -> {
                            createDefaultSettings(issuerBank);
                            issuerBankDto.setId(issuerBank.getId());
                            return issuerBank;
                        })
                .orElseThrow(
                        () -> new OceanException(ResultStatus.SERVER_ERROR, "create bank error"));

        return issuerBankDto;
    }

    /** 銀行代碼是否存在 */
    public boolean isBankCodeExisted(String code) {
        try {
            return issuerBankDao.existsByCodeAndDeleteFlagIsFalse(code);
        } catch (DatabaseException e) {
            log.error("[isBankCodeExisted] database error", e);
            throw new OceanException(e.getResultStatus());
        }
    }

    //00003 allow control asc/3ds oper id by user
    public Optional<IssuerBankDto> getAcsOperatorId(String acsOperatorId) throws DatabaseException {
        Optional<IssuerBankDO> result = issuerBankDao.findByAcsOperatorId(acsOperatorId);
        return result.map(IssuerBankDto::valueOf);
    }

    /** 建立預設設定 */
    private IssuerBankDO createDefaultSettings(IssuerBankDO issuerBank) {
        saveSysDefaultBankAdminUserGroup(issuerBank);
        saveDefaultSecretKey(issuerBank);
        saveDefaultSmsTemplate(issuerBank);
        saveDefaultChallengeViewMessage(issuerBank);
        saveDefaultChallengeViewOtpSetting(issuerBank);
        saveDefaultIssuerTradingChannel(issuerBank);
        saveDefaultIssuerHandingFee(issuerBank);
        saveDefaultBlackListAuthStatus(issuerBank);
        saveDefaultBlackListBatchId(issuerBank);
        saveDefaultIssuerLogo(issuerBank);
        saveDefaultIssuerBrandLogo(issuerBank);
        saveDefaultOtpSendingSetting(issuerBank);
        saveDefaultRbaSetting(issuerBank);
        return issuerBank;
    }

    /** 設定會員銀行系統預設銀行授權模組權限 */
    private void saveSysDefaultBankAdminUserGroup(IssuerBankDO issuerBank) {
        if (isSysDefaultBankAdminUserGroupExisted(issuerBank.getId())) {
            return;
        }

        UserGroupDO sysDefaultBankAdminUserGroup =
                createSysDefaultBankAdminUserGroup(issuerBank.getId(), issuerBank.getCreator());
        userGroupDao.save(sysDefaultBankAdminUserGroup);
    }

    /** 會員銀行系統預設銀行授權模組權限是否已存在 */
    private boolean isSysDefaultBankAdminUserGroupExisted(Long issuerBankId) {
        return userGroupDao
                .findByIssuerBankIdAndName(issuerBankId, SYSTEM_DEFAULT_BANK_ADMIN_USER_GROUP_NAME)
                .isPresent();
    }

    /** 建立會員銀行系統預設銀行群組模組權限 */
    private UserGroupDO createSysDefaultBankAdminUserGroup(long issuerBankId, String user) {
        UserGroupDO userGroup = new UserGroupDO();
        userGroup.setIssuerBankId(issuerBankId);
        userGroup.setName(SYSTEM_DEFAULT_BANK_ADMIN_USER_GROUP_NAME);
        userGroup.setType(
                UserGroupType.SYSTEM); // Each bank has only one USER_GROUP record with SYSTEM type
        userGroup.setScope(UserGroupScope.BANK);

        for (String permissionName : EnvironmentConstants.BANK_ADMIN_DEFAULT_PERMISSION) {
            String methodName = null;
            try {
                methodName =
                        StringCustomizedUtils.methodNameUnderlineToCamel(
                                "set", permissionName.trim());
                Method method = userGroup.getClass().getDeclaredMethod(methodName, Boolean.class);
                method.invoke(userGroup, Boolean.TRUE);
            } catch (Exception e) {
                String errMsg =
                        String.format(
                                "[createSysDefaultBankAdminUserGroup] method not found, methodName=%s",
                                methodName);
                log.error(errMsg, methodName);
                throw new OceanException(ResultStatus.SERVER_ERROR, errMsg);
            }
        }

        userGroup.setCreator(user);
        userGroup.setCreateMillis(System.currentTimeMillis());
        return userGroup;
    }

    private void saveDefaultSecretKey(IssuerBankDO issuerBank) throws OceanException {
        List<CardBrandDTO> cardBrandList = cardBrandConfigurationHelper.findCardBrandList();

        try {
            for (CardBrandDTO cardBrand : cardBrandList) {
                SecretKeyDO keyEntity = new SecretKeyDO();
                keyEntity.setIssuerBankId(issuerBank.getId());
                keyEntity.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
                keyEntity.setKeyA("System default, please replace it.");
                keyEntity.setKeyB("System default, please replace it.");
                keyEntity.setCardBrand(cardBrand.getName());
                keyEntity.setCreator(issuerBank.getCreator());
                keyEntity.setCreateMillis(System.currentTimeMillis());
                secretKeyDao.saveOrUpdate(keyEntity);
            }
        } catch (DatabaseException e) {
            log.error("[saveDefaultSecretKey] database error");
            throw new OceanException(e.getResultStatus());
        }
    }

    private void saveDefaultSmsTemplate(IssuerBankDO issuerBank) throws OceanException {
        try {
            SmsTemplateDO smsEntity = new SmsTemplateDO();
            smsEntity.setIssuerBankId(issuerBank.getId());
            smsEntity.setExpireMillis(180000L);
            smsEntity.setVerifyMessage(
                    "Your verification code is {{authId}}-{{authCode}}, ensure your card number ending in {{lastFour}}, and transaction amount is {{amount}}");
            smsEntity.setExceedLimitMessage(
                    "You have exceeded the maximum number of attempts to provide a valid OTP, this transaction transaction has been cancelled");
            smsEntity.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
            smsEntity.setCreator(issuerBank.getCreator());
            smsEntity.setCreateMillis(System.currentTimeMillis());
            smsTemplateDao.saveOrUpdate(smsEntity);
        } catch (DatabaseException e) {
            log.error("[saveDefaultSmsTemplate] database error", e);
            throw new OceanException(e.getResultStatus());
        }
    }

    private void saveDefaultChallengeViewMessage(IssuerBankDO issuerBank) throws OceanException {
        try {
            for (LangDO lang : langDao.findAll()) {
                for (ChallengeViewMessageCategory category :
                        ChallengeViewMessageCategory.values()) {
                    ChallengeViewMessageDO challengeMsgEntity = new ChallengeViewMessageDO();
                    challengeMsgEntity.setIssuerBankId(issuerBank.getId());
                    challengeMsgEntity.setCategory(category);
                    challengeMsgEntity.setLanguageCode(lang.getLanguageCode());

                    if (category.equals(ChallengeViewMessageCategory.phoneVerifyPage)) {
                        challengeMsgEntity.setMainBodyTitle("Choose Authentication Channel");
                        challengeMsgEntity.setMainBodyMessage(
                            "Merchant: {{merchantName}}<br>"
                                + "Amount: {{amount}}<br>"
                                + "Card Number: {{cardNumber}}<br>"
                                + "Date: {{time}}");
                        challengeMsgEntity.setNpaMainBodyMessage(
                            "Merchant: {{merchantName}}<br>"
                                + "Amount: {{amount}}<br>"
                                + "Card Number: {{cardNumber}}<br>"
                                + "Date: {{time}}");
                        challengeMsgEntity.setWebRemarkBodyMessage(
                                "● Choose an authentication channel and press \"Submit\".<br>"
                                        + "● Press \"Authentication Information Incorrect\" and contact customer service if the information is incorrect.");
                        challengeMsgEntity.setWebRemarkBodyColor("#F44336");
                        challengeMsgEntity.setBtnBodyPhoneErrorButton("Authentication Information Incorrect");
                    } else {
                        challengeMsgEntity.setMainBodyTitle("OTP Authentication");
                        challengeMsgEntity.setMainBodyMessage(
                            "Merchant: {{merchantName}}<br>"
                                + "Amount: {{amount}}<br>"
                                + "Card Number: {{cardNumber}}<br>"
                                + "Date: {{time}}<br>");
                        challengeMsgEntity.setNpaMainBodyMessage(
                            "Merchant: {{merchantName}}<br>"
                                + "Amount: {{amount}}<br>"
                                + "Card Number: {{cardNumber}}<br>"
                                + "Date: {{time}}<br>");
                        challengeMsgEntity.setWebRemarkBodyMessage(
                                "Enter the code in the field below:");
                        challengeMsgEntity.setWebRemarkBodyColor("#212121");
                        challengeMsgEntity.setBtnBodyPhoneErrorButton("Cancel");
                    }
                    challengeMsgEntity.setVerifyInputPlaceholder("Enter code here");
                    challengeMsgEntity.setAppRemarkBodyMessage(
                            "Provide the required data for the test case.");
                    challengeMsgEntity.setBtnBodySubmitButton("Submit");
                    challengeMsgEntity.setBtnBodyOtpResendButton("Resend OTP code");
                    challengeMsgEntity.setPhoneErrorBodyMessage(
                            "Please call and our customer service center will handle it for you.");
                    challengeMsgEntity.setPhoneErrorBodySymbol(true);
                    challengeMsgEntity.setBtnBodyCancelButton("Cancel");

                    // OTP Setting
                    challengeMsgEntity.setVerifyFailedMessage("Sorry, that code is not right.");
                    challengeMsgEntity.setResendMessage("Not getting the OTP?");
                    challengeMsgEntity.setNotResendMessage("Exceeding the maximum number of attempts to provide a valid OTP");

                    challengeMsgEntity.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
                    challengeMsgEntity.setCreator(issuerBank.getCreator());
                    challengeMsgEntity.setCreateMillis(System.currentTimeMillis());

                    challengeViewMessageDao.saveOrUpdate(challengeMsgEntity);
                }
            }
        } catch (DatabaseException e) {
            log.error("[saveDefaultChallengeViewMessage] database error", e);
            throw new OceanException(e.getResultStatus());
        }
    }

    private void saveDefaultChallengeViewOtpSetting(IssuerBankDO issuerBank) throws OceanException {
        try {
            ChallengeViewOtpSettingDO otpSettingEntity = new ChallengeViewOtpSettingDO();
            otpSettingEntity.setIssuerBankId(issuerBank.getId());
            otpSettingEntity.setMaxResendTimes(2);
            otpSettingEntity.setMaxChallengeTimes(2);
            otpSettingEntity.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
            otpSettingEntity.setCreator(issuerBank.getCreator());
            otpSettingEntity.setCreateMillis(System.currentTimeMillis());
            challengeViewOtpSettingDao.saveOrUpdate(otpSettingEntity);
        } catch (DatabaseException e) {
            log.error("[saveDefaultChallengeViewOtpSetting] database error", e);
            throw new OceanException(e.getResultStatus());
        }
    }

    private void saveDefaultIssuerTradingChannel(IssuerBankDO issuerBank) throws OceanException {
        try {
            List<String> cardBrandList =
                    cardBrandConfigurationHelper.findCardBrandList().stream()
                            .map(CardBrandDTO::getName)
                            .collect(Collectors.toList());

            IssuerTradingChannelDO channelEntity = new IssuerTradingChannelDO();
            channelEntity.setIssuerBankId(issuerBank.getId());
            channelEntity.setEnabledChannelList(cardBrandList);
            channelEntity.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
            channelEntity.setCreator(issuerBank.getCreator());
            channelEntity.setCreateMillis(System.currentTimeMillis());
            issuerTradingChannelDao.save(channelEntity);
        } catch (Exception e) {
            log.error("[saveDefaultIssuerTradingChannel] unknown exception", e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    private void saveDefaultIssuerHandingFee(IssuerBankDO issuerBank) throws OceanException {
        try {
            IssuerHandingFeeDO feeEntity = new IssuerHandingFeeDO();
            feeEntity.setIssuerBankId(issuerBank.getId());
            feeEntity.setHandingFeeType(HandingFeeType.CARD.getCode());
            feeEntity.setFeePerCard(0.0);
            feeEntity.setThresholdFee(0.0);
            feeEntity.setMinimumFee(0.0);
            feeEntity.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
            feeEntity.setCreator(issuerBank.getCreator());
            feeEntity.setCreateMillis(System.currentTimeMillis());
            issuerHandingFeeDao.save(feeEntity);
        } catch (Exception e) {
            log.error("[saveDefaultIssuerHandingFee] unknown exception", e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    private void saveDefaultBlackListAuthStatus(IssuerBankDO issuerBank) throws OceanException {
        try {
            for (BlackListAuthStatusCategory category : BlackListAuthStatusCategory.values()) {
                if (BlackListAuthStatusCategory.UNKNOWN.equals(category)) {
                    continue;
                }
                BlackListAuthStatusDO authEntity = new BlackListAuthStatusDO();
                authEntity.setIssuerBankId(issuerBank.getId());
                authEntity.setCategory(category.getCode());
                authEntity.setTransStatus(TransStatus.Challenge.getCode());
                authEntity.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
                authEntity.setCreator(issuerBank.getCreator());
                authEntity.setCreateMillis(System.currentTimeMillis());
                blackListAuthStatusDao.save(authEntity);
            }
        } catch (Exception e) {
            log.error("[saveDefaultBlackListAuthStatus] unknown exception", e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    /** 各銀行"單筆新增"預設ID * */
    private void saveDefaultBlackListBatchId(IssuerBankDO issuerBank) throws OceanException {
        try {
            BatchImportDO batchEntity = new BatchImportDO();
            batchEntity.setIssuerBankId(issuerBank.getId());
            batchEntity.setBatchName(EnvironmentConstants.DEFAULT_BLACK_LIST_PAN_BATCH_NAME);
            batchEntity.setFileName(" "); // oracle空格=null, 所以這裡多空了一格
            batchEntity.setTransStatus(TransStatus.Challenge.getCode());
            batchEntity.setPanNumber(0); // 批次卡號數量
            batchEntity.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
            batchEntity.setCreator(issuerBank.getCreator());
            batchEntity.setCreateMillis(System.currentTimeMillis());
            batchImportDao.add(batchEntity);
        } catch (Exception e) {
            log.error("[saveDefaultBlackListBatchId] unknown exception", e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    /** 設定預設發卡行驗證畫面logo */
    private void saveDefaultIssuerLogo(IssuerBankDO issuerBank) {
        IssuerLogoDO issuerLogo = new IssuerLogoDO();
        issuerLogo.setIssuerBankId(issuerBank.getId());
        issuerLogo.setImageName("default");

        byte[] defaultIssuerLogoImageBytes = getDefaultIssuerLogoImageBytes();
        issuerLogo.setImageContent(defaultIssuerLogoImageBytes);
        issuerLogo.setImageSize(defaultIssuerLogoImageBytes.length);

        issuerLogo.setCreator(issuerBank.getCreator());
        issuerLogo.setCreateMillis(System.currentTimeMillis());
        issuerLogo.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        issuerLogoDao.save(issuerLogo);
    }

    /** 取得預設發卡行驗證畫面logo圖檔 */
    private byte[] getDefaultIssuerLogoImageBytes() {
        return FileUtils.getFileBytes("static/img/bank.jpg");
    }

    /** 設定預設發卡行logo (給Portal的銀行商標使用) */
    private void saveDefaultIssuerBrandLogo(IssuerBankDO issuerBank) {
        IssuerBrandLogoDO issuerBrandLogo = new IssuerBrandLogoDO();
        issuerBrandLogo.setIssuerBankId(issuerBank.getId());
        issuerBrandLogo.setImageName("no image");
        // NOTE: 不給預設圖片，Portal UI 會預設顯示組織的 logo
        //        byte[] defaultIssuerBrandLogoImageBytes = getDefaultIssuerBankLogoImageBytes();
        //        issuerBrandLogo.setImageContent(defaultIssuerBrandLogoImageBytes);
        issuerBrandLogo.setImageSize(1);
        issuerBrandLogo.setCreator(issuerBank.getCreator());
        issuerBrandLogo.setCreateMillis(System.currentTimeMillis());
        issuerBrandLogo.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        issuerBrandLogoDao.save(issuerBrandLogo);
    }

    private void saveDefaultOtpSendingSetting(IssuerBankDO issuerBank) {
        OtpSendingSettingDO otpSendingSetting =
                OtpSendingSettingDO.builder()
                        .issuerBankId(issuerBank.getId())
                        .orgEnable(true)
                        .bankEnable(false)
                        .bankUrl(null)
                        .jweRsaPublicKey(null)
                        .jwsSecretKey(null)
                        .creator(issuerBank.getCreator())
                        .auditStatus(AuditStatus.PUBLISHED.getSymbol())
                        .build();
        otpSendingSettingDao.save(otpSendingSetting);
    }

    private void saveDefaultRbaSetting(IssuerBankDO issuerBank) {
        ClassicRbaCheckDO classicRbaCheck = new ClassicRbaCheckDO();
        classicRbaCheck.setIssuerBankId(issuerBank.getId());

        classicRbaCheck.setClassicRbaEnabled(true);

        classicRbaCheck.setPurchaseAmountEnabled(true);
        classicRbaCheck.setPurchaseAmountAmount("1000");
        classicRbaCheck.setPurchaseAmountMinAmount("3");
        classicRbaCheck.setPurchaseAmountCurrencyCode(String.valueOf(EnvironmentConstants.SYSTEM_CURRENCY_CODE));

        classicRbaCheck.setCardholderDataEnabled(true);
        classicRbaCheck.setCardholderDataName(true);
        classicRbaCheck.setCardholderDataPhone(false);
        classicRbaCheck.setCardholderDataEmail(false);
        classicRbaCheck.setCardholderDataPostcode(false);

        classicRbaCheck.setCumulativeAmountEnabled(true);
        classicRbaCheck.setCumulativeAmountPeriod(200L);
        classicRbaCheck.setCumulativeAmountAmount("5000");
        classicRbaCheck.setCumulativeAmountCurrencyCode(String.valueOf(EnvironmentConstants.SYSTEM_CURRENCY_CODE));
        classicRbaCheck.setCumulativeAmountTxEnabled(true);
        classicRbaCheck.setCumulativeAmountTxCount(5L);

        // CumulativeTransaction is deprecated
        classicRbaCheck.setCumulativeTransactionEnabled(false);
        classicRbaCheck.setCumulativeTransactionCount(5L);

        classicRbaCheck.setLocationConsistencyEnabled(true);
        classicRbaCheck.setLocationConsistencyIp(true);
        classicRbaCheck.setLocationConsistencyBrwTz(true);
        classicRbaCheck.setLocationConsistencyBilling(true);
        classicRbaCheck.setLocationConsistencyShipping(true);

        classicRbaCheck.setBrowserLanguageEnabled(true);
        classicRbaCheck.setBrowserLanguageList("en-us");

        classicRbaCheck.setVpnEnabled(true);
        classicRbaCheck.setProxyEnabled(true);
        classicRbaCheck.setPrivateBrowsingEnabled(true);
        classicRbaCheck.setDeviceVariationEnabled(true);
        classicRbaCheck.setMccEnabled(false);

        classicRbaCheck.setRecurringPaymentEnabled(true);
        classicRbaCheck.setRecurringPaymentFrequency(30L);
        classicRbaCheck.setRecurringPaymentExpiration(1000L);

        classicRbaCheck.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        classicRbaCheck.setCreator(issuerBank.getCreator());
        classicRbaCheck.setCreateMillis(issuerBank.getCreateMillis());
        try {
            classicRbaCheckDao.save(classicRbaCheck);
        } catch (DatabaseException e) {
            log.error("[saveDefaultRbaSetting] unknown exception", e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    private String createIssuerHsmEncryptKey(String bankCode, Long issuerBankId) {
        log.info("[createIssuerHsmEncryptKey] start for issuerBank code={}", StringUtils.normalizeSpace(bankCode));
        String keyLabel = hsmPlugin.bankDataKyLabelFormat(issuerBankId);
        String keyId = hsmPlugin.generateEncryptKey(keyLabel, issuerBankId);
        log.info(
                "[createIssuerHsmEncryptKey] success create HsmEncryptKey={} for issuerBank code={}",
                StringUtils.normalizeSpace(keyId),
                StringUtils.normalizeSpace(bankCode));
        return keyId;
    }

    private String createIssuerHsmSensitiveDataKey(String bankCode, Long issuerBankId) {
        log.info("[createIssuerHsmSensitiveDataKey] start for issuerBank code={}", StringUtils.normalizeSpace(bankCode));
        String keyLabel = hsmPlugin.sensitiveDataKyLabelFormat(issuerBankId);
        String keyId = hsmPlugin.generateEncryptKey(keyLabel, issuerBankId);
        log.info(
            "[createIssuerHsmSensitiveDataKey] success create HsmEncryptKey={} for issuerBank code={}",
            StringUtils.normalizeSpace(keyId),
            StringUtils.normalizeSpace(bankCode));
        return keyId;
    }
    
    private String createAcsOperatorId() {
        String newSequence = issuerBankDao.getAcsOperatorIdNextVal().toString();
        String newAcsOperatorId = "ITMX_ACS_" + StringUtils.leftPad(newSequence, 7, "0");

        log.info("[createIssuerBank] newAcsOperatorId=" + newAcsOperatorId);
        return newAcsOperatorId;
    }

    /** 取得預設發卡行logo圖檔 */
    private byte[] getDefaultIssuerBankLogoImageBytes() {
        return FileUtils.getFileBytes("static/img/fisc_logo.jpg");
    }

    /** 修改銀行 */
    public IssuerBankDto updateIssuerBank(IssuerBankDto issuerBankDto) {
        return issuerBankDao
                .update(
                        PortalIssuerBankDO.builder()
                                .name(issuerBankDto.getName())
                                .code(issuerBankDto.getCode())
                                .id(issuerBankDto.getId())
                                .issuerBankId(issuerBankDto.getIssuerBankId())
                                .symmetricKeyId(issuerBankDto.getSymmetricKeyId())
                                .threeDSMethodUrl(issuerBankDto.getThreeDSMethodUrl())
                                .acsUrl(issuerBankDto.getAcsUrl())
                                .acsRefNumber(issuerBankDto.getAcsRefNumber())
                                .auditStatus(issuerBankDto.getAuditStatus())
                                .user(issuerBankDto.getUser())
                                .fileName(issuerBankDto.getFileName())
                                .fileContent(issuerBankDto.getFileContent())
                                .version(issuerBankDto.getVersion())
                                .build())
                .map(
                        e -> {
                            issuerBankDto.setId(e.getId());
                            return issuerBankDto;
                        })
                .orElse(issuerBankDto);
    }

    /** 刪除銀行 */
    @Transactional(rollbackFor = Exception.class)
    public DeleteDataDTO deleteIssuerBank(DeleteDataDTO deleteDataDTO) {
        return issuerBankDao
                .delete(
                        DeleteDataDO.builder()
                                .user(deleteDataDTO.getUser())
                                .auditStatus(deleteDataDTO.getAuditStatus())
                                .id(deleteDataDTO.getId())
                                .build())
                .map(
                        e -> {
                            deleteRelatedTableData(e.getId(), deleteDataDTO.getUser(), e.getDeleteMillis());

                            deleteDataDTO.setId(e.getId());
                            deleteDataDTO.setAuditStatus(
                                    AuditStatus.getStatusBySymbol(e.getAuditStatus()));
                            return deleteDataDTO;
                        })
                .orElse(deleteDataDTO);
    }

    /** 刪除銀行相關資料表資料，但交易/報表資料不刪除 delete_flag = true */
    private void deleteRelatedTableData(long issuerBankId, String deleter, long deleteMillis) {
        log.info("[deleteRelatedTableData] start");

        // BIN_RANGE
        binRangeDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // CHALLENGE_VIEW_MESSAGE
        challengeViewMessageDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // CHALLENGE_VIEW_OTP_SETTING
        challengeViewOtpSettingDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // SMS_TEMPLATE
        smsTemplateDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // ISSUER_LOGO
        issuerLogoDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // ISSUER_TRADING_CHANNEL
        issuerTradingChannelDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // ISSUER_BRAND_LOGO
        issuerBrandLogoDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // USER_ACCOUNT
        userAccountDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // USER_GROUP
        userGroupDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // OTP_LOCK_LOG
        otpOperationLogDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // THREE_D_S_VERIFY_ENABLED_LOG
        threeDSVerifiedDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // AUDITING
        auditingDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // ISSUER_HANDING_FEE
        issuerHandingFeeDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // OTP_SENDING_SETTING
        otpSendingSettingDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // SECRET_KEY
        secretKeyDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // BLACK_LIST_AUTH_STATUS
        blackListAuthStatusDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // BLACK_LIST_PAN_BATCH
        batchImportDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // WHITE_LIST_ATTEMPT_AUTHORIZE
        whiteListAttemptAuthorizeDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // PAN_OTP_STATISTICS
        panOtpStatisticsDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // BLACK_LIST_PAN
        blackListPanDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // WHITE_LIST_PAN
        whiteListPanDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // WHITE_LIST_ATTEMPT_SETTING
        whiteListAttemptSettingDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // BLACK_LIST_IP_GROUP
        blackListIpGroupDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // BLACK_LIST_MERCHANT_URL
        blackListMerchantUrlDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // PAN_INFO
        panInfoDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // BLACK_LIST_DEVICE_INFO
        blackListDeviceInfoDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // CLASSIC_RBA_CHECK
        classicRbaCheckDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        // PLUGIN_ISSUER_RUNTIME_PROPERTY
        issuerRuntimePropertyDao.deleteByIssuerBankId(issuerBankId, deleter, deleteMillis);

        log.info("[deleteRelatedTableData] done");
    }

    public IssuerTradingChannelQueryResultDto queryByIssuerBankId(IssuerQueryDto issuerQueryDto) {
        List<String> cardBrandList =
                cardBrandConfigurationHelper.findCardBrandList().stream()
                        .map(CardBrandDTO::getName)
                        .collect(Collectors.toList());
        Long queryIssuerBankId = issuerQueryDto.getIssuerBankId();
        IssuerTradingChannelDO issuerTradingChannel =
                issuerTradingChannelDao
                        .getByIssuerBankId(queryIssuerBankId)
                        .orElseThrow(
                                () ->
                                        new NoSuchDataException(
                                                "Issuer trading channel has not setup."));
        return IssuerTradingChannelQueryResultDto.valueOf(issuerTradingChannel, cardBrandList);
    }

    public Optional<IssuerTradingChannelUpdateDto> findOneIssuerTradingChannel(Long issuerBankId) {
        List<String> cardBrandList =
                cardBrandConfigurationHelper.findCardBrandList().stream()
                        .map(CardBrandDTO::getName)
                        .collect(Collectors.toList());
        return issuerTradingChannelDao
                .getByIssuerBankId(issuerBankId)
                .map(
                        issuerTradingChannel ->
                                IssuerTradingChannelUpdateDto.valueOf(
                                        issuerTradingChannel, cardBrandList));
    }

    public IssuerTradingChannelUpdateDto updateIssuerTradingChannel(
            IssuerTradingChannelUpdateDto updateDto) {
        IssuerTradingChannelDO issuerTradingChannel =
                issuerTradingChannelDao
                        .getByIssuerBankId(updateDto.getIssuerBankId())
                        .map(
                                e -> {
                                    e.setAuditStatus(updateDto.getAuditStatus().getSymbol());
                                    e.setUpdater(updateDto.getUser());
                                    e.setUpdateMillis(System.currentTimeMillis());
                                    if (AuditStatus.AUDITING
                                            .getSymbol()
                                            .equals(e.getAuditStatus())) {
                                        return e;
                                    }
                                    List<String> enabledTradingChannelList =
                                            updateDto.getTradingChannelList().stream()
                                                    .filter(TradingChannel::isEnabled)
                                                    .map(TradingChannel::getCardBrand)
                                                    .collect(Collectors.toList());
                                    e.setEnabledChannelList(enabledTradingChannelList);
                                    return e;
                                })
                        .orElseThrow(
                                () ->
                                        new NoSuchDataException(
                                                "Issuer trading channel has not setup."));

        issuerTradingChannel = issuerTradingChannelDao.save(issuerTradingChannel);
        updateDto.setId(issuerTradingChannel.getId());
        return updateDto;
    }

    public AbstractMap.SimpleEntry<AuditStatus, byte[]> queryIssuerLogoByIssuerBankId(
            IssuerQueryDto issuerQueryDto) {
        Long issuerBankId = issuerQueryDto.getIssuerBankId();
        return issuerBrandLogoDao
                .findByIssuerBankId(issuerBankId)
                .map(
                        issuerBrandLogo ->
                                new AbstractMap.SimpleEntry<>(
                                        AuditStatus.getStatusBySymbol(
                                                issuerBrandLogo.getAuditStatus()),
                                        issuerBrandLogo.getImageContent()))
                .orElse(new AbstractMap.SimpleEntry<>(AuditStatus.PUBLISHED, new byte[0]));
    }

    public Optional<IssuerLogoUpdateDto> findOneIssuerBrandLogo(Long issuerBankId) {
        return issuerBrandLogoDao
                .findByIssuerBankId(issuerBankId)
                .map(IssuerLogoUpdateDto::valueOf);
    }

    public IssuerLogoUpdateDto updateIssuerBrandLogo(IssuerLogoUpdateDto updateDto) {
        IssuerBrandLogoDO issuerBrandLogo =
                issuerBrandLogoDao
                        .findByIssuerBankId(updateDto.getIssuerBankId())
                        .map(
                                entity -> {
                                    entity.setAuditStatus(updateDto.getAuditStatus().getSymbol());
                                    entity.setUpdater(updateDto.getUser());
                                    entity.setUpdateMillis(System.currentTimeMillis());

                                    if (AuditStatus.AUDITING.equals(updateDto.getAuditStatus())) {
                                        return entity;
                                    }

                                    entity.setImageName(updateDto.getFileName());
                                    entity.setImageSize(updateDto.getImageSize());
                                    entity.setImageContent(updateDto.getFileContent());
                                    return entity;
                                })
                        .orElseThrow(
                                () -> new NoSuchDataException("issuer bank logo has not setup."));

        issuerBrandLogo = issuerBrandLogoDao.save(issuerBrandLogo);
        updateDto.setId(issuerBrandLogo.getId());
        return updateDto;
    }

    public IssuerHandingFeeQueryResultDto queryIssuerHandingFeeByIssuerBankId(
            IssuerQueryDto issuerQueryDto) throws NoSuchDataException {
        Long issuerBankId = issuerQueryDto.getIssuerBankId();
        log.debug("query issuer handling Fee. issuerBankId={}", issuerBankId);

        IssuerHandingFeeDO issuerHandingFee =
                issuerHandingFeeDao
                        .getByIssuerBankId(issuerBankId)
                        .orElseThrow(
                                () ->
                                        new NoSuchDataException(
                                                "issuer bank handling fee has not setup."));

        return IssuerHandingFeeQueryResultDto.valueOf(issuerHandingFee);
    }

    public Optional<IssuerHandingFeeUpdateDto> findOneIssuerHandingFee(Long issuerHandingFeeId) {
        return issuerHandingFeeDao
                .findOneById(issuerHandingFeeId)
                .map(IssuerHandingFeeUpdateDto::valueOf);
    }

    public IssuerHandingFeeUpdateDto updateIssuerHandingFee(IssuerHandingFeeUpdateDto updateDto) {
        IssuerHandingFeeDO issuerHandingFee =
                issuerHandingFeeDao
                        .findOneById(updateDto.getId())
                        .map(
                                entity -> {
                                    entity.setAuditStatus(updateDto.getAuditStatus().getSymbol());
                                    entity.setUpdater(updateDto.getUser());
                                    entity.setUpdateMillis(System.currentTimeMillis());

                                    if (AuditStatus.AUDITING.equals(updateDto.getAuditStatus())) {
                                        return entity;
                                    }
                                    // PUBLISHED時才更新以下資訊
                                    entity.setHandingFeeType(updateDto.getHandlingFeeType());
                                    entity.setFeePerCard(
                                            updateDto.getChargePerCard().getFeePerCard());
                                    entity.setThresholdFee(
                                            updateDto.getChargePerCard().getThresholdFee());
                                    entity.setMinimumFee(
                                            updateDto.getChargePerCard().getMinimumFee());
                                    entity.setFeePerMonth(
                                            updateDto.getChargePerMonth().getFeePerMonth());
                                    return entity;
                                })
                        .orElseThrow(
                                () ->
                                        new NoSuchDataException(
                                                "IssuerHandingFee not found by id="
                                                        + updateDto.getId()));
        if (!issuerHandingFeeDao.save(issuerHandingFee)) {
            throw new OceanException(
                    ResultStatus.DB_SAVE_ERROR, "Update issuer-handing-fee error.");
        }
        return updateDto;
    }

    private HttpServletResponse configHttpResponse() {
        httpServletResponse.setContentType("text/plain");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "IssuerHandingFee_" + sdf.format(new Date());
        httpServletResponse.setHeader(
                "Content-Disposition", "attachment; filename=" + fileName + ".txt");
        return httpServletResponse;
    }

    public OtpSendingSettingResDTO getOtpSendingSetting(Long issuerBankId) {
        Optional<OtpSendingSettingDO> otpSendingSettingOpt =
                otpSendingSettingDao.findByIssuerBankIdAndNotDelete(issuerBankId);
        if (!otpSendingSettingOpt.isPresent()) {
            throw new OceanException(
                    ResultStatus.ILLEGAL_ARGUMENT,
                    "have been deleted or illegal argument : issuerBankId");
        } else {
            return OtpSendingSettingResDTO.valueOf(otpSendingSettingOpt.get());
        }
    }

    public OtpSendingSettingDO getOteSendingSettingByIssuerBankId(Long issuerBankId) {
        Optional<OtpSendingSettingDO> settingOpt =
                otpSendingSettingDao.findByIssuerBankIdAndNotDelete(issuerBankId);
        return settingOpt.orElseThrow(
                () ->
                        new OceanException(
                                ResultStatus.ILLEGAL_ARGUMENT,
                                "have been deleted or illegal argument : issuerBankId"));
    }

    public OtpSendingSettingDto updateOtpSendingSetting(OtpSendingSettingDto otpSendingSettingDto) {
        Optional<OtpSendingSettingDO> otpSendingSettingOpt =
                otpSendingSettingDao.findByIssuerBankIdAndNotDelete(
                        otpSendingSettingDto.getIssuerBankId());
        if (!otpSendingSettingOpt.isPresent()) {
            throw new OceanException(
                    ResultStatus.ILLEGAL_ARGUMENT,
                    "have been deleted or illegal argument : issuerBankId");
        } else {
            OtpSendingSettingDO otpSendingSetting = otpSendingSettingOpt.get();
            otpSendingSetting.setOrgEnable(otpSendingSettingDto.getOrgEnable());
            otpSendingSetting.setBankEnable(otpSendingSettingDto.getBankEnable());
            if (null != otpSendingSettingDto.getBankUrl()) {
                otpSendingSetting.setBankUrl(otpSendingSettingDto.getBankUrl());
            }
            otpSendingSetting.setUpdater(otpSendingSettingDto.getUserAccount());
            otpSendingSetting.setUpdateMillis(System.currentTimeMillis());
            otpSendingSetting.setAuditStatus(otpSendingSettingDto.getAuditStatus().getSymbol());
            otpSendingSettingOpt = otpSendingSettingDao.save(otpSendingSetting);
            if (!otpSendingSettingOpt.isPresent()) {
                throw new OceanException(
                        ResultStatus.DB_SAVE_ERROR, "Update otp_sending_setting error");
            } else {
                return OtpSendingSettingDto.valueOf(otpSendingSettingOpt.get());
            }
        }
    }

    public OtpSendingKeyUploadDto uploadJweOrJwsKey(OtpSendingKeyUploadDto otpSendingKeyUploadDto) {
        OtpSendingSettingDO otpSendingSetting =
                otpSendingSettingDao
                        .findByIssuerBankIdAndNotDelete(otpSendingKeyUploadDto.getIssuerBankId())
                        .orElseThrow(
                                () ->
                                        new OceanException(
                                                ResultStatus.ILLEGAL_ARGUMENT,
                                                "have been deleted or illegal argument : issuerBankId"));

        if (null != otpSendingKeyUploadDto.getFileContent()) {
            otpSendingSetting.setJweRsaPublicKey(otpSendingKeyUploadDto.getFileContent());
        }
        if (null != otpSendingKeyUploadDto.getJwsSecretKey()) {
            otpSendingSetting.setJwsSecretKey(otpSendingKeyUploadDto.getJwsSecretKey());
        }
        otpSendingSetting.setUpdater(otpSendingKeyUploadDto.getUserAccount());
        otpSendingSetting.setUpdateMillis(System.currentTimeMillis());
        otpSendingSetting.setAuditStatus(otpSendingKeyUploadDto.getAuditStatus().getSymbol());

        otpSendingSetting =
                otpSendingSettingDao
                        .save(otpSendingSetting)
                        .orElseThrow(
                                () ->
                                        new OceanException(
                                                ResultStatus.DB_SAVE_ERROR,
                                                "Update otp_sending_setting error"));

        return OtpSendingKeyUploadDto.valueOf(otpSendingSetting);
    }
}
