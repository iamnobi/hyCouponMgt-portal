package com.cherri.acs_portal.service;

import com.cherri.acs_portal.config.MimaPolicyUiColumnConfig;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.SystemConstants;
import com.cherri.acs_portal.controller.request.CheckMimaIsExpiredRequest;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.MimaRecordDto;
import com.cherri.acs_portal.dto.mima.MimaPolicyDto;
import com.cherri.acs_portal.dto.usermanagement.AccountLockReason;
import com.cherri.acs_portal.util.DateUtil;
import com.cherri.acs_portal.util.HmacUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.models.dao.MimaPolicyDAO;
import ocean.acs.models.dao.MimaRecordDAO;
import ocean.acs.models.data_object.entity.MimaPolicyDO;
import ocean.acs.models.data_object.entity.MimaRecordDO;
import ocean.acs.models.data_object.portal.UserAccountDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mima Policy Service 密碼原則管理服務
 *
 * @author Alan Chen
 */
@Slf4j
@Service
public class MimaPolicyService {


    private final MimaPolicyDAO mimaPolicyDao;
    private final MimaRecordDAO mimaRecordDao;
    private final MimaPolicyUiColumnConfig mimaPolicyUiColumnConfig;

    @Autowired
    public MimaPolicyService(MimaPolicyDAO mimaPolicyDao, MimaRecordDAO mimaRecordDao,
      MimaPolicyUiColumnConfig mimaPolicyUiColumnConfig) {
        this.mimaPolicyDao = mimaPolicyDao;
        this.mimaRecordDao = mimaRecordDao;
        this.mimaPolicyUiColumnConfig = mimaPolicyUiColumnConfig;
    }

    /**
     * UI欄位基本檢核條件
     *
     * @return 檢核條件Json Object
     */
    public Map<String, Object> getColumnBaseRule() {
        Map<String, Object> map = new HashMap<>(9);
        map.put("min_retry_count", mimaPolicyUiColumnConfig.getRetryCountMin());
        map.put("max_retry_count", mimaPolicyUiColumnConfig.getRetryCountMax());
        map.put("min_fresh_interval", mimaPolicyUiColumnConfig.getFreshIntervalNumMin());
        map.put("max_fresh_interval", mimaPolicyUiColumnConfig.getFreshIntervalNumMax());
        map.put("min_account_idle_day", mimaPolicyUiColumnConfig.getAccountIdleDayMin());
        map.put("max_account_idle_day", mimaPolicyUiColumnConfig.getAccountIdleDayMax());
        map.put("history_duplicate_count", mimaPolicyUiColumnConfig.getHistoryDuplicateCount());
        map.put("min_length", mimaPolicyUiColumnConfig.getLengthMin());
        map.put("max_length", mimaPolicyUiColumnConfig.getLengthMax());
        return map;
    }

    /**
     * 密碼管理原則是否存在
     *
     * @param issuerBankId 銀行代碼
     * @return 是否存在
     */
    public boolean isPolicyExistByIssuerBankId(long issuerBankId) {
        return mimaPolicyDao.isPolicyExistByIssuerBankId(issuerBankId);
    }

    /**
     * 密碼管理原則是否存在
     *
     * @param id Mima policy id 密碼管理原則ID
     * @return 是否存在
     */
    public boolean isPolicyExistById(long id) {
        return mimaPolicyDao.isPolicyExistById(id);
    }

    /**
     * 查詢密碼管理原則 - 銀行代碼
     *
     * @param issuerBankId 銀行代碼
     * @return 已存在密碼管理原則 {@link MimaPolicyDto}
     * @throws OceanException {@link ResultStatus} MIMA_POLICY_NOT_FOUND
     * @throws OceanException {@link ResultStatus} DB_SAVE_ERROR
     */
    public MimaPolicyDto query(long issuerBankId) {
        Optional<MimaPolicyDO> mimaPolicyDtoOptional = mimaPolicyDao
          .findByIssuerBankId(issuerBankId);
        if (mimaPolicyDtoOptional.isPresent()) {
            return MimaPolicyDto.valueOf(mimaPolicyDtoOptional.get());
        } else {
            return MimaPolicyDto.valueOf(mimaPolicyDao.createDefaultPolicy(issuerBankId));
        }
    }

    /**
     * 新增密碼管理原則
     *
     * @param dto {@link MimaPolicyDto} 密碼管理原則
     * @return isSuccess 是否建立成功
     * @throws OceanException {@link ResultStatus} DB_SAVE_ERROR
     */
    @Transactional(timeout = 15, rollbackFor = Exception.class)
    public boolean create(MimaPolicyDto dto) {
        long now = System.currentTimeMillis();
        return mimaPolicyDao.createPolicy(MimaPolicyDO.builder()
          .id(dto.getId())
          .issuerBankId(dto.getIssuerBankId())
          .mimaMinLength(dto.getMimaMinLength())
          .mimaMaxLength(dto.getMimaMaxLength())
          .mimaAlphabetCount(dto.getMimaAlphabetCount())
          .mimaMinLowerCase(dto.getMimaMinLowerCase())
          .mimaMinUpperCase(dto.getMimaMinUpperCase())
          .mimaMinNumeric(dto.getMimaMinNumeric())
          .mimaMinSpecialChar(dto.getMimaMinSpecialChar())
          .accountMaxIdleDay(dto.getAccountMaxIdleDay())
          .loginRetryCount(dto.getLoginRetryCount())
          .mimaFreshInterval(dto.getMimaFreshInterval())
          .mimaHistoryDupCount(dto.getMimaHistoryDupCount())
          .createTime(now)
          .updateTime(now)
          .build()
        );
    }

    /**
     * 更新密碼管理原則
     *
     * @param dto {@link MimaPolicyDto} 新密碼管理原則
     * @return isSuccess 是否更新成功
     * @throws OceanException {@link ResultStatus} DB_SAVE_ERROR
     */
    @Transactional(timeout = 15, rollbackFor = Exception.class)
    public boolean update(MimaPolicyDto dto) {
        long now = System.currentTimeMillis();
        return mimaPolicyDao.updatePolicy(MimaPolicyDO.builder()
          .id(dto.getId())
          .issuerBankId(dto.getIssuerBankId())
          .mimaMinLength(dto.getMimaMinLength())
          .mimaMaxLength(dto.getMimaMaxLength())
          .mimaAlphabetCount(dto.getMimaAlphabetCount())
          .mimaMinLowerCase(dto.getMimaMinLowerCase())
          .mimaMinUpperCase(dto.getMimaMinUpperCase())
          .mimaMinNumeric(dto.getMimaMinNumeric())
          .mimaMinSpecialChar(dto.getMimaMinSpecialChar())
          .accountMaxIdleDay(dto.getAccountMaxIdleDay())
          .loginRetryCount(dto.getLoginRetryCount())
          .mimaFreshInterval(dto.getMimaFreshInterval())
          .mimaHistoryDupCount(dto.getMimaHistoryDupCount())
          .updateTime(now)
          .build());
    }

    /**
     * 密碼原則驗證-IssuerBankId
     *
     * @param issuerBankId 銀行代碼
     * @param account      使用者帳號
     * @param mima         密碼
     * @throws OceanException {@link ResultStatus} MIMA_POLICY_NOT_FOUND
     * @throws OceanException {@link ResultStatus} DB_SAVE_ERROR
     */
    public boolean verifyMimaByIssuerBankId(long issuerBankId, String account, String mima) {
        return verify(account, mima, query(issuerBankId));
    }
    // NOTE: isVerifyOtp 時, send otp count 的 lock reason 要 +1
    public boolean isAccountLocked(UserAccountDO userAccountDO, boolean isVerifyOtp) {
        MimaPolicyDto mimaPolicyDto = this.query(userAccountDO.getIssuerBankId());
        return getAccountLockReason(mimaPolicyDto, userAccountDO, isVerifyOtp) != null;
    }
    public boolean isAccountLocked(UserAccountDO userAccountDO) {
        return isAccountLocked(userAccountDO, false);
    }

    public AccountLockReason getAccountLockReason(MimaPolicyDto mimaPolicyDto, UserAccountDO userAccountDO) {
     return getAccountLockReason(mimaPolicyDto, userAccountDO, false);
    }

    // NOTE: isVerifyOtp 時, send otp count 的 lock reason 要 +1
    public AccountLockReason getAccountLockReason(MimaPolicyDto mimaPolicyDto, UserAccountDO userAccountDO, boolean isVerifyOtp) {
        Long lastActiveMillis = (userAccountDO.getLastLoginMillis() != null)
            ? userAccountDO.getLastLoginMillis()
            : userAccountDO.getCreateMillis();
        if (userAccountDO.getTryFailCount() >= mimaPolicyDto.getLoginRetryCount()) {
            return AccountLockReason.EXCEED_MAX_LOGIN_ATTEMPT;
        }
        if (userAccountDO.getForgetMimaCount() > SystemConstants.FORGET_MIMA_COUNT) {
            return AccountLockReason.EXCEED_MAX_FORGET_MIMA;
        }
        if (System.currentTimeMillis() >
            lastActiveMillis + (SystemConstants.ONE_DAY_MILLIS * new Long(mimaPolicyDto.getAccountMaxIdleDay()))) {
            return AccountLockReason.EXCEED_MAX_UNUSED_PERIOD;
        }
        if (isVerifyOtp) {
            if (userAccountDO.getSendOtpCount() > SystemConstants.SEND_OTP_COUNT + 1) {
                return AccountLockReason.EXCEED_MAX_SEND_MFA_TIMES;
            }
        } else {
            if (userAccountDO.getSendOtpCount() > SystemConstants.SEND_OTP_COUNT) {
                return AccountLockReason.EXCEED_MAX_SEND_MFA_TIMES;
            }
        }

        if (userAccountDO.getVerifyOtpCount() > SystemConstants.VERIFY_OTP_COUNT) {
            return AccountLockReason.EXCEED_MAX_VERIFY_MFA_ATTEMPT;
        }
        return null;
    }

    /**
     * 檢查密碼是否已過期
     *
     * @param request 密碼是否已過期檢查請求
     * @return 是否已過期
     */
    public boolean checkMimaIsExpired(Long issuerBankId, String account) {
        /* find Mima last change time */
        MimaRecordDO mimaRecord = mimaRecordDao
            .findLastChangeMimaRecord(issuerBankId, account)
            .orElse(null);
        if (mimaRecord == null) {
            return false;
        }

        int lastChangeMimaDay = DateUtil.calDayDuration(System.currentTimeMillis(),
            mimaRecord.getCreateTime());
        MimaPolicyDto mimaPolicyDto = this.query(issuerBankId);
        return lastChangeMimaDay > mimaPolicyDto.getMimaFreshInterval();
    }

    /**
     * 驗證流程 1.是否不重複 2.是否符合規範
     *
     * @param account 使用者帳號
     * @param mima    密碼
     * @param policy  密碼原則
     * @return 是否通過驗證
     */
    private boolean verify(String account, String mima, MimaPolicyDto policy) {
        String mimaHash = HmacUtils.encrypt(mima, EnvironmentConstants.hmacHashKey);
        boolean isNotDuplicate = isNotDuplicatedByHistory(
          policy.getMimaHistoryDupCount(), policy.getIssuerBankId(), account, mimaHash
        );
        boolean isMatch = policy.isMatchPattern(mima);
        log.debug("isNotDuplicate: {}, isMatch: {}", isNotDuplicate, isMatch);
        return isNotDuplicate && isMatch;
    }

    /**
     * 是否重複使用密碼
     *
     * @param range        追朔紀錄筆數
     * @param issuerBankId 銀行代碼
     * @param account      使用者帳號
     * @param mimaHash     密碼hash
     * @return 是否使用重複密碼
     * @throws OceanException {@link ResultStatus} DB_READ_ERROR
     */
    private boolean isNotDuplicatedByHistory(int range, long issuerBankId, String account,
      String mimaHash) {
        return !mimaRecordDao.isDuplicatedByMimaRecordHistory(issuerBankId, account, range, mimaHash);
    }
}
