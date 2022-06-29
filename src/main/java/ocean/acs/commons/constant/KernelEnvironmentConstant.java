package ocean.acs.commons.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * ConditionalOnProperty<br>
 * The setting for batch server, because batch server no need setting kernel properties<br>
 * - havingValue = true, ignore setting properties<br>
 * - havingValue = false, must setting properties<br>
 * - havingValue is missing, must setting properties
 */
@Component
@ConditionalOnProperty(name = "kernel.properties.ignore", havingValue = "false", matchIfMissing = true)
public class KernelEnvironmentConstant {

    public static final long ORG_ISSUER_BANK_ID = -1L;

    // public static IssuerBankListDTO issuerBankDto = new IssuerBankListDTO();
    // public static ChallengeViewDTO challengeViewDto;
    // public static Map<Long, ClassicRbaCheckDTO> issuerBankIdClassicRbaCheckDTOMap;

    public static final int MILLISECONDS = 1000;

    /** RReqFactory - SMS OTP */
    public static final String AUTHENTICATION_METHOD = "02";

    /** RReqFactory - SMS OTP */
    public static final String AUTHENTICATION_TYPE = "02";

    public static final String UNKNOWN_CARD_BRAND = "UNKNOWN";

    // Kernel
    public static Boolean IS_PRODUCTION_MODE;
    public static String MESSAGE_VERSION;
    public static String SUPPORTED_MESSAGE_VERSION_LIST;
    public static String ACS_URL;
    public static String CONTEXT_PATH;
    public static String ACS_CHALLENGE_URL;
    public static String ACS_INTEGRATOR_URL;
    public static String ACS_INTERFACE;
    public static boolean IS_BIIL_ADDR_COUNTRY_REQUIRED;

    // Transaction timeout, Unit: second
    public static int FIRST_CREQ_TIMEOUT;
    public static int TRANSACTION_TIMEOUT;

    // OkHttp Proxy
    public static Boolean OKHTTP_PROXY_ENABLED;
    public static String OKHTTP_PROXY_SERVER_HOSTNAME;
    public static Integer OKHTTP_PROXY_SERVER_PORT;

    // DDCA
    public static Boolean IS_CONSIDER_CLASSIC_RBA_RESULT;
    public static String DDCA_APP_ID;
    public static String DDCA_APP_KEY;
    public static String DDCA_TIMEOUT_MILLIS;
    public static String DDCA_JS_URL;
    public static String DDCA_ENV_TEST = "TEST";
    public static String DDCA_ENV_PROD = "PROD";

    @Value("${is.production}")
    public void setProductionMode(String productionMode) {
        IS_PRODUCTION_MODE = convertToBoolean(productionMode);
    }

    @Value("${message.version}")
    public void setMessageVersion(String messageVersion) {
        MESSAGE_VERSION = messageVersion;
    }

    @Value("${supported.message.version.list}")
    public void setSupportedMessageVersionList(String supportedMessageVersionList) {
        SUPPORTED_MESSAGE_VERSION_LIST = supportedMessageVersionList;
    }

    @Value("${acs.url}")
    public void setAcsUrl(String acsUrl) {
        ACS_URL = acsUrl;
    }

    @Value("${server.servlet.contextPath}")
    public void setContextPath(String contextPath) {
        CONTEXT_PATH = contextPath;
    }

    @Value("${acs.challenge.url}")
    public void setAcsChallengeUrl(String acsChallengeUrl) {
        ACS_CHALLENGE_URL = acsChallengeUrl;
    }

    @Value("${acs.integrator.url}")
    public void setAcsIntegratorUrl(String acsIntegratorUrl) {
        ACS_INTEGRATOR_URL = acsIntegratorUrl;
    }

    @Value("${first.creq.timeout}")
    public void setFirstCreqTimeout(int firstCreqTimeout) {
        FIRST_CREQ_TIMEOUT = firstCreqTimeout;
    }

    @Value("${transaction.timeout}")
    public void setTransactionTimeout(int transactionTimeout) {
        TRANSACTION_TIMEOUT = transactionTimeout;
    }

    @Value("${acs.interface}")
    public void setAcsInterface(String acsInterface) {
        ACS_INTERFACE = acsInterface;
    }

    @Value("${asc.isBillAddrCountryRequired}")
    public void setIsBillAddrCountryRequired(boolean isBillAddrCountryRequired) {
        IS_BIIL_ADDR_COUNTRY_REQUIRED = isBillAddrCountryRequired;
        // if (isBillAddrCountryRequired) { //TODO
        // log.warn("asc.isBillAddrCountryRequired = true, only set to true when testing EMV test
        // cases");
        // }
    }

    @Value("${okhttp.proxy.enabled}")
    public void setOkhttpProxyEnabled(Boolean okhttpProxyEnabled) {
        OKHTTP_PROXY_ENABLED = okhttpProxyEnabled;
    }

    @Value("${okhttp.proxy.server.hostname}")
    public void setOkhttpProxyServerIp(String okhttpProxyServerHostname) {
        OKHTTP_PROXY_SERVER_HOSTNAME = okhttpProxyServerHostname;
    }

    @Value("${okhttp.proxy.server.port}")
    public void setOkhttpProxyServerPort(Integer okhttpProxyServerPort) {
        OKHTTP_PROXY_SERVER_PORT = okhttpProxyServerPort;
    }

    @Value("${is.consider.classic.rba.result}")
    public void setIS_CONSIDER_CLASSIC_RBA_RESULT(String iS_CONSIDER_CLASSIC_RBA_RESULT) {
        IS_CONSIDER_CLASSIC_RBA_RESULT = convertToBoolean(iS_CONSIDER_CLASSIC_RBA_RESULT);
    }

    @Value("${ddca.app.id}")
    public void setDdcaAppId(String ddcaAppId) {
        DDCA_APP_ID = ddcaAppId;
    }

    @Value("${ddca.app.key}")
    public void setDdcaAppKey(String ddcaAppKey) {
        DDCA_APP_KEY = ddcaAppKey;
    }

    @Value("${ddca.timeout.millis}")
    public void setDdcaTimeoutMillis(String ddcaTimeoutMillis) {
        DDCA_TIMEOUT_MILLIS = ddcaTimeoutMillis;
    }

    @Value("${ddca.js.url}")
    public void setDdcaJsUrl(String ddcaJsUrl) {
        DDCA_JS_URL = ddcaJsUrl;
    }

    private boolean convertToBoolean(String value) {
        boolean returnValue = false;
        if ("1".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value)
                || "y".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value)
                || "on".equalsIgnoreCase(value)) {
            returnValue = true;
        }
        return returnValue;
    }

}
