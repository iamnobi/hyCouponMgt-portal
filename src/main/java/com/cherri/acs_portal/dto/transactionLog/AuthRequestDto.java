package com.cherri.acs_portal.dto.transactionLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.models.data_object.entity.AuthenticationLogDO;
import ocean.acs.models.data_object.entity.AuthenticationMeLogDO;
import ocean.acs.models.data_object.entity.TransactionLogDO;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@JsonInclude
public class AuthRequestDto {

    private String threeDSCompInd;
    private String threeDSRequestorAuthenticationInd;
    // threeDSRequestorAuthenticationInfo
    private String tdsraiThreeDSReqAuthData;
    private String tdsraiThreeDSReqAuthMethod;
    private String tdsraiThreeDSReqAuthTimestamp;
    private String threeDSRequestorChallengeInd;
    private String threeDSRequestorID;
    private String threeDSRequestorName;
    // threeDSRequestorPriorAuthenticationInfo
    private String tdsrpaiThreeDSReqPriorAuthData;
    private String tdsrpaiThreeDSReqPriorAuthMethod;
    private String tdsrpaiThreeDSReqPriorAuthTimestamp;
    private String tdsrpaiThreeDSReqPriorRef;
    private String threeDSRequestorURL;
    private String threeDSServerRefNumber;
    private String threeDSServerOperatorID;
    private String threeDSServerTransID; // from transaction log
    private String threeDSServerURL;
    private String dsTransID;
    private String dsURL;
    private String threeRIInd;
    private String acctType;
    private String acquirerBIN;
    private String acquirerMerchantID;
    private String addrMatch;
    private String broadInfo;
    private String browserAcceptHeader;
    private String browserIP; // from transaction log
    private Boolean browserJavaEnabled;
    private String browserLanguage;
    private String browserColorDepth;
    private String browserScreenHeight;
    private String browserScreenWidth;
    private String browserTZ;
    private String browserUserAgent;
    private String cardExpiryDate;
    // acctInfo
    private String aifChAccAgeInd;
    private String aifChAccChange;
    private String aifChAccChangeInd;
    private String aifChAccDate;
    private String aifChAccPwChange;
    private String aifChAccPwChangeInd;
    private String aifNbPurchaseAccount;
    private String aifProvisionAttemptsDay;
    private String aifTxnActivityDay;
    private String aifTxnActivityYear;
    private String aifPaymentAccAge;
    private String aifPaymentAccInd;
    private String aifShipAddressUsage;
    private String aifShipAddressUsageInd;
    private String aifShipNameIndicator;
    private String aifSuspiciousAccActivity;
    private String acctNumber;
    private String acctID;
    private String billAddrCity;
    private String billAddrCountry;
    private String billAddrLine1;
    private String billAddrLine2;
    private String billAddrLine3;
    private String billAddrPostCode;
    private String billAddrState;
    private String email;
    // home phone
    private String hpCc;
    private String hpSubscriber;
    // mobile phone
    private String mpCc;
    private String mpSubscriber;
    private String cardholderName;
    private String shipAddrCity;
    private String shipAddrCountry;
    private String shipAddrLine1;
    private String shipAddrLine2;
    private String shipAddrLine3;
    private String shipAddrPostCode;
    private String shipAddrState;
    // work phone
    private String wpCc;
    private String wpSubscriber;
    private String deviceChannel;
    private String deviceInfo;
    // deviceRenderOptions
    private String droSdkInterface;
    private List<String> sdkUiType; // from sdk ui type log
    private String dsReferenceNumber;
    private Boolean payTokenInd;
    private String purchaseInstalData;
    private String mcc;
    private String merchantCountryCode;
    private String merchantName;
    // merchantRiskIndicator;
    private String mriDeliveryEmailAddress;
    private String mriDeliveryTimeframe;
    private String mriGiftCardAmount;
    private String mriGiftCardCount;
    private String mriGiftCardCurr;
    private String mriPreOrderDate;
    private String mriPreOrderPurchaseInd;
    private String mriReorderItemsInd;
    private String mriShipIndicator;
    private String messageCategory;
    private List<MessageExtensionDto> messageExtension; // list from authencationMeLog
    private String messageType = MessageType.AReq.name(); // fix AReq
    private String messageVersion;
    private String notificationURL;
    private String purchaseAmount;
    private String purchaseCurrency;
    private String purchaseExponent;
    private String purchaseDate;
    private String recurringExpiry;
    private String recurringFrequency;
    private String sdkAppID;
    // private String sdkEphemPubKey; // from app but not save
    private String sdkMaxTimeout;
    private String sdkReferenceNumber;
    private String sdkTransID; // from transaction log
    private String transType;

    public static AuthRequestDto valueOf(
      TransactionLogDO transactionLog,
      AuthenticationLogDO authenticationLog,
      List<AuthenticationMeLogDO> authenticationMeLog,
      List<String> sdkUiTypeList,
      String maskAcctNumber) {
        AuthRequestDto authRequestDto = new AuthRequestDto();
        BeanUtils.copyProperties(authenticationLog, authRequestDto);
        authRequestDto.setMessageExtension(
          MessageExtensionDto.valueOfAuthenticationMeLogList(authenticationMeLog));
        authRequestDto.setThreeDSServerTransID(transactionLog.getThreeDSServerTransID());
        authRequestDto.setBrowserIP(authenticationLog.getBrowserIp());
        authRequestDto.setSdkTransID(transactionLog.getSdkTransID());
        authRequestDto.setSdkUiType(sdkUiTypeList);
        authRequestDto.setThreeDSServerOperatorID(transactionLog.getThreeDSServerOperatorID());
        authRequestDto.setDsURL(transactionLog.getDsURL());
        authRequestDto.setDsTransID(transactionLog.getDsTransID());
        authRequestDto.setNotificationURL(transactionLog.getNotificationURL());
        authRequestDto.setAcctNumber(maskAcctNumber);
        return authRequestDto;
    }
}
