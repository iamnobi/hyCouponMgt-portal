package ocean.acs.commons.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ocean.acs.commons.utils.AuditLogToStringStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * ConditionalOnProperty<br>
 * The setting for batch server, because batch server no need setting portal properties<br>
 * - havingValue = true, ignore setting properties<br>
 * - havingValue = false, must setting properties<br>
 * - havingValue is missing, must setting properties
 */
@Configuration
@ConditionalOnProperty(name = "portal.properties.ignore", havingValue = "false", matchIfMissing = true)
public class PortalEnvironmentConstants {

    public static Boolean IS_MULTI_ISSUER;

    public static final long ORG_ISSUER_BANK_ID = -1L;

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

    public static Integer PAGINATION_MAX_ROWS;

    public static Long WHITELIST_ATTEMPT_AVAILABLE_TRIES_LIMIT;
    public static Long WHITELIST_ATTEMPT_AVAILABLE_DURATION;

    public static Integer ABNORMAL_TRANSACTION_QUANTITY;
    public static Integer SYSTEM_HEALTH_NORMAL;

    /** 銀行管理員預設權限 */
    public static String[] BANK_ADMIN_DEFAULT_PERMISSION;

    /** 單筆新增的卡號黑名單批次名稱 */
    public static final String DEFAULT_BLACK_LIST_PAN_BATCH_NAME = "Manual";
    public static Integer JDBC_BATCH_SIZE;

    /** 檔案名稱最大長度 */
    public static final int MAX_FILENAME_LENGTH = 255;

    /** ACS支援的CardBrand */
    public static List<String> ACS_SUPPORTED_CARD_BRAND_LIST = new ArrayList<>();

    public static final AuditLogToStringStyle AUDIT_LOG_STYLE = new AuditLogToStringStyle();

    @Value("${acs.is-multi-issuer}")
    public void setIsMultiIssuer(Boolean isMultiIssuer) {
        IS_MULTI_ISSUER = isMultiIssuer;
    }

    @Value("${acs.kernel.url}")
    public void setAcsKernelUrl(String acsKernelUrl) {
        ACS_KERNEL_URL = acsKernelUrl;
    }

    @Value("${acs.integrator.url}")
    public void setAcsIntegratorUrl(String acsIntegratorUrl) {
        ACS_INTEGRATOR_URL = acsIntegratorUrl;
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

        // Verify method Name, //TODO
        // UserGroup userGroup = new UserGroup();
        // for (String permissionName : BANK_ADMIN_DEFAULT_PERMISSION) {
        // String methodName = null;
        // try {
        // methodName = StringCustomizedUtils.methodNameUnderlineToCamel("set",
        // permissionName.trim());
        // userGroup.getClass().getDeclaredMethod(methodName, Boolean.class);
        // } catch (Exception e) {
        // String errMsg =
        // String.format(
        // "[setSysDefaultBankAdminUserGroup] method:%s not found, please check if the
        // parameter:[bank.admin.permission] has unknown permission value in
        // application.properties.",
        // methodName);
        // throw new OceanException(ResultStatus.SERVER_ERROR, errMsg);
        // }
        // }
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

}


