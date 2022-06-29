package ocean.acs.models.oracle.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.AuthenticationLogDO;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_AUTHENTICATION_LOG)
public class AuthenticationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "authentication_log_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "AUTHENTICATION_LOG_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "authentication_log_seq_generator")
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ID)
    private Long id;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ISSUER_BANK_ID)
    private Long issuerBankId;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_THREE_D_S_COMP_IND)
    private String threeDSCompInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ACCT_ID)
    private String acctID;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ACCT_TYPE)
    private String acctType;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ACQUIRER_B_I_N)
    private String acquirerBIN;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ACS_CHALLENGE_MANDATED)
    private String acsChallengeMandated;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ACS_DEC_CON_IND)
    private String acsDecConInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ACS_OPERATOR_ID)
    private String acsOperatorID;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ACQUIRER_MERCHANT_ID)
    private String acquirerMerchantID;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ACS_REFERENCE_NUMBER)
    private String acsReferenceNumber;

    @Lob
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ACS_SIGNED_CONTENT)
    private String acsSignedContent;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ACS_URL)
    private String acsURL;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ADDR_MATCH)
    private String addrMatch;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AUTHENTICATION_TYPE)
    private String authenticationType;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AUTHENTICATION_VALUE_EN)
    private String authenticationValueEn;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BILL_ADDR_CITY)
    private String billAddrCity;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BILL_ADDR_COUNTRY)
    private String billAddrCountry;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BILL_ADDR_LINE_1)
    private String billAddrLine1;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BILL_ADDR_LINE_1_EN)
    private String billAddrLine1En;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BILL_ADDR_LINE_2)
    private String billAddrLine2;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BILL_ADDR_LINE_2_EN)
    private String billAddrLine2En;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BILL_ADDR_LINE_3)
    private String billAddrLine3;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BILL_ADDR_LINE_3_EN)
    private String billAddrLine3En;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BILL_ADDR_POST_CODE)
    private String billAddrPostCode;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BILL_ADDR_STATE)
    private String billAddrState;

    @Lob
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BROAD_INFO)
    private String broadInfo;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BROWSER_IP)
    private String browserIp;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BROWSER_ACCEPT_HEADER)
    private String browserAcceptHeader;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BROWSER_COLOR_DEPTH)
    private String browserColorDepth;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BROWSER_JAVA_ENABLED)
    private Boolean browserJavaEnabled;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BROWSER_JAVASCRIPT_ENABLED)
    private Boolean browserJavascriptEnabled;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BROWSER_LANGUAGE)
    private String browserLanguage;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BROWSER_PRIVATE_MODE)
    private Boolean browserPrivateMode;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BROWSER_SCREEN_HEIGHT)
    private String browserScreenHeight;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BROWSER_SCREEN_WIDTH)
    private String browserScreenWidth;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BROWSER_TZ)
    private String browserTZ;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_BROWSER_USER_AGENT)
    private String browserUserAgent;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_CARD_EXPIRY_DATE)
    private String cardExpiryDate;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_CARDHOLDER_INFO)
    private String cardholderInfo;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_CARDHOLDER_NAME)
    private String cardholderName;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_CARDHOLDER_NAME_EN)
    private String cardholderNameEn;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_DEVICE_CHANNEL)
    private String deviceChannel;

    @Lob
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_DEVICE_INFO_EN)
    private String deviceInfoEn;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_DS_REFERENCE_NUMBER)
    private String dsReferenceNumber;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ECI)
    private String eci;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_EMAIL)
    private String email;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MCC)
    private String mcc;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MERCHANT_COUNTRY_CODE)
    private String merchantCountryCode;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MERCHANT_NAME)
    private String merchantName;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MESSAGE_CATEGORY)
    private String messageCategory;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MESSAGE_VERSION)
    private String messageVersion;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_PAY_TOKEN_IND)
    private Boolean payTokenInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_PAY_TOKEN_SOURCE)
    private String payTokenSource;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_PURCHASE_AMOUNT)
    private String purchaseAmount;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_PURCHASE_CURRENCY)
    private String purchaseCurrency;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_PURCHASE_DATE)
    private String purchaseDate;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_PURCHASE_EXPONENT)
    private String purchaseExponent;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_PURCHASE_INSTAL_DATA)
    private String purchaseInstalData;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_RECURRING_EXPIRY)
    private String recurringExpiry;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_RECURRING_FREQUENCY)
    private String recurringFrequency;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SDK_APP_ID)
    private String sdkAppID;

    @Lob
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SDK_ENC_DATA)
    private String sdkEncData;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SDK_MAX_TIMEOUT)
    private String sdkMaxTimeout;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SDK_REFERENCE_NUMBER)
    private String sdkReferenceNumber;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SHIP_ADDR_CITY)
    private String shipAddrCity;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SHIP_ADDR_COUNTRY)
    private String shipAddrCountry;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SHIP_ADDR_LINE_1)
    private String shipAddrLine1;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SHIP_ADDR_LINE_1_EN)
    private String shipAddrLine1En;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SHIP_ADDR_LINE_2)
    private String shipAddrLine2;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SHIP_ADDR_LINE_2_EN)
    private String shipAddrLine2En;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SHIP_ADDR_LINE_3)
    private String shipAddrLine3;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SHIP_ADDR_LINE_3_EN)
    private String shipAddrLine3En;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SHIP_ADDR_POST_CODE)
    private String shipAddrPostCode;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SHIP_ADDR_STATE)
    private String shipAddrState;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_THREE_D_S_REQ_AUTH_IND)
    private String threeDSRequestorAuthenticationInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_THREE_D_S_REQ_AUTH_METHOD_IND)
    private String threeDSReqAuthMethodInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_THREE_D_S_REQ_CHALLENGE_IND)
    private String threeDSRequestorChallengeInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_THREE_D_S_REQ_DEC_MAX_TIME)
    private Integer threeDSRequestorDecMaxTime;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_THREE_D_S_REQ_DEC_REQ_IND)
    private String threeDSRequestorDecReqInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_THREE_D_S_REQ_ID)
    private String threeDSRequestorID;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_THREE_D_S_REQ_NAME)
    private String threeDSRequestorName;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_THREE_D_S_REQ_URL)
    private String threeDSRequestorURL;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_THREE_D_S_SERVER_REF_NUMBER)
    private String threeDSServerRefNumber;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_THREE_D_S_SERVER_URL)
    private String threeDSServerURL;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_THREE_R_I_IND)
    private String threeRIInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_TRANS_STATUS)
    private String transStatus;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_TRANS_STATUS_REASON)
    private String transStatusReason;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_TRANS_TYPE)
    private String transType;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_WHITE_LIST_STATUS)
    private String whiteListStatus;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_WHITE_LIST_STATUS_SOURCE)
    private String whiteListStatusSource;

    // acctInfo
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_CH_ACC_AGE_IND)
    private String aifChAccAgeInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_CH_ACC_CHANGE)
    private String aifChAccChange;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_CH_ACC_CHANGE_IND)
    private String aifChAccChangeInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_CH_ACC_DATE)
    private String aifChAccDate;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_CH_ACC_PW_CHANGE)
    private String aifChAccPwChange;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_CH_ACC_PW_CHANGE_IND)
    private String aifChAccPwChangeInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_NB_PURCHASE_ACCOUNT)
    private String aifNbPurchaseAccount;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_PAYMENT_ACC_AGE)
    private String aifPaymentAccAge;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_PAYMENT_ACC_IND)
    private String aifPaymentAccInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_SHIP_ADDRESS_USAGE)
    private String aifShipAddressUsage;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_SHIP_ADDRESS_USAGE_IND)
    private String aifShipAddressUsageInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_SHIP_NAME_INDICATOR)
    private String aifShipNameIndicator;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_PROVISION_ATTEMPTS_DAY)
    private String aifProvisionAttemptsDay;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_SUSPICIOUS_ACC_ACTIVITY)
    private String aifSuspiciousAccActivity;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_TXN_ACTIVITY_DAY)
    private String aifTxnActivityDay;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_AIF_TXN_ACTIVITY_YEAR)
    private String aifTxnActivityYear;
    // acctInfo_end

    // acsRenderingType
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ART_ACS_INTERFACE)
    private String artAcsInterface;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ART_ACS_UI_TEMPLATE)
    private String artAcsUiTemplate;
    // acsRenderingType_end

    // deviceRenderOptions
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_DRO_SDK_INTERFACE)
    private String droSdkInterface;
    // end

    // homePhone
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_HP_CC)
    private String hpCc;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_HP_SUBSCRIBER)
    private String hpSubscriber;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_HP_SUBSCRIBER_EN)
    private String hpSubscriberEn;
    // homePhone_end

    // workPhone
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_WP_CC)
    private String wpCc;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_WP_SUBSCRIBER)
    private String wpSubscriber;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_WP_SUBSCRIBER_EN)
    private String wpSubscriberEn;
    // workPhone_end

    // mobilePhone
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MP_CC)
    private String mpCc;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MP_SUBSCRIBER)
    private String mpSubscriber;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MP_SUBSCRIBER_EN)
    private String mpSubscriberEn;
    // mobilePhone_end

    // merchantRiskIndicator
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MRI_DELIVERY_EMAIL_ADDRESS)
    private String mriDeliveryEmailAddress;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MRI_DELIVERY_TIMEFRAME)
    private String mriDeliveryTimeframe;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MRI_GIFT_CARD_AMOUNT)
    private String mriGiftCardAmount;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MRI_GIFT_CARD_COUNT)
    private String mriGiftCardCount;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MRI_GIFT_CARD_CURR)
    private String mriGiftCardCurr;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MRI_PRE_ORDER_DATE)
    private String mriPreOrderDate;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MRI_PRE_ORDER_PURCHASE_IND)
    private String mriPreOrderPurchaseInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MRI_REORDER_ITEMS_IND)
    private String mriReorderItemsInd;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_MRI_SHIP_INDICATOR)
    private String mriShipIndicator;
    // merchantRiskIndicator_end

    // threeDSRequestorAuthenticationInfo
    @Lob
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_TDSRAI_THR_D_S_REQ_A_DATA)
    private String tdsraiThreeDSReqAuthData;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_TDSRAI_THR_D_S_REQ_A_METHOD)
    private String tdsraiThreeDSReqAuthMethod;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_TDSRAI_THR_D_S_REQ_A_TIMESTAMP)
    private String tdsraiThreeDSReqAuthTimestamp;
    // threeDSRequestorAuthenticationInfo_end

    // threeDSRequestorPriorAuthenticationInfo
    @Lob
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_TDSRPAI_THR_D_S_REQ_PA_DATA)
    private String tdsrpaiThreeDSReqPriorAuthData;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_TDSRPAI_THR_D_S_REQ_PA_METHOD)
    private String tdsrpaiThreeDSReqPriorAuthMethod;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_TDSRPAI_THR_D_S_REQ_PA_T_STAMP)
    private String tdsrpaiThreeDSReqPriorAuthTimestamp;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_TDSRPAI_THR_D_S_REQ_P_REF)
    private String tdsrpaiThreeDSReqPriorRef;
    // threeDSRequestorPriorAuthenticationInfo_end

    // ARes
    @Lob
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ARES_BROAD_INFO)
    private String aresBroadInfo;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ARES_WHITE_LIST_STATUS)
    private String aresWhiteListStatus;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_ARES_WHITE_LIST_STATUS_SOURCE)
    private String aresWhiteListStatusSource;

    @NonNull
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_AUTHENTICATION_LOG_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_SYS_UPDATER)
    private String sysUpdater;

    @Column(name = DBKey.COL_AUTHENTICATION_LOG_UPDATE_MILLIS)
    private Long updateMillis;

    public static AuthenticationLog valueOf(AuthenticationLogDO d) {
        return new AuthenticationLog(d.getId(), d.getIssuerBankId(), d.getThreeDSCompInd(),
                d.getAcctID(), d.getAcctType(), d.getAcquirerBIN(), d.getAcsChallengeMandated(),
                d.getAcsDecConInd(), d.getAcsOperatorID(), d.getAcquirerMerchantID(),
                d.getAcsReferenceNumber(), d.getAcsSignedContent(), d.getAcsURL(), d.getAddrMatch(),
                d.getAuthenticationType(), d.getAuthenticationValueEn(), d.getBillAddrCity(),
                d.getBillAddrCountry(), d.getBillAddrLine1(), d.getBillAddrLine1En(),
                d.getBillAddrLine2(), d.getBillAddrLine2En(), d.getBillAddrLine3(),
                d.getBillAddrLine3En(), d.getBillAddrPostCode(), d.getBillAddrState(),
                d.getBroadInfo(), d.getBrowserIp(), d.getBrowserAcceptHeader(),
                d.getBrowserColorDepth(), d.getBrowserJavaEnabled(),
                d.getBrowserJavascriptEnabled(), d.getBrowserLanguage(), d.getBrowserPrivateMode(),
                d.getBrowserScreenHeight(), d.getBrowserScreenWidth(), d.getBrowserTZ(),
                d.getBrowserUserAgent(), d.getCardExpiryDate(), d.getCardholderInfo(),
                d.getCardholderName(), d.getCardholderNameEn(), d.getDeviceChannel(),
                d.getDeviceInfoEn(), d.getDsReferenceNumber(), d.getEci(), d.getEmail(), d.getMcc(),
                d.getMerchantCountryCode(), d.getMerchantName(), d.getMessageCategory(),
                d.getMessageVersion(), d.getPayTokenInd(), d.getPayTokenSource(),
                d.getPurchaseAmount(), d.getPurchaseCurrency(), d.getPurchaseDate(),
                d.getPurchaseExponent(), d.getPurchaseInstalData(), d.getRecurringExpiry(),
                d.getRecurringFrequency(), d.getSdkAppID(), d.getSdkEncData(), d.getSdkMaxTimeout(),
                d.getSdkReferenceNumber(), d.getShipAddrCity(), d.getShipAddrCountry(),
                d.getShipAddrLine1(), d.getShipAddrLine1En(), d.getShipAddrLine2(),
                d.getShipAddrLine2En(), d.getShipAddrLine3(), d.getShipAddrLine3En(),
                d.getShipAddrPostCode(), d.getShipAddrState(),
                d.getThreeDSRequestorAuthenticationInd(), d.getThreeDSReqAuthMethodInd(),
                d.getThreeDSRequestorChallengeInd(), d.getThreeDSRequestorDecMaxTime(),
                d.getThreeDSRequestorDecReqInd(), d.getThreeDSRequestorID(),
                d.getThreeDSRequestorName(), d.getThreeDSRequestorURL(),
                d.getThreeDSServerRefNumber(), d.getThreeDSServerURL(), d.getThreeRIInd(),
                d.getTransStatus(), d.getTransStatusReason(), d.getTransType(),
                d.getWhiteListStatus(), d.getWhiteListStatusSource(), d.getAifChAccAgeInd(),
                d.getAifChAccChange(), d.getAifChAccChangeInd(), d.getAifChAccDate(),
                d.getAifChAccPwChange(), d.getAifChAccPwChangeInd(), d.getAifNbPurchaseAccount(),
                d.getAifPaymentAccAge(), d.getAifPaymentAccInd(), d.getAifShipAddressUsage(),
                d.getAifShipAddressUsageInd(), d.getAifShipNameIndicator(),
                d.getAifProvisionAttemptsDay(), d.getAifSuspiciousAccActivity(),
                d.getAifTxnActivityDay(), d.getAifTxnActivityYear(), d.getArtAcsInterface(),
                d.getArtAcsUiTemplate(), d.getDroSdkInterface(), d.getHpCc(), d.getHpSubscriber(),
                d.getHpSubscriberEn(), d.getWpCc(), d.getWpSubscriber(), d.getWpSubscriberEn(),
                d.getMpCc(), d.getMpSubscriber(), d.getMpSubscriberEn(),
                d.getMriDeliveryEmailAddress(), d.getMriDeliveryTimeframe(),
                d.getMriGiftCardAmount(), d.getMriGiftCardCount(), d.getMriGiftCardCurr(),
                d.getMriPreOrderDate(), d.getMriPreOrderPurchaseInd(), d.getMriReorderItemsInd(),
                d.getMriShipIndicator(), d.getTdsraiThreeDSReqAuthData(),
                d.getTdsraiThreeDSReqAuthMethod(), d.getTdsraiThreeDSReqAuthTimestamp(),
                d.getTdsrpaiThreeDSReqPriorAuthData(), d.getTdsrpaiThreeDSReqPriorAuthMethod(),
                d.getTdsrpaiThreeDSReqPriorAuthTimestamp(), d.getTdsrpaiThreeDSReqPriorRef(),
                d.getAresBroadInfo(), d.getAresWhiteListStatus(), d.getAresWhiteListStatusSource(),
                d.getSysCreator(), d.getCreateMillis(), d.getSysUpdater(), d.getUpdateMillis());
    }

}
