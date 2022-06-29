package com.cherri.acs_portal.constant;

import com.cherri.acs_kernel.plugin.enumerator.HSMEncryptDecryptMechanism;
import com.cherri.acs_portal.dto.system.CavvImportMode;
import com.cherri.acs_portal.util.StringCustomizedUtils;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.data_object.entity.UserGroupDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class EnvironmentConstants {

    public static boolean IS_MULTI_ISSUER;
    public static boolean IS_EXTERNAL_AUTHENTICATION_SERVICE;
    public static boolean IS_MFA;
    public static String MFA_SYSTEM;
    public static final long ORG_ISSUER_BANK_ID = -1L;
    public static final long MONO_ISSUER_BANK_ID = 1L;

    public static byte[] hmacHashKey;
    public static String cavvEncryptPublicKey;
    public static String cavvEncryptPrivateKey;

    public static int VISA_KPI_N_RATE;
    public static int VISA_KPI_U_RATE;

    public static int JCB_KPI_N_RATE;
    public static int JCB_KPI_U_RATE;

    public static int MASTERCARD_KPI_N_RATE;
    public static int MASTERCARD_KPI_U_RATE;

    public static int AMERICAN_EXPRESS_KPI_N_RATE;
    public static int AMERICAN_EXPRESS_KPI_U_RATE;

    public static int UNION_PAY_KPI_N_RATE;
    public static int UNION_PAY_KPI_U_RATE;

    public static String DATABASE_TYPE;
    public static String ACS_KERNEL_URL;
    public static String[] ACS_KERNEL_URL_LIST;
    public static String ACS_INTEGRATOR_URL;
    public static ZoneId ACS_TIMEZONE_ID;
    public static TimeZone ACS_TIMEZONE;

    public static Integer PAGINATION_MAX_ROWS;

    public static Long WHITELIST_ATTEMPT_AVAILABLE_TRIES_LIMIT;
    public static Long WHITELIST_ATTEMPT_AVAILABLE_DURATION;

    public static Integer ABNORMAL_TRANSACTION_QUANTITY;
    public static Integer SYSTEM_HEALTH_NORMAL;

    public static CavvImportMode CAVV_IMPORT_MODE;
    public static boolean ATTEMPT_SETTING_SELECT_CURRENCY;

    /**
     * 銀行管理員預設權限
     */
    public static String[] BANK_ADMIN_DEFAULT_PERMISSION;

    /**
     * ORG Role 可以操作的 module list (org.role.module.list)
     */
    public static String[] ORG_ROLE_MODULE_LIST;

    /**
     * 單筆新增的卡號黑名單批次名稱
     */
    public static final String DEFAULT_BLACK_LIST_PAN_BATCH_NAME = "Manual";
    public static Integer JDBC_BATCH_SIZE;

    /**
     * 檔案名稱最大長度
     */
    public static final int MAX_FILENAME_LENGTH = 255;

    /**
     * ACS支援的CardBrand
     */
    public static List<String> ACS_SUPPORTED_CARD_BRAND_LIST = new ArrayList<>();

    public static HSMEncryptDecryptMechanism HSM_ENCRYPT_DECRYPT_MECHANISM;

    public static Boolean IS_HSM_SUPPORT_CALCULATE_CVV;
    public static int SYSTEM_CURRENCY_CODE;

    /**
     * 同個 session 中，若變更密碼時舊密碼輸入錯誤超過此次數，會強制登出
     */
    public static int CHANGE_MIMA_MAX_FAILED_TIMES;

    /**
     * for email link
     */
    public static String ACS_URL;

    public static String RECAPTCHA_SITE_KEY;
    public static String RECAPTCHA_SECRET;

    public static Boolean IS_PROXY_ENABLED;
    public static String PROXY_HOST;
    public static Integer PROXY_PORT;

    @Value("${acs.login.use.external.authentication.service:false}")
    public void setIsExternalAuthenticationService(boolean isExternalAuthenticationService) {
        IS_EXTERNAL_AUTHENTICATION_SERVICE = isExternalAuthenticationService;
    }

    @Value("${acs.is-multi-issuer:false}")
    public void setIsMultiIssuer(boolean isMultiIssuer) {
        IS_MULTI_ISSUER = isMultiIssuer;
    }

    @Value("${mfa.enable: false}")
    public void setIsMfa(boolean isMfa) {
        IS_MFA = isMfa;
    }

    @Value("${mfa.system: SIMPLE_OTP}")
    public void setMfaSystem(String mfaSystem) {
        MFA_SYSTEM = mfaSystem;
    }

    @Value("${acs.kernel.url}")
    public void setAcsKernelUrl(String acsKernelUrl) {
        ACS_KERNEL_URL = acsKernelUrl;
    }

    @Value("${acs.integrator.url}")
    public void setAcsIntegratorUrl(String acsIntegratorUrl) {
        ACS_INTEGRATOR_URL = acsIntegratorUrl;
    }

    @Value("${acs.timezone}")
    public void setAcsTimezone(String acsTimezone) {
        try {
            ACS_TIMEZONE_ID = ZoneId.of(acsTimezone);
            ACS_TIMEZONE = TimeZone.getTimeZone(ACS_TIMEZONE_ID);
        } catch (Exception e) {
            log.error("[setAcsTimezone] invalid timezone configure, acs.timezone = {}", acsTimezone, e);
            throw e;
        }
    }

    @Value("${spring.jpa.database}")
    public void setDatabaseType(String databaseType) {
        DATABASE_TYPE = databaseType.toLowerCase();
    }

    @Value("${pagination.max.rows}")
    public void setPaginationMaxRows(Integer paginationMaxRows) {
        PAGINATION_MAX_ROWS = paginationMaxRows;
    }

    @Value("${white.list.attempt.available.tries.limit}")
    public void setWhitelistAttemptAvailableTriesLimit(Long whitelistAttemptAvailableTriesLimit) {
        WHITELIST_ATTEMPT_AVAILABLE_TRIES_LIMIT = whitelistAttemptAvailableTriesLimit;
    }

    @Value("${white.list.attempt.available.duration}")
    public void setWhitelistAttemptAvailableDuration(Long whitelistAttemptAvailableDuration) {
        WHITELIST_ATTEMPT_AVAILABLE_DURATION = whitelistAttemptAvailableDuration;
    }

    @Value("${VISA.n.rate.kpi}")
    public void setVisaKpiNRate(int visaKpiNRate) {
        VISA_KPI_N_RATE = visaKpiNRate;
    }

    @Value("${VISA.u.rate.kpi}")
    public void setVisaKpiURate(int visaKpiURate) {
        VISA_KPI_U_RATE = visaKpiURate;
    }

    @Value("${JCB.n.rate.kpi}")
    public void setJcbKpiNRate(int jcbKpiNRate) {
        JCB_KPI_N_RATE = jcbKpiNRate;
    }

    @Value("${JCB.u.rate.kpi}")
    public void setJcbKpiURate(int jcbKpiURate) {
        JCB_KPI_U_RATE = jcbKpiURate;
    }

    @Value("${MASTERCARD.n.rate.kpi}")
    public void setMastercardKpiNRate(int mastercardKpiNRate) {
        MASTERCARD_KPI_N_RATE = mastercardKpiNRate;
    }

    @Value("${MASTERCARD.u.rate.kpi}")
    public void setMastercardKpiURate(int mastercardKpiURate) {
        MASTERCARD_KPI_U_RATE = mastercardKpiURate;
    }

    @Value("${AMERICAN_EXPRESS.n.rate.kpi}")
    public void setAmericanExpressKpiNRate(int americanExpressKpiNRate) {
        AMERICAN_EXPRESS_KPI_N_RATE = americanExpressKpiNRate;
    }

    @Value("${AMERICAN_EXPRESS.u.rate.kpi}")
    public void setAmericanExpressKpiURate(int americanExpressKpiURate) {
        AMERICAN_EXPRESS_KPI_U_RATE = americanExpressKpiURate;
    }

    @Value("${UNION_PAY.n.rate.kpi}")
    public void setUnionPayKpiNRate(int unionPayKpiNRate) {
        UNION_PAY_KPI_N_RATE = unionPayKpiNRate;
    }

    @Value("${UNION_PAY.u.rate.kpi}")
    public void setUnionPayKpiURate(int unionPayKpiURate) {
        UNION_PAY_KPI_U_RATE = unionPayKpiURate;
    }

    @Value("${system.health.normal}")
    public void setSystemHealthNormal(Integer systemHealthNormal) {
        SYSTEM_HEALTH_NORMAL = systemHealthNormal;
    }

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    public void setJdbcBatchSize(Integer jdbcBatchSize) {
        JDBC_BATCH_SIZE = jdbcBatchSize;
    }

    @Value("${bank.admin.permission}")
    public void setBankAdminPermission(String bankAdminPermission) {
        if (bankAdminPermission == null) {
            BANK_ADMIN_DEFAULT_PERMISSION = new String[0];
            return;
        }
        BANK_ADMIN_DEFAULT_PERMISSION = bankAdminPermission.split(",");
        // Verify method Name
        UserGroupDO userGroup = new UserGroupDO();
        for (String permissionName : BANK_ADMIN_DEFAULT_PERMISSION) {
            String methodName = null;
            try {
                methodName = StringCustomizedUtils
                  .methodNameUnderlineToCamel("set", permissionName.trim());
                userGroup.getClass().getDeclaredMethod(methodName, Boolean.class);
            } catch (Exception e) {
                String errMsg =
                  String.format(
                    "[setSysDefaultBankAdminUserGroup] method:%s not found, please check if the parameter:[bank.admin.permission] has unknown permission value in application.properties.",
                    methodName);
                throw new OceanException(ResultStatus.SERVER_ERROR, errMsg);
            }
        }
    }

    @Value("${org.role.module.list}")
    public void setOrgRoleModuleList(String orgRoleModuleList) {
        if (orgRoleModuleList == null) {
            ORG_ROLE_MODULE_LIST = new String[0];
            return;
        }
        ORG_ROLE_MODULE_LIST = orgRoleModuleList.split(",");
        // Verify method Name
        UserGroupDO userGroup = new UserGroupDO();
        for (String permissionName : ORG_ROLE_MODULE_LIST) {
            String methodName = null;
            try {
                methodName = StringCustomizedUtils
                  .methodNameUnderlineToCamel("set", permissionName.trim());
                userGroup.getClass().getDeclaredMethod(methodName, Boolean.class);
            } catch (Exception e) {
                String errMsg =
                  String.format(
                    "[setOrgRoleModuleList] method:%s not found, please check if the parameter:[org.role.module.list] has unknown permission value in application.properties.",
                    methodName);
                throw new OceanException(ResultStatus.SERVER_ERROR, errMsg);
            }
        }
    }

    public static String[] getAcsKernelList() {
        ACS_KERNEL_URL_LIST = ACS_KERNEL_URL.split(",");
        return ACS_KERNEL_URL_LIST;
    }

    @Value("${acs.supported-card-brand-list}")
    public void setAcsSupportedCardBrandList(String cardBrandNames) {
        // Valid cardBrand name
        List<String> cardBrandList = Arrays.asList(cardBrandNames.split(","));

        ACS_SUPPORTED_CARD_BRAND_LIST = cardBrandList;
    }

    @Value("${acs.hsm-encrypt-decrypt-mechanism}")
    public void setHsmEncryptDecryptMechanism(String hsmEncryptDecryptMechanism) {
        EnvironmentConstants.HSM_ENCRYPT_DECRYPT_MECHANISM = HSMEncryptDecryptMechanism
          .valueOf(hsmEncryptDecryptMechanism);
    }

    @Value("${acs.is-hsm-support-calculate-cvv}")
    public void setIsHsmSupportCalculateCvv(String isHsmSupportCalculateCvv) {
        IS_HSM_SUPPORT_CALCULATE_CVV = convertToBoolean(isHsmSupportCalculateCvv);
    }

    @Value("${acs.cavv-import-mode}")
    public void setCavvImportMode(CavvImportMode cavvImportMode) {
        CAVV_IMPORT_MODE = cavvImportMode;
    }

    @Value("${acs.attempt-setting-select-currency}")
    public void setAttemptSettingSelectCurrency(boolean attemptSettingSelectCurrency) {
        ATTEMPT_SETTING_SELECT_CURRENCY = attemptSettingSelectCurrency;
    }

    @Value("${acs.system-currency-code}")
    public void setSystemCurrencyCode(int systemCurrencyCode) {
        SYSTEM_CURRENCY_CODE = systemCurrencyCode;
    }

    @Value("${acs.change-mima-max-failed-times}")
    public void setChangeMimaMaxFailedTimes(int changeMimaMaxFailedTimes) {
        CHANGE_MIMA_MAX_FAILED_TIMES = changeMimaMaxFailedTimes;
    }

    @Value("${acs.url}")
    public void setAcsUrl(String acsUrl) {
        ACS_URL = acsUrl;
    }

    private boolean convertToBoolean(String value) {
        boolean returnValue = false;
        if ("1".equalsIgnoreCase(value)
          || "yes".equalsIgnoreCase(value)
          || "y".equalsIgnoreCase(value)
          || "true".equalsIgnoreCase(value)
          || "on".equalsIgnoreCase(value)) {
            returnValue = true;
        }
        return returnValue;
    }

    @Value("${recaptcha.sitekey}")
    private void setRecaptchaSiteKey(String recaptchaSiteKey) {
        RECAPTCHA_SITE_KEY = recaptchaSiteKey;
    }

    @Value("${recaptcha.secret}")
    private void setRecaptchaSecret(String recaptchaSecret) {
        RECAPTCHA_SECRET = recaptchaSecret;
    }

    @Value("${proxy.enable}")
    private void setIsProxyEnabled(String isProxyEnabled) {
        IS_PROXY_ENABLED = convertToBoolean(isProxyEnabled);
    }

    @Value("${proxy.host}")
    public void setProxyHost(String proxyHost) {
        PROXY_HOST = proxyHost;
    }

    @Value("${proxy.port}")
    public void setProxyPort(String proxyPort) {
        if (StringUtils.isNotEmpty(proxyPort)) {
            PROXY_PORT = Integer.parseInt(proxyPort);
        }
    }
}
