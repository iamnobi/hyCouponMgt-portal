package ocean.acs.models.data_object.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationLogDO {

    private Long id;
    private Long issuerBankId;
    private String threeDSCompInd;
    private String acctID;
    private String acctType;
    private String acquirerBIN;
    private String acsChallengeMandated;
    private String acsDecConInd;
    private String acsOperatorID;
    private String acquirerMerchantID;
    private String acsReferenceNumber;
    private String acsSignedContent;
    private String acsURL;
    private String addrMatch;
    private String authenticationType;
    private String authenticationValueEn;
    private String billAddrCity;
    private String billAddrCountry;
    private String billAddrLine1;
    private String billAddrLine1En;
    private String billAddrLine2;
    private String billAddrLine2En;
    private String billAddrLine3;
    private String billAddrLine3En;
    private String billAddrPostCode;
    private String billAddrState;
    private String broadInfo;
    private String browserIp;
    private String browserAcceptHeader;
    private String browserColorDepth;
    private Boolean browserJavaEnabled;
    private Boolean browserJavascriptEnabled;
    private String browserLanguage;
    private Boolean browserPrivateMode;
    private String browserScreenHeight;
    private String browserScreenWidth;
    private String browserTZ;
    private String browserUserAgent;
    private String cardExpiryDate;
    private String cardholderInfo;
    private String cardholderName;
    private String cardholderNameEn;
    private String deviceChannel;
    private String deviceInfoEn;
    private String dsReferenceNumber;
    private String eci;
    private String email;
    private String mcc;
    private String merchantCountryCode;
    private String merchantName;
    private String messageCategory;
    private String messageVersion;
    private Boolean payTokenInd;
    private String payTokenSource;
    private String purchaseAmount;
    private String purchaseCurrency;
    private String purchaseDate;
    private String purchaseExponent;
    private String purchaseInstalData;
    private String recurringExpiry;
    private String recurringFrequency;
    private String sdkAppID;
    private String sdkEncData;
    private String sdkMaxTimeout;
    private String sdkReferenceNumber;
    private String shipAddrCity;
    private String shipAddrCountry;
    private String shipAddrLine1;
    private String shipAddrLine1En;
    private String shipAddrLine2;
    private String shipAddrLine2En;
    private String shipAddrLine3;
    private String shipAddrLine3En;
    private String shipAddrPostCode;
    private String shipAddrState;
    private String threeDSRequestorAuthenticationInd;
    private String threeDSReqAuthMethodInd;
    private String threeDSRequestorChallengeInd;
    private Integer threeDSRequestorDecMaxTime;
    private String threeDSRequestorDecReqInd;
    private String threeDSRequestorID;
    private String threeDSRequestorName;
    private String threeDSRequestorURL;
    private String threeDSServerRefNumber;
    private String threeDSServerURL;
    private String threeRIInd;
    private String transStatus;
    private String transStatusReason;
    private String transType;
    private String whiteListStatus;
    private String whiteListStatusSource;
    private String aifChAccAgeInd;
    private String aifChAccChange;
    private String aifChAccChangeInd;
    private String aifChAccDate;
    private String aifChAccPwChange;
    private String aifChAccPwChangeInd;
    private String aifNbPurchaseAccount;
    private String aifPaymentAccAge;
    private String aifPaymentAccInd;
    private String aifShipAddressUsage;
    private String aifShipAddressUsageInd;
    private String aifShipNameIndicator;
    private String aifProvisionAttemptsDay;
    private String aifSuspiciousAccActivity;
    private String aifTxnActivityDay;
    private String aifTxnActivityYear;
    private String artAcsInterface;
    private String artAcsUiTemplate;
    private String droSdkInterface;
    private String hpCc;
    private String hpSubscriber;
    private String hpSubscriberEn;
    private String wpCc;
    private String wpSubscriber;
    private String wpSubscriberEn;
    private String mpCc;
    private String mpSubscriber;
    private String mpSubscriberEn;
    private String mriDeliveryEmailAddress;
    private String mriDeliveryTimeframe;
    private String mriGiftCardAmount;
    private String mriGiftCardCount;
    private String mriGiftCardCurr;
    private String mriPreOrderDate;
    private String mriPreOrderPurchaseInd;
    private String mriReorderItemsInd;
    private String mriShipIndicator;
    private String tdsraiThreeDSReqAuthData;
    private String tdsraiThreeDSReqAuthMethod;
    private String tdsraiThreeDSReqAuthTimestamp;
    private String tdsrpaiThreeDSReqPriorAuthData;
    private String tdsrpaiThreeDSReqPriorAuthMethod;
    private String tdsrpaiThreeDSReqPriorAuthTimestamp;
    private String tdsrpaiThreeDSReqPriorRef;
    private String aresBroadInfo;
    private String aresWhiteListStatus;
    private String aresWhiteListStatusSource;
    private String sysCreator;
    @Builder.Default
    private Long createMillis = System.currentTimeMillis();
    private String sysUpdater;
    private Long updateMillis;

    public static AuthenticationLogDO valueOf(ocean.acs.models.oracle.entity.AuthenticationLog e) {
        return new AuthenticationLogDO(e.getId(), e.getIssuerBankId(), e.getThreeDSCompInd(),
                e.getAcctID(), e.getAcctType(), e.getAcquirerBIN(), e.getAcsChallengeMandated(),
                e.getAcsDecConInd(), e.getAcsOperatorID(), e.getAcquirerMerchantID(),
                e.getAcsReferenceNumber(), e.getAcsSignedContent(), e.getAcsURL(), e.getAddrMatch(),
                e.getAuthenticationType(), e.getAuthenticationValueEn(), e.getBillAddrCity(),
                e.getBillAddrCountry(), e.getBillAddrLine1(), e.getBillAddrLine1En(),
                e.getBillAddrLine2(), e.getBillAddrLine2En(), e.getBillAddrLine3(),
                e.getBillAddrLine3En(), e.getBillAddrPostCode(), e.getBillAddrState(),
                e.getBroadInfo(), e.getBrowserIp(), e.getBrowserAcceptHeader(),
                e.getBrowserColorDepth(), e.getBrowserJavaEnabled(),
                e.getBrowserJavascriptEnabled(), e.getBrowserLanguage(), e.getBrowserPrivateMode(),
                e.getBrowserScreenHeight(), e.getBrowserScreenWidth(), e.getBrowserTZ(),
                e.getBrowserUserAgent(), e.getCardExpiryDate(), e.getCardholderInfo(),
                e.getCardholderName(), e.getCardholderNameEn(), e.getDeviceChannel(),
                e.getDeviceInfoEn(), e.getDsReferenceNumber(), e.getEci(), e.getEmail(), e.getMcc(),
                e.getMerchantCountryCode(), e.getMerchantName(), e.getMessageCategory(),
                e.getMessageVersion(), e.getPayTokenInd(), e.getPayTokenSource(),
                e.getPurchaseAmount(), e.getPurchaseCurrency(), e.getPurchaseDate(),
                e.getPurchaseExponent(), e.getPurchaseInstalData(), e.getRecurringExpiry(),
                e.getRecurringFrequency(), e.getSdkAppID(), e.getSdkEncData(), e.getSdkMaxTimeout(),
                e.getSdkReferenceNumber(), e.getShipAddrCity(), e.getShipAddrCountry(),
                e.getShipAddrLine1(), e.getShipAddrLine1En(), e.getShipAddrLine2(),
                e.getShipAddrLine2En(), e.getShipAddrLine3(), e.getShipAddrLine3En(),
                e.getShipAddrPostCode(), e.getShipAddrState(),
                e.getThreeDSRequestorAuthenticationInd(), e.getThreeDSReqAuthMethodInd(),
                e.getThreeDSRequestorChallengeInd(), e.getThreeDSRequestorDecMaxTime(),
                e.getThreeDSRequestorDecReqInd(), e.getThreeDSRequestorID(),
                e.getThreeDSRequestorName(), e.getThreeDSRequestorURL(),
                e.getThreeDSServerRefNumber(), e.getThreeDSServerURL(), e.getThreeRIInd(),
                e.getTransStatus(), e.getTransStatusReason(), e.getTransType(),
                e.getWhiteListStatus(), e.getWhiteListStatusSource(), e.getAifChAccAgeInd(),
                e.getAifChAccChange(), e.getAifChAccChangeInd(), e.getAifChAccDate(),
                e.getAifChAccPwChange(), e.getAifChAccPwChangeInd(), e.getAifNbPurchaseAccount(),
                e.getAifPaymentAccAge(), e.getAifPaymentAccInd(), e.getAifShipAddressUsage(),
                e.getAifShipAddressUsageInd(), e.getAifShipNameIndicator(),
                e.getAifProvisionAttemptsDay(), e.getAifSuspiciousAccActivity(),
                e.getAifTxnActivityDay(), e.getAifTxnActivityYear(), e.getArtAcsInterface(),
                e.getArtAcsUiTemplate(), e.getDroSdkInterface(), e.getHpCc(), e.getHpSubscriber(),
                e.getHpSubscriberEn(), e.getWpCc(), e.getWpSubscriber(), e.getWpSubscriberEn(),
                e.getMpCc(), e.getMpSubscriber(), e.getMpSubscriberEn(),
                e.getMriDeliveryEmailAddress(), e.getMriDeliveryTimeframe(),
                e.getMriGiftCardAmount(), e.getMriGiftCardCount(), e.getMriGiftCardCurr(),
                e.getMriPreOrderDate(), e.getMriPreOrderPurchaseInd(), e.getMriReorderItemsInd(),
                e.getMriShipIndicator(), e.getTdsraiThreeDSReqAuthData(),
                e.getTdsraiThreeDSReqAuthMethod(), e.getTdsraiThreeDSReqAuthTimestamp(),
                e.getTdsrpaiThreeDSReqPriorAuthData(), e.getTdsrpaiThreeDSReqPriorAuthMethod(),
                e.getTdsrpaiThreeDSReqPriorAuthTimestamp(), e.getTdsrpaiThreeDSReqPriorRef(),
                e.getAresBroadInfo(), e.getAresWhiteListStatus(), e.getAresWhiteListStatusSource(),
                e.getSysCreator(), e.getCreateMillis(), e.getSysUpdater(), e.getUpdateMillis());
    }
    
    public static AuthenticationLogDO valueOf(ocean.acs.models.sql_server.entity.AuthenticationLog e) {
        return new AuthenticationLogDO(e.getId(), e.getIssuerBankId(), e.getThreeDSCompInd(),
                e.getAcctID(), e.getAcctType(), e.getAcquirerBIN(), e.getAcsChallengeMandated(),
                e.getAcsDecConInd(), e.getAcsOperatorID(), e.getAcquirerMerchantID(),
                e.getAcsReferenceNumber(), e.getAcsSignedContent(), e.getAcsURL(), e.getAddrMatch(),
                e.getAuthenticationType(), e.getAuthenticationValueEn(), e.getBillAddrCity(),
                e.getBillAddrCountry(), e.getBillAddrLine1(), e.getBillAddrLine1En(),
                e.getBillAddrLine2(), e.getBillAddrLine2En(), e.getBillAddrLine3(),
                e.getBillAddrLine3En(), e.getBillAddrPostCode(), e.getBillAddrState(),
                e.getBroadInfo(), e.getBrowserIp(), e.getBrowserAcceptHeader(),
                e.getBrowserColorDepth(), e.getBrowserJavaEnabled(),
                e.getBrowserJavascriptEnabled(), e.getBrowserLanguage(), e.getBrowserPrivateMode(),
                e.getBrowserScreenHeight(), e.getBrowserScreenWidth(), e.getBrowserTZ(),
                e.getBrowserUserAgent(), e.getCardExpiryDate(), e.getCardholderInfo(),
                e.getCardholderName(), e.getCardholderNameEn(), e.getDeviceChannel(),
                e.getDeviceInfoEn(), e.getDsReferenceNumber(), e.getEci(), e.getEmail(), e.getMcc(),
                e.getMerchantCountryCode(), e.getMerchantName(), e.getMessageCategory(),
                e.getMessageVersion(), e.getPayTokenInd(), e.getPayTokenSource(),
                e.getPurchaseAmount(), e.getPurchaseCurrency(), e.getPurchaseDate(),
                e.getPurchaseExponent(), e.getPurchaseInstalData(), e.getRecurringExpiry(),
                e.getRecurringFrequency(), e.getSdkAppID(), e.getSdkEncData(), e.getSdkMaxTimeout(),
                e.getSdkReferenceNumber(), e.getShipAddrCity(), e.getShipAddrCountry(),
                e.getShipAddrLine1(), e.getShipAddrLine1En(), e.getShipAddrLine2(),
                e.getShipAddrLine2En(), e.getShipAddrLine3(), e.getShipAddrLine3En(),
                e.getShipAddrPostCode(), e.getShipAddrState(),
                e.getThreeDSRequestorAuthenticationInd(), e.getThreeDSReqAuthMethodInd(),
                e.getThreeDSRequestorChallengeInd(), e.getThreeDSRequestorDecMaxTime(),
                e.getThreeDSRequestorDecReqInd(), e.getThreeDSRequestorID(),
                e.getThreeDSRequestorName(), e.getThreeDSRequestorURL(),
                e.getThreeDSServerRefNumber(), e.getThreeDSServerURL(), e.getThreeRIInd(),
                e.getTransStatus(), e.getTransStatusReason(), e.getTransType(),
                e.getWhiteListStatus(), e.getWhiteListStatusSource(), e.getAifChAccAgeInd(),
                e.getAifChAccChange(), e.getAifChAccChangeInd(), e.getAifChAccDate(),
                e.getAifChAccPwChange(), e.getAifChAccPwChangeInd(), e.getAifNbPurchaseAccount(),
                e.getAifPaymentAccAge(), e.getAifPaymentAccInd(), e.getAifShipAddressUsage(),
                e.getAifShipAddressUsageInd(), e.getAifShipNameIndicator(),
                e.getAifProvisionAttemptsDay(), e.getAifSuspiciousAccActivity(),
                e.getAifTxnActivityDay(), e.getAifTxnActivityYear(), e.getArtAcsInterface(),
                e.getArtAcsUiTemplate(), e.getDroSdkInterface(), e.getHpCc(), e.getHpSubscriber(),
                e.getHpSubscriberEn(), e.getWpCc(), e.getWpSubscriber(), e.getWpSubscriberEn(),
                e.getMpCc(), e.getMpSubscriber(), e.getMpSubscriberEn(),
                e.getMriDeliveryEmailAddress(), e.getMriDeliveryTimeframe(),
                e.getMriGiftCardAmount(), e.getMriGiftCardCount(), e.getMriGiftCardCurr(),
                e.getMriPreOrderDate(), e.getMriPreOrderPurchaseInd(), e.getMriReorderItemsInd(),
                e.getMriShipIndicator(), e.getTdsraiThreeDSReqAuthData(),
                e.getTdsraiThreeDSReqAuthMethod(), e.getTdsraiThreeDSReqAuthTimestamp(),
                e.getTdsrpaiThreeDSReqPriorAuthData(), e.getTdsrpaiThreeDSReqPriorAuthMethod(),
                e.getTdsrpaiThreeDSReqPriorAuthTimestamp(), e.getTdsrpaiThreeDSReqPriorRef(),
                e.getAresBroadInfo(), e.getAresWhiteListStatus(), e.getAresWhiteListStatusSource(),
                e.getSysCreator(), e.getCreateMillis(), e.getSysUpdater(), e.getUpdateMillis());
    }

    public static void addEncryptData(AuthenticationLogDO authLog, String cardExpiryDate) {
        if (cardExpiryDate == null || cardExpiryDate.isEmpty()) {
            return;
        }
      authLog.setCardExpiryDate(cardExpiryDate);

    }

}
